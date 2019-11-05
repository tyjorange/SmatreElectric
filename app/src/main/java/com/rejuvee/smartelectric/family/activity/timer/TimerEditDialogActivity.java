package com.rejuvee.smartelectric.family.activity.timer;

import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.utils.TimePickerUIUtil;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.TimeTaskBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 定时开关 编辑
 */
public class TimerEditDialogActivity extends BaseActivity implements View.OnClickListener {
    private List<CheckBox> mListDataCheckButton = new ArrayList<>();
    private TimePicker mTimePicker;
    private TextView txtSwitchState;
    private ImageView imgUpload;
    private ImageView imgCaozuo;
    private TextView txtCaozuo;
    private TimeTaskBean.TimeTask mTask;
    private SwitchBean switchBean;
    //    private String time;
    private LoadingDlg mWaitDialog;

    private int uploadToCollect = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_time_task_edit;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
//        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = dm.heightPixels;
//        p.width = dm.widthPixels; // 宽度设置为屏幕的0.8*/
//        dialogWindow.setAttributes(p);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mListDataCheckButton.add(findViewById(R.id.cb_day0));
        mListDataCheckButton.add(findViewById(R.id.cb_day1));
        mListDataCheckButton.add(findViewById(R.id.cb_day2));
        mListDataCheckButton.add(findViewById(R.id.cb_day3));
        mListDataCheckButton.add(findViewById(R.id.cb_day4));
        mListDataCheckButton.add(findViewById(R.id.cb_day5));
        mListDataCheckButton.add(findViewById(R.id.cb_day6));
        mListDataCheckButton.add(findViewById(R.id.cb_day7));

//        int width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32 + 28)) / mListDataCheckButton.size();
        for (int i = 0; i < mListDataCheckButton.size(); i++) {
            CheckBox checkBox = mListDataCheckButton.get(i);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) checkBox.getLayoutParams();
//            params.width = width;
//            params.height = width;
            checkBox.setText(getResources().getStringArray(R.array.week_day)[i]);
        }

        mTimePicker = findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);
        TimePickerUIUtil.setTimepickerTextColour(mTimePicker, this);
//        TimePickerUIUtil.setNumberPickerTextSize(mTimePicker);
//        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                time = String.format("%1$02d:%2$02d", hourOfDay, minute);// hourOfDay + ":" + minute;
//            }
//        });

//        txtTime = (TextView) findViewById(R.id.txt_time);
        txtSwitchState = findViewById(R.id.txt_switch_state);
//        imgSwitchOff = (ImageView) findViewById(R.id.img_check_off);
//        imgSwithOn = (ImageView) findViewById(R.id.img_check_on);
        imgUpload = findViewById(R.id.iv_upload);
        imgCaozuo = findViewById(R.id.iv_check);
        txtCaozuo = findViewById(R.id.tv_caozuo);

//        findViewById(R.id.rl_on).setOnClickListener(mSwitchListener);
//        findViewById(R.id.rl_off).setOnClickListener(mSwitchListener);
        findViewById(R.id.iv_upload).setOnClickListener(this);
        findViewById(R.id.iv_tips).setOnClickListener(this);
        findViewById(R.id.st_quxiao).setOnClickListener(this);
        findViewById(R.id.st_ok).setOnClickListener(this);
        imgCaozuo.setOnClickListener(this);

        mWaitDialog = new LoadingDlg(this, -1);
    }

    private boolean isSwicthOn;
//    private onSwitchCheckListener mSwitchListener = new onSwitchCheckListener();

