package com.base.library.core;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.base.library.R;
import com.base.library.widget.MyFragmentTabHost;

import java.util.List;

/**
 * Created by kimhuang on 2018/4/17.
 */
public abstract class AbstractBaseFragmentActivity extends AbstractBaseToolbarCoreActivity {
    private MyFragmentTabHost mTabHost = null;

    protected abstract List<Class> initFragment();

    @Override
    protected int getLayoutResId() {
        return R.layout.base_activity_with_fragment_layout;
    }

    @Override
    protected void initView() {
        mTabHost = initFragment(this, findViewById(R.id.parentView),
                getSupportFragmentManager(),
                new MyFragmentTabHost.OnFragmentChangedListener() {
                    @Override
                    public void onChanaged(Fragment fragments) {
                        fragments.setArguments(getIntent().getExtras());
                    }
                });

        for (int i = 0; i < initFragment().size(); i ++) {
            addCustomTab(this, "页面" + i, "fragment_" + i, 0,
                    initFragment().get(i), mTabHost);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTabHost.getCurFragment().onActivityResult(requestCode, resultCode, data);
    }

    public MyFragmentTabHost initFragment(Context mContext,
                                          View parentView,
                                          FragmentManager frg,
                                          MyFragmentTabHost.OnFragmentChangedListener
                                                  onFragmentChangedListener) {
        MyFragmentTabHost mTabHost = (MyFragmentTabHost) parentView.findViewById(R.id.tabhost);
        mTabHost.setChanagedListener(onFragmentChangedListener);
        mTabHost.setup(mContext, frg, android.R.id.tabcontent);
        if (disableScroll()) {
            // TODO: 实现禁用左右滑
        }
        return mTabHost;
    }

    public void addCustomTab(Context context, String tabHostTitle, String tag,
                             int resId, Class<?> c, MyFragmentTabHost fth) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.tab_customtab, null);
        ImageView image = (ImageView) view.findViewById(R.id.tab_icon);
        TextView text = (TextView) view.findViewById(R.id.tabtitle);
        image.setBackgroundResource(resId);
        text.setText(tabHostTitle);
        TabHost.TabSpec spec = fth.newTabSpec(tag);
        spec.setIndicator(view);
        fth.addTab(spec, c, null);
    }

    public void changeFragment(int index) {
        mTabHost.setCurrentTab(index);
    }

    public Fragment getFragment() {
        return mTabHost.getCurFragment();
    }

    protected boolean disableScroll() {
        return false;
    }
}
