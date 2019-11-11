package com.rejuvee.smartelectric.family.activity.report;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityReportBinding;
import com.rejuvee.smartelectric.family.fragment.Report1Fragment;
import com.rejuvee.smartelectric.family.fragment.Report3Fragment;
import com.rejuvee.smartelectric.family.fragment.Report6Fragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表
 */
public class ReportActivity extends BaseActivity {
    private CollectorBean collectorBean;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_report;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityReportBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        TabLayout mTabLayout = findViewById(R.id.tab_report_list);
//        ViewPager viewPager = findViewById(R.id.vp_report_list);
        MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mBinding.vpReportList.setAdapter(mAdapter);
        mBinding.tabReportList.setupWithViewPager(mBinding.vpReportList, true);
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

    }
    @Override
    protected void dealloc() {

    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        private Bundle bundle;
        private List<Class> listFragments = new ArrayList<>();

        MyFragmentAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            bundle = new Bundle();
            bundle.putParcelable("collectorBean", collectorBean);
            listFragments.add(Report1Fragment.class);
            listFragments.add(Report3Fragment.class);
            listFragments.add(Report6Fragment.class);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Class fragment = listFragments.get(position);
            return Fragment.instantiate(ReportActivity.this, fragment.getName(), bundle);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.vs252);
            } else if (position == 1) {
                return getString(R.string.vs253);
            } else if (position == 2) {
                return getString(R.string.vs254);
            }
            return super.getPageTitle(position);
        }
    }
}
