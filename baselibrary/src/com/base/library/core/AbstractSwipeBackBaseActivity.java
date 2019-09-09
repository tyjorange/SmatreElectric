package com.base.library.core;

import android.os.Bundle;
import android.view.View;

import com.base.frame.greenandroid.swipebacklayout.SwipeBackLayout;
import com.base.frame.greenandroid.swipebacklayout.Utils;
import com.base.frame.greenandroid.swipebacklayout.app.SwipeBackActivityBase;
import com.base.frame.greenandroid.swipebacklayout.app.SwipeBackActivityHelper;


/**
 * base activity
 * <p/>
 * 可左滑关闭activity
 * <p/>
 * Created by admin on 4/2/16.
 */
public abstract class AbstractSwipeBackBaseActivity extends AbstractBaseCoreActivity implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

}
