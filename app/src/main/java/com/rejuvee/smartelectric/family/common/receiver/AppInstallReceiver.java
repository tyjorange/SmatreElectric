package com.rejuvee.smartelectric.family.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.common.utils.WifiUtil;

import java.util.Objects;

public class AppInstallReceiver extends BroadcastReceiver {
    private String TAG = "AppInstallReceiver";

    /**
     * 有可能接收不到卸载广播
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = Objects.requireNonNull(intent.getData()).getSchemeSpecificPart();
            Log.d(TAG, "安装成功" + packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = Objects.requireNonNull(intent.getData()).getSchemeSpecificPart();
            MainApplication.unbindAliCloud();
            Log.d(TAG, "卸载成功" + packageName);
            String name = WifiUtil.getInstance(context).forgetWifi();
            if (name.isEmpty()) {
                Log.i(TAG, "forgetWifi[" + name + "] false");
            } else {
                Log.i(TAG, "forgetWifi[" + name + "] true");
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = Objects.requireNonNull(intent.getData()).getSchemeSpecificPart();
            Log.d(TAG, "替换成功" + packageName);
            String name = WifiUtil.getInstance(context).forgetWifi();
            if (name.isEmpty()) {
                Log.i(TAG, "forgetWifi[" + name + "] false");
            } else {
                Log.i(TAG, "forgetWifi[" + name + "] true");
            }
        }
    }
}
