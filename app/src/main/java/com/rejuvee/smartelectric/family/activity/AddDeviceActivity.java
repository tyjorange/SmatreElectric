package com.rejuvee.smartelectric.family.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.PermissionUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchModifyActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.PermisionManage;
import com.rejuvee.smartelectric.family.custom.DeviceEventMsg;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.utils.AccountHelper;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 添加电箱 或 线路
 */
public class AddDeviceActivity extends BaseActivity {
    private EditText edtScan;
    private LoadingDlg mWaitDialog;

    public static int EQUIPMENT_ADD = 0;
    public static int BREAK_ADD = 1;
    private int addType = EQUIPMENT_ADD;

    private CollectorBean collectorBean;//集中器 Collect
    private SwitchBean mSwitch;// 父断路器 switch
    private TextView add_title;
    private TextView txtLineName;
    private LinearLayout llSetLineName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_device;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.rl_scan_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermisionManage.getInstance().startRequest(AddDeviceActivity.this, new String[]{Manifest.permission.CAMERA}, new PermissionUtils.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent(AddDeviceActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, CommonRequestCode.REQUEST_SCAN_CODE);
                        }

                        @Override
                        public void onPermissionDenied(String[] deniedPermissions) {
                            CustomToast.showCustomErrorToast(AddDeviceActivity.this, "请打开相机权限");
                        }
                    });
                } else {
                    Intent intent = new Intent(AddDeviceActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, CommonRequestCode.REQUEST_SCAN_CODE);
                }
            }
        });
        findViewById(R.id.st_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addType == EQUIPMENT_ADD) {
                    bindDevice();
                } else {
                    addBreak();
                }
            }
        });
        edtScan = (EditText) findViewById(R.id.edt_input_device);
        mWaitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        intent.setExtrasClassLoader(getClass().getClassLoader());
        addType = intent.getIntExtra("add_type", 0);
        mSwitch = intent.getParcelableExtra("switch");
        add_title = (TextView) findViewById(R.id.add_title);
        if (addType == BREAK_ADD) {
            add_title.setText("添加线路");
//            getToolbarTextView().setText(getResources().getString(R.string.sce_addxianlu));
            collectorBean = getIntent().getParcelableExtra("collectorBean");
            llSetLineName = (LinearLayout) findViewById(R.id.ll_set_line_name);
            llSetLineName.setVisibility(View.VISIBLE);
            txtLineName = (TextView) findViewById(R.id.txt_line_name);

            llSetLineName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddDeviceActivity.this, SwitchModifyActivity.class);
                    startActivityForResult(intent, CommonRequestCode.REQUEST_SET_LINE_NAME);
                }
            });
            edtScan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        } else {
            add_title.setText("添加电箱");
            edtScan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
        }
    }

    private void addBreak() {
        String switchCode = edtScan.getEditableText().toString();
        if (switchCode.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.device_code_empty));
            return;
        }
        if (switchCode.length() != 12) {
            CustomToast.showCustomErrorToast(this, String.format(getString(R.string.input_correct_device_code), "12"));
            return;
        }
        if (lineName == null) {
            CustomToast.showCustomErrorToast(this, getString(R.string.ple_set_line_name));
            return;
        }
        mWaitDialog.show();
        Core.instance(this).addSwitch(collectorBean.getCollectorID(), lineName, switchCode, iconType, mSwitch == null ? "0" : mSwitch.getSwitchID(), new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddDeviceActivity.this, getString(R.string.operator_sucess));
                setResult(RESULT_OK);
                mWaitDialog.dismiss();
                finish();
//                jumpToConfig();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(AddDeviceActivity.this, getString(R.string.operator_failure));
                mWaitDialog.dismiss();
            }
        });
    }

//    private void jumpToConfig() {
//        Intent intent = new Intent(this, ConfigActivity.class);
//        startActivity(intent);
//    }

    private void bindDevice() {
        String setupCode = edtScan.getEditableText().toString();
        if (setupCode.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.device_code_empty));
            return;
        }
        if (setupCode.length() != 11) {
            CustomToast.showCustomErrorToast(this, String.format(getString(R.string.input_correct_device_code), "11"));
            return;
        }
        mWaitDialog.show();
        Core.instance(this).bindDevice(setupCode, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddDeviceActivity.this, getString(R.string.operator_sucess));
                setResult(RESULT_OK);
                mWaitDialog.dismiss();
                finish();

                AccountHelper.setUserFirstUse(false, AddDeviceActivity.this);
//                startActivity(new Intent(AddDeviceActivity.this, ConfigActivity.class));
                EventBus.getDefault().post(new DeviceEventMsg(DeviceEventMsg.EVENT_REFRESH_CONCENTRATOR));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(AddDeviceActivity.this, getString(R.string.operator_failure));
            }
        });
    }

//    @Override
//    protected String getToolbarTitle() {
//        return getResources().getString(R.string.add_device);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }

    private String lineName;
    private int iconType;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_SCAN_CODE) {
                String code = data.getExtras().getString(CodeUtils.RESULT_STRING);
                edtScan.setText(code);
            } else if (requestCode == CommonRequestCode.REQUEST_SET_LINE_NAME) {
                lineName = data.getStringExtra("name");
                iconType = data.getIntExtra("icontype", 0);
                txtLineName.setText(lineName);
            }
        }
    }
}
