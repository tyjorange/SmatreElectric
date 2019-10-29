package com.rejuvee.smartelectric.family.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.report.ReportDetailActivity;
import com.rejuvee.smartelectric.family.adapter.ReportAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;

import java.util.ArrayList;
import java.util.List;

public class Report6Fragment extends BaseFragment {
    private ReportAdapter adapter;
    private List<ReportBean> mListData = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_report_6month;
    }

    @Override
    protected void initView(View v) {
        CollectorBean collectorBean = getArguments().getParcelable("collectorBean");
        ListView listView = v.findViewById(R.id.report_list_6);
        adapter = new ReportAdapter(v.getContext(), mListData);
        listView.setAdapter(adapter);
        listView.setEmptyView(v.findViewById(R.id.empty_layout));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(v.getContext(), ReportDetailActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            intent.putExtra("reportBean", mListData.get(position));
            startActivity(intent);
        });
    }

    @Override
    protected void initData() {
        getReportList();
    }

    private void getReportList() {
//        loadingDlg.show();
        Core.instance(getContext()).getReportList(6, new ActionCallbackListener<List<ReportBean>>() {

            @Override
            public void onSuccess(List<ReportBean> data) {
                mListData.clear();
                mListData.addAll(data);
                adapter.notifyDataSetChanged();
//                loadingDlg.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(getContext(), message);
//                loadingDlg.dismiss();
            }
        });
    }
}
