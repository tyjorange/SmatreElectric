package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ReportPowerAdapter;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentReportPowerBinding;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;

import java.util.List;
import java.util.Objects;

/**
 * 电量报表
 */
public class ReportPowerFragment extends BaseFragment {
//    private List<ReportDetailBean.Power> mListData = new ArrayList<>();

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_report_power;
//    }

    @Override
    protected View initView() {
        FragmentReportPowerBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_report_power, null, false);
//        ListView listView = mBinding.listPower;//v.findViewById(R.id.list_power);
        ReportPowerAdapter adapter = new ReportPowerAdapter(Objects.requireNonNull(getContext()));
        mBinding.listPower.setAdapter(adapter);
        mBinding.listPower.setLayoutManager(new LinearLayoutManager(getContext()));
//        listView.setEmptyView(mBinding.emptyLayout);

        Bundle arguments = getArguments();
        if (arguments != null) {
            ReportDetailBean parcelable = arguments.getParcelable("reportDetailBean");
            if (parcelable != null) {
                List<ReportDetailBean.Power> list = parcelable.getPower();
                if (list != null) {
                    adapter.addAll(list);
                }
            }
        }
        return mBinding.getRoot();
    }
}
