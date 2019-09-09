package com.base.library.core;

import android.os.Bundle;
import android.view.View;

import com.base.frame.log.LogUtil;


/**
 * toolbar base activity
 * Created by admin on 4/2/16.
 * http://stackoverflow.com/questions/31788678/android-toolbar-back-arrow-with-icon-like-whatsapp
 */
public abstract class AbstractBaseToolbarActivity extends AbstractSwipeBackBaseActivity implements View.OnClickListener {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseToolbarActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
