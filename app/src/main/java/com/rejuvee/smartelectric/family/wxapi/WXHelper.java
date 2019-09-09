package com.rejuvee.smartelectric.family.wxapi;

import android.content.Context;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * Created by liuchengran on 2018/11/26.
 */

public class WXHelper {
    public static final String state_login = "smart_e_wx_login";
    public static final String state_bind = "smart_e_wx_bind";

    public static void startWxLogin(Context context) {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (MainApplication.mWxApi == null || !MainApplication.mWxApi.isWXAppInstalled()) {
            CustomToast.showCustomErrorToast(context, context.getString(R.string.without_weixin_client));
            return;
        }
        //微信登录
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state_login;
        //向微信发送请求
        MainApplication.mWxApi.sendReq(req);
    }

    public static void startWxBind(Context context) {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (MainApplication.mWxApi == null || !MainApplication.mWxApi.isWXAppInstalled()) {
            CustomToast.showCustomErrorToast(context, context.getString(R.string.without_weixin_client));
            return;
        }
        //微信绑定
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state_bind;
        //向微信发送请求
        MainApplication.mWxApi.sendReq(req);
    }

}
