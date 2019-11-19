package com.rejuvee.smartelectric.family.common.manager;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有活动管理器
 *
 * @author tyj
 */
public class ActivityFragmentManager {

    public static List<Activity> activities = new ArrayList<>();
    public static List<Fragment> fragments = new ArrayList<>();

    /**
     * 添加活动Activity
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    /**
     * 添加活动Fragment
     *
     * @param fragment
     */
    public static void addFragment(Fragment fragment) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
        }
    }

    /**
     * 移除活动Activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 移除活动Fragment
     *
     * @param fragment
     */
    public static void removeFragment(Fragment fragment) {
        fragments.remove(fragment);
    }

    /**
     * 结束所有活动Activity
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束所有活动Fragment
     */
    public static void removeAll() {
        for (Fragment fragment : fragments) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                activity.onBackPressed();
            }
        }
    }
}
