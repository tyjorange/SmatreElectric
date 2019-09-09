package com.base.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;


import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by liuchengran on 2018/9/13.
 */

public class LanguageUtil {
    private static String TAG = "LanguageUtil";
    private static String configueName = "configue";
    public static int getLangType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(configueName, MODE_PRIVATE);
        int type = sp.getInt("langtype", -1);
        if (type == -1) {//初次进入
            String defaultLanguage = Locale.getDefault().getLanguage();
            Log.d(TAG, "default language = " + defaultLanguage);
            if (defaultLanguage.equals(Locale.CHINA.getLanguage())
                    || defaultLanguage.equals(Locale.CHINESE.getLanguage())
                    || defaultLanguage.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())
                    || defaultLanguage.equals(Locale.TRADITIONAL_CHINESE.getLanguage())) {
                type = 0;
            } else {
                type = 1;
            }
            setConfigueType(context, type);
        }
        Log.d(TAG, "langType = " + type);
        return  type;
    }

    public static void setConfigueType(Context context, int type) {
        SharedPreferences sp = context.getSharedPreferences(configueName, MODE_PRIVATE);
        sp.edit().putInt("langtype", type).commit();
    }

    public static void SwitchLang(Context context) {
        Locale targetLocale = null;
        if (getLangType(context) == 0) {
            targetLocale = Locale.CHINA;
        } else if (getLangType(context) == 1) {
            targetLocale = Locale.ENGLISH;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(targetLocale);
        } else {
            config.locale = targetLocale;
        }

        resources.updateConfiguration(config, dm);//语言更换生效的代码!
    }

    public static Locale getLocale(Context context) {
        Locale targetLocale = null;
        if (getLangType(context) == 0) {
            targetLocale = Locale.CHINA;
        } else if (getLangType(context) == 1) {
            targetLocale = Locale.ENGLISH;
        }

        return targetLocale;
    }
}
