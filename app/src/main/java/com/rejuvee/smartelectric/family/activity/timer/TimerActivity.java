package com.rejuvee.smartelectric.family.activity.timer;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeDialog;
import com.rejuvee.smartelectric.family.adapter.ListTimerAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.TimeTaskBean;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时开关
 */
public class TimerActivity extends BaseActivity implements View.OnClickListener, ListTimerAdapter.MyListener {
    private static final String TAG = "TimerActivity";
    private CollectorBean collectorBean;
    private SwitchBean switchBean;
    private ImageView backBtn;
    private LinearLayout img_change;
    private ImageView img_remove;
    private ImageView img_add;
    private TextView tv_line_name;

    private List<TimeTaskBean.TimeTask> mListTaskData = new ArrayList<>();
    private ListTimerAdapter mListTimerAdapter;
    private ListView mTimerView;
    private DialogTip dialogTip;
    private LoadingDlg mWaitDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_timer;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        switchId = getIntent().getStringExtra("switchId");
        tv_line_name = findViewById(R.id.line_name);
        mTimerView = findViewById(R.id.lv_timer);
        img_add = findViewById(R.id.img_add);
        img_add.setOnClickListener(this);
        backBtn = findViewById(R.id.img_cancel);
        backBtn.setOnClickListener(this);
        img_remove = findViewById(R.id.img_remove);
        img_remove.setOnClickListener(this);
        img_change = findViewById(R.id.img_change);
        img_change.setOnClickListener(this);
        mListTimerAdapter = new ListTimerAdapter(TimerActivity.this, mListTaskData, this);
        mTimerView.setAdapter(mListTimerAdapter);
        mTimerView.setEmptyView(findViewById(R.id.empty_layout));
        mTimerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTask(mListTaskData.get(position));
            }
        });
        mWaitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        getSwitchByCollector();
    }

    private void getData(SwitchBean switchBean) {
        tv_line_name.setText(String.format("%s%s", getString(R.string.vs4), switchBean.getName()));
        mWaitDialog.show();
        Core.instance(this).findTimeControllerBySwitch(switchBean.getSwitchID(), new ActionCallbackListener<List<TimeTaskBean.TimeTask>>() {
            @Override
            public void onSuccess(List<TimeTaskBean.TimeTask> data) {
                mListTaskData.clear();
                mListTaskData.addAll(data);
                mListTimerAdapter.notifyDataSetChanged();
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {//无数据
                    mListTaskData.clear();
                    mListTimerAdapter.notifyDataSetChanged();
                }
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(TimerActivity.this, message);
            }
        });
    }

    /**
     * 添加/修改
     *
     * @param task
     */
    private void editTask(TimeTaskBean.TimeTask task) {
        Intent intent = new Intent(TimerActivity.this, TimerEditDialogActivity.class);
        intent.putExtra("task", task);
        intent.putExtra("switchBean", switchBean);
        startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_TIMER);
    }

    @Override
    public void onEnable(int position, final boolean enable) {
        final TimeTaskBean.TimeTask task = mListTaskData.get(position);
        Core.instance(this).updateOrInsertTask(task.taskId,
                switchBean.getSwitchID(),
                task.time,
                task.repeatState,
                enable ? 1 : 0,
                task.switchState, task.upload, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        getData(switchBean);
                        CustomToast.showCustomToast(TimerActivity.this, (enable ? getString(R.string.timed_task_enable) : getString(R.string.timed_task_unenable)));
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        task.enable = !enable ? 1 : 0;
                        mListTimerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteTask(String timerId) {
        mWaitDialog.show();
        Core.instance(this).deleteTimeTask(timerId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                mWaitDialog.dismiss();
                getData(switchBean);
                CustomToast.showCustomToast(TimerActivity.this, "删除成功");
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(TimerActivity.this, message);
            }
        });
    }

    private void toggleDelIcon() {
        for (TimeTaskBean.TimeTask taskBean : mListTaskData) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        mListTimerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelItem(String timerId) {
        dialogTip = new DialogTip(this);
        dialogTip.setTitle("删除定时任务");
        dialogTip.setContent("确定需要删除此定时任务吗？");
        dialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
                Log.i(TAG, timerId + "=ID");
            }

            @Override
            public void onEnsure() {
                deleteTask(timerId);
                dialogTip.dismiss();
            }
        });
        dialogTip.show();
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "hierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                switchBean = data.get(0);//init bean
                getData(switchBean);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(TimerActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(TimerActivity.this, message);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                editTask(null);
                break;
            case R.id.img_cancel:
                finish();
                break;
            case R.id.img_remove:
                toggleDelIcon();
                break;
            case R.id.img_change:
//                Intent intent = new Intent(TimerActivity.this, SwitchTreeDialog.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.DINGSHI);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.DINGSHI, collectorBean, new SwitchTreeDialog.ChoseCallBack() {

                    @Override
                    public void onChose(SwitchBean s) {
                        switchBean = s;
                        getData(switchBean);
                    }
                });
                switchTreeDialog.show();
                break;
        }
    }

    @Override
    protected void dealloc() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_CHOSE_LINE) {//线路选择 Deprecated
                switchBean = data.getParcelableExtra("switchBean");
                getData(switchBean);
            } else if (requestCode == CommonRequestCode.REQUEST_ADD_TIMER) {//添加 修改
                SwitchBean s = data.getParcelableExtra("switchBean");
                getData(s);
            }
        }
    }
}
