package com.rejuvee.smartelectric.family.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rejuvee.smartelectric.family.MainApplication;

import java.util.Objects;

public class AppInstallReceiver extends BroadcastReceiver {
    private String TAG = "AppInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = Objects.requireNonNull(intent.getData()).getSchemeSpecificPart();
            Log.d(TAG, "安装成功" + packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            unbindAccount();
            MainApplication.unbindAliCloud();
            Log.d(TAG, "卸载成功" + packageName);
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG, "替换成功" + packageName);
        }
    }
}
