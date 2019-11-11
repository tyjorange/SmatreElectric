package com.rejuvee.smartelectric.family.activity.login;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.common.widget.ClearEditText;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityAccountBinding;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.rejuvee.smartelectric.family.model.viewmodel.AccountBaseViewModel;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AccountBaseActivity extends BaseActivity implements ClearEditText.OnCheckListener {

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
//    protected TextView mTvSure;
    /**
     * 标题
     */
//    protected TextView mTvTitle;
    /**
     * Imei号或原Imei号
     */
//    protected String mImei;
    /**
     * 电话号或原手机号
     */
//    protected String mPhoneOrOld;
    /**
     * 新电话号
     */
//    protected String mNewPhone;
    /**
     * 验证码
     */
//    protected String mCode;
    /**
     * 密码、原密码或新密码
     */
//    protected String mPwdOrNewOrOld;
    /**
     * 原密码
     */
    protected String mPwdOrOld;

    protected String oLdpassword;

    /**
     * 确认密码
     */
    protected String mRePwd;

    //    private TextView mBtCode;// 获取验证码
    private Handler mHandler;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int time = 60;

//    private ClearEditText mEtImei;
//    private ClearEditText mEtPhone;
//    private ClearEditText mEtNewPhone;
//    private ClearEditText mEtCode;
//    private ClearEditText mEtPwd;
//    private ClearEditText mEtRePwd;
//    private ClearEditText mEtOrgPwd;

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

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_account;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    //    @SuppressLint("HandlerLeak")
    protected ActivityAccountBinding mBinding;
    private AccountBaseViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        mViewModel = ViewModelProviders.of(this).get(AccountBaseViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        mCore = Core.instance(this);
        mCustomToast = new CustomToast();
        mLoadingDlg = new LoadingDlg(this, -1);
        // 计时重新获取验证码
        mHandler = new MyHandler(this);
        baseInit();
//        init();
    }

    private void baseInit() {
//        ImageView iv_back = findViewById(R.id.iv_back);
//        mEtImei = findViewById(R.id.edit_imei);
//        mEtPhone = findViewById(R.id.edit_phone);
//        mEtNewPhone = findViewById(R.id.edit_phone_new);
//        mEtCode = findViewById(R.id.edit_code);
//        mEtPwd = findViewById(R.id.edit_pwd);
//        mEtRePwd = findViewById(R.id.edit_repwd);
//        mEtOrgPwd = findViewById(R.id.edit_org_pwd);

        //错误提示
        mTvImei = mBinding.txtWrongImei;//findViewById(R.id.txt_wrong_imei);
        mTvPhone = mBinding.txtWrongPhone;//findViewById(R.id.txt_wrong_phone);
        mTvNewPhone = mBinding.txtWrongPhoneNew;//findViewById(R.id.txt_wrong_phone_new);
        mTvCode = mBinding.txtWrongCode;//findViewById(R.id.txt_wrong_code);
        mTvPwd = mBinding.txtWrongPwd;//findViewById(R.id.txt_wrong_pwd);
        mTvRePwd = mBinding.txtWrongRepwd;//findViewById(R.id.txt_wrong_repwd);
        mTvOrgPwd = mBinding.txtWrongOrgpwd;//findViewById(R.id.txt_wrong_orgpwd);
        //正确提示
        mIvImei = mBinding.imgImei;//findViewById(R.id.img_imei);
        mIvPhone = mBinding.imgPhone;//findViewById(R.id.img_phone);
        mIvNewPhone = mBinding.imgPhoneNew;//findViewById(R.id.img_phone_new);
        mIvCode = mBinding.imgCode;//findViewById(R.id.img_code);
        mIvPwd = mBinding.imgPwd;//findViewById(R.id.img_pwd);
        mIvRePwd = mBinding.imgRepwd;//findViewById(R.id.img_repwd);
        mIvOrgPwd = mBinding.imgOrgPwd;//findViewById(R.id.img_org_pwd);

//        mBtCode = findViewById(R.id.txt_code);
//        mTvSure = findViewById(R.id.txt_sure);
//        iv_back.setOnClickListener(this);
//        mBtCode.setOnClickListener(this);
//        mTvSure.setOnClickListener(this);

        mBinding.editImei.setOnCheckListener(this);
        mBinding.editPhone.setOnCheckListener(this);
        mBinding.editPhoneNew.setOnCheckListener(this);
        mBinding.editCode.setOnCheckListener(this);
        mBinding.editPwd.setOnCheckListener(this);
        mBinding.editRepwd.setOnCheckListener(this);
        mBinding.editOrgPwd.setOnCheckListener(this);
    }

    private static class MyHandler extends Handler {
        WeakReference<AccountBaseActivity> activityWeakReference;

        MyHandler(AccountBaseActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AccountBaseActivity activity = activityWeakReference.get();
            activity.mBinding.txtCode.setText(String.format(Locale.getDefault(), "%ds", msg.what));
            if (msg.what == 0) {
                activity.closeTimer();
                activity.mBinding.txtCode.setText(R.string.mark_regetcode);
                activity.mBinding.txtCode.setEnabled(true);
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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onGetCode(View view) {
            getCode();
        }

        public void onSure(View view) {
            if (canCommitOrRegist()) {
                commitOrRegist();
            } else {
                Toast.makeText(view.getContext(), R.string.hint_input, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void dealloc() {

    }

    /**
     * 根据不同操作初始化更改界面
     */
//    protected void init() {
//
//    }

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
//    protected String codePhone() {
//        return null;
//    }

    /**
     * 获取加密密码
     */
    protected String pwd() {
        return EncryptUtils.encryptMD5ToString(mViewModel.getPwd().getValue(), Core.SALT);
    }

    /**
     * 检查Imei号或原Imei号是否正确
     */
    protected boolean checkImeiOrOldImei() {
        isOk = false;
        String mImei = mViewModel.getImei().getValue();//mEtImei.getEditableText().toString();
        if (Objects.requireNonNull(mImei).length() != 12) {
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
        String mPhoneOrOld = mViewModel.getPhone().getValue();//mEtPhone.getEditableText().toString();
        if (Objects.requireNonNull(mPhoneOrOld).length() <= 0) {
            mTvPhone.setVisibility(View.VISIBLE);
            mTvPhone.setText(R.string.hint_phone);
            mIvPhone.setVisibility(View.GONE);
        } else if (mPhoneOrOld.length() < 11 || !ValidateUtils.isPhone(mPhoneOrOld)) {
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
        String mNewPhone = mViewModel.getNewPhone().getValue();    //mEtNewPhone.getEditableText().toString();
        if (Objects.requireNonNull(mNewPhone).length() <= 0) {
            mTvNewPhone.setVisibility(View.VISIBLE);
            mTvNewPhone.setText(R.string.hint_phone);
            mIvNewPhone.setVisibility(View.GONE);
        } else if (mNewPhone.length() < 11 || !ValidateUtils.isPhone(mNewPhone)) {
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
        String mCode = mViewModel.getCode().getValue();// mEtCode.getEditableText().toString();
        if (Objects.requireNonNull(mCode).length() <= 0) {
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
        String mPwdOrNewOrOld = mViewModel.getPwd().getValue();//mEtPwd.getEditableText().toString();
        if (Objects.requireNonNull(mPwdOrNewOrOld).length() <= 0) {
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
        mPwdOrOld = mViewModel.getOrgPwd().getValue();// mEtOrgPwd.getEditableText().toString();
        if (Objects.requireNonNull(mPwdOrOld).length() <= 0) {
            mTvOrgPwd.setVisibility(View.VISIBLE);
            mTvOrgPwd.setText(R.string.hint_null_password);
            mTvOrgPwd.setVisibility(View.GONE);
        } else if (mPwdOrOld.length() < 4 || mPwdOrOld.length() > 12 || !EncryptUtils.encryptMD5ToString(mPwdOrOld, Core.SALT).equals(oLdpassword)) {
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
        String mRePwd = mViewModel.getRePwd().getValue();//mEtRePwd.getText().toString();
        if (Objects.requireNonNull(mRePwd).length() <= 0) {
            mTvRePwd.setVisibility(View.VISIBLE);
            mTvRePwd.setText(R.string.hint_repassword);
            mIvRePwd.setVisibility(View.GONE);
        } else if (!mRePwd.equals(mViewModel.getPwd().getValue())) {
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
            Core.instance(this).isPhoneRegister(mViewModel.getPhone().getValue(), new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {

                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    if (errorEvent == 7) {
                        CustomToast.showCustomErrorToast(AccountBaseActivity.this, getString(R.string.phone_unregist));
                    } else if (errorEvent == 8) {
                        Core.instance(AccountBaseActivity.this).getPhoneCode(mViewModel.getPhone().getValue(), new ActionCallbackListener<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                mBinding.txtCode.setEnabled(false);
                                openTimer();
                            }

                            @Override
                            public void onFailure(int errorEvent, String message) {
                                CustomToast.showCustomErrorToast(AccountBaseActivity.this, message);
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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.txt_code:
//                getCode();
//                break;
//            case R.id.txt_sure:
//                if (canCommitOrRegist()) {
//                    commitOrRegist();
//                } else {
//                    Toast.makeText(this, R.string.hint_input, Toast.LENGTH_LONG).show();
//                }
//                break;
//            case R.id.iv_back:
//                finish();
//                break;
//        }
//    }
}
