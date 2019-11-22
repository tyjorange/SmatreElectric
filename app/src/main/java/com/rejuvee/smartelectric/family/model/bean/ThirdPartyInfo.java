package com.rejuvee.smartelectric.family.model.bean;

/**
 * QQ WX
 * Created by liuchengran on 2018/12/3.
 */
public class ThirdPartyInfo {
    public static final int LOGIN_WEIXIN = 0;
    public static final int LOGIN_QQ = 1;
    public int loginType;
    public static final int BIND_WEIXIN = 2;
    public static final int BIND_QQ = 3;
    public int bindType;

    public String code;
    public String type;

    public String openId;
    public String nickName;
    public String headImgUrl1;
    public String headImgUrl2;
    public String unionid;

    public boolean isSucess;

    @Override
    public String toString() {
        return "ThirdPartyInfo{" +
                "loginType=" + loginType +
                ", bindType=" + bindType +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                ", openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl1='" + headImgUrl1 + '\'' +
                ", headImgUrl2='" + headImgUrl2 + '\'' +
                ", unionid='" + unionid + '\'' +
                ", isSucess=" + isSucess +
                '}';
    }
}
