package com.rejuvee.smartelectric.family.activity.report;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityReportDetailBinding;
import com.rejuvee.smartelectric.family.fragment.ReportAlarmFragment;
import com.rejuvee.smartelectric.family.fragment.ReportPowerFragment;
import com.rejuvee.smartelectric.family.fragment.ReportWarnFragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 报表详情
 */
public class ReportDetailActivity extends BaseActivity {
    private CollectorBean collectorBean;
    private ReportBean reportBean;
    //    private Context mContext;
    private LoadingDlg loadingDlg;
    //    private TabLayout mTabLayout;
//    private ViewPager viewPager;
    private ReportDetailBean reportDetailBean;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_report_detail;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivityReportDetailBinding mBinding;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_report_detail);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        reportBean = getIntent().getParcelableExtra("reportBean");
        loadingDlg = new LoadingDlg(this, -1);
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        mTabLayout = findViewById(R.id.tab_report);
//        viewPager = findViewById(R.id.vp_report);
//        TextView textViewTitle = findViewById(R.id.tv_report_title);
//        String a = reportBean.timeType == 1 ? getString(R.string.vs88) : getString(R.string.vs137);
        mBinding.tvReportTitle.setText(String.format("%s %s - %s", reportBean.timeType == 1 ? getString(R.string.vs88) : getString(R.string.vs137), reportBean.startDay, reportBean.endDay));
        getReport();
    }

    /**
     * 获取报表详情
     */
    private void getReport() {
        loadingDlg.show();
        currentCall = Core.instance(this).getReport(reportBean.id, collectorBean.getCollectorID(), new ActionCallbackListener<ReportDetailBean>() {
            @Override
            public void onSuccess(ReportDetailBean data) {
                reportDetailBean = data;
                fillData();
                loadingDlg.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(ReportDetailActivity.this, message);
                fillData();
                loadingDlg.dismiss();
            }
        });
    }

    private void fillData() {
        MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager(), reportDetailBean);
        mBinding.vpReport.setAdapter(mAdapter);
        mBinding.tabReport.setupWithViewPager(mBinding.vpReport, true);
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    class MyFragmentAdapter extends FragmentStatePagerAdapter {
        private FragmentFactory fragmentFactory = new FragmentFactory();
        private Bundle bundle;
        private List<Class> listFragments = new ArrayList<>();

        @SuppressLint("WrongConstant")
        MyFragmentAdapter(FragmentManager fm, ReportDetailBean reportDetailBean) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            bundle = new Bundle();
            bundle.putParcelable("reportDetailBean", reportDetailBean);
            listFragments.add(ReportAlarmFragment.class);
            listFragments.add(ReportWarnFragment.class);
            listFragments.add(ReportPowerFragment.class);
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
                return getString(R.string.electric_quantity);
            }
            return super.getPageTitle(position);
        }
    }
}