//    private class onSwitchCheckListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            updateSwicthState(v.getId() == R.id.rl_on);
//        }
//    }

    private void updateSwicthState(boolean isSwicthOn) {
        this.isSwicthOn = isSwicthOn;
        imgCaozuo.setImageResource(isSwicthOn ? R.drawable.yk_hezha : R.drawable.yk_kaizha);
        txtCaozuo.setText(isSwicthOn ? getString(R.string.vs74) : getString(R.string.vs75));
//        imgSwithOn.setImageResource(isSwicthOn ? R.drawable.timer_check : R.drawable.timer_uncheck);
//        imgSwitchOff.setImageResource(!isSwicthOn ? R.drawable.timer_check : R.drawable.timer_uncheck);
        txtSwitchState.setText(isSwicthOn ? getString(R.string.switch_on) : getString(R.string.switch_off));
    }

    @Override
    protected void initData() {
        mTask = getIntent().getParcelableExtra("task");
        switchBean = getIntent().getParcelableExtra("switchBean");
        if (mTask != null) {
            updateSwicthState(mTask.switchState == 1);
            String time = mTask.time;
            String[] split = time.split(":");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mTimePicker.setHour(Integer.valueOf(split[0]));
                mTimePicker.setMinute(Integer.valueOf(split[1]));
            } else {
                mTimePicker.setCurrentHour(Integer.valueOf(split[0]));
                mTimePicker.setCurrentMinute(Integer.valueOf(split[1]));
            }
           /* if (time != null) {
                time = time.substring(0, time.lastIndexOf(":"));
            }*/
            uploadToCollect = mTask.upload;
//            txtTime.setText(time);
            initRepeatState();
        } else {
            uploadToCollect = 0;
            updateSwicthState(true);
//            String time = String.format("%1$02d:%2$02d", mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
//            txtTime.setText(time);
        }
        imgUpload.setImageResource(uploadToCollect == 1 ? R.drawable.yixuan : R.drawable.weixuan);
    }

    private void initRepeatState() {
        int repeatInfo = mTask.repeatState;
        if (repeatInfo == 0) {
            return;
        }
        int mask = 0x000000001;
        for (int i = 0; i < mListDataCheckButton.size(); i++) {
            if ((repeatInfo & (mask << (i))) != 0) {
                mListDataCheckButton.get(i).setChecked(true);
            }
        }
//        if ((repeatInfo & 64) != 0) {
//            mListDataCheckButton.get(6).setChecked(true);
//        }
    }

    private int getRepeatState() {
        int repeatInfo = 0;
        int mask = 0x00000001;
        for (int i = 0; i < mListDataCheckButton.size(); i++) {
            if (mListDataCheckButton.get(i).isChecked()) {
                repeatInfo |= (mask << (i));
            }
        }
//        if (mListDataCheckButton.get(6).isChecked()) {//星期天
//            repeatInfo += 64;
//        }
        return repeatInfo;
    }

//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.timed_task);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_finish, menu);
//        MenuItem menuFinishItem = menu.findItem(R.id.menu_item_finish);
//        menuFinishItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                finishEdit();
//                return false;
//            }
//        });
//        return true;
//    }

    private void finishEdit() {
        int weekDay = getRepeatState();
        if (weekDay == 0 & uploadToCollect != 0) {
            CustomToast.showCustomErrorToast(TimerEditDialogActivity.this, getString(R.string.vs265));
            return;
        }
        int cmdData = isSwicthOn ? 1 : 0;
        mWaitDialog.show();
        String runTime = null;// hourOfDay + ":" + minute;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            runTime = String.format(Locale.getDefault(), "%1$02d:%2$02d", mTimePicker.getHour(), mTimePicker.getMinute());
        } else {
            runTime = String.format(Locale.getDefault(), "%1$02d:%2$02d", mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
        }
        if (mTask == null) {
            //for New
            Core.instance(this).updateOrInsertTask(null, switchBean.getSwitchID(), runTime, weekDay, 1, cmdData, uploadToCollect, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Intent intent = new Intent();
                    intent.putExtra("switchBean", switchBean);
                    setResult(RESULT_OK, intent);
                    CustomToast.showCustomToast(TimerEditDialogActivity.this, getString(R.string.vs138));
                    finish();
                    mWaitDialog.dismiss();
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    CustomToast.showCustomErrorToast(TimerEditDialogActivity.this, message);
                    mWaitDialog.dismiss();
                }
            });
        } else {
            //for update
            Core.instance(this).updateOrInsertTask(mTask.taskId, switchBean.getSwitchID(), runTime, weekDay, mTask.enable, cmdData, uploadToCollect, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Intent intent = new Intent();
                    intent.putExtra("switchBean", switchBean);
                    setResult(RESULT_OK, intent);
                    CustomToast.showCustomToast(TimerEditDialogActivity.this, getString(R.string.vs139));
                    finish();
                    mWaitDialog.dismiss();
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    CustomToast.showCustomErrorToast(TimerEditDialogActivity.this, message);
                    mWaitDialog.dismiss();
                }
            });
        }
    }

//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.st_quxiao) {
            finish();
        } else if (v.getId() == R.id.st_ok) {
            finishEdit();
        } else if (v.getId() == R.id.iv_upload) {
            uploadToCollect = uploadToCollect == 1 ? 0 : 1;
            imgUpload.setImageResource(uploadToCollect == 1 ? R.drawable.yixuan : R.drawable.weixuan);
        } else if (v.getId() == R.id.iv_tips) {
            new DialogTipWithoutOkCancel(TimerEditDialogActivity.this).setTitle(getString(R.string.vs140)).setContent(getString(R.string.upload_tips)).show();
        } else if (v.getId() == R.id.iv_check) {
            updateSwicthState(!isSwicthOn);
        }
    }
}
