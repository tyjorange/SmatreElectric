package com.base.library.core;

import com.base.frame.log.DebugLog;
import com.base.frame.log.LogUtil;
/**
 * Lazy base fragment
 * Created by admin on 7/20/16.
 */
public abstract class AbstractLazyBaseFragment extends AbstractBaseFragment {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractLazyBaseFragment.class);
    /**
     *
     */
    private boolean isLoaded = false;

    /**
     * request net data
     */
    protected abstract void onLazyLoadData();

    /**
     * visible
     */
    protected abstract void onVisible();

    /**
     * Invisible
     */
    protected abstract void onInvisible();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        DebugLog.i(LOGTAG, "isVisibleToUser:" + isVisibleToUser);
        if (isVisibleToUser) {
            onVisible(); //visible
        } else {
            onInvisible();//invisible
        }

        if (isVisibleToUser && !isLoaded()) {//request data
            isLoaded = true;//
            onLazyLoadData();//request data ，request failure set isLoaded=false

        }

    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }
}
