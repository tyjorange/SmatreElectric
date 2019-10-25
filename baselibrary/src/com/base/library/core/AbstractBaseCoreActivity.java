package com.base.library.core;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.base.frame.event.KeyboardEvent;
import com.base.frame.log.DebugLog;
import com.base.frame.log.LogUtil;
import com.base.frame.os.ActivityStack;
import com.base.frame.os.SystemBarTintManager;
import com.base.library.R;
import com.base.library.utils.LanguageUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;

/**
 * base activity
 * <p/>
 * 不可左滑关闭activity
 * <p/>
 * Created by admin.
 */
public abstract class AbstractBaseCoreActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseCoreActivity.class);
    private Toolbar toolbar;
    private TextView txtSave;
    private TextView toolbarTitle;

    public TextView getToolbarTextView() {
        return toolbarTitle;
    }

    /**
     * 返回当前Activity布局文件的id
     *
     * @return
     */
    abstract protected int getLayoutResId();

    /**
     * initView
     */
    abstract protected void initView();

    /**
     * initData
     */
    abstract protected void initData();

    abstract protected String getToolbarTitle();

    /**
     * 是否显示返回键
     *
     * @return
     */
    abstract protected boolean isDisplayHomeAsUpEnabled();


    abstract protected void dealloc();

    private LinearLayout contentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//
        super.onCreate(savedInstanceState);
        initBase();
    }

    protected View parentView;
    private int statusBarHeight, toolbarHeight;

    private void initBase() {
        ActivityStack.getActivityManage().addActivity(this);
        if (getNavToolbarLayoutResId() != 0)
            initContentView();
        setContentView(getLayoutResId());//把设置布局文件的操作交给继承的子类
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Boolean internal attribute to adjust view layout based on system windows such as
// the status bar. If true, adjusts the padding of this view to leave space for the system windows.
// Will only take effect if this view is in a non-embedded activity.
            parentView.setFitsSystemWindows(false);
        }

        toolbar = (Toolbar) this.findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //是否显示toolbar 自带title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (isDisplayHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.base_nav_back_icon);
            // toolbar.seton
        }
        toolbarTitle = (TextView) toolbar.findViewById(R.id.nav_toolbar_title);
        toolbarTitle.setText(getToolbarTitle());
        toolbarTitle.setOnClickListener(this);
        //返回
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        initSystemBar();
        statusBarHeight = mTintManager.getConfig().getStatusBarHeight();
        toolbarHeight = toolbar.getLayoutParams().height;
        DebugLog.i(LOGTAG, "Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT);

        DebugLog.i(LOGTAG, "mTintManager.getConfig().getActionBarHeight():"
                + mTintManager.getConfig().getActionBarHeight());
        DebugLog.i(LOGTAG, " mTintManager.getConfig().getStatusBarHeight():"
                + mTintManager.getConfig().getStatusBarHeight());
        DebugLog.i(LOGTAG, " toolbar.getLayoutParams().height:"
                + toolbar.getLayoutParams().height);

        if (calculateToolbarHeight() &&
                Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//calculate Toolbar Height
            mTintManager.setTintColor(Color.parseColor("#00000000"));
            parentView.setFitsSystemWindows(false);
            setToolbarHeight();
        } else {
            DebugLog.i(LOGTAG, "setTintColor...");
            mTintManager.setTintColor(Color.parseColor("#20b0fc"));
            parentView.setFitsSystemWindows(true);
        }

//        ButterKnife.inject(this);
        initView();
        if (onEnableInitWebView()) onWebConfig();
        initData();

    }

    /**
     * set toolbar height
     */
    private void setToolbarHeight() {
//        DebugLog.i(LOGTAG, "getStatusBarHeight:" + statusBarHeight);
//        DebugLog.i(LOGTAG, "getNavigationBarHeight:" + mTintManager.getConfig().getNavigationBarHeight());
//        DebugLog.i(LOGTAG, "toolbar.getHeight():" + toolbar.getLayoutParams().height);
        toolbar.getLayoutParams().height = statusBarHeight + toolbarHeight;
//        DebugLog.i(LOGTAG, "toolbar.getHeight()-2:" + toolbar.getLayoutParams().height);
    }

    /**
     * calculate toolbar height
     *
     * @return
     */
    protected boolean calculateToolbarHeight() {

        return false;
    }

    protected boolean onEnableInitWebView() {

        return false;
    }

    protected void onWebConfig() {

    }

    protected SystemBarTintManager mTintManager;

    private void initSystemBar() {
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
//        mTintManager.setTintColor(Color.parseColor("#00000000"));
    }

    /**
     * hide support action bar
     *
     * @param isHide
     */
    protected void setToolbarHide(boolean isHide) {
        if (getSupportActionBar() != null) {
            if (isHide) {
               /* if (calculateToolbarHeight() &&
                        Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                    getWindow().findViewById(Window.ID_ANDROID_CONTENT).setFitsSystemWindows(false);
//                    mTintManager.setTintColor(Color.parseColor("#20b0fc"));
                } else {
//                    getWindow().findViewById(Window.ID_ANDROID_CONTENT).setFitsSystemWindows(true);
//                    mTintManager.setTintColor(Color.parseColor("#20b0fc"));
                }*/
                getSupportActionBar().hide();
            } else {
//                getWindow().findViewById(Window.ID_ANDROID_CONTENT).setFitsSystemWindows(true);
//                mTintManager.setTintColor(Color.parseColor("#20b0fc"));
                getSupportActionBar().show();
            }
        }
    }

    /**
     *
     */
    protected void setStatusBarHide() {
        if (Build.VERSION.SDK_INT < 16) {//before Jelly Bean Versions{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else { // Jelly Bean and up

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int ui = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(ui);

            //Hide actionbar
//            ActionBar actionBar = getActionBar();
//            actionBar.hide();
        }
    }

    /**
     * hide support action bar
     *
     * @param isHide
     */
    protected void setSystemToolbarHide(boolean isHide) {
        if (getSupportActionBar() != null) {
            if (isHide) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        onAgentResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardEvent.hideSoftInput(this, getWindow().getDecorView());

        onAgentPause();
    }

    protected void onAgentResume() {
        MobclickAgent.onResume(this);
    }

    protected void onAgentPause() {
        MobclickAgent.onPause(this);
    }


    /**
     * default nav toolbar layout resId
     *
     * @return
     */
    protected int getNavToolbarLayoutResId() {
        return R.layout.navigation_toolbar;
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (getNavToolbarLayoutResId() == 0) {//default
            super.setContentView(layoutResID);
        } else {
            LayoutInflater.from(this).inflate(layoutResID, contentLayout, true);
        }
    }

    protected void initContentView() {
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.removeAllViews();
        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        content.addView(contentLayout);
        LayoutInflater.from(this).inflate(getNavToolbarLayoutResId(), contentLayout, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dealloc();
        ActivityStack.getActivityManage().removeActivity(this);
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }


    public Toolbar getToolbar() {
        return toolbar;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public int getToolbarHeight() {
        return toolbarHeight;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = languageWork(newBase);
        super.attachBaseContext(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Build.VERSION.SDK_INT >= 26) {
            LanguageUtil.SwitchLang(this);
        }
    }

    private Context languageWork(Context context) {
        // 8.0及以上使用createConfigurationContext设置configuration
        if (Build.VERSION.SDK_INT >= 26) {
            return updateResources(context);
        } else {
            return context;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Context updateResources(Context context) {
        Resources resources = context.getResources();
        Locale locale = LanguageUtil.getLocale(context);
        if (locale==null) {
            return context;
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }


}
