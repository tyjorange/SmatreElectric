package com.base.library.core;

import android.os.Bundle;

import com.base.frame.log.LogUtil;


/**
 * toolbar base activity
 * <p/>
 * <p/>
 * <p/>
 * Created by admin on 4/2/16.
 * <p/>
 * http://stackoverflow.com/questions/31788678/android-toolbar-back-arrow-with-icon-like-whatsapp
 */
public abstract class AbstractBaseToolbarCoreActivity extends AbstractBaseCoreActivity {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseToolbarCoreActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * hide support action bar
     *
     * @param isHide
     */
    protected void setToolbarHide(boolean isHide) {
        if (getSupportActionBar() != null) {
            if (isHide) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }
    }


}
