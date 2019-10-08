package com.rejuvee.smartelectric.family.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rejuvee.smartelectric.family.activity.login.ReLoginActivity;


/**
 * 强制下线广播接收器
 *
 * @author tyj
 */
public class ForceOfflineBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(getClass().getSimpleName(), "onReceive");
        Intent i = new Intent(context, ReLoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
