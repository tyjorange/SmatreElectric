package com.rejuvee.smartelectric.family.activity.report;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ReportAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ReportBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表
 */
public class ReportActivity extends BaseActivity {
    private CollectorBean collectorBean;
    private ListView listView;
    private ReportAdapter adapter;
    private List<ReportBean> mListData = new ArrayList<>();
    private Context mContext;
    private LoadingDlg loadingDlg;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_report;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadingDlg = new LoadingDlg(this, -1);
        listView = findViewById(R.id.report_list);
        adapter = new ReportAdapter(this, mListData);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty_layout));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ReportDetailActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                intent.putExtra("reportBean", mListData.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        getReportList();
    }

    private void getReportList() {
        loadingDlg.show();
        Core.instance(this).getReportList(0, 99, new ActionCallbackListener<List<ReportBean>>() {

            @Override
            public void onSuccess(List<ReportBean> data) {
                mListData.clear();
                mListData.addAll(data);
                adapter.notifyDataSetChanged();
                loadingDlg.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(mContext, message);
                loadingDlg.dismiss();
            }
        });
    }

    @Override
    protected void dealloc() {

    }
}
