package com.base.library.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * Created by liuchengran on 2018/9/13.
 */

public class LanguageUtil {
    private static String TAG = "LanguageUtil";
//    private static String configueName = "configue";

//    private static int getLangType(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(configueName, MODE_PRIVATE);
//        //获取 Locale  对象的正确姿势：
//        Locale locale;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            locale = context.getResources().getConfiguration().getLocales().get(0);
//        } else {
//            locale = context.getResources().getConfiguration().locale;
//        }
//        //获取语言的正确姿势：
//        String lang = locale.getLanguage() + "-" + locale.getCountry();
//        Log.d(TAG, "lang = " + lang);
//
//        int type = sp.getInt("langtype", -1);
//        if (type == -1) {//初次进入
//            String defaultLanguage = Locale.getDefault().getLanguage();
//            Log.d(TAG, "default language = " + defaultLanguage);
//            if (defaultLanguage.equals(Locale.CHINA.getLanguage())
//                    || defaultLanguage.equals(Locale.CHINESE.getLanguage())
//                    || defaultLanguage.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())
//                    || defaultLanguage.equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
//                type = 0;
//            } else {
//                type = 1;
//            }
//            setConfigueType(context, type);
//        }
//        Log.d(TAG, "langType = " + type);
//        return type;
//    }

//    public static void setConfigueType(Context context, int type) {
//        SharedPreferences sp = context.getSharedPreferences(configueName, MODE_PRIVATE);
//        sp.edit().putInt("langtype", type).commit();
//    }

//    private static LocaleList sLocaleList;

    public static void SwitchLang(Context context) {
//        Locale targetLocale = null;
//        if (getLangType(context) == 0) {
//            targetLocale = Locale.CHINA;
//        } else if (getLangType(context) == 1) {
//            targetLocale = Locale.ENGLISH;
//        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(getLocale(context));
        resources.updateConfiguration(config, dm);//语言更换生效的代码!
    }

    public static Locale getLocale(Context context) {
//        Locale targetLocale = null;
//        if (getLangType(context) == 0) {
//            targetLocale = Locale.CHINA;
//        } else if (getLangType(context) == 1) {
//            targetLocale = Locale.ENGLISH;
//        }
        //获取 Locale  对象的正确姿势：
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        //获取语言的正确姿势：
        String lang = locale.getLanguage() + "-" + locale.getCountry();
        Log.d(TAG, "lang = " + lang);
        return locale;
    }
}
