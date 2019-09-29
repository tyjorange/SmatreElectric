package com.rejuvee.smartelectric.family.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.Utils;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.AppGlobalConfig;
import com.rejuvee.smartelectric.family.custom.AccountEventMsg;
import com.rejuvee.smartelectric.family.custom.DeviceEventMsg;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by liuchengran on 2018/10/31.
 * <p>
 * 账户操作类， UI <----> helper <----> model
 */
public class AccountHelper {

    private LoadingDlg mWaitDialog;
    private boolean isFirstUse = false;//是否新用户第一次使用
    private String TAG = "AccountHelper";

    /**
     * 获取缓存账户信息
     *
     * @return
     */
    public static AccountInfo getCacheAccount() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.open();

        List<AccountInfo> listAccount = accountInfoRealm.getAccountInfoList();
        AccountInfo cache = null;
        if (listAccount != null && listAccount.size() > 0) {
            cache = listAccount.get(0);
        }

        accountInfoRealm.close();
        return cache;
    }

    /**
     * 删除所有缓存信息
     */
    public static void deleteCache() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.deleteRealm();
    }

    /**
     * 保存用户信息
     *
     * @param accountInfo
     */
    public static void saveAccount(AccountInfo accountInfo) {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.open();
        accountInfoRealm.addAccount(accountInfo);
        accountInfoRealm.close();
    }

    public void Login(Context context, final String account, final String password, boolean showLoading) {
        showDialogs(context, showLoading);
        String userName = null, phone = null;
        if (Utils.isPhone(account)) {
            phone = account;
        } else {
            userName = account;
        }

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            Core.instance(context).Login(userName, phone, packageInfo.versionCode + "", password, new ActionCallbackListener<AccountInfo>() {
                @Override
                public void onSuccess(AccountInfo data) {
                    AccountEventMsg eventMsg = new AccountEventMsg(AccountEventMsg.EVENT_LOGIN);
                    eventMsg.setSucess(true);
                    if (data == null) {
                        data = new AccountInfo();
                    }
                    data.setUserName(account);
                    data.setPassword(password);
                    eventMsg.setEventMsg(data);
//                    AppGlobalConfig.USER_KRY = data.getUserkey();
                    EventBus.getDefault().post(eventMsg);
                    dimissDialog();
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    AccountEventMsg eventMsg = new AccountEventMsg(AccountEventMsg.EVENT_LOGIN);
                    eventMsg.setSucess(false);
                    EventBus.getDefault().post(eventMsg);
                    dimissDialog();
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param phone
     * @param password
     * @param vcode    手机验证码
     */
    public void Register(final Context context, String userName, final String phone, final String password, String vcode) {
        showDialogs(context, true);
        Core.instance(context).RegisterByPhone(phone, userName, vcode, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                postResult(AccountEventMsg.EVENT_REGISTER, data, null, true);
                //标识第一次使用
                setUserFirstUse(true, context);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                postResult(AccountEventMsg.EVENT_REGISTER, null, message, false);
            }
        });
    }

    //手机号码是否注册过
    public void IsPhoneReg(Context context, String phone, boolean showLoading) {
        showDialogs(context, showLoading);
        Core.instance(context).isPhoneRegister(phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Log.i(TAG, "SUCCESS");
                postResult(AccountEventMsg.EVENT_PHONE_REG, null, "", true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.i(TAG, "errorEvent=" + errorEvent + "--" + "message=" + message.toString());
                postResult(AccountEventMsg.EVENT_PHONE_REG, errorEvent, message, false);
            }
        });
    }

    //获取手机验证码
    public void getPhoneCode(final Context context, final String phone, boolean showLoading) {
        showDialogs(context, showLoading);
        Core.instance(context).getPhoneCode(phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                postResult(AccountEventMsg.EVENT_GET_CODE, data, null, true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                postResult(AccountEventMsg.EVENT_GET_CODE, errorEvent, message, false);
            }
        });
    }


    /**
     * 添加过电箱后，应把此值置为false
     *
     * @param firstUse
     * @param context
     */
    public static void setUserFirstUse(boolean firstUse, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppGlobalConfig.BASIC_CONFIG, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("first_use", firstUse).commit();
    }

    /**
     * 主要用于初始界面时是否进入添加电箱
     *
     * @param context
     * @return
     */
    public static boolean isFirstUse(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppGlobalConfig.BASIC_CONFIG, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("first_use", false);
    }

    /**
     * 修改密码
     *
     * @param context
     * @param newpwd
     * @param password
     * @param showLoading
     */
    public void modifyPwd(Context context, String newpwd, String password, boolean showLoading) {
        if (showLoading) {
            mWaitDialog = new LoadingDlg(context, -1);
            mWaitDialog.show();
        }
        Core.instance(context).updatePwd(newpwd, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {

                postResult1(AccountEventMsg.EVENT_MODIFY_PASSWORD, data, null, true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                postResult(AccountEventMsg.EVENT_MODIFY_PASSWORD, errorEvent, message, false);

            }
        });
    }

    /**
     * 忘记密码 重置密码
     *
     * @param context
     * @param phone
     * @param code
     * @param password
     * @param showLoading
     */
    public void onsetPwdAgain(Context context, String phone, String code, String password, boolean showLoading) {
        Core.instance(context).resetPwd(phone, code, password, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                postResult(AccountEventMsg.EVENT_GET_PWD_AGAIN, null, "", true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                postResult(AccountEventMsg.EVENT_GET_PWD_AGAIN, null, "", false);

            }
        });

    }

    private void postResult(int eventType, Object result, String errorMsg, boolean isSucess) {
        DeviceEventMsg eventMsg = new DeviceEventMsg(eventType);
        eventMsg.setEventMsg(result);
        eventMsg.setSucess(isSucess);
        eventMsg.setErrorMessage(errorMsg);
        dimissDialog();
        EventBus.getDefault().post(eventMsg);
    }

    private void postResult1(int eventType, Object result, String errorMsg, boolean isSucess) {
        DeviceEventMsg eventMsg = new DeviceEventMsg(eventType);
        eventMsg.setEventMsg(result);
        eventMsg.setSucess(isSucess);
        eventMsg.setErrorMessage(errorMsg);
        dimissDialog();
        EventBus.getDefault().postSticky(eventMsg);
    }

    private void showDialogs(Context context, boolean showLoading) {
        if (showLoading) {
            if (mWaitDialog != null) {
                mWaitDialog.dismiss();
            }
            mWaitDialog = new LoadingDlg(context, -1);
            mWaitDialog.show();
        }
    }

    private void dimissDialog() {
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }
}
