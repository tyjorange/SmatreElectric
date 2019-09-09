package com.base.library.core;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.log.LogUtil;
import com.base.frame.os.ActivityStack;
import com.base.library.R;

/**
 * toolbar base activity
 * Created by admin on 4/2/16.
 * http://stackoverflow.com/questions/31788678/android-toolbar-back-arrow-with-icon-like-whatsapp
 */
public abstract class AbstractBaseToolbarChatActivity extends AbstractSwipeBackBaseActivity
        implements View.OnClickListener {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseToolbarChatActivity.class);
    private Toolbar toolbar;
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

    abstract protected void dealloc();

    /**
     * 是否显示返回键
     *
     * @return
     */
    abstract protected boolean isDisplayHomeAsUpEnabled();

    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.getActivityManage().addActivity(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());//把设置布局文件的操作交给继承的子类
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        if (toolbar == null) {
            initView();
            initData();
            return;
        }
        setSupportActionBar(toolbar);
        //是否显示toolbar 自带title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (isDisplayHomeAsUpEnabled()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_back_icon);
            // toolbar.seton
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId) {
                    //case R.id.
                }
//
                return true;
            }
        });
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

        initView();
        initData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dealloc();
        ActivityStack.getActivityManage().removeActivity(this);
    }


}
