package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.EncryptUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.AccountBaseActivity;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;

/**
 * Created by Administrator on 2018/3/22.
 */
public class ModifyPasswordActivity extends AccountBaseActivity {

    private final static String TAG = "ModifyPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
    protected void init() {
        ((TextView) findViewById(R.id.txt_mark_phone)).setText(R.string.original_password);
        findViewById(R.id.ll_phone).setVisibility(View.GONE);
        findViewById(R.id.ll_imei).setVisibility(View.GONE);
        findViewById(R.id.ll_code).setVisibility(View.GONE);
        findViewById(R.id.ll_newphone).setVisibility(View.GONE);
    }


//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.settings_change_password);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }


    @Override
    protected boolean checkCodePhone() {
        return checkPhoneOrOldPhone();
    }

    @Override
    protected boolean canCommitOrRegist() {
        return checkPwdOrOldOrNew() && checkRePwd();
    }

//    @Override
//    protected String codePhone() {
//        return mPhoneOrOld;
//    }

    @Override
    protected void commitOrRegist() {
        mLoadingDlg.show();
        mCore.resetPassword(EncryptUtils.encryptMD5ToString(mPwdOrOld, Core.SALT), pwd(), new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                mLoadingDlg.dismiss();
                //mCustomToast.setText(R.string.forgetpwd_success).show();
                //存入本地
                AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
                accountInfoRealm.open();
                AccountInfo accountInfo = accountInfoRealm.getAccountInfoList().get(0);
                accountInfo.setPassword(pwd());
                accountInfoRealm.addAccount(accountInfo);
                accountInfoRealm.close();

                startActivity(new Intent(ModifyPasswordActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                CustomToast.showCustomToast(ModifyPasswordActivity.this, getString(R.string.forgetpwd_success));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mLoadingDlg.dismiss();
                CustomToast.showCustomToast(ModifyPasswordActivity.this, getString(R.string.forgetpwd_fail));
            }
        });
    }
}
