package com.base.library.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.base.frame.log.DebugLog;
import com.base.frame.log.LogUtil;
import com.base.library.R;
import com.base.library.widget.MyFragmentTabHost;


/**
 * Created by shaxiaoning on 8/10/16.
 */
public final class FragmentTabConfiguration implements FragmentBuilder {
    private static final String LOGTAG = LogUtil.makeLogTag(FragmentTabConfiguration.class);
    private MyFragmentTabHost tabHost;

    private FragmentTabConfiguration(MyFragmentTabHost tabHost) {
        this.tabHost = tabHost;
        DebugLog.i(LOGTAG, "tabHost:" + tabHost.getCurFragment());
    }

    @Override
    public MyFragmentTabHost getTabHost() {
        return tabHost;
    }

    public static class Builder {
        private MyFragmentTabHost frgTabHost;
        private MyFragmentTabHost.OnFragmentChangedListener onChangedListener;
        private Context mContext;
        private FragmentManager frg;

        public Builder(Context mContext, FragmentManager frg, View parentView) {
            this.mContext = mContext;
            this.frg = frg;
            this.create(parentView);
        }

        private void create(View parentView) {
            frgTabHost = (MyFragmentTabHost) parentView.findViewById(R.id.tabhost);
            frgTabHost.setup(mContext, frg,
                    android.R.id.tabcontent);
        }

        /**
         * @param onFragmentChangedListener
         * @return
         */
        public FragmentTabConfiguration.Builder setFragmentChangedListener(MyFragmentTabHost.OnFragmentChangedListener
                                                                                   onFragmentChangedListener) {
            this.onChangedListener = onFragmentChangedListener;
            return this;
        }

        /**
         * @param cla
         * @param fgTag
         * @param tabTitle
         * @param tabResId
         * @return
         */
        public FragmentTabConfiguration.Builder addCustomTab(Class<?> cla,
                                                             String fgTag,
                                                             String tabTitle,
                                                             int tabResId) {
            View view = LayoutInflater.from(this.mContext).inflate(
                    R.layout.tab_customtab, null);
            ImageView image = (ImageView) view.findViewById(R.id.tab_icon);
            TextView text = (TextView) view.findViewById(R.id.tabtitle);
            image.setBackgroundResource(tabResId);
            text.setText(tabTitle);
            TabHost.TabSpec spec = frgTabHost.newTabSpec(fgTag);
            spec.setIndicator(view);
            frgTabHost.addTab(spec, cla, null);
            return this;
        }

        public FragmentTabConfiguration build() {
            frgTabHost.setChanagedListener(onChangedListener);
            return new FragmentTabConfiguration(frgTabHost);
        }
    }


}
