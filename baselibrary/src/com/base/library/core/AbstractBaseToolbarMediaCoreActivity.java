package com.base.library.core;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.base.frame.log.LogUtil;
import com.base.library.R;


/**
 * toolbar base activity
 * <p/>
 * <p/>
 * <p/>
 * Created by admin on 4/2/16.
 * <p/>
 * http://stackoverflow.com/questions/31788678/android-toolbar-back-arrow-with-icon-like-whatsapp
 */
public abstract class AbstractBaseToolbarMediaCoreActivity extends AbstractBaseCoreActivity {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseToolbarMediaCoreActivity.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected boolean calculateToolbarHeight() {
        return true;
    }



    protected void setFitsSystemWindowsTrue() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getToolbar().getLayoutParams().height = getStatusBarHeight() + getToolbarHeight();
        }
    }

    protected void setFitsSystemWindowsFalse() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //  this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getToolbar().getLayoutParams().height = getToolbarHeight();
            int size = getResources().getDimensionPixelOffset(R.dimen.global_base_text_size_14);
            getToolbar().setPaddingRelative(0, 0, 0, getToolbarHeight() / 2 - size * 2);
        }
    }
}
