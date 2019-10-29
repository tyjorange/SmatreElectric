package com.rejuvee.smartelectric.family.activity.timer;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListTimerSwitchAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class TimerSwitchActivity extends BaseActivity implements ListTimerSwitchAdapter.MyListener {
    private String TAG = "TimerSwitchActivity";
    //    private ListView mListLineView;
    private ListView mListTimeTaskView;
    private ImageView backBtn;
    //    private ImageView ivAdd;

    private List<SwitchBean> mListData = new ArrayList<>();
    private ListTimerSwitchAdapter mListTimerSwitchAdapter;

    private String currentBreakId;
    private int currentPosition = 0;
    private LoadingDlg mWaitDialog;
    private CollectorBean mDevicollectorBeane;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_timer_switch;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mDevicollectorBeane = getIntent().getParcelableExtra("collectorBean");
//        mListLineView = (ListView) findViewById(R.id.lv_left);
        backBtn = findViewById(R.id.img_cancel);
        backBtn.setOnClickListener(v -> finish());
        mListTimeTaskView = findViewById(R.id.lv_right);
        TextView collectorName = findViewById(R.id.tv_device);
        collectorName.setText(mDevicollectorBeane.getDeviceName());
        mWaitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        getData();
    }

    private void setupData() {
        mListTimerSwitchAdapter = new ListTimerSwitchAdapter(TimerSwitchActivity.this, mListData, this);
        mListTimeTaskView.setAdapter(mListTimerSwitchAdapter);
    }

    @Override
    protected void dealloc() {

    }

    private void getData() {
        mWaitDialog.show();
        Core.instance(this).getSwitchByCollector(mDevicollectorBeane.getCode(), "no", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                mListData.clear();
                mListData.addAll(data);
                setupData();
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mWaitDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickItem(String switchId) {
        Intent intent = new Intent(TimerSwitchActivity.this, TimerActivity.class);
        intent.putExtra("switchId", switchId);
        startActivity(intent);
    }

}
