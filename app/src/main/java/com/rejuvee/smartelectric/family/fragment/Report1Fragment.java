package com.rejuvee.smartelectric.family.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.report.ReportDetailActivity;
import com.rejuvee.smartelectric.family.adapter.ReportAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentReport1monthBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;

import java.util.List;
import java.util.Objects;

public class Report1Fragment extends BaseFragment {
    private ReportAdapter adapter;
//    private List<ReportBean> mListData = new ArrayList<>();

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_report_1month;
//    }

    @Override
    protected View initView() {
        FragmentReport1monthBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_report_1month, null, false);
        CollectorBean collectorBean = Objects.requireNonNull(getArguments()).getParcelable("collectorBean");
//        ListView listView = mBinding.reportList1;//v.findViewById(R.id.report_list_1);
        adapter = new ReportAdapter(Objects.requireNonNull(getContext()));
        mBinding.reportList1.setAdapter(adapter);
        mBinding.reportList1.setLayoutManager(new LinearLayoutManager(getContext()));
//        listView.setEmptyView(mBinding.emptyLayout);
        adapter.setCallback(bean -> {
            Intent intent = new Intent(getContext(), ReportDetailActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            intent.putExtra("reportBean", bean);
            startActivity(intent);
        });
//        mBinding.reportList1.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(getContext(), ReportDetailActivity.class);
//            intent.putExtra("collectorBean", collectorBean);
//            intent.putExtra("reportBean", mListData.get(position));
//            startActivity(intent);
//        });
        getReportList();
        return mBinding.getRoot();
    }

    private void getReportList() {
//        loadingDlg.show();
        Core.instance(getContext()).getReportList(1, new ActionCallbackListener<List<ReportBean>>() {

            @Override
            public void onSuccess(List<ReportBean> data) {
//                mListData.clear();
//                mListData.addAll(data);
                adapter.addAll(data);
//                adapter.notifyDataSetChanged();
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
