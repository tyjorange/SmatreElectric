package com.rejuvee.smartelectric.family.custom;

/**
 * Created by liuchengran on 2018/10/31.
 */

public class AccountEventMsg extends BaseEventMsg{
    public static final int EVENT_LOGIN = 0;//登录
    public static final int EVENT_REGISTER = 1;//注册
    public static final int EVENT_MODIFY_PASSWORD = 2;//修改密码
    public static final int EVENT_PHONE_REG = 3;//手机号码是否注册
    public static final int EVENT_GET_CODE = 4;

    public static final int EVENT_GET_PWD_AGAIN = 5; //忘记密码

    public AccountEventMsg(int eventType) {
        this.eventType = eventType;
    }


}
