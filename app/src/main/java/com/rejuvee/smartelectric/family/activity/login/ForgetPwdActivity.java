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
import com.rejuvee.smartelectric.family.databinding.ActivityForgetpwdBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.ForgetPwdViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/22.
 */
public class ForgetPwdActivity extends BaseActivity {
    private static final String TAG = "ForgetPwdActivity";
    //    private EditText et_phone, et_code;
//    private ClearEditText login_cet_password, login_cet_password_again;
    private AccountHelper accountHelper;
    private TextView tvGetCode;
    private CountDownUtil countDownUtil;
//    private TextView txt_tishi;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_forgetpwd;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ForgetPwdViewModel mViewModel;

    @Override
    protected void initView() {
        ActivityForgetpwdBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgetpwd);
        mViewModel = ViewModelProviders.of(this).get(ForgetPwdViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        et_phone = findViewById(R.id.et_phone);
//        et_code = findViewById(R.id.et_code);
//        login_cet_password = findViewById(R.id.login_cet_password);
//        login_cet_password_again = findViewById(R.id.login_cet_password_again);
        tvGetCode = mBinding.tvGetcode;//findViewById(R.id.tv_getcode);
//        txt_tishi = (TextView) findViewById(R.id.txt_tishi);


        accountHelper = new AccountHelper();
        EventBus.getDefault().register(ForgetPwdActivity.this);

        initGetCode();
//        findViewById(R.id.st_getpwdagain).setOnClickListener(view -> setPasswordAgain());
    }

    private void initGetCode() {
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

    private void getVerifyCode() {
        String phone = mViewModel.getPhone().getValue();//et_phone.getEditableText().toString();
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
                    CustomToast.showCustomErrorToast(ForgetPwdActivity.this, getString(R.string.phone_unregist));
                } else if (errorEvent == 8) {//已注册
                    countDownUtil.start();
                    accountHelper.getPhoneCode(ForgetPwdActivity.this, phone, true);
                } else {
                    CustomToast.showCustomErrorToast(ForgetPwdActivity.this, getString(R.string.operator_failure));
                }
            }
        });
//        accountHelper.IsPhoneReg(this, phone, true);
    }

    private void setPasswordAgain() {
        String password1 = mViewModel.getPwd().getValue(); //login_cet_password.getEditableText().toString();
        String password2 = mViewModel.getRePwd().getValue(); //login_cet_password_again.getEditableText().toString();
        String phone = mViewModel.getPhone().getValue(); //et_phone.getEditableText().toString();
        String code = mViewModel.getCode().getValue();  //et_code.getEditableText().toString();
        if (password1 == null || password1.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.name_password_cannot_empty));
            return;
        }
        if (password2 == null || password2.isEmpty()) {
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
//        if (!(Utils.isValidPassword(password1))) {
//            CustomToast.showCustomErrorToast(this, getString(R.string.set_err_password));
//            return;
//        }
        if (!password1.equals(password2)) {
            CustomToast.showCustomErrorToast(this, getString(R.string.password_not_same));
            return;
        }
        String password = EncryptUtils.encryptMD5ToString(password1, Core.SALT);
        onsetPwdAgain(phone, code, password);
//      accountHelper.onsetPwdAgain(this, phone, code, password, true);
    }

    private void onsetPwdAgain(final String phone, String code, final String password) {
        currentCall = Core.instance(this).resetPwd(phone, code, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                intent.putExtra("username", phone);
                intent.putExtra("password", password);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(ForgetPwdActivity.this, getString(R.string.operator_failure));
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onsetPwdAgain(AccountEventMsg eventMsg) {
        if (eventMsg.eventType == AccountEventMsg.EVENT_GET_PWD_AGAIN) {
            if (eventMsg.isSucess()) {
                startActivity(new Intent(ForgetPwdActivity.this, LoginActivity.class));
            } else {
                CustomToast.showCustomErrorToast(ForgetPwdActivity.this, getString(R.string.login_fail));
            }
        } /*else if (eventMsg.eventType == AccountEventMsg.EVENT_PHONE_REG) {
            Log.i(TAG, "JINRUN");
            int retCode = (int) eventMsg.eventMsg;
            if (retCode == 7) {//未注册
                countDownUtil.start();
                accountHelper.getPhoneCode(this, et_phone.getEditableText().toString(), true);
            } else if (retCode == 8) {//已注册
                CustomToast.showCustomErrorToast(ForgetPwdActivity.this, getString(R.string.phone_registed));
            } else {
                CustomToast.showCustomErrorToast(this, getString(R.string.operator_failure));
            }
        }*/
    }

//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.forget_password);
//    }

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

        public void onCommit(View view) {
            setPasswordAgain();
        }
    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
        EventBus.getDefault().unregister(ForgetPwdActivity.this);
    }
}
