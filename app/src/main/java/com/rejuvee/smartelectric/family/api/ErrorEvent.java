package com.rejuvee.smartelectric.family.api;

import android.content.Context;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;

public class ErrorEvent {
    static final public int CODE_UNKNOW_ERROR = 1001;//未知错误
    static final public int PARAM_ILLEGAL = 1002; // 参数不合法
    static final public int USER_UNLOGIN = 1003;// 未登录
    
    static final public int CODE_CONNECT_TIMEOUT = 1003;//连接超时
    static final public int CODE_NET_UNCONNECTED = 1004;//网络未连接
    
    static final public int CODE_NO_DATA = 12;//没有数据
    
    
    public static String MSG_CONNECT_TIMEOUT = "连接超时";
    public static String MSG_NET_UNCONNECTED = "网络未连接";

    public static void ParseAndShowGetDataError(Context context, int errorEvent) {
        if (errorEvent == ErrorEvent.CODE_NO_DATA) {
            CustomToast.showCustomToast(context, context.getString(R.string.local_error_message_no_data));
        } else {
            CustomToast.showCustomErrorToast(context, context.getString(R.string.get_data_fail));
        }
    }
}
