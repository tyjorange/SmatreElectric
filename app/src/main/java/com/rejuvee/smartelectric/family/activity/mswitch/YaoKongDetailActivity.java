package com.rejuvee.smartelectric.family.activity.mswitch;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.ArrayList;
import java.util.List;

public class YaoKongDetailActivity extends BaseActivity {

    private List<SwitchBean> mListData = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_yao_kong_detail;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mListData = getIntent().getParcelableExtra("SwitchBeanList");
    }

    @Override
    protected void initData() {

    }

    private void doS() {
        for (SwitchBean c : mListData) {

        }
    }

    @Override
    protected void dealloc() {

    }
}
