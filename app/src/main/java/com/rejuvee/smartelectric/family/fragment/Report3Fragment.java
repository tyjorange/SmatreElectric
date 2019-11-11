package com.rejuvee.smartelectric.family.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.report.ReportDetailActivity;
import com.rejuvee.smartelectric.family.adapter.ReportAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentReport3monthBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;

import java.util.ArrayList;
import java.util.List;

public class Report3Fragment extends BaseFragment {
    private ReportAdapter adapter;
    private List<ReportBean> mListData = new ArrayList<>();

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_report_3month;
//    }

    @Override
    protected View initView() {
        FragmentReport3monthBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_report_3month, null, false);
        CollectorBean collectorBean = getArguments().getParcelable("collectorBean");
        ListView listView = mBinding.reportList3;// v.findViewById(R.id.report_list_3);
        adapter = new ReportAdapter(getContext(), mListData);
        listView.setAdapter(adapter);
        listView.setEmptyView(mBinding.emptyLayout);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), ReportDetailActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            intent.putExtra("reportBean", mListData.get(position));
            startActivity(intent);
        });
        getReportList();
        return mBinding.getRoot();
    }

    private void getReportList() {
//        loadingDlg.show();
        Core.instance(getContext()).getReportList(3, new ActionCallbackListener<List<ReportBean>>() {

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
