package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ReportWarnAdapter;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentReportWarnBinding;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警报表
 */
public class ReportWarnFragment extends BaseFragment {
    private List<ReportDetailBean.Warn> mListData = new ArrayList<>();
    private ReportWarnAdapter adapter;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_report_warn;
//    }

    @Override
    protected View initView() {
        FragmentReportWarnBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_report_warn, null, false);
        ListView listView = mBinding.listWarn;//v.findViewById(R.id.list_warn);
        adapter = new ReportWarnAdapter(getContext(), mListData);
        listView.setAdapter(adapter);
        listView.setEmptyView(mBinding.emptyLayout);

        Bundle arguments = getArguments();
        if (arguments != null) {
            ReportDetailBean parcelable = arguments.getParcelable("reportDetailBean");
            if (parcelable != null) {
                List<ReportDetailBean.Warn> list = parcelable.getWarn();
                if (list != null) {
                    mListData.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        return mBinding.getRoot();
    }
}
