package com.rejuvee.smartelectric.family.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rejuvee.smartelectric.family.common.manager.ActivityFragmentManager;

/**
 * 所有活动的基类
 *
 * @author tyj
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //每创建一个活动，就加入到活动管理器中
        ActivityFragmentManager.addFragment(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //每销毁一个活动，就从活动管理器中移除
        ActivityFragmentManager.removeFragment(this);
    }

    protected abstract View initView();

//    @Override
//    protected void initData() {
//
//    }
}
