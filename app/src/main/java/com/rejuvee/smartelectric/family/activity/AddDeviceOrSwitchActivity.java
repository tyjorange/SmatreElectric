package com.rejuvee.smartelectric.family.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchModifyActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.PermissionManage;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.custom.DeviceEventMsg;
import com.rejuvee.smartelectric.family.common.utils.AccountHelper;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityAddDeviceBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.viewmodel.AddDeviceOrSwitchViewModel;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

/**
 * 添加电箱 或 线路
 */
public class AddDeviceOrSwitchActivity extends BaseActivity {
    //    private EditText mBinding.edtInputDevice;
    private LoadingDlg mWaitDialog;

    public static int EQUIPMENT_ADD = 0;
    public static int BREAK_ADD = 1;
    private int addType = EQUIPMENT_ADD;

    private CollectorBean collectorBean;//集中器 Collect
    private SwitchBean mSwitch;// 父断路器 switch
    //    private TextView add_title;
    //    private TextView txtLineName;
//    private LinearLayout llSetLineName;
    private ActivityAddDeviceBinding mBinding;
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_add_device;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_device);
        AddDeviceOrSwitchViewModel mViewModel = ViewModelProviders.of(this).get(AddDeviceOrSwitchViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        PermissionManage.getInstance().setCallBack(new PermissionManage.PermissionCallBack() {

            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied() {
                CustomToast.showCustomErrorToast(AddDeviceOrSwitchActivity.this, getString(R.string.vs46));
            }
        }).hasCamera(this);

        mWaitDialog = new LoadingDlg(this, -1);
        Intent intent = getIntent();
        intent.setExtrasClassLoader(getClass().getClassLoader());
        addType = intent.getIntExtra("add_type", 0);
        mSwitch = intent.getParcelableExtra("switch");
        if (addType == BREAK_ADD) {
            mBinding.addTitle.setText(getString(R.string.sce_addxianlu));
            mBinding.typeDianxiang.setVisibility(View.GONE);
            mBinding.typeXianlu.setVisibility(View.VISIBLE);
            if (mSwitch != null) {
                mBinding.txtParentTip.setText(String.format("%s%s", getString(R.string.vs1), mSwitch.getName()));
                mBinding.txtParentTip.setVisibility(View.VISIBLE);
            } else {
                mBinding.txtParentTip.setVisibility(View.INVISIBLE);
            }
//            getToolbarTextView().setText(getResources().getString(R.string.sce_addxianlu));
            collectorBean = getIntent().getParcelableExtra("collectorBean");
            mBinding.llSetLineName.setVisibility(View.VISIBLE);
//            mBinding.llSetLineName.setOnClickListener(v -> {
//                Intent intent1 = new Intent(AddDeviceOrSwitchActivity.this, SwitchModifyActivity.class);
//                startActivityForResult(intent1, CommonRequestCode.REQUEST_SET_LINE_NAME);
//            });
            mBinding.edtInputDevice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            mBinding.edtInputDevice.setHint(getString(R.string.scan_rusults_hint1));
        } else {
            mBinding.addTitle.setText(getString(R.string.vs49));
            mBinding.typeDianxiang.setVisibility(View.VISIBLE);
            mBinding.typeXianlu.setVisibility(View.GONE);
            mBinding.txtParentTip.setVisibility(View.INVISIBLE);
            mBinding.edtInputDevice.setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
            mBinding.edtInputDevice.setHint(getString(R.string.scan_rusults_hint2));
        }
    }

    /**
     * 添加线路
     */
    private void addBreak() {
        String switchCode = mBinding.edtInputDevice.getEditableText().toString();
        if (switchCode.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.switch_code_empty));
            return;
        }
        if (switchCode.length() != 12) {
            CustomToast.showCustomErrorToast(this, String.format(getString(R.string.input_correct_switch_code), "12"));
            return;
        }
        if (lineName == null) {
            CustomToast.showCustomErrorToast(this, getString(R.string.ple_set_line_name));
            return;
        }
        mWaitDialog.show();
        Core.instance(this).addSwitch(collectorBean.getCollectorID(), lineName, switchCode, iconType, mSwitch == null ? 0 : mSwitch.getSwitchID(), new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddDeviceOrSwitchActivity.this, getString(R.string.operator_sucess));
                setResult(RESULT_OK);
                mWaitDialog.dismiss();
                finish();
//                jumpToConfig();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(AddDeviceOrSwitchActivity.this, message);
                mWaitDialog.dismiss();
            }
        });
    }

//    private void jumpToConfig() {
//        Intent intent = new Intent(this, ConfigActivity.class);
//        startActivity(intent);
//    }

    /**
     * 添加电箱
     */
    private void bindDevice() {
        String setupCode = mBinding.edtInputDevice.getEditableText().toString();
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
                CustomToast.showCustomToast(AddDeviceOrSwitchActivity.this, getString(R.string.operator_sucess));
                setResult(RESULT_OK);
                mWaitDialog.dismiss();
                finish();

                AccountHelper.setUserFirstUse(false, AddDeviceOrSwitchActivity.this);
//                startActivity(new Intent(AddDeviceOrSwitchActivity.this, ConfigActivity.class));
                EventBus.getDefault().post(new DeviceEventMsg(DeviceEventMsg.EVENT_REFRESH_CONCENTRATOR));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(AddDeviceOrSwitchActivity.this, message);
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
    public class Presenter {
        public void onAdd(View view) {
            Intent intent = new Intent(AddDeviceOrSwitchActivity.this, CaptureActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_SCAN_CODE);
        }

        public void onCancel(View view) {
            finish();
        }

        public void onSubmit(View view) {
            if (addType == EQUIPMENT_ADD) {
                bindDevice();
            } else {
                addBreak();
            }
        }

        public void setLineName(View view) {
            Intent intent1 = new Intent(view.getContext(), SwitchModifyActivity.class);
            startActivityForResult(intent1, CommonRequestCode.REQUEST_SET_LINE_NAME);
        }
    }

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
                String code = Objects.requireNonNull(data.getExtras()).getString(CodeUtils.RESULT_STRING);
                mBinding.edtInputDevice.setText(code);
            } else if (requestCode == CommonRequestCode.REQUEST_SET_LINE_NAME) {
                lineName = data.getStringExtra("name");
                iconType = data.getIntExtra("icontype", 0);
//                txtLineName.setText(lineName);
                mBinding.txtLineName.setText(lineName);
            }
        }
    }
}
