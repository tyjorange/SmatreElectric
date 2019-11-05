package com.rejuvee.smartelectric.family.common;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.converter.Param;
import com.rejuvee.smartelectric.family.common.constant.AppGlobalConfig;

/**
 * Created by liuchengran on 2018/9/12.
 * 控制不同厂商版本 显示不同的logo
 */
public class LogoVersionManage {
    private String TAG = "LogoVersionManage";
    private static LogoVersionManage instance;
    private ApplicationInfo applicationInfo;
    private String companyName = "";

    private LogoVersionManage(Application application) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = application.getPackageManager()
                    .getApplicationInfo(application.getPackageName(),
                            PackageManager.GET_META_DATA);
            companyName = appInfo.metaData.getString("APP_COMPANY_NAME");
            Log.d(TAG, "companyName=" + companyName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static LogoVersionManage getInstance() {
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            instance = new LogoVersionManage(application);
        }
    }

    public boolean showQuickLogin() {
        return companyName.equals("solarstem") || companyName.equals("yongji");
    }

    //获取登录页面的logo
    public int getLoginLogo() {
        switch (companyName) {
            case "solarstem":
                return R.drawable.login_logo_solarstem;
            case "yongji":
                return R.drawable.login_logo;
            case "isteks":
                return R.drawable.login_logo_isteks;
            case "smarte":
                return R.drawable.login_logo_smarte;
        }
        return R.drawable.login_logo;
    }

    //获取主页的
    public int getMainActivityLogo() {
        switch (companyName) {
            case "solarstem":
                return R.drawable.main_page_top_solarstem;
            case "yongji":
                return R.drawable.main_page_top;
            case "isteks":
                return R.drawable.main_page_top_isteks;
            case "smarte":
                return R.drawable.main_page_top_smarte;
        }
        return R.drawable.main_page_top;
    }

    String getVersionInfoUrl() {
        switch (companyName) {
            case "solarstem":
                return AppGlobalConfig.HTTP_URL_VERSION_CONTROL + "null1.xml";
            case "yongji":
                return AppGlobalConfig.HTTP_URL_VERSION_CONTROL + "null2.xml";
            case "isteks":
                return AppGlobalConfig.HTTP_URL_VERSION_CONTROL + "null3.xml";
            case "smarte":
                return AppGlobalConfig.HTTP_URL_VERSION_CONTROL + "smarte_family_index.xml";
        }
        return AppGlobalConfig.HTTP_URL_VERSION_CONTROL + "null.xml";
    }

    String getFileAuthor() {
        switch (companyName) {
            case "solarstem":
                return "com.rejuvee.smartelectric.family.slst";
            case "yongji":
                return "com.rejuvee.smartelectric.family.yongji";
            case "isteks":
                return "com.rejuvee.smartelectric.family.isteks";
            case "smarte":
                return "com.rejuvee.smartelectric.family.smarte";
        }
        return "null";
    }

    public String getWXAppId() {
        switch (companyName) {
            case "solarstem":
                return "wxa3aff7647479905d";
            case "yongji":
                return "wxd7a3aac249217921";
            case "isteks":
                return "wxa3aff7647479905d";
            case "smarte":
                return "wxa54b80207f1a4fd7";
        }
        return "null";
    }

    @Deprecated
    public String getWXSecret() {
        switch (companyName) {
            case "solarstem":
                return "0";
            case "yongji":
                return "0";
            case "isteks":
                return "0";
            case "smarte":
                return "0";
        }
        return "null";
    }

    public String getVersionType() {
        if (companyName.equals("yongji")) {
            return Param.YJ_DM_FAMILY;
        } else if (companyName.equals("smarte")) {
            return Param.DM_FAMILY;
        }
        return "null";
    }

    public String getQQAppId() {
        switch (companyName) {
            case "solarstem":
                return "0";
            case "yongji":
                return "101698266";
            case "isteks":
                return "0";
            case "smarte":
                return "101693343";
        }
        return "null";
    }

    public String getQQAppKey() {
        switch (companyName) {
            case "solarstem":
                return "0";
            case "yongji":
                return "4db22ec0a6c9c84459d303f168ad20e0";
            case "isteks":
                return "0";
            case "smarte":
                return "90f211965c99fadc35043e7705b1a84d";
        }
        return "null";
    }
}
