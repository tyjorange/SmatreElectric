package com.rejuvee.smartelectric.family.common;

import android.os.Bundle;
import android.view.View;

import com.base.library.core.AbstractBaseFragment;

/**
 * 所有活动的基类
 *
 * @author tyj
 */
public class BaseFragment extends AbstractBaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //每创建一个活动，就加入到活动管理器中
        ActivityFragmentManager.addFragment(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //每销毁一个活动，就从活动管理器中移除
        ActivityFragmentManager.removeFragment(this);
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void initData() {

    }
}
