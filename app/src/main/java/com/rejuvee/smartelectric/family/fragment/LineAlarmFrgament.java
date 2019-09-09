package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.LineAlarmAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警记录
 */
public class LineAlarmFrgament extends BaseFragment {
    private LineAlarmAdapter mAdapter;
    private List<RecordBean> mListData = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private int curPage = 0, pageSize = 20;
    private LoadingDlg loadingDlg;
    private String CollectorID;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_line_alarm;
    }

//    public static LineAlarmFrgament newInstance() {
//        return new LineAlarmFrgament();
//    }

    @Override
    protected void initView(View v) {
        loadingDlg = new LoadingDlg(getContext(), -1);
        ListView listView = (ListView) v.findViewById(R.id.list_alarms);
        mAdapter = new LineAlarmAdapter(getContext(), mListData);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(v.findViewById(R.id.empty_layout));

        smartRefreshLayout = (SmartRefreshLayout) v.findViewById(R.id.smart_refreshLayout);
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
        Core.instance(getContext()).getAlarmListByCollector(curPage, pageSize, CollectorID, new ActionCallbackListener<List<RecordBean>>() {
            @Override
            public void onSuccess(List<RecordBean> data) {
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
