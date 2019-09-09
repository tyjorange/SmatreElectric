package com.base.library.core;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.base.frame.log.LogUtil;
import com.base.library.utils.FragmentBuilder;

/**
 * toolbar base activity
 * Created by admin on 4/2/16.
 * http://stackoverflow.com/questions/31788678/android-toolbar-back-arrow-with-icon-like-whatsapp
 */
public abstract class AbstractBaseTabToolbarActivity extends AbstractSwipeBackBaseActivity implements View.OnClickListener {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseTabToolbarActivity.class);

    /**
     * init tab
     *
     * @return
     */
    abstract protected FragmentBuilder initTab();

    abstract protected void dealloc();

    private LinearLayout contentLayout;
    private FragmentBuilder fragmentBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public FragmentBuilder getFragmentBuilder() {
        return fragmentBuilder;
    }

    public void setFragmentBuilder(FragmentBuilder fragmentBuilder) {
        this.fragmentBuilder = fragmentBuilder;
    }



}
