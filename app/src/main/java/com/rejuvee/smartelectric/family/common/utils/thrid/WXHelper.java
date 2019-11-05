package com.rejuvee.smartelectric.family.common.utils.thrid;

import android.content.Context;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.R;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * Created by liuchengran on 2018/11/26.
 */

public class WXHelper {
    public static final String state_login = "smarte_wx_login";
    public static final String state_bind = "smarte_wx_bind";

    /**
     * @param context
     */
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

    /**
     * @param context
     */
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

    /**
     *
     * @param context
     */
    public static void toMiniProgram(Context context) {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (MainApplication.mWxApi == null || !MainApplication.mWxApi.isWXAppInstalled()) {
            CustomToast.showCustomErrorToast(context, context.getString(R.string.without_weixin_client));
            return;
        }
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_f2d280fc7239"; // 填小程序原始id
        // 拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//        req.path = path;
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
        MainApplication.mWxApi.sendReq(req);
    }

    /**
     *
     * @param context
     */
    public static void toGZH(Context context) {
        //先判断是否安装微信APP,按照微信的说法，目前移动应用上微信登录只提供原生的登录方式，需要用户安装微信客户端才能配合使用。
        if (MainApplication.mWxApi == null || !MainApplication.mWxApi.isWXAppInstalled()) {
            CustomToast.showCustomErrorToast(context, context.getString(R.string.without_weixin_client));
            return;
        }
        SubscribeMessage.Req req = new SubscribeMessage.Req();
        req.scene = 1069;
        //O4Nf1nVFV6oQ2OJ3ozFSeVJEp3-siSuRfKrknk5beIU
        req.templateID = "XghSAERrupizDfHj7gKXYQT1KIf-fm7NSs6WY-hbTtY";
        req.reserved = "";
        MainApplication.mWxApi.sendReq(req);
    }

}
