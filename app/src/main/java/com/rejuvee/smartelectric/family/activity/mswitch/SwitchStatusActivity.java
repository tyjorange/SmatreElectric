package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.custom.FlushTimeTask;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchSignalItem;

import java.text.DecimalFormat;
import java.util.List;
import java.util.TimerTask;

/**
 * 实时情况
 */
public class SwitchStatusActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SwitchStatusActivity";
    private CollectorBean collectorBean;
    private SwitchBean curBreaker;
    private TextView line_name;
    private ImageView online_icon;
    private TextView online_text;
    private TextView dl_val;
    private TextView dy_val;
    private TextView ldl_val;
    private TextView wd_val;
    private TextView ygdy_val;
    private TextView switch_ver;
    private Handler mHandler;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_switch_status;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        ImageView backBtn = findViewById(R.id.img_cancel);
        LinearLayout change = findViewById(R.id.img_change);
        line_name = findViewById(R.id.line_name);
        online_icon = findViewById(R.id.online_icon);
        online_text = findViewById(R.id.online_text);
        dl_val = findViewById(R.id.dl_val);
        dy_val = findViewById(R.id.dy_val);
        ldl_val = findViewById(R.id.ldl_val);
        wd_val = findViewById(R.id.wd_val);
        ygdy_val = findViewById(R.id.ygdy_val);
        switch_ver = findViewById(R.id.switch_ver);
        backBtn.setOnClickListener(this);
        change.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_SWTCH_REFRESH_TASK) {
                    getSwitchState();
                }
            }
        };
        getSwitchByCollector();
    }

    private static final int MSG_SWTCH_REFRESH_TASK = 5123;// 刷新单个线路 定时任务id
    private FlushTimeTask flushTimeTask;
    private final int flushTimeMill = 3000;//刷新间隔
    private boolean runTask = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runTask = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        runTask = false;
    }

    /**
     * 开始定时任务
     */
    private void startTimer() {
        flushTimeTask = new FlushTimeTask(flushTimeMill, new TimerTask() {
            @Override
            public void run() {
                if (runTask) {
                    Log.d(TAG, "FlushTimeTask run");
                    mHandler.sendEmptyMessage(MSG_SWTCH_REFRESH_TASK);
                }
            }
        });
        flushTimeTask.start();
    }

    /**
     * 停止定时任务
     */
    private void stopTimer() {
        flushTimeTask.stop();
    }

    private DecimalFormat df = new DecimalFormat("00");

    /**
     * 获取信号值
     *
     * @param switchBean
     */
    public void getBreakSignalValue(SwitchBean switchBean) {
        line_name.setText("线路:" + switchBean.getName());
        String s = df.format(switchBean.getModelMajor()) +
                df.format(switchBean.getModelMinor()) +
                df.format(switchBean.getVerMajor()) +
                df.format(switchBean.getVerMinor());
        switch_ver.setText(s);
        Core.instance(this).getSignals(switchBean.getSwitchID(), new ActionCallbackListener<List<SwitchSignalItem>>() {
            @Override
            public void onSuccess(List<SwitchSignalItem> data) {
                for (SwitchSignalItem item : data) {
                    switch (Integer.valueOf(item.getSignalsTypeID())) {
                        case 1://电流
                            dl_val.setText(item.getValue() + item.getUnit());
                            break;
                        case 3://当前月有功电量
                            ygdy_val.setText(item.getValue() + item.getUnit());
                            break;
                        case 4://电压
                            dy_val.setText(item.getValue() + item.getUnit());
                            break;
                        case 5://功率因数
                            break;
                        case 6://频率
                            break;
                        case 7://温度
                            wd_val.setText(item.getValue() + item.getUnit());
                            break;
                        case 8://无功电量
                            break;
                        case 9://无功功率
                            break;
                        case 10://有功电量
                            break;
                        case 11://有功功率
                            break;
                        case 12://漏电流
                            ldl_val.setText(item.getValue() + item.getUnit());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                online_icon.setVisibility(View.INVISIBLE);
//                online_text.setText("-");
                dl_val.setText("-");
                dy_val.setText("-");
                wd_val.setText("-");
                ldl_val.setText("-");
                ygdy_val.setText("-");
                switch_ver.setText("-");
                CustomToast.showCustomErrorToast(SwitchStatusActivity.this, message);
            }
        });
    }

    @Override
    protected void dealloc() {

    }

    /**
     * 刷新单条线路状态
     */
    private void getSwitchState() {
        if (curBreaker == null) {
            return;
        }
        Core.instance(this).getSwitchState(curBreaker.getSerialNumber(), new ActionCallbackListener<SwitchBean>() {

            @Override
            public void onSuccess(SwitchBean cb) {
//                curBreaker = cb;
//                curBreaker.setFault(cb.getFault()); //fault
//                curBreaker.setSwitchState(cb.getSwitchState());//state
                judgSwitchstate(cb);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(SwitchStatusActivity.this, message);
            }
        });
    }

    /**
     * 设置线路状态
     */
    public void judgSwitchstate(SwitchBean ss) {
        online_icon.setVisibility(View.VISIBLE);
        if (ss.getSwitchState() == 1) {
            online_text.setText("合闸");
            online_icon.setBackgroundResource(R.drawable.hezha_p);
        } else if (ss.getSwitchState() == 0) {
            online_text.setText("拉闸");
            online_icon.setBackgroundResource(R.drawable.kaizha_p);
        } else {
            online_text.setText("错误");
            online_icon.setBackgroundResource(R.drawable.cuowu_p);
        }
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                curBreaker = data.get(0);//init bean
                getBreakSignalValue(curBreaker);
                judgSwitchstate(curBreaker);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    online_icon.setVisibility(View.INVISIBLE);
                    online_text.setText("-");
                    dl_val.setText("-");
                    dy_val.setText("-");
                    wd_val.setText("-");
                    ldl_val.setText("-");
                    ygdy_val.setText("-");
                    switch_ver.setText("-");
                    CustomToast.showCustomErrorToast(SwitchStatusActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(SwitchStatusActivity.this, message);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_cancel://返回
                finish();
                break;
            case R.id.img_change://改变线路
//                Intent intent = new Intent(SwitchStatusActivity.this, SwitchTreeDialog.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.SHISHI);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.SHISHI, collectorBean, new SwitchTreeDialog.ChoseCallBack() {

                    @Override
                    public void onChose(SwitchBean s) {
                        curBreaker = s;
                        getBreakSignalValue(curBreaker);
                        judgSwitchstate(curBreaker);
                    }
                });
                switchTreeDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_CHOSE_LINE) { //添加后 刷新数据 Deprecated
                curBreaker = data.getParcelableExtra("switchBean");
                getBreakSignalValue(curBreaker);
                judgSwitchstate(curBreaker);
            }
        }
    }
}
