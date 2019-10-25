package com.rejuvee.smartelectric.family.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.logger.LogDetailActivity;
import com.rejuvee.smartelectric.family.adapter.LineOperateRecordAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作记录
 */
public class LineOperateRecordFragment extends BaseFragment {
    private LineOperateRecordAdapter mAdapter;
    private List<RecordBean> mListData = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private int curPage = 0, pageSize = 20;
    private String CollectorID;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_message_log;
    }

//    public static LineOperateRecordFragment newInstance() {
//        return new LineOperateRecordFragment();
//    }

    @Override
    protected void initView(View v) {
        ListView listView = (ListView) v.findViewById(R.id.list_logs);
        mAdapter = new LineOperateRecordAdapter(getContext(), mListData);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(v.findViewById(R.id.empty_layout));
        smartRefreshLayout = (SmartRefreshLayout) v.findViewById(R.id.smart_refreshLayout);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                doRequest();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPage = 0;
                doRequest();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            }
        });
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            CollectorID = arguments.getString("CollectorID");
        }
        doRequest();
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
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
            }
        });
    }

}
