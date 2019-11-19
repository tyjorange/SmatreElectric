package com.rejuvee.smartelectric.family.common;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.AppGlobalConfig;
import com.rejuvee.smartelectric.family.common.utils.WifiUtil;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.DownloadProgressDialog;
import com.rejuvee.smartelectric.family.model.bean.AutoUpgradeEventMessage;

import org.greenrobot.eventbus.EventBus;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liuchengran on 2017/9/12.
 */
/*
    /usr/apache-tomcat-8.5.11/base_sample/webapps/ROOT/apk
* <?xml version="1.0" encoding="UTF-8"?>
    <packge_info count="4">
        <versionName>1.5.0</versionName>
	    <versionCode>5</versionCode>
	    <apkName>daluge.apk</apkName>
	    <apkInfo>修复一些用户反馈问题</apkInfo>
	    <apkInfo>修复一些用户反馈问题</apkInfo>
    </packge_info>
*isteks_apk_packeg_info.xml
* solarstem_apk_packeg_info.xml
* */
public class AutoUpgrade {
    private final String TAG = "AutoUpgrade";
    private static AutoUpgrade instacne;
    private String versionInfoUrl; //版本信息XML Url
    private String downloadUrl;//APK下载地址
    private DownloadManager downloadManager;
    private DownloadCompleteReceiver receiver;
    private VersionInfo mVersionInfo;
    private final Runnable mQueryProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (queryState())
                mHandler.postDelayed(mQueryProgressRunnable, 1000);
        }
    };
    private Context mContext;
    private SharedPreferences sharedPreferences;
    //    private DialogTip mDialogTip;
    private Handler mHandler;
    private boolean showTip = false;

    private AutoUpgrade(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(AppGlobalConfig.BASIC_CONFIG, MODE_PRIVATE);
        mHandler = new MyHandler(this);
        receiver = new DownloadCompleteReceiver();

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.setPriority(2147483647);
        mContext.registerReceiver(receiver, intentFilter);
        versionInfoUrl = LogoVersionManage.getInstance().getVersionInfoUrl();
        downloadUrl = AppGlobalConfig.HTTP_DOWNLOAD_NEW_VERSION_URL;
    }

    private static class MyHandler extends Handler {
        SoftReference<AutoUpgrade> activityWeakReference;

        MyHandler(AutoUpgrade autoUpgrade) {
            activityWeakReference = new SoftReference<>(autoUpgrade);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            AutoUpgrade autoUpgrade = activityWeakReference.get();
            if (msg.what == 1021) {
                if (autoUpgrade.progressDialog != null) {
                    autoUpgrade.progressDialog.setProgress(msg.arg1);
                    autoUpgrade.progressDialog.setMax(msg.arg2);
                }
            } else if (msg.what == 1022) {
                if (autoUpgrade.showTip) {
                    CustomToast.showCustomErrorToast(autoUpgrade.mContext, autoUpgrade.mContext.getString(R.string.vs219));
                }
            }
        }
    }

    public static AutoUpgrade getInstacne(Context context) {
        if (instacne == null) {
            instacne = new AutoUpgrade(context);
        } else {
            instacne.mContext = context;
        }
        return instacne;
    }

    public void destroyInstance() {
        instacne = null;
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    public void start() {
        showTip = false;
        new Thread(new readVersionRunable()).start();
    }

    public void startWithTip() {
        showTip = true;
        new Thread(new readVersionRunable()).start();
    }

    private class VersionInfo {
        private String versionName;
        private String versionCode;
        private List<String> upgradeItems;
        private String apkName;

        void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        void setUpgradeItems(List<String> upgradeItems) {
            this.upgradeItems = upgradeItems;
        }

        void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        String getApkName() {
            return apkName;
        }

        void setApkName(String apkName) {
            this.apkName = apkName;
        }
    }

    private class readVersionRunable implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL(versionInfoUrl);
                Log.d(TAG, versionInfoUrl);
                //打开连接
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (200 == urlConnection.getResponseCode()) {
                    //得到输入流
                    InputStream is = urlConnection.getInputStream();
                    parseXML(is);
                } else {
                    Log.e(TAG, "xml ResponseCode=" + urlConnection.getResponseCode());
                    Message msg = Message.obtain();
                    msg.what = 1022;
                    mHandler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseXML(InputStream inputStream) {
        try {
            //创建解析器
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLContentHandler handler = new XMLContentHandler();
            saxParser.parse(inputStream, handler);
            inputStream.close();
            mHandler.post(this::compareVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void compareVersion() {
        try {
            PackageInfo currentPackageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            long appVersionCode = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                appVersionCode = currentPackageInfo.getLongVersionCode();
            } else {
                appVersionCode = currentPackageInfo.versionCode;
            }
            Log.d(TAG, "localVersionCode = " + appVersionCode + " remoteVersionCode=" + mVersionInfo.versionCode);
            if (appVersionCode < Integer.valueOf(mVersionInfo.versionCode)) {//版本不同，需要更新版本
                downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
                createDialog();
            } else {
                if (showTip) {
                    CustomToast.showCustomToast(mContext, mContext.getString(R.string.vs216));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示更新提示
     */
    private void createDialog() {
        StringBuilder res = new StringBuilder();
        for (String s : mVersionInfo.upgradeItems) {
            res = res.append(s);
        }
        DialogTip mDialogTip = new DialogTip(mContext, false);
        mDialogTip.setCanceledOnTouchOutside(false);// 设置为点击窗口外部不可关闭
        mDialogTip.setTitle(mContext.getString(R.string.new_version) + mVersionInfo.versionName)
                .setContent(res.toString().replace("\\n", "\n"))
                .setOkTxt(mContext.getString(R.string.intall_now))
                .setCancelTxt(mContext.getString(R.string.vs215))
                .setDialogListener(new DialogTip.onEnsureDialogListener() {
                    @Override
                    public void onEnsure() {
                        initDownManager();
                        mDialogTip.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        mDialogTip.dismiss();
                    }
                }).show();
    }

    /**
     * 添加下载任务
     */
    private void initDownManager() {
        downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        Log.i(TAG, "attempt download from : " + downloadUrl + mVersionInfo.getApkName());
        Uri parse = Uri.parse(downloadUrl + mVersionInfo.getApkName());
        DownloadManager.Request dmr = new DownloadManager.Request(parse);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
//        dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 下载时，通知栏显示途中
        dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
//        dmr.setVisibleInDownloadsUi(true);
        dmr.setTitle(mVersionInfo.getApkName());
        // 设置下载后文件存放的位置
        String apkName = parse.getLastPathSegment();
        dmr.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, apkName);
        String downloadPath = String.format("%s/%s", Objects.requireNonNull(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getPath(), apkName);
        File file = new File(downloadPath);
        if (file.exists()) {
            boolean delete = file.delete();
            Log.d(TAG, "file[" + file.getPath() + "]isDelete=" + delete);
        }
        //确认网络类型
        if (WifiUtil.getInstance(mContext).isWifiConnect()) {
            startDownload(dmr);
        } else {
            DialogTip _DialogTip = new DialogTip(mContext, false);
            _DialogTip.setCanceledOnTouchOutside(false);// 设置为点击窗口外部不可关闭
            _DialogTip.setTitle("")
                    .setContent(mContext.getString(R.string.vs273))
                    .setOkTxt(mContext.getString(R.string.vs274))
                    .setCancelTxt(mContext.getString(R.string.cancel))
                    .setDialogListener(new DialogTip.onEnsureDialogListener() {
                        @Override
                        public void onEnsure() {
                            startDownload(dmr);
                            _DialogTip.dismiss();
                        }

                        @Override
                        public void onCancel() {
                            _DialogTip.dismiss();
                        }
                    }).show();
        }
    }

    /**
     * 将下载请求放入队列
     *
     * @param dmr
     */
    private void startDownload(DownloadManager.Request dmr) {
        long downloadId = downloadManager.enqueue(dmr);
        sharedPreferences.edit().putLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, downloadId).apply();
        startQuery();
    }
    /**
     * 查询下载进度，文件总大小多少，已经下载多少？
     *
     * @return 下载失败 false
     */
    private boolean queryState() {
        if (getDownloadStatus() == DownloadManager.STATUS_FAILED) {
            Log.e(TAG, "STATUS_FAILED");
            CustomToast.showCustomErrorToast(mContext, mContext.getString(R.string.vs217));
            return false;
        }
        long downLoadId = sharedPreferences.getLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, -1);
        // 通过ID向下载管理查询下载情况，返回一个cursor
        Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(downLoadId));
        if (c != null) {
            if (c.moveToFirst()) {
                int mDownload_so_far = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int mDownload_all = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                if (mDownload_all > 0) {
                    Message msg = Message.obtain();
                    msg.what = 1021;
                    msg.arg1 = mDownload_so_far;
                    msg.arg2 = mDownload_all;
                    mHandler.sendMessage(msg);
                }
            }
            if (!c.isClosed()) {
                c.close();
            }
        }
        return true;
    }

    /**
     * 查询下载状态
     *
     * @return
     */
    private int getDownloadStatus() {
        long downLoadId = sharedPreferences.getLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, -1);
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downLoadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * xml解析回调
     */
    public class XMLContentHandler extends DefaultHandler {
        private String elementName;
        private List<String> listUpgradeItem = new ArrayList<>();

        @Override
        public void startDocument() {
            mVersionInfo = new VersionInfo();
            mVersionInfo.setUpgradeItems(listUpgradeItem);
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
            elementName = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            String data = new String(ch, start, length);
            data = replaceBlank(data);
            if (data == null || data.length() == 0)
                return;
            switch (elementName) {
                case "versionName":
                    mVersionInfo.setVersionName(data);
                    break;
                case "versionCode":
                    mVersionInfo.setVersionCode(data);
                    break;
                case "apkInfo":
                    listUpgradeItem.add(data);
                    break;
                case "apkName":
                    mVersionInfo.setApkName(data);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) {
        }

        private String replaceBlank(String str) {
            String dest = "";
            if (str != null) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(str);
                dest = m.replaceAll("");
            }
            return dest;
        }
    }

    //更新下载进度
    private void startQuery() {
        long downLoadId = sharedPreferences.getLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, -1);
        if (downLoadId > 0) {
            displayProgressDialog();
            mHandler.post(mQueryProgressRunnable);
        }
    }

    //下载进度弹窗
    private DownloadProgressDialog progressDialog;

    //进度对话框
    private void displayProgressDialog() {
        // 创建ProgressDialog对象
        progressDialog = new DownloadProgressDialog(mContext);
        // 设置进度条风格，风格为长形
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
//            progressDialog.setTitle("下载提示");
        // 设置ProgressDialog 提示信息
//        progressDialog.setMessage(mContext.getString(R.string.vs218));
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
//            progressDialog.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.btn_def));
//        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.cancel), (dialog, which) -> {
//            removeDownload();
//            dialog.dismiss();
////                    mContext.finish();
//        });
        progressDialog.setCancel(() -> {
            removeDownload();
            progressDialog.dismiss();
        });
        if (!progressDialog.isShowing()) {
            // 保持ProgressDialog显示
            progressDialog.show();
        }
    }

    //下载停止同时删除下载文件
    private void removeDownload() {
        if (downloadManager != null) {
            long downLoadId = sharedPreferences.getLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, -1);
            downloadManager.remove(downLoadId);
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private boolean isHasInstallPermissionWithO(Context context) {
//        if (context == null) {
//            return false;
//        }
//        return context.getPackageManager().canRequestPackageInstalls();
//    }

    /**
     * 接受下载完成后的intent
     */
    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (Objects.equals(intent.getAction(), DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                progressDialog.dismiss();
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downId == -1) {
                    return;
                }
                Uri uriForDownloadedFile = downloadManager.getUriForDownloadedFile(downId);
                if (uriForDownloadedFile == null) {
                    return;
                }
                //自动安装apk
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    if (isHasInstallPermissionWithO(mContext)) {
                AutoUpgradeEventMessage eventMessage = new AutoUpgradeEventMessage();
                eventMessage.upGradeUri = uriForDownloadedFile;
                EventBus.getDefault().post(eventMessage);
//                        return;
//                    }
//                }
//                installApkNew(uriForDownloadedFile);
            }
        }
    }

    /**
     * 安装apk
     *
     * @param uri
     */
    public void installApkNew(Uri uri) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//7.0以下
            File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + mVersionInfo.getApkName());
            if (file != null) {
                String path = file.getAbsolutePath();
                uri = Uri.parse("file://" + path);
            }
        } else {
            File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), mVersionInfo.getApkName());
            uri = FileProvider.getUriForFile(mContext, LogoVersionManage.getInstance().getFileAuthor(), file);//在AndroidManifest中的android:authorities值
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
