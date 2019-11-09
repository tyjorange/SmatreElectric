package com.rejuvee.smartelectric.family.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.utils.LanguageUtil;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.MainNavigationActivity;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.custom.AccountEventMsg;
import com.rejuvee.smartelectric.family.common.utils.AccountHelper;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.common.utils.thrid.QQLoginHelper;
import com.rejuvee.smartelectric.family.common.utils.thrid.WXHelper;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityLoginBinding;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.rejuvee.smartelectric.family.model.viewmodel.LoginViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by SH on 2017/12/19.
 */
public class LoginActivity extends BaseActivity {
    private String TAG = "LoginActivity";
    //    private TextView hiddenBtn;
//    private ClearEditText cetPassword;
//    private ClearEditText cetUsername;
//    private AccountInfo cacheAccount;
//    private AccountHelper accountHelper;
    //    private int requestCode_adddevice = 1000;
//    private ArrayList<CollectorBean> arrCollectorBean;
//    private DeviceHelper deviceHelper;
    private LoadingDlg mWaitDialog;
//    private Context mContext;

    private ActivityLoginBinding mBinding;
    private LoginViewModel mViewModel;
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_login;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mViewModel.setAccountHelper(new AccountHelper());
        mViewModel.setCacheAccountInfo(AccountHelper.getCacheAccount());
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mViewModel);

//        mContext = this;
//        setToolbarHide(true);
//        setTitle("");
//        setContentView(getLayoutResId());
//        hiddenBtn = findViewById(R.id.hidden_btn);
//        cetUsername = findViewById(R.id.login_cet_username);
//        cetPassword = findViewById(R.id.login_cet_password);
//        ImageView imgLogo = findViewById(R.id.iv_logo);
//        mBinding.ivLogo.setImageResource(LogoVersionManage.getInstance().getLoginLogo());
//        accountHelper = new AccountHelper();
//        deviceHelper = new DeviceHelper();


//        mBinding.ivWeixinLogin.setOnClickListener(this);
//        mBinding.ivQqLogin.setOnClickListener(this);

        LanguageUtil.SwitchLang(this);
        EventBus.getDefault().register(this);
        mWaitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        if (ValidateUtils.isApkInDebug(this)) {
            // 隐藏按钮 事件
            mBinding.hiddenBtn.setOnTouchListener(new HiddenClickListener(new HiddenClickListener.MyClickCallBack() {
                @Override
                public void oneClick() {
//                CustomToast.showCustomErrorToast(LoginActivity.this, "111");
                }

                @Override
                public void doubleClick() {
//                CustomToast.showCustomErrorToast(LoginActivity.this, "222");
                }

                @Override
                public void threeClick() {
                    startActivity(new Intent(getBaseContext(), HiddenDialogActivity.class));
                }
            }));
        }
        // 查看密码 事件
//        CheckableImageView checkableImageView = findViewById(R.id.login_civ_eye);
        mBinding.loginCivEye.setOnCheckChangeListener(checked -> {
            int type = checked ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
//                checkableImageView.setBackgroundDrawable(checked ? getDrawable(R.drawable.eye) : getDrawable(R.drawable.eye_close));
            mBinding.loginCetPassword.setInputType(type);
        });
//        mBinding.llPrivacy.setOnClickListener(v -> {
//            startActivity(new Intent(mContext, PrivacyActivity.class));
//        });
        autoLogin();
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return false;
//    }
//

    /**
     * 自动登录
     */
    private void autoLogin() {
        AccountInfo cacheAccount = mViewModel.getCacheAccountInfo().getValue();
        if (cacheAccount != null) {
            mBinding.loginCetUsername.setText(cacheAccount.getUserName());
            mBinding.loginCetPassword.setText("******");
            apiLogin(cacheAccount.getUserName(), cacheAccount.getPassword());
        } else {
            if (ValidateUtils.isApkInDebug(this)) {
                mBinding.loginCetUsername.setText("test");// TODO 测试账号
                mBinding.loginCetPassword.setText("test");
            }
        }
    }

    /**
     * 点击登录
     *
     * @param view
     */
    private void onCheckLogin(View view) {
        String userName = mBinding.loginCetUsername.getEditableText().toString();
        String password = mBinding.loginCetPassword.getEditableText().toString();
        if (userName.isEmpty() || password.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.name_password_cannot_empty));
            return;
        }
        if (password.equals("******")) {
            autoLogin();
        } else {
            password = EncryptUtils.encryptMD5ToString(password, Core.SALT);
            apiLogin(userName, password);
        }
    }

    private void apiLogin(final String userName, final String password) {
        mViewModel.getAccountHelper().getValue().toLogin(this, userName, password, true);
    }

