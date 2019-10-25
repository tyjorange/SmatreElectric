package com.rejuvee.smartelectric.family.activity.logger;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.fragment.LineAlarmFrgament;
import com.rejuvee.smartelectric.family.fragment.LineOperateRecordFragment;
import com.rejuvee.smartelectric.family.fragment.LineWarnFrgament;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全信息 (告警 预警 操作记录)
 */
public class SecurityInformationActivity extends BaseActivity {
    private CollectorBean mCollectorBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_security_information;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
        ImageView img_cancel = findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_alarm);
        ViewPager view_import_infor = (ViewPager) findViewById(R.id.viwe_import_infor);
        MyFragmentAdapter mInforAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        view_import_infor.setAdapter(mInforAdapter);
        mTabLayout.setupWithViewPager(view_import_infor, true);
    }

    @Override
    protected void initData() {

    }

//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.important_info);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {
    }


    class MyFragmentAdapter extends FragmentPagerAdapter {

        private Bundle bundle;
        private List<Class> listFragments = new ArrayList<>();

        MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            bundle = new Bundle();
            bundle.putString("CollectorID", mCollectorBean.getCollectorID());
            listFragments.add(LineAlarmFrgament.class);
            listFragments.add(LineWarnFrgament.class);
            listFragments.add(LineOperateRecordFragment.class);
        }

        @Override
        public Fragment getItem(int position) {
            Class fragment = listFragments.get(position);
            return Fragment.instantiate(SecurityInformationActivity.this, fragment.getName(), bundle);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.fault_alarm);
            } else if (position == 1) {
                return getString(R.string.fault_warn);
            } else if (position == 2) {
                return getString(R.string.log);
            }
            return super.getPageTitle(position);
        }
    }
}
