package com.base.library.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by liuchengran on 2017/9/11.
 */

public class ActivityLifeCycle implements Application.ActivityLifecycleCallbacks {
    private static ActivityLifeCycle instance;

    private ActivityLifeCycle() {

    }

    public static ActivityLifeCycle getInstance() {
        if (instance == null) {
            instance = new ActivityLifeCycle();
        }

        return instance;
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        android.util.Log.w("test", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        android.util.Log.w("test", "application is visible: " + (started > stopped));
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public  boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }
}
