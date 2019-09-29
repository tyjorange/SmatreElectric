package com.rejuvee.smartelectric.family.activity.login;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.rejuvee.smartelectric.family.utils.utils;
import com.rejuvee.smartelectric.family.widget.ClearEditText;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AccountBaseActivity extends BaseActivity implements ClearEditText.OnCheckListener, View.OnClickListener {

    private final static String TAG = "AccountBaseActivity";
    /**
     * api接口
     */
    protected Core mCore;
    /**
     * 等待提示
     */
    protected LoadingDlg mLoadingDlg;
    /**
     * 提示信息
     */
    protected CustomToast mCustomToast;

    /**
     * 确定或注册按钮
     */
    protected TextView mTvSure;
    /**
     * 标题
     */
    protected TextView mTvTitle;
    /**
     * Imei号或原Imei号
     */
    protected String mImei;
    /**
     * 电话号或原手机号
     */
    protected String mPhoneOrOld;
    /**
     * 新电话号
     */
    protected String mNewPhone;
    /**
     * 验证码
     */
    protected String mCode;
    /**
     * 密码、原密码或新密码
     */
    protected String mPwdOrNewOrOld;
    /**
     * 原密码
     */
    protected String mPwdOrOld;

    protected String oLdpassword;

    /**
     * 确认密码
     */
    protected String mRePwd;

    private TextView mBtCode;// 获取验证码
    private Handler mHandler;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int time = 60;

    private ClearEditText mEtImei;
    private ClearEditText mEtPhone;
    private ClearEditText mEtNewPhone;
    private ClearEditText mEtCode;
    private ClearEditText mEtPwd;
    private ClearEditText mEtRePwd;
    private ClearEditText mEtOrgPwd;

    private TextView mTvImei;
    private TextView mTvPhone;
    private TextView mTvNewPhone;
    private TextView mTvCode;
    private TextView mTvPwd;
    private TextView mTvRePwd;
    private TextView mTvOrgPwd;

    private ImageView mIvImei;
    private ImageView mIvPhone;
    private ImageView mIvNewPhone;
    private ImageView mIvCode;
    private ImageView mIvPwd;
    private ImageView mIvRePwd;
    private ImageView mIvOrgPwd;

    private boolean isOk = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    //    @SuppressLint("HandlerLeak")
    @Override
    protected void initView() {
        mCore = Core.instance(this);
        mCustomToast = new CustomToast();
        mLoadingDlg = new LoadingDlg(this, -1);
        // 计时重新获取验证码
        mHandler = new MyHandler(this);
        baseInit();
        init();
    }

    private static class MyHandler extends Handler {
        WeakReference<AccountBaseActivity> activityWeakReference;

        MyHandler(AccountBaseActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AccountBaseActivity activity = activityWeakReference.get();
            activity.mBtCode.setText(msg.what + "s");
            if (msg.what == 0) {
                activity.closeTimer();
                activity.mBtCode.setText(R.string.mark_regetcode);
                activity.mBtCode.setEnabled(true);
            }
        }
    }
    @Override
    protected void initData() {

    }

//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.reset_password);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }

    private void baseInit() {

        ImageView iv_back = findViewById(R.id.iv_back);
        mEtImei = findViewById(R.id.edit_imei);
        mEtPhone = findViewById(R.id.edit_phone);
        mEtNewPhone = findViewById(R.id.edit_phone_new);
        mEtCode = findViewById(R.id.edit_code);
        mEtPwd = findViewById(R.id.edit_pwd);
        mEtRePwd = findViewById(R.id.edit_repwd);
        mEtOrgPwd = findViewById(R.id.edit_org_pwd);

        mTvImei = findViewById(R.id.txt_wrong_imei);
        mTvPhone = findViewById(R.id.txt_wrong_phone);
        mTvNewPhone = findViewById(R.id.txt_wrong_phone_new);
        mTvCode = findViewById(R.id.txt_wrong_code);
        mTvPwd = findViewById(R.id.txt_wrong_pwd);
        mTvRePwd = findViewById(R.id.txt_wrong_repwd);
        mTvOrgPwd = findViewById(R.id.txt_wrong_orgpwd);

        mIvImei = findViewById(R.id.img_imei);
        mIvPhone = findViewById(R.id.img_phone);
        mIvNewPhone = findViewById(R.id.img_phone_new);
        mIvCode = findViewById(R.id.img_code);
        mIvPwd = findViewById(R.id.img_pwd);
        mIvRePwd = findViewById(R.id.img_repwd);
        mIvOrgPwd = findViewById(R.id.img_org_pwd);

        mBtCode = findViewById(R.id.txt_code);
        mTvSure = findViewById(R.id.txt_sure);

        iv_back.setOnClickListener(this);
        mBtCode.setOnClickListener(this);
        mTvSure.setOnClickListener(this);
        mEtImei.setOnCheckListener(this);
        mEtPhone.setOnCheckListener(this);
        mEtNewPhone.setOnCheckListener(this);
        mEtCode.setOnCheckListener(this);
        mEtPwd.setOnCheckListener(this);
        mEtRePwd.setOnCheckListener(this);
        mEtOrgPwd.setOnCheckListener(this);


    }

    /**
     * 根据不同操作初始化更改界面
     */
    protected void init() {

    }

    /**
     * 检查是否可以提交或注册
     */
    protected boolean canCommitOrRegist() {
        return isOk;
    }

    /**
     * 提交或注册
     */
    protected void commitOrRegist() {

    }

    /**
     * 检查获取验证码的手机号是否正确
     */
    protected boolean checkCodePhone() {
        return isOk;
    }

    /**
     * 获取验证码的手机号
     */
    protected String codePhone() {
        return null;
    }

    /**
     * 获取加密密码
     */
    protected String pwd() {
        return EncryptUtils.encryptMD5ToString(mPwdOrNewOrOld, Core.SALT);
    }

    /**
     * 检查Imei号或原Imei号是否正确
     */
    protected boolean checkImeiOrOldImei() {
        isOk = false;
        mImei = mEtImei.getEditableText().toString();
        if (mImei.length() != 12) {
            mTvImei.setVisibility(View.VISIBLE);
            mTvImei.setText(R.string.hint_imei);
            mIvImei.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvImei.setVisibility(View.GONE);
            mIvImei.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    /**
     * 检查手机号或原手机号是否正确
     */
    protected boolean checkPhoneOrOldPhone() {
        isOk = false;
        mPhoneOrOld = mEtPhone.getEditableText().toString();
        if (mPhoneOrOld.length() <= 0) {
            mTvPhone.setVisibility(View.VISIBLE);
            mTvPhone.setText(R.string.hint_phone);
            mIvPhone.setVisibility(View.GONE);
        } else if (mPhoneOrOld.length() < 11 || !utils.isPhone(mPhoneOrOld)) {
            mTvPhone.setVisibility(View.VISIBLE);
            mTvPhone.setText(R.string.wrong_phone);
            mIvPhone.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvPhone.setVisibility(View.GONE);
            mIvPhone.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    /**
     * 检查新手机号是否正确
     */
    protected boolean checkNewPhone() {
        isOk = false;
        mNewPhone = mEtNewPhone.getEditableText().toString();
        if (mNewPhone.length() <= 0) {
            mTvNewPhone.setVisibility(View.VISIBLE);
            mTvNewPhone.setText(R.string.hint_phone);
            mIvNewPhone.setVisibility(View.GONE);
        } else if (mNewPhone.length() < 11 || !utils.isPhone(mNewPhone)) {
            mTvNewPhone.setVisibility(View.VISIBLE);
            mTvNewPhone.setText(R.string.wrong_phone);
            mIvNewPhone.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvNewPhone.setVisibility(View.GONE);
            mIvNewPhone.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    /**
     * 检查验证码是否正确
     */
    protected boolean checkCode() {
        isOk = false;
        mCode = mEtCode.getEditableText().toString();
        if (mCode.length() <= 0) {
            mTvCode.setVisibility(View.VISIBLE);
            mTvCode.setText(R.string.hint_code);
            mIvCode.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvCode.setVisibility(View.GONE);
            mIvCode.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    /**
     * 检查密码、旧密码或新密码是否正确
     */
    protected boolean checkPwdOrOldOrNew() {
        isOk = false;
        mPwdOrNewOrOld = mEtPwd.getEditableText().toString();
        if (mPwdOrNewOrOld.length() <= 0) {
            mTvPwd.setVisibility(View.VISIBLE);
            mTvPwd.setText(R.string.hint_password);
            mIvPwd.setVisibility(View.GONE);
        } else if (mPwdOrNewOrOld.length() < 6 || mPwdOrNewOrOld.length() > 12) {
            mTvPwd.setVisibility(View.VISIBLE);
            mTvPwd.setText(R.string.wrong_password);
            mIvPwd.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvPwd.setVisibility(View.GONE);
            mIvPwd.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    private void getCacheAccount() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.open();
        List<AccountInfo> listAccount = accountInfoRealm.getAccountInfoList();
        if (listAccount != null && listAccount.size() > 0) {
            AccountInfo cacheAccount = listAccount.get(0);
            oLdpassword = cacheAccount.getPassword();
        }
        accountInfoRealm.close();
    }

    /**
     * 检查原始密码是否正确
     */
    protected boolean checkPwdOrOld() {
        getCacheAccount();
        Log.i("old", oLdpassword);
        isOk = false;
        mPwdOrOld = mEtOrgPwd.getEditableText().toString();
        if (mPwdOrOld.length() <= 0) {
            mTvOrgPwd.setVisibility(View.VISIBLE);
            mTvOrgPwd.setText(R.string.hint_null_password);
            mTvOrgPwd.setVisibility(View.GONE);
        } else if (mPwdOrOld.length() < 6 || mPwdOrOld.length() > 12 || !EncryptUtils.encryptMD5ToString(mPwdOrOld, Core.SALT).equals(oLdpassword)) {
            mTvOrgPwd.setVisibility(View.VISIBLE);
            mTvOrgPwd.setText(R.string.wrong_password);
            mIvOrgPwd.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvOrgPwd.setVisibility(View.GONE);
            mIvOrgPwd.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    /**
     * 检查确认密码是否正确
     */
    protected boolean checkRePwd() {
        isOk = false;
        mRePwd = mEtRePwd.getText().toString();
        if (mRePwd.length() <= 0) {
            mTvRePwd.setVisibility(View.VISIBLE);
            mTvRePwd.setText(R.string.hint_repassword);
            mIvRePwd.setVisibility(View.GONE);
        } else if (!mRePwd.equals(mPwdOrNewOrOld)) {
            mTvRePwd.setVisibility(View.VISIBLE);
            mTvRePwd.setText(R.string.wrong_repassword);
            mIvRePwd.setVisibility(View.GONE);
        } else {
            isOk = true;
            mTvRePwd.setVisibility(View.GONE);
            mIvRePwd.setVisibility(View.VISIBLE);
        }
        return isOk;
    }

    // 获取验证码
    private void getCode() {
        if (checkPhoneOrOldPhone()) {
            Core.instance(this).isPhoneRegister(mPhoneOrOld, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {

                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    if (errorEvent == 7) {
                        CustomToast.showCustomErrorToast(AccountBaseActivity.this, getString(R.string.phone_unregist));
                    } else if (errorEvent == 8) {
                        Core.instance(AccountBaseActivity.this).getPhoneCode(mPhoneOrOld, new ActionCallbackListener<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                mBtCode.setEnabled(false);
                                openTimer();
                            }

                            @Override
                            public void onFailure(int errorEvent, String message) {
                                CustomToast.showCustomErrorToast(AccountBaseActivity.this, getString(R.string.op_fail));
                            }
                        });
                    }
                }
            });
        }
    }

    private void openTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = time--;
                mHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void closeTimer() {
        mTimer.cancel();
        mTimerTask.cancel();
        time = 60;
    }

    @Override
    public void onCheck(int id) {
        switch (id) {
            case R.id.edit_imei:
                checkImeiOrOldImei();
                break;
            case R.id.edit_phone:
                checkPhoneOrOldPhone();
                break;
            case R.id.edit_phone_new:
                checkNewPhone();
                break;
            case R.id.edit_code:
                checkCode();
                break;
            case R.id.edit_pwd:
                checkPwdOrOldOrNew();
                break;
            case R.id.edit_repwd:
                checkRePwd();
                break;
            case R.id.edit_org_pwd:
                checkPwdOrOld();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_code:
                getCode();
                break;
            case R.id.txt_sure:
                if (canCommitOrRegist()) {
                    commitOrRegist();
                } else {
                    Toast.makeText(this, R.string.hint_input, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
