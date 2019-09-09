package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ReportAlarmAdapter;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警报表
 */
public class ReportAlarmFragment extends BaseFragment {
    private List<ReportDetailBean.Alarm> mListData = new ArrayList<>();
    private ReportAlarmAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_report_alarm;
    }

    @Override
    protected void initView(View v) {
        ListView listView = (ListView) v.findViewById(R.id.list_alarm);
        adapter = new ReportAlarmAdapter(getContext(), mListData);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_layout));
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            ReportDetailBean parcelable = arguments.getParcelable("reportDetailBean");
            if (parcelable != null) {
                List<ReportDetailBean.Alarm> list = parcelable.getAlarm();
                System.out.println("ReportAlarmFragment " + list);
                if (list != null) {
                    mListData.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
