package com.rejuvee.smartelectric.family.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.custom.AccountEventMsg;
import com.rejuvee.smartelectric.family.common.utils.AccountHelper;
import com.rejuvee.smartelectric.family.common.utils.CountDownUtil;
import com.rejuvee.smartelectric.family.databinding.ActivityRegisterBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.RegisterViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";
    //    private EditText edtUserName, edtPhone, edtVerifyCode, edtPassword, edtEnsurePassword;
    private String encryptPwd;
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
    private ActivityRegisterBinding mBinding;
    private RegisterViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        edtUserName = findViewById(R.id.login_cet_username);
//        edtPassword = findViewById(R.id.login_cet_password);
//        edtEnsurePassword = findViewById(R.id.login_cet_password_again);
//        edtPhone = findViewById(R.id.et_phone);
//        edtVerifyCode = findViewById(R.id.et_code);
//        txtRegister = findViewById(R.id.st_register);
//        txtRegister.setOnClickListener(v -> startRegister());
        accountHelper = new AccountHelper();
        mBinding.loginCetUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {// 失去焦点
//                EditText e = (EditText) v;
                String value = mViewModel.getUsername().getValue();
                if (value != null) {
                    usernameVerify(value);
                }
            }
        });
        initGetCode();
        EventBus.getDefault().register(this);
    }

    private void initGetCode() {
        TextView tvGetCode = mBinding.tvReget;//findViewById(R.id.tv_reget);
        tvGetCode.setSelected(true);
        countDownUtil = new CountDownUtil(60, seconds -> {
            tvGetCode.setText(seconds == 0 ? getString(R.string.mark_getcode) : String.format(getString(R.string.resend_time), seconds));
            tvGetCode.setSelected(seconds == 0);
        });
//        tvGetCode.setOnClickListener(v -> {
//            if (v.isSelected()) {
//                getVerifyCode();
//            }
//        });
    }

    private void usernameVerify(String username) {
        if (username.isEmpty())
            return;
        currentCall = Core.instance(this).validateUsername(username, new ActionCallbackListener<Void>() {
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
        String phone = mViewModel.getPhone().getValue();//edtPhone.getEditableText().toString();
        if (phone == null || phone.length() != 11) {
            CustomToast.showCustomErrorToast(this, getString(R.string.input_correct_phone));
            return;
        }

        currentCall = Core.instance(this).isPhoneRegister(phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 7) {//未注册
                    countDownUtil.start();
                    accountHelper.getPhoneCode(RegisterActivity.this, phone, true);
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
        String userName = mViewModel.getUsername().getValue();//edtUserName.getEditableText().toString();
        String password1 = mViewModel.getPwd().getValue();//edtPassword.getEditableText().toString();
        String password2 = mViewModel.getRePwd().getValue();//edtEnsurePassword.getEditableText().toString();
        String phone = mViewModel.getPhone().getValue();//edtPhone.getEditableText().toString();
        String code = mViewModel.getCode().getValue();//edtVerifyCode.getEditableText().toString();
        if (userName == null || userName.isEmpty() ||
                password1 == null || password1.isEmpty() ||
                password2 == null || password2.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.name_password_cannot_empty));
            return;
        }
        if (phone == null || phone.length() != 11) {
            CustomToast.showCustomErrorToast(this, getString(R.string.input_correct_phone));
            return;
        }
        if (code == null || code.isEmpty()) {
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
        encryptPwd = EncryptUtils.encryptMD5ToString(password1, Core.SALT);
        AccountHelper accountHelper = new AccountHelper();
        Register(userName, phone, encryptPwd, code);
//     accountHelper.Register(RegisterActivity.this, userName, phone, password, code);
    }

    public void Register(final String userName, final String phone, final String password, String vcode) {
        currentCall = Core.instance(this).RegisterByPhone(phone, userName, vcode, password, new ActionCallbackListener<Void>() {
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
                intent.putExtra("username", mViewModel.getUsername().getValue());
                intent.putExtra("password", encryptPwd);
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
                accountHelper.getPhoneCode(this, mViewModel.getPhone().getValue(), true);
            } else if (retCode == 8) {//已注册
                CustomToast.showCustomErrorToast(RegisterActivity.this, getString(R.string.phone_registed));
            } else {
                CustomToast.showCustomErrorToast(this, getString(R.string.operator_failure));
            }
        }
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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onGetCode(View view) {
            if (view.isSelected()) {
                getVerifyCode();
            }
        }

        public void onRegister(View view) {
            startRegister();
        }
    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

}
