package com.rejuvee.smartelectric.family.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.model.bean.AutoUpgradeEventMessage;

import org.greenrobot.eventbus.EventBus;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    private String versionInfoUrl; //版本信息XML Url
    private String downloadUrl;//APK下载地址
    private DownloadManager downloadManager;
    private DownloadCompleteReceiver receiver;
    private VersionInfo mVersionInfo;

    private Context mContext;
    private String TAG = "AutoUpgrade";
    private AlertDialog mAlertDialog;
    private Handler mHandler;
    private static AutoUpgrade instacne;

    private AutoUpgrade(Context context) {
        mContext = context;
        mHandler = new Handler();
        mAlertDialog = createDialog();
        receiver = new DownloadCompleteReceiver();

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.setPriority(2147483647);
        mContext.registerReceiver(receiver, intentFilter);
        versionInfoUrl = LogoVersionManage.getInstance().getVersionInfoUrl();
        downloadUrl = AppGlobalConfig.HTTP_DOWNLOAD_NEW_VERSION_URL;
    }

    public static AutoUpgrade getInstacne(Context context) {
        if (instacne == null) {
            instacne = new AutoUpgrade(context);
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
        new Thread(new readVersionRunable()).start();
    }

    public VersionInfo getVersionInfo() {
        return mVersionInfo;
    }

    public static class VersionInfo {
        private String versionName;
        private String versionCode;
        private List<String> upgradeItems;
        private String apkName;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public List<String> getUpgradeItems() {
            return upgradeItems;
        }

        public void setUpgradeItems(List<String> upgradeItems) {
            this.upgradeItems = upgradeItems;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getApkName() {
            return apkName;
        }

        public void setApkName(String apkName) {
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
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    compareVersion();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void compareVersion() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            Log.d(TAG, "packageInfo.versionCode = " + packageInfo.versionCode + " mVersionInfo.versionCode=" + mVersionInfo.versionCode);
            if (packageInfo.versionCode < Integer.valueOf(mVersionInfo.versionCode)) {//版本不同，需要更新版本
                downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
                long downLoadId = mContext.getSharedPreferences(AppGlobalConfig.BASIC_CONFIG, MODE_PRIVATE).getLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, -1);
                int statues = getDownloadStatus(downLoadId);
                if (statues != DownloadManager.STATUS_RUNNING) {
                    mAlertDialog.show();
                }

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
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

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                R.style.Dialog);
        builder.setTitle(mContext.getString(R.string.new_version));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(mContext.getString(R.string.is_upgrade_to_new_version));
        builder.setPositiveButton(mContext.getString(R.string.intall_now), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
                initDownManager();
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        return dialog;
    }

    public class XMLContentHandler extends DefaultHandler {
        private String elementName;
        private List<String> listUpgradeItem = new ArrayList<>();

        @Override
        public void startDocument() throws SAXException {
            mVersionInfo = new VersionInfo();
            mVersionInfo.setUpgradeItems(listUpgradeItem);
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            elementName = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String data = new String(ch, start, length);
            data = replaceBlank(data);
            if (data == null || data.length() == 0)
                return;
            if (elementName.equals("versionName")) {
                mVersionInfo.setVersionName(data);
            } else if (elementName.equals("versionCode")) {
                mVersionInfo.setVersionCode(data);
            } else if (elementName.equals("apkInfo")) {
                listUpgradeItem.add(data);
            } else if (elementName.equals("apkName")) {
                mVersionInfo.setApkName(data);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
        }
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

    private void initDownManager() {
        downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);

        Uri parse = Uri.parse(downloadUrl + mVersionInfo.getApkName());
        DownloadManager.Request down = new DownloadManager.Request(parse);

        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 下载时，通知栏显示途中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        down.setTitle(mVersionInfo.getApkName());
        // 设置下载后文件存放的位置
        String apkName = parse.getLastPathSegment();
        down.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, apkName);
        String downloadPath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + apkName;
        File file = new File(downloadPath);
        if (file.exists()) {
            file.delete();
        }
        // 将下载请求放入队列
        long downloadId = downloadManager.enqueue(down);
        mContext.getSharedPreferences(AppGlobalConfig.BASIC_CONFIG, MODE_PRIVATE).edit().putLong(AppGlobalConfig.CONFIG_UPGRADE_DOWNLOAD_ID, downloadId).apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }


    // 接受下载完成后的intent
    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @SuppressLint("NewApi")
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downId == -1) {
                    return;
                }
                //自动安装apk
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Uri uriForDownloadedFile = downloadManager.getUriForDownloadedFile(downId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (isHasInstallPermissionWithO(mContext)) {
                            AutoUpgradeEventMessage eventMessage = new AutoUpgradeEventMessage();
                            eventMessage.upGradeUri = uriForDownloadedFile;
                            EventBus.getDefault().post(eventMessage);
                            return;
                        }
                    }
                    if (uriForDownloadedFile == null) {
                        return;
                    }

                    installApkNew(uriForDownloadedFile);
                }
            }
        }
    }

    //安装apk
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
            //File file= mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/" + mVersionInfo.getApkName());
            uri = FileProvider.getUriForFile(mContext, LogoVersionManage.getInstance().getFileAuthor(), file);//在AndroidManifest中的android:authorities值
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
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
