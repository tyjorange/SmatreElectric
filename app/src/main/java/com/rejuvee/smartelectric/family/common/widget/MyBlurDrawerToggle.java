package com.rejuvee.smartelectric.family.common.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import at.favre.lib.dali.Dali;
import at.favre.lib.dali.builder.nav.DaliBlurDrawerToggle;
import at.favre.lib.dali.builder.nav.NavigationDrawerListener;
import at.favre.lib.dali.util.BuilderUtil;
import at.favre.lib.dali.util.LegacySDKUtil;

/**
 * 改写 ImageView.ScaleType.FIT_CENTER =》 ImageView.ScaleType.FIT_XY
 * 解决模糊时右侧黑边问题
 */
public class MyBlurDrawerToggle extends DaliBlurDrawerToggle {
    private DrawerLayout drawerLayout;
    private Dali dali;
    private ImageView blurView;
    private DaliBlurDrawerToggle.CacheMode cacheMode;
    private boolean forceRedraw;
    private boolean enableBlur;

    public MyBlurDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes, NavigationDrawerListener listener) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes, listener);
        this.cacheMode = DaliBlurDrawerToggle.CacheMode.AUTO;
        this.forceRedraw = false;
        this.enableBlur = true;
        this.dali = Dali.create(drawerLayout.getContext());
        this.drawerLayout = drawerLayout;
    }

    public void onDrawerSlide(View drawerView, float slideOffset) {
        this.renderBlurLayer(slideOffset);
//        super.onDrawerSlide(drawerView, slideOffset);
    }

    private void renderBlurLayer(float slideOffset) {
        if (this.enableBlur) {
            if (slideOffset == 0.0F || this.forceRedraw) {
                this.clearBlurView();
            }

            if (slideOffset > 0.0F && this.blurView == null) {
                if (this.drawerLayout.getChildCount() == 2) {
                    this.blurView = new ImageView(this.drawerLayout.getContext());
                    this.blurView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                    // 把图片按照指定的大小在ImageView中显示，拉伸显示图片，不保持原比例，填满ImageView.
                    // 关键，否则会有黑边
                    this.blurView.setScaleType(ImageView.ScaleType.FIT_XY);
                    this.drawerLayout.addView(this.blurView, 1);
                }

                if (BuilderUtil.isOnUiThread()) {
                    // 模糊程度
                    int blurRadius = 16;
                    int downSample = 4;
                    if (!this.cacheMode.equals(DaliBlurDrawerToggle.CacheMode.AUTO) && !this.forceRedraw) {
                        this.dali.load(this.drawerLayout.getChildAt(0)).blurRadius(blurRadius).downScale(downSample).noFade().error(-1).concurrent().into(this.blurView);
                    } else {
                        this.dali.load(this.drawerLayout.getChildAt(0)).blurRadius(blurRadius).downScale(downSample).noFade().error(-1).concurrent().skipCache().into(this.blurView);
                        this.forceRedraw = false;
                    }
                }
            }

            if (slideOffset > 0.0F && slideOffset < 1.0F) {
                int alpha = (int) Math.ceil((double) slideOffset * 255.0D);
                LegacySDKUtil.setImageAlpha(this.blurView, alpha);
            }
        }
    }

    private void clearBlurView() {
        if (this.drawerLayout.getChildCount() == 3) {
            this.drawerLayout.removeViewAt(1);
        }
        this.blurView = null;
    }
}
