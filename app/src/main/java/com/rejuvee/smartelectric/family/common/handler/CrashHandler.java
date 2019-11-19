package com.rejuvee.smartelectric.family.common.handler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.manager.ActivityFragmentManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * 全局异常捕获
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    //用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // 多放查阅资料，发现App Crash之后系统会重新启动Task栈顶的Activity，具体请自行google！
            // 解决方法是：killProcess之前，结束掉Task栈中所有的Activity。
            ActivityFragmentManager.removeAll();
            ActivityFragmentManager.finishAll();
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, mContext.getString(R.string.vs214), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中 将文件传送到服务器
     *
     * @param ex
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "/crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/rejuvee.net";
                File dir = new File(path);
                if (!dir.exists()) {
                    boolean mkdirs = dir.mkdirs();
                    if (!mkdirs) {
                        Log.e(TAG, "can not mkdir:" + dir);
                        return;
                    }
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                Log.i(TAG, path + fileName);
                upLoadFile(path + fileName);
            }
//            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
//        return null;
    }

    /**
     * 上传日志文件到服务器
     *
     * @param path
     */
    private void upLoadFile(String path) {
        Log.i(TAG, "upLoadFile...");
        currentCall = Core.instance(mContext).uploadLogFile(getLogPart(path), new ActionCallbackListener<Void>() {

            @Override
            public void onSuccess(Void data) {
                Log.i(TAG, "upload log_file onSuccess():" + path);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, "upload log_file onFailure():" + message);
            }
        });
    }

    /**
     * 读取日志文件
     *
     * @param path
     * @return
     */
    private MultipartBody.Part getLogPart(String path) {
        Log.i(TAG, "getLogPart...");
        File tempFile = new File(path);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), tempFile);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用log
        return MultipartBody.Part.createFormData("log", tempFile.getName(), requestFile);
    }

    private Call<?> currentCall;

    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }
}