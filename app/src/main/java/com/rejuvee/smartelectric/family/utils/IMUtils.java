package com.rejuvee.smartelectric.family.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.base.library.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2018/6/4.
 */
public class IMUtils {

    public static DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    public static String CONFIG_FILE_NAME = "wificonfig";
    public static String WIFIPOSITION_IMAGE_CACHE_NAME = "imagecache";
    public static String INTENT_PLAY_VIDEO_ITEM = "INTENT_PLAY_VIDEO_ITEM";
    private static float SCREEN_WIDTH = 0;
    private static float SCREEN_HEIGHT = 0;
    private static IMUtils self;

    public final static float  VIDEO_IMG_SCALE = (float)9 / 16;

    private Context context;
    private String TAG = "IMUtils";
    private IMUtils(Context context) {
        this.context = context;
    }

    public static IMUtils instance(Context context) {
        if (self == null) {
            self = new IMUtils(context);
        }

        return self;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String getExternalStorage() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }

        return null;
    }

    public String getBitmapCacheDir() {
        return context.getDir("image", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public String saveBitmapToCacheDir(Bitmap bmp, String name) {
        File file = new File(getBitmapCacheDir() + name);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            String path = file.toURI().toString();
            Log.d(TAG, "saveBitmapToCacheDir path=" + path);
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "saveBitmapToCacheDir." + e.toString());
        }

        return null;
    }

    public static String getFileName(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                cachePath = context.getExternalCacheDir().getPath();
            }

        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean isNetWorkConnected(Context context) {
        ConnectivityManager mConnectionManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectionManager.getActiveNetworkInfo();
        if(networkInfo == null){
            return false;
        }

        return networkInfo.isConnected();
    }

    public static float getScreenWidth(Context context) {
        if (SCREEN_WIDTH == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
            SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();
        }

        return SCREEN_WIDTH;
    }

    public static float getScreenHeight(Context context) {
        if (SCREEN_HEIGHT == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
            SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();
        }
        return SCREEN_HEIGHT;
    }

    public static String getTime(long time, String timeFormat) {
        Calendar calendarMsg = Calendar.getInstance();
        calendarMsg.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        return sdf.format(calendarMsg.getTime());
    }
/*
    @SuppressLint("SimpleDateFormat")
    public String getMsgTime(long time) {
        Calendar calendarNow = Calendar.getInstance();

        Calendar calendarMsg = Calendar.getInstance();
        calendarMsg.setTimeInMillis(time);
        //calendarMsg.add(Calendar.DATE, 1);

        String timeFormat = null;
        if (calendarNow.get(Calendar.YEAR) > calendarMsg.get(Calendar.YEAR)) {//前几年
            timeFormat = context.getResources().getString(R.string.time_format_yyyy_M_d_HH_mm_Chinese);
        } else {
            if (calendarNow.get(Calendar.MONTH) > calendarMsg.get(Calendar.MONTH)) {//同一年里前几个月
                timeFormat = context.getResources().getString(R.string.time_format_M_d_HH_mm_Chinese);
            } else {//同一个月
                if (calendarNow.get(Calendar.DAY_OF_WEEK_IN_MONTH) > calendarMsg.get(Calendar.DAY_OF_WEEK_IN_MONTH)) {//同一月里前几周
                    timeFormat = context.getResources().getString(R.string.time_format_M_d_HH_mm_Chinese);
                } else {//
                    if (calendarNow.get(Calendar.DAY_OF_MONTH) == calendarMsg.get(Calendar.DAY_OF_MONTH)) {//同一天
                        timeFormat = context.getResources().getString(R.string.time_format_hh_mm);
                    } else {//同一个星期
                        timeFormat = context.getResources().getString(R.string.time_format_e_HH_mm_Chinese);
                    }
                }
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(calendarMsg.getTime());
    }
*/


}