//    @Override
//    public void onClick(View v) {
////        super.onClick(v);
//        switch (v.getId()) {
//            case R.id.login_tv_forget:
////                Intent intent = new Intent();
//                startActivity(new Intent(this, ForgetPwdActivity.class));
//                break;
//            case R.id.login_tv_login:
//                onClickLogin();
//                break;
//            case R.id.login_tv_regist:
//                startActivity(new Intent(this, RegisterActivity.class));
//                break;
////            case R.id.login_tv_visitor:
////                gotoMainActivity();
////                break;
//            case R.id.iv_weixin_login:
//                WXHelper.startWxLogin(this);
//                break;
//            case R.id.iv_qq_login:
//                QQLoginHelper.getInstance().qqLogin(this);
//                break;
//        }
//    }

    public class Presenter {
        public void onForget(View view) {
            startActivity(new Intent(view.getContext(), ForgetPwdActivity.class));
        }

        public void onRegister(View view) {
            startActivity(new Intent(view.getContext(), RegisterActivity.class));
        }

        public void onLogin(View view) {
            onCheckLogin(view);
        }

        public void onWXLogin(View view) {
            WXHelper.startWxLogin(view.getContext());
        }

        public void onQQLogin(View view) {
            QQLoginHelper.getInstance().qqLogin((Activity) view.getContext());
        }

        public void onPrivacy(View view) {
            startActivity(new Intent(view.getContext(), PrivacyActivity.class));
        }
    }

    private void deleteCache() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.deleteRealm();
    }

    @Override
    protected void dealloc() {
        //防止内存泄漏
        mBinding.loginCetPassword.removeTextChangedListener();
        mBinding.loginCetUsername.removeTextChangedListener();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QQLoginHelper.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        String userName = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        if (userName != null && password != null) {
            apiLogin(userName, password);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThirdPartLogin(ThirdPartyInfo thirdPartyInfo) {
        if (thirdPartyInfo.isSucess) {
            mWaitDialog.show();
            Core.instance(this).ThirdPartLogin(thirdPartyInfo, new ActionCallbackListener<AccountInfo>() {
                @Override
                public void onSuccess(AccountInfo data) {
                    mWaitDialog.dismiss();
                    deleteCache();
//                    AppGlobalConfig.USER_KRY = data.getUserkey();
//                    bindAccount(data.getUserName());
                    MainApplication.bindAliCloud(data.getUserName());
                    gotoMainActivity();
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    mWaitDialog.dismiss();
                    CustomToast.showCustomErrorToast(LoginActivity.this, message);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(AccountEventMsg eventMsg) {
        if (eventMsg.eventType == AccountEventMsg.EVENT_LOGIN) {
            if (eventMsg.isSucess()) {
                AccountHelper.deleteCache();
                AccountHelper.saveAccount((AccountInfo) eventMsg.getEventMsg());
//                bindAccount(((AccountInfo) eventMsg.getEventMsg()).getUserName());
                MainApplication.bindAliCloud(((AccountInfo) eventMsg.getEventMsg()).getUserName());
                gotoMainActivity();
            } else {
                //Toast.makeText(this, "3=", Toast.LENGTH_LONG).show();
                CustomToast.showCustomErrorToast(LoginActivity.this, getString(R.string.login_fail));
            }
        }
    }

    private void gotoMainActivity() {
        startActivity(new Intent(this, MainNavigationActivity.class));
        overridePendingTransition(R.anim.top_in, R.anim.top_out);
        finish();
    }

    public static class HiddenClickListener implements View.OnTouchListener {
        private int timeout = 500;//双击间四百毫秒延时
        private int clickCount = 0;//记录连续点击次数
        private Handler handler;
        private MyClickCallBack myClickCallBack;

        public interface MyClickCallBack {
            void oneClick();//点击一次的回调

            void doubleClick();//连续点击两次的回调

            void threeClick();//连续点击三次及其以上的回调
        }

        HiddenClickListener(MyClickCallBack myClickCallBack) {
            this.myClickCallBack = myClickCallBack;
            handler = new Handler();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                clickCount++;
                handler.postDelayed(() -> {
                    if (clickCount == 1) {
                        myClickCallBack.oneClick();
                    } else if (clickCount == 2) {
                        myClickCallBack.doubleClick();
                    } else if (clickCount == 3) {
                        myClickCallBack.threeClick();
                    }
                    handler.removeCallbacksAndMessages(null);
                    //清空handler延时，并防内存泄漏
                    clickCount = 0;//计数清零
                }, timeout);//延时timeout后执行run方法中的代码
            }
            return false;//让点击事件继续传播，方便再给View添加其他事件监听
        }
    }
}
