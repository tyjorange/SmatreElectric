package com.rejuvee.smartelectric.family.common.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public final class ViewUtil {

    private ViewUtil() {
    }

    /*视图参数*/
    private static float density;
    private static float scaledDensity;
    private static int widthPixels;
    private static int heightPixels;

    private static boolean isInit = false;

    private static void confirmInit() {
        if (!isInit) {
            throw new IllegalStateException("ViewUtil还未初始化");
        }
    }

    public static void init(Context context) {
        if (isInit) {
            return;
        }
        // Displaymetrics 是取得手机屏幕大小的关键类
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // 显示器的逻辑密度，Density Independent Pixel（如3.0)
        density = displayMetrics.density;
        //缩放密度（scaledDensity和density数值是一样的）
        scaledDensity = displayMetrics.scaledDensity;

        //屏幕的像素宽度
        widthPixels = displayMetrics.widthPixels;
        //屏幕的像素高度
        heightPixels = displayMetrics.heightPixels;
        isInit = true;
    }

    public static float getDisplayMetricsDensity() {
        confirmInit();
        return density;
    }

    public static float getDisplayMetricsScaledDensity() {
        confirmInit();
        return scaledDensity;
    }

    public static int getScreenWidthPx() {
        confirmInit();
        return widthPixels;
    }

    public static int getScreenHeightPx() {
        confirmInit();
        return heightPixels;
    }

    /* 单位转换 */

    public static int dpToPx(float dpValue) {
        confirmInit();
        return (int) (dpValue * getDisplayMetricsDensity() + 0.5F);
    }

    public static int pxToDp(float pxValue) {
        confirmInit();
        return (int) (pxValue / getDisplayMetricsDensity() + 0.5F);
    }

    public static int pxToSp(float pxValue) {
        confirmInit();
        return (int) (pxValue / getDisplayMetricsScaledDensity() + 0.5f);
    }

    public static int spToPx(float spValue) {
        confirmInit();
        return (int) (spValue * getDisplayMetricsScaledDensity() + 0.5f);
    }
}
