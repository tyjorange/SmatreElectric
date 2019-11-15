package com.rejuvee.smartelectric.family.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.logger.LogDetailActivity;
import com.rejuvee.smartelectric.family.adapter.LineOperateRecordAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.databinding.FragmentMessageLogBinding;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作记录
 */
public class LineOperateRecordFragment extends BaseFragment {
    private String TAG = "LineOperateRecordFragment";
    private LineOperateRecordAdapter mAdapter;
    private List<RecordBean> mListData = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private int curPage = 0, pageSize = 20;
    private String CollectorID;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_message_log;
//    }

//    public static LineOperateRecordFragment newInstance() {
//        return new LineOperateRecordFragment();
//    }

    @Override
    protected View initView() {
        FragmentMessageLogBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_message_log, null, false);
        ListView listView = mBinding.listLogs;//v.findViewById(R.id.list_logs);
        mAdapter = new LineOperateRecordAdapter(getContext(), mListData);
        listView.setAdapter(mAdapter);
//        listView.setEmptyView(mBinding.emptyLayout);
        smartRefreshLayout = mBinding.smartRefreshLayout;//v.findViewById(R.id.smart_refreshLayout);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> doRequest());
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            doRequest();
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            RecordBean recordBean = mListData.get(position);
            if (recordBean.type == 1) {//
                String title = recordBean.name;
                ArrayList<RecordBean> listRecord = (ArrayList<RecordBean>) recordBean.switchs;
                if (listRecord != null)
                    for (RecordBean recordBean1 : listRecord) {
                        recordBean1.time = recordBean.time;
                        recordBean1.type = -1;
                    }
                Intent intent = new Intent(getActivity(), LogDetailActivity.class);
                intent.putExtra("title", title);
                intent.putParcelableArrayListExtra("records", listRecord);
                startActivity(intent);
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            CollectorID = arguments.getString("CollectorID");
        }
        doRequest();
        return mBinding.getRoot();
    }

    private void doRequest() {
        Core.instance(getContext()).getOperateRecordByCollector(curPage, pageSize, CollectorID, new ActionCallbackListener<List<RecordBean>>() {
            @Override
            public void onSuccess(List<RecordBean> data) {
                if (data != null && data.size() > 0) {
                    if (curPage == 0) {
                        mListData.clear();
                    }
                    mListData.addAll(data);
                    mAdapter.notifyDataSetChanged();
                    curPage++;
                }
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }
        });
    }

}
