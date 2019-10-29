package com.rejuvee.smartelectric.family.activity.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.utils.LanguageUtil;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.MainNavigationActivity;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.LogoVersionManage;
import com.rejuvee.smartelectric.family.custom.AccountEventMsg;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.rejuvee.smartelectric.family.utils.AccountHelper;
import com.rejuvee.smartelectric.family.utils.thrid.QQLoginHelper;
import com.rejuvee.smartelectric.family.utils.thrid.WXHelper;
import com.rejuvee.smartelectric.family.widget.CheckableImageView;
import com.rejuvee.smartelectric.family.widget.ClearEditText;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by SH on 2017/12/19.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";
    private TextView hiddenBtn;
    private ClearEditText cetPassword;
    private ClearEditText cetUsername;
    private AccountInfo cacheAccount;

    private AccountHelper accountHelper;
    //    private int requestCode_adddevice = 1000;
//    private ArrayList<CollectorBean> arrCollectorBean;
//    private DeviceHelper deviceHelper;
    private LoadingDlg mWaitDialog;
    private Context mContext;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
//        setToolbarHide(true);
//        setTitle("");
//        setContentView(getLayoutResId());
        hiddenBtn = findViewById(R.id.hidden_btn);
        cetUsername = findViewById(R.id.login_cet_username);
        cetPassword = findViewById(R.id.login_cet_password);
        ImageView imgLogo = findViewById(R.id.iv_logo);
        imgLogo.setImageResource(LogoVersionManage.getInstance().getLoginLogo());
        LanguageUtil.SwitchLang(this);
        accountHelper = new AccountHelper();
//        deviceHelper = new DeviceHelper();


        findViewById(R.id.iv_weixin_login).setOnClickListener(this);
        findViewById(R.id.iv_qq_login).setOnClickListener(this);

        EventBus.getDefault().register(this);
        mWaitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        if (isApkInDebug(this)) {
            // 隐藏按钮 事件
            hiddenBtn.setOnTouchListener(new HiddenClickListener(new HiddenClickListener.MyClickCallBack() {
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
                    startActivity(new Intent(mContext, HiddenDialogActivity.class));
                }
            }));
        }
        // 查看密码 事件
        CheckableImageView checkableImageView = findViewById(R.id.login_civ_eye);
        checkableImageView.setOnCheckChangeListener(new CheckableImageView.OnCheckChangeListener() {
            @Override
            public void onCheckChanged(boolean checked) {
                int type = checked ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
//                checkableImageView.setBackgroundDrawable(checked ? getDrawable(R.drawable.eye) : getDrawable(R.drawable.eye_close));
                cetPassword.setInputType(type);
            }
        });
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
    @Override
    protected void dealloc() {
        //防止内存泄漏
        cetPassword.removeTextChangedListener();
        cetUsername.removeTextChangedListener();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        String userName = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        if (userName != null && password != null) {
            login(userName, password);
        }
    }

    @SuppressLint("SetTextI18n")
    private void autoLogin() {
        cacheAccount = AccountHelper.getCacheAccount();
        if (cacheAccount != null) {
            cetUsername.setText(cacheAccount.getUserName());
            cetPassword.setText("******");
            login(cacheAccount.getUserName(), cacheAccount.getPassword());
        } else {
            if (isApkInDebug(this)) {
                cetUsername.setText("test");// TODO 测试账号
                cetPassword.setText("test");
            }
        }
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void login(final String userName, final String password) {
        accountHelper.Login(this, userName, password, true);
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

    private void onClickLogin() {
        String userName = cetUsername.getEditableText().toString();
        String password = cetPassword.getEditableText().toString();
        if (userName.isEmpty() || password.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.name_password_cannot_empty));
            return;
        }
        if (password.equals("******")) {
            autoLogin();
        } else {
            password = EncryptUtils.encryptMD5ToString(password, Core.SALT);
            login(userName, password);
        }
    }

    private void gotoMainActivity() {
        startActivity(new Intent(this, MainNavigationActivity.class));
        overridePendingTransition(R.anim.top_in, R.anim.top_out);
        finish();
    }

    @Override
    public void onClick(View v) {
//        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_tv_forget:
//                Intent intent = new Intent();
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.login_tv_login:
                onClickLogin();
                break;
            case R.id.login_tv_regist:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
//            case R.id.login_tv_visitor:
//                gotoMainActivity();
//                break;
            case R.id.iv_weixin_login:
                WXHelper.startWxLogin(this);
                break;
            case R.id.iv_qq_login:
                QQLoginHelper.getInstance().qqLogin(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QQLoginHelper.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    private void deleteCache() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.deleteRealm();
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
                    CustomToast.showCustomErrorToast(LoginActivity.this, getString(R.string.login_fail));
                }
            });
        }
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
