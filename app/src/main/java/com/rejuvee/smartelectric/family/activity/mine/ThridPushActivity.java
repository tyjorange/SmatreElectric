package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.base.library.widget.SuperTextView;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.model.bean.UserPushSetting;

import java.util.List;

/**
 * 推送 设置 界面
 * <p>
 * 100 系统通知
 * 101 更新通知
 * 200 线路告警
 * 201 集中器告警
 * 300 用电量
 */
@Deprecated
public class ThridPushActivity extends BaseActivity {
    private String TAG = "ThridPushActivity";
    private String[] opt = new String[]{"100,", "101,", "200,", "201,", "300,"};
    private LinearLayout ll_cb1;
    private LinearLayout ll_cb2;
    private LinearLayout ll_cb3;
    private LinearLayout ll_cb4;
    private LinearLayout ll_cb5;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private SuperTextView superTextView;
    private Context context;
    private LoadingDlg mWaitDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_push;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        context = this;
        mWaitDialog = new LoadingDlg(this, -1);
        ll_cb1 = findViewById(R.id.ll_cb_1);
        ll_cb2 = findViewById(R.id.ll_cb_2);
        ll_cb3 = findViewById(R.id.ll_cb_3);
        ll_cb4 = findViewById(R.id.ll_cb_4);
        ll_cb5 = findViewById(R.id.ll_cb_5);
        checkBox1 = findViewById(R.id.id_checkbox_1);
        checkBox2 = findViewById(R.id.id_checkbox_2);
        checkBox3 = findViewById(R.id.id_checkbox_3);
        checkBox4 = findViewById(R.id.id_checkbox_4);
        checkBox5 = findViewById(R.id.id_checkbox_5);
        superTextView = findViewById(R.id.st_save);
    }

    @Override
    protected void initData() {
        initBox();
        getData();
    }

    private void getData() {
        mWaitDialog.show();
        Core.instance(context).getPushSetting(new ActionCallbackListener<List<UserPushSetting>>() {
            @Override
            public void onSuccess(List<UserPushSetting> data) {
                for (UserPushSetting ups : data) {
                    if (ups.getNoticeID().equals(100)) {
                        checkBox1.setChecked(true);
                    }
                    if (ups.getNoticeID().equals(101)) {
                        checkBox2.setChecked(true);
                    }
                    if (ups.getNoticeID().equals(200)) {
                        checkBox3.setChecked(true);
                    }
                    if (ups.getNoticeID().equals(201)) {
                        checkBox4.setChecked(true);
                    }
                    if (ups.getNoticeID().equals(300)) {
                        checkBox5.setChecked(true);
                    }
                }
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(ThridPushActivity.this, message);
                mWaitDialog.dismiss();
            }
        });
    }

    private void initBox() {
        checkBox1.setChecked(true);
        checkBox1.setEnabled(false);
        ll_cb1.setOnClickListener(v -> {
//                checkBox1.toggle();
//                CustomToast.showCustomErrorToast(ThridPushActivity.this, "系统通知必选");
        });
        ll_cb2.setOnClickListener(v -> {
//                checkBox2.toggle();
//                CustomToast.showCustomErrorToast(ThridPushActivity.this, "更新通知必选");
        });
        ll_cb3.setOnClickListener(v -> checkBox3.toggle());
        ll_cb4.setOnClickListener(v -> checkBox4.toggle());
        ll_cb5.setOnClickListener(v -> checkBox5.toggle());
        superTextView.setOnClickListener(v -> {
            String remind = "";
            if (checkBox1.isChecked()) {
                remind += opt[0];
            }
            if (checkBox2.isChecked()) {
                remind += opt[1];
            }
            if (checkBox3.isChecked()) {
                remind += opt[2];
            }
            if (checkBox4.isChecked()) {
                remind += opt[3];
            }
            if (checkBox5.isChecked()) {
                remind += opt[4];
            }
            Core.instance(context).updatePushSetting(remind, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    CustomToast.showCustomToast(ThridPushActivity.this, getString(R.string.operator_sucess));
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    CustomToast.showCustomErrorToast(ThridPushActivity.this, message);
                }
            });
        });
    }

//    @Override
//    protected String getToolbarTitle() {
//        return getResources().getString(R.string.mine_subtitle_push);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }
}
