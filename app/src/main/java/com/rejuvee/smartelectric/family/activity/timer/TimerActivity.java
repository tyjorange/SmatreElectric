package com.rejuvee.smartelectric.family.activity.timer;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeDialog;
import com.rejuvee.smartelectric.family.adapter.ListTimerAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityTimerBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.TimeTaskBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

/**
 * 定时开关
 */
public class TimerActivity extends BaseActivity implements ListTimerAdapter.MyListener {
    private static final String TAG = "TimerActivity";
    private CollectorBean collectorBean;
    private SwitchBean switchBean;
    //    private ImageView backBtn;
//    private LinearLayout img_change;
//    private ImageView img_remove;
//    private ImageView img_add;
//    private TextView tv_line_name;

    private List<TimeTaskBean.TimeTask> mListTaskData = new ArrayList<>();
    private ListTimerAdapter mListTimerAdapter;
    //    private ListView mTimerView;
    private DialogTip dialogTip;
    private LoadingDlg mWaitDialog;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_timer;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivityTimerBinding mBinding;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_timer);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        switchId = getIntent().getStringExtra("switchId");
//        tv_line_name = findViewById(R.id.line_name);
//        mTimerView = findViewById(R.id.lv_timer);
//        img_add = findViewById(R.id.img_add);
//        img_add.setOnClickListener(this);
//        backBtn = findViewById(R.id.img_cancel);
//        backBtn.setOnClickListener(this);
//        img_remove = findViewById(R.id.img_remove);
//        img_remove.setOnClickListener(this);
//        img_change = findViewById(R.id.img_change);
//        img_change.setOnClickListener(this);
        mListTimerAdapter = new ListTimerAdapter(TimerActivity.this, mListTaskData, this);
        mBinding.lvTimer.setAdapter(mListTimerAdapter);
        mBinding.lvTimer.setEmptyView(mBinding.emptyLayout.getRoot());
        mBinding.lvTimer.setOnItemClickListener((parent, view, position, id) -> editTask(mListTaskData.get(position)));
        mWaitDialog = new LoadingDlg(this, -1);
        getSwitchByCollector();
    }

    private void getData(SwitchBean switchBean) {
        mBinding.lineName.setText(String.format("%s%s", getString(R.string.vs4), switchBean.getName()));
        mWaitDialog.show();
        currentCall = Core.instance(this).findTimeControllerBySwitch(switchBean.getSwitchID(), new ActionCallbackListener<List<TimeTaskBean.TimeTask>>() {
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
        if (switchBean == null) {
            CustomToast.showCustomErrorToast(this, getResources().getString(R.string.vs29));
            return;
        }
        Intent intent = new Intent(TimerActivity.this, TimerEditDialogActivity.class);
        intent.putExtra("task", task);
        intent.putExtra("switchBean", switchBean);
        startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_TIMER);
    }

    @Override
    public void onEnable(int position, final boolean enable) {
        final TimeTaskBean.TimeTask task = mListTaskData.get(position);
        currentCall = Core.instance(this).updateOrInsertTask(task.taskId,
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
                        Log.e(TAG, message);
                        task.enable = !enable ? 1 : 0;
                        mListTimerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteTask(String timerId) {
        mWaitDialog.show();
        currentCall = Core.instance(this).deleteTimeTask(timerId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                mWaitDialog.dismiss();
                getData(switchBean);
                CustomToast.showCustomToast(TimerActivity.this, getString(R.string.vs161));
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

    private void closeDelIcon() {
        for (TimeTaskBean.TimeTask taskBean : mListTaskData) {
            if (taskBean.showDelIcon == View.VISIBLE) {
                taskBean.showDelIcon = View.GONE;
            }
        }
        mListTimerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelItem(String timerId) {
        dialogTip = new DialogTip(this);
        dialogTip.setTitle(getString(R.string.vs162));
        dialogTip.setContent(getString(R.string.vs163));
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
        currentCall = Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "hierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                switchBean = data.get(0);//init bean
                getData(switchBean);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(TimerActivity.this, getString(R.string.vs29));
                } else {
                    CustomToast.showCustomErrorToast(TimerActivity.this, message);
                }
            }
        });
//        if (switchBean == null) {// 没有线路
//            mBinding.imgAdd.setVisibility(View.INVISIBLE);
//            mBinding.imgRemove.setVisibility(View.INVISIBLE);
//        } else {
//            mBinding.imgAdd.setVisibility(View.VISIBLE);
//            mBinding.imgRemove.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onBackPressed() {
        closeDelIcon();
        super.onBackPressed();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_add:
//                editTask(null);
//                break;
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.img_remove:
//                toggleDelIcon();
//                break;
//            case R.id.img_change:
////                Intent intent = new Intent(TimerActivity.this, SwitchTreeDialog.class);
////                intent.putExtra("collectorBean", collectorBean);
////                intent.putExtra("viewType", SwitchTree.DINGSHI);
////                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
//                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.DINGSHI, collectorBean, s -> {
//                    switchBean = s;
//                    getData(switchBean);
//                });
//                switchTreeDialog.show();
//                break;
//        }
//    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onAdd(View view) {
            editTask(null);
        }

        public void onRemove(View view) {
            toggleDelIcon();
        }

        public void onChange(View view) {
            //                Intent intent = new Intent(TimerActivity.this, SwitchTreeDialog.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.DINGSHI);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
            SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(view.getContext(), SwitchTree.DINGSHI, collectorBean, s -> {
                switchBean = s;
                getData(switchBean);
            });
            switchTreeDialog.show();
        }

    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_CHOSE_LINE) {//线路选择 Deprecated
                switchBean = data.getParcelableExtra("switchBean");
                getData(Objects.requireNonNull(switchBean));
            } else if (requestCode == CommonRequestCode.REQUEST_ADD_TIMER) {//添加 修改
                SwitchBean s = data.getParcelableExtra("switchBean");
                getData(Objects.requireNonNull(s));
            }
        }
    }
}
