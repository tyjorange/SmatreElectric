package com.rejuvee.smartelectric.family.activity.logger;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivitySecurityInformationBinding;
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

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_security_information;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivitySecurityInformationBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_security_information);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
//        ImageView img_cancel = findViewById(R.id.img_cancel);
//        img_cancel.setOnClickListener(v -> finish());
        TabLayout mTabLayout = mBinding.tabAlarm;//findViewById(R.id.tab_alarm);
        ViewPager viewPager = mBinding.viweImportInfor;//findViewById(R.id.viwe_import_infor);
        MyFragmentAdapter mInforAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mInforAdapter);
        mTabLayout.setupWithViewPager(viewPager, true);
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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

    }

    @Override
    protected void dealloc() {
    }


    class MyFragmentAdapter extends FragmentStatePagerAdapter {
        private FragmentFactory fragmentFactory = new FragmentFactory();
        private Bundle bundle;
        private List<Class> listFragments = new ArrayList<>();

        @SuppressLint("WrongConstant")
        MyFragmentAdapter(FragmentManager fm) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            bundle = new Bundle();
            bundle.putString("CollectorID", mCollectorBean.getCollectorID());
            listFragments.add(LineAlarmFrgament.class);
            listFragments.add(LineWarnFrgament.class);
            listFragments.add(LineOperateRecordFragment.class);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment instantiate = fragmentFactory.instantiate(getClassLoader(), listFragments.get(position).getName());
            instantiate.setArguments(bundle);
            return instantiate;
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
