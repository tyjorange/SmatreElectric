package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ReportWarnAdapter;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentReportWarnBinding;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;

import java.util.List;
import java.util.Objects;

/**
 * 预警报表
 */
public class ReportWarnFragment extends BaseFragment {
//    private List<ReportDetailBean.Warn> mListData = new ArrayList<>();

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_report_warn;
//    }

    @Override
    protected View initView() {
        FragmentReportWarnBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_report_warn, null, false);
//        ListView listView = mBinding.listWarn;//v.findViewById(R.id.list_warn);
        ReportWarnAdapter adapter = new ReportWarnAdapter(Objects.requireNonNull(getContext()));
        mBinding.listWarn.setAdapter(adapter);
        mBinding.listWarn.setLayoutManager(new LinearLayoutManager(getContext()));
//        listView.setEmptyView(mBinding.emptyLayout);

        Bundle arguments = getArguments();
        if (arguments != null) {
            ReportDetailBean parcelable = arguments.getParcelable("reportDetailBean");
            if (parcelable != null) {
                List<ReportDetailBean.Warn> list = parcelable.getWarn();
                if (list != null) {
                    adapter.addAll(list);
                }
            }
        }
        return mBinding.getRoot();
    }
}
