package com.rejuvee.smartelectric.family.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.LineWarnAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.FragmentLineAlarmBinding;
import com.rejuvee.smartelectric.family.model.bean.WarnBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警记录
 */
public class LineWarnFrgament extends BaseFragment {
    private String TAG = "LineWarnFrgament";
    private LineWarnAdapter mAdapter;
    private List<WarnBean> mListData = new ArrayList<>();
    private SmartRefreshLayout smartRefreshLayout;
    private int curPage = 0, pageSize = 20;
    private LoadingDlg loadingDlg;
    private String CollectorID;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_line_alarm;
//    }

    @Override
    protected View initView() {
        FragmentLineAlarmBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_line_alarm, null, false);
        loadingDlg = new LoadingDlg(getContext(), -1);
        ListView listView = mBinding.listAlarms;//v.findViewById(R.id.list_alarms);
        mAdapter = new LineWarnAdapter(getContext(), mListData);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(mBinding.emptyLayout);

        smartRefreshLayout = mBinding.smartRefreshLayout;//v.findViewById(R.id.smart_refreshLayout);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> doRequest(false));
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            curPage = 0;
            doRequest(false);
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            CollectorID = arguments.getString("CollectorID");
        }
        doRequest(true);
        return mBinding.getRoot();
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
                Log.e(TAG, message);
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                loadingDlg.dismiss();
            }
        });
    }
}
