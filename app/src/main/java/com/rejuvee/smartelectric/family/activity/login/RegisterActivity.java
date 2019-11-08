package com.rejuvee.smartelectric.family.activity.login;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.custom.AccountEventMsg;
import com.rejuvee.smartelectric.family.common.utils.AccountHelper;
import com.rejuvee.smartelectric.family.common.utils.CountDownUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    private EditText edtUserName, edtPhone, edtVerifyCode, edtPassword, edtEnsurePassword;
    private String userName, password;
    private TextView txtRegister, tvGetCode;
    private CountDownUtil countDownUtil;
    private AccountHelper accountHelper;
//    private boolean usernameFlag = true;
    //    private String uMessage = getResources().getString(R.string.vs143);

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_register;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        edtUserName = findViewById(R.id.login_cet_username);
        edtPassword = findViewById(R.id.login_cet_password);
        edtEnsurePassword = findViewById(R.id.login_cet_password_again);
        edtPhone = findViewById(R.id.et_phone);
        edtVerifyCode = findViewById(R.id.et_code);
        txtRegister = findViewById(R.id.st_register);
        txtRegister.setOnClickListener(v -> startRegister());
        accountHelper = new AccountHelper();
        initGetCode();
        edtUserName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {// 失去焦点
                EditText e = (EditText) v;
                usernameVerify(e.getEditableText().toString());
            }
        });
        EventBus.getDefault().register(this);
    }

    private void initGetCode() {
        tvGetCode = findViewById(R.id.tv_reget);
        tvGetCode.setSelected(true);
        countDownUtil = new CountDownUtil(60, seconds -> {
            tvGetCode.setText(seconds == 0 ? getString(R.string.mark_getcode) : String.format(getString(R.string.resend_time), seconds));
            tvGetCode.setSelected(seconds == 0);
        });
        tvGetCode.setOnClickListener(v -> {
            if (v.isSelected()) {
                getVerifyCode();
            }
        });
    }

    private void usernameVerify(String username) {
        if (username.isEmpty())
            return;
        Core.instance(this).validateUsername(username, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 19) {//未注册
//                    countDownUtil.start();
//                    accountHelper.getPhoneCode(RegisterActivity.this, edtPhone.getEditableText().toString(), true);
//                    usernameFlag = false;
                } else if (errorEvent == 13) {//已注册
                    CustomToast.showCustomErrorToast(RegisterActivity.this, message);
//                    uMessage = message;
//                    usernameFlag = true;
                } else {
                    CustomToast.showCustomErrorToast(RegisterActivity.this, message);
                }
            }
        });
    }

    private void getVerifyCode() {
        final String phone = edtPhone.getEditableText().toString();
        if (phone.length() != 11) {
            CustomToast.showCustomErrorToast(this, getString(R.string.input_correct_phone));
            return;
        }

        Core.instance(this).isPhoneRegister(phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 7) {//未注册
                    countDownUtil.start();
                    accountHelper.getPhoneCode(RegisterActivity.this, edtPhone.getEditableText().toString(), true);
                } else if (errorEvent == 8) {//已注册
                    CustomToast.showCustomErrorToast(RegisterActivity.this, getString(R.string.phone_registed));
                } else {
                    CustomToast.showCustomErrorToast(RegisterActivity.this, message);
                }
            }
        });
//       accountHelper.IsPhoneReg(this, phone, true);
    }

    private void startRegister() {
        String userName = edtUserName.getEditableText().toString();
        String password1 = edtPassword.getEditableText().toString();
        String password2 = edtEnsurePassword.getEditableText().toString();
        String phone = edtPhone.getEditableText().toString();
        String code = edtVerifyCode.getEditableText().toString();
        if (userName.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.name_password_cannot_empty));
            return;
        }

        if (phone.length() != 11) {
            CustomToast.showCustomErrorToast(this, getString(R.string.input_correct_phone));
            return;
        }
        if (code.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.reg_hint_input_verify));
            return;
        }
        if (password1.length() < 6 || password1.length() > 12) {
            CustomToast.showCustomErrorToast(this, getString(R.string.vs146));
            return;
        }

        if (password2.length() < 6 || password2.length() > 12) {
            CustomToast.showCustomErrorToast(this, getString(R.string.vs146));
            return;
        }
        if (!password1.equals(password2)) {
            CustomToast.showCustomErrorToast(this, getString(R.string.password_not_same));
            return;
        }
        password = EncryptUtils.encryptMD5ToString(password1, Core.SALT);
        AccountHelper accountHelper = new AccountHelper();
        Register(userName, phone, password, code);
//     accountHelper.Register(RegisterActivity.this, userName, phone, password, code);
    }

    public void Register(final String userName, final String phone, final String password, String vcode) {
        Core.instance(this).RegisterByPhone(phone, userName, vcode, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("username", userName);
                intent.putExtra("password", password);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(RegisterActivity.this, message);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegister(AccountEventMsg eventMsg) {
        if (eventMsg.eventType == AccountEventMsg.EVENT_REGISTER) {
            if (eventMsg.isSucess()) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("username", userName);
                intent.putExtra("password", password);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                CustomToast.showCustomErrorToast(RegisterActivity.this, getString(R.string.register_fail));
            }
        } else if (eventMsg.eventType == AccountEventMsg.EVENT_GET_CODE) {
            if (eventMsg.isSucess()) {

            } else {
                CustomToast.showCustomErrorToast(RegisterActivity.this, getString(R.string.op_fail));
            }

        } else if (eventMsg.eventType == AccountEventMsg.EVENT_PHONE_REG) {
            int retCode = (int) eventMsg.eventMsg;
            if (retCode == 7) {//未注册
                countDownUtil.start();
                accountHelper.getPhoneCode(this, edtPhone.getEditableText().toString(), true);
            } else if (retCode == 8) {//已注册
                CustomToast.showCustomErrorToast(RegisterActivity.this, getString(R.string.phone_registed));
            } else {
                CustomToast.showCustomErrorToast(this, getString(R.string.operator_failure));
            }
        }
    }

    @Override
    protected void initData() {

    }

//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.login_regist);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {
        EventBus.getDefault().unregister(this);
    }

}
