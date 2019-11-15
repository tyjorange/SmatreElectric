package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ReportAlarmAdapter;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentReportAlarmBinding;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警报表
 */
public class ReportAlarmFragment extends BaseFragment {
    private List<ReportDetailBean.Alarm> mListData = new ArrayList<>();

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_report_alarm;
//    }

    @Override
    protected View initView() {
        FragmentReportAlarmBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_report_alarm, null, false);
        ListView listView = mBinding.listAlarm;//v.findViewById(R.id.list_alarm);
        ReportAlarmAdapter adapter = new ReportAlarmAdapter(getContext(), mListData);
        listView.setAdapter(adapter);
//        listView.setEmptyView(mBinding.emptyLayout);

        Bundle arguments = getArguments();
        if (arguments != null) {
            ReportDetailBean parcelable = arguments.getParcelable("reportDetailBean");
            if (parcelable != null) {
                List<ReportDetailBean.Alarm> list = parcelable.getAlarm();
                if (list != null) {
                    mListData.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        return mBinding.getRoot();
    }
}
