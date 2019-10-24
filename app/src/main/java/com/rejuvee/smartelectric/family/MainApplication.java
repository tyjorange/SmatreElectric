package com.rejuvee.smartelectric.family;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.base.library.utils.Utils;
import com.rejuvee.smartelectric.family.common.CrashHandler;
import com.rejuvee.smartelectric.family.common.LogoVersionManage;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.utils.WifiUtil;
import com.rejuvee.smartelectric.family.utils.thrid.QQLoginHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import io.realm.Realm;
//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by liuchengran on 2018/10/31.
 */
public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication self;
    public static IWXAPI mWxApi;

    public static MainApplication instance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Realm.init(this);
        LogoVersionManage.init(this);
        ZXingLibrary.initDisplayOpinion(this);
        NativeLine.init(this);
        self = this;
            /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        registerToWX();
        QQLoginHelper.getInstance().init(this);
        initCloudChannel(this);
        initNotificationChannel();
        // 字体
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/syht.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build());
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
            WifiUtil.getInstance(this);//TODO Android 6.0 会报错
        }
        CrashHandler.getInstance().init(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, LogoVersionManage.getInstance().getWXAppId(), true);
        // 将该app注册到微信
        mWxApi.registerApp(LogoVersionManage.getInstance().getWXAppId());
    }

    private static CloudPushService pushService;

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success:" + response);
                Log.d(TAG, pushService.getDeviceId());
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    /**
     * 初始化云推送通道
     * api > 26 的才能收到通知
     */
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * 绑定阿里云推送
     */
    public static void bindAliCloud(String username) {
        pushService.bindAccount(username, new CommonCallback() {

            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "bindAccount success:" + response);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "bindAccount failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    /**
     * 解绑阿里云推送
     */
    public static void unbindAliCloud() {
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("SettingsActivity", "unbindAccount success:" + response);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("SettingsActivity", "unbindAccount failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }
}
