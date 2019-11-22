package com.rejuvee.smartelectric.family.common.utils.thrid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.rejuvee.smartelectric.family.common.manager.LogoVersionManage;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;
import com.tencent.connect.UnionInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by liuchengran on 2018/11/29.
 */

public class QQLoginHelper {
    private static QQLoginHelper instance;
    private Tencent mTencent;
    private String authType = "0";
    private String authTypeLogin = "1";
    private String authTypeBind = "2";
    private static final String TAG = "QQLoginHelper";

    private QQLoginHelper() {

    }

    public static QQLoginHelper getInstance() {
        if (instance == null) {
            instance = new QQLoginHelper();
        }
        return instance;
    }

    /**
     * need ApplicationContext
     *
     * @param context
     */
    public void init(Context context) {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(LogoVersionManage.getInstance().getQQAppId(), context);
        }
    }

    public Tencent getTencent() {
        return mTencent;
    }

    public void qqLogin(final Activity activity) {
        authType = authTypeLogin;
        Log.d(TAG, "start qqLogin");
        if (!mTencent.isSessionValid()) {
            Log.d(TAG, "qqLogin Session isValid...");
            loginListener = new LoginListener(activity);
            mTencent.login(activity, "get_user_info", loginListener);
        } else {
            getUserInfo(activity);
        }
    }

    public void qqBind(final Activity activity) {
        authType = authTypeBind;
        Log.d(TAG, "start qqBind");
        if (mTencent.isSessionValid()) {
            Log.d(TAG, "logout...");
            mTencent.logout(activity);
        }
        loginListener = new LoginListener(activity);
        mTencent.login(activity, "get_user_info", loginListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

    private LoginListener loginListener = null;

    private class LoginListener implements IUiListener {
        private Activity activity;

        LoginListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onComplete(Object object) {
            Log.d(TAG, "onComplete");
            if (null == object) {
                Log.d(TAG, "登录失败 1");
                return;
            }
            JSONObject jsonResponse = (JSONObject) object;
            if (jsonResponse.length() == 0) {
                Log.d(TAG, "返回为空 登录失败 ");
                return;
            }
            initOpenidAndToken((JSONObject) object);
            getUserInfo(activity);
        }

        @Override
        public void onError(UiError uiError) {
            Log.d(TAG, "error" + uiError.errorMessage + "  detail= " + uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }
    }

    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Gson gson = new Gson();

    private void getUserInfo(final Activity activity) {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {
                    postLoginMessage(null, false);
                }

                @Override
                public void onComplete(final Object response) {
                    if (response != null) {
                        QQUserInfo qqUserInfo = gson.fromJson(response.toString(), QQUserInfo.class);
                        if (qqUserInfo != null) {
                            //Log.d(TAG, qqUserInfo.nickname);
                            thirdPartyInfo = new ThirdPartyInfo();
                            thirdPartyInfo.openId = mTencent.getOpenId();
                            thirdPartyInfo.nickName = qqUserInfo.nickname;
                            thirdPartyInfo.headImgUrl1 = qqUserInfo.figureurl_qq_1;
                            thirdPartyInfo.headImgUrl2 = qqUserInfo.figureurl_qq_2;
                            getUnionId(activity);
//                            postLoginMessage(thirdPartyInfo, true);
                        }
                    }
                }

                @Override
                public void onCancel() {
                    postLoginMessage(null, false);
                }
            };
            UserInfo mUserInfo = new UserInfo(activity, mTencent.getQQToken());
            mUserInfo.getUserInfo(listener);

        }
    }

    private ThirdPartyInfo thirdPartyInfo;

    private void getUnionId(Activity activity) {
        Tencent mTencent = QQLoginHelper.getInstance().getTencent();
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                    Log.e(TAG, "UiError " + e);
                }

                @Override
                public void onComplete(final Object response) {
                    if (response != null) {
                        JSONObject jsonObject = (JSONObject) response;
                        try {
                            thirdPartyInfo.unionid = jsonObject.getString("unionid");
//                            Util.showResultDialog(AutoLinkActivity.this, "unionid:\n" + unionid, "onComplete");
//                            Util.dismissDialog();
//                            Toast.makeText(activity, unionid);
                            postLoginMessage(thirdPartyInfo, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Exception no unionid " + e);
                        }
                    } else {
                        Log.e(TAG, "no unionid. response is null");
                    }
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "onCancel");
                }
            };
            UnionInfo unionInfo = new UnionInfo(activity, mTencent.getQQToken());
            unionInfo.getUnionId(listener);
        } else {
            Log.d(TAG, "please login QQ frist!");
        }
    }

    private void postLoginMessage(ThirdPartyInfo partyLogin, boolean isSucess) {
//        if (!isSucess) {
//            partyLogin = new ThirdPartyInfo();
//        }
//        partyLogin.loginType = ThirdPartyInfo.LOGIN_QQ;
        if (authType.equals(authTypeLogin)) {
            partyLogin.loginType = ThirdPartyInfo.LOGIN_QQ;
        } else if (authType.equals(authTypeBind)) {
            partyLogin.bindType = ThirdPartyInfo.BIND_QQ;
        }
        partyLogin.isSucess = isSucess;
        EventBus.getDefault().post(partyLogin);
    }


    public static class QQUserInfo {
        public int is_yellow_year_vip;
        public int ret;
        public String figureurl_qq_1;// "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
        public String figureurl_qq_2;// "http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
        public String nickname;//

        @Override
        public String toString() {
            return "QQUserInfo{" +
                    "is_yellow_year_vip=" + is_yellow_year_vip +
                    ", ret=" + ret +
                    ", figureurl_qq_1='" + figureurl_qq_1 + '\'' +
                    ", figureurl_qq_2='" + figureurl_qq_2 + '\'' +
                    ", nickname='" + nickname + '\'' +
                    '}';
        }
    }


}
