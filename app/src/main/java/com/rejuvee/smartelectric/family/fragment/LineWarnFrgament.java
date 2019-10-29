package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.LineWarnAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.model.bean.WarnBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警记录
 */
public class LineWarnFrgament extends BaseFragment {
    private LineWarnAdapter mAdapter;
    private List<WarnBean> mListData = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private int curPage = 0, pageSize = 20;
    private LoadingDlg loadingDlg;
    private String CollectorID;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_line_alarm;
    }

    @Override
    protected void initView(View v) {
        loadingDlg = new LoadingDlg(getContext(), -1);
        ListView listView = v.findViewById(R.id.list_alarms);
        mAdapter = new LineWarnAdapter(getContext(), mListData);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(v.findViewById(R.id.empty_layout));

        smartRefreshLayout = v.findViewById(R.id.smart_refreshLayout);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                doRequest(false);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPage = 0;
                doRequest(false);
            }
        });
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            CollectorID = arguments.getString("CollectorID");
        }
        doRequest(true);
    }

    private void doRequest(boolean showDlg) {
        if (showDlg) {
            loadingDlg.show();
        }
        Core.instance(getContext()).getWarnListByCollector(curPage, pageSize, CollectorID, new ActionCallbackListener<List<WarnBean>>() {
            @Override
            public void onSuccess(List<WarnBean> data) {
                if (data != null && data.size() > 0) {
                    if (curPage == 0) {
                        mListData.clear();
                    }
//                    Iterator<RecordBean> iterator = data.iterator();
//                    while (iterator.hasNext()) {
//                        RecordBean value = iterator.next();
//                        String switchFaultState = SwitchBean.getSwitchFaultState(getContext(), value.state);
//                        if (switchFaultState.equals("手动拉闸")) {
//                            iterator.remove();
//                        }
//                    }
                    mListData.addAll(data);
                    mAdapter.notifyDataSetChanged();
                    curPage++;
                }
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                loadingDlg.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                loadingDlg.dismiss();
            }
        });
    }
}
