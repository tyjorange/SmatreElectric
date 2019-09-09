package com.base.library.core;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.frame.log.LogUtil;

/**
 * abstract base Fragment
 * Created by admin on 7/19/16.
 */
public abstract class AbstractBaseFragment extends Fragment {
    private static final String LOGTAG = LogUtil.makeLogTag(AbstractBaseFragment.class);

    /**
     * get layout resource
     */
    protected abstract int getLayoutResId();

    /**
     *
     */
    protected abstract void initView(View v);

    /**
     *
     */
    protected abstract void initData();


    protected View mView;


    public AbstractBaseFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            if (getLayoutResId() == 0)
                return super.onCreateView(inflater, container, savedInstanceState);
            mView = inflater.inflate(getLayoutResId(), container, false);
            initView(mView);
            initData();

        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }
    protected int getScreenHeight() {
        return getActivity().findViewById(android.R.id.content).getHeight();
    }

    @Override
    public void onResume() {
        super.onResume();
        onAgentPageStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        onAgentPageEnd();
    }

    protected void onAgentPageStart() {
        //MobclickAgent.onPageStart(getClass().getSimpleName());
//        Agent.onPageStart(getActivity(), getClass().getSimpleName());
    }

    protected void onAgentPageEnd() {
        //MobclickAgent.onPageEnd(getClass().getSimpleName());
//        Agent.onPageEnd(getActivity(), getClass().getSimpleName(), null);
    }


}
