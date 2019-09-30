package com.rejuvee.smartelectric.family.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.LogoVersionManage;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;
import com.rejuvee.smartelectric.family.model.bean.WXAccessTokenRet;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by liuchengran on 2018/11/26.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private String TAG = "WXEntryActivity";

    private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
    private static final int RETURN_MSG_TYPE_SHARE = 2; //分享

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        MainApplication.mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i(TAG, "onResp:------>");
        Log.i(TAG, "error_code:---->" + baseResp.errCode);
        int type = baseResp.getType(); //类型：分享还是登录
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝授权
                CustomToast.showCustomErrorToast(this, getString(R.string.vs176));
                postWXLoginEvent(null);
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                String message = "";
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    message = getString(R.string.vs177);
                } else if (type == RETURN_MSG_TYPE_SHARE) {
                    message = getString(R.string.vs178);
                }
                postWXLoginEvent(null);
                CustomToast.showCustomErrorToast(this, message);
                break;
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                if (type == RETURN_MSG_TYPE_LOGIN) {
                    //用户换取access_token的code，仅在ErrCode为0时有效
                    String code = ((SendAuth.Resp) baseResp).code;
                    String state = ((SendAuth.Resp) baseResp).state;
                    Log.i(TAG, "code:------>" + code);
                    Log.i(TAG, "state:------>" + state);
                    if (state.equals(WXHelper.state_login)) {
                        postWXLoginEvent(code);
                    } else if (state.equals(WXHelper.state_bind)) {
                        postWXBindEvent(code);
                    }
                } else {
                    postWXLoginEvent(null);
                }
                break;
        }
        finish();

    }

    @Deprecated
    private void getWXLoginAccessToken(String code) {
       /* appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
        secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
        code	是	填写第一步获取的code参数
        grant_type	是	填authorization_code*/
        Core.instance(this).getWXAccessToken(LogoVersionManage.getInstance().getWXAppId(),
                LogoVersionManage.getInstance().getWXSecret(),
                code,
                "authorization_code",
                new ActionCallbackListener<WXAccessTokenRet>() {
                    @Override
                    public void onSuccess(WXAccessTokenRet data) {
                        if (data != null && data.access_token != null) {
//                            postWXLoginEvent(data.openid, data.access_token);
                        } else {
                            postWXLoginEvent(null);
                        }
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        postWXLoginEvent(null);
                    }
                }
        );
    }

   /* private void getWXUserInfo(String openId, String accessToken) {
        Log.d(TAG, " openId=" + openId + "  accessToken=" + accessToken);
        Core.instance(this).getWXUserInfo(openId, accessToken, new ActionCallbackListener<WXUserInfoRetBean>() {
            @Override
            public void onSuccess(WXUserInfoRetBean data) {
                if (data.nickname != null) {
                    data.isGetSucess = true;
                    postWXLoginEvent(data);
                } else {
                    postWXLoginEvent(null);
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                postWXLoginEvent(null);
            }
        });
    }*/

    private void postWXLoginEvent(String code) {
        ThirdPartyInfo thirdPartyInfo = new ThirdPartyInfo();
        thirdPartyInfo.isSucess = code != null;
        thirdPartyInfo.code = code;
        thirdPartyInfo.type = LogoVersionManage.getInstance().getVersionType();
//        thirdPartyInfo.openId = openid;
//        thirdPartyInfo.accessToken = accessToken;
        thirdPartyInfo.loginType = ThirdPartyInfo.LOGIN_WEIXIN;

        EventBus.getDefault().post(thirdPartyInfo);
        finish();
    }

    private void postWXBindEvent(String code) {
        ThirdPartyInfo thirdPartyInfo = new ThirdPartyInfo();
        thirdPartyInfo.isSucess = code != null;
        thirdPartyInfo.code = code;
        thirdPartyInfo.type = LogoVersionManage.getInstance().getVersionType();
//        thirdPartyLogin.openId = openid;
//        thirdPartyLogin.accessToken = accessToken;
        thirdPartyInfo.bindType = ThirdPartyInfo.BIND_WEIXIN;

        EventBus.getDefault().post(thirdPartyInfo);
        finish();
    }
}
