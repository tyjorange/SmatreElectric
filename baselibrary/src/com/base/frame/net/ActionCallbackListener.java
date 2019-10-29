package com.base.frame.net;

/**
 * Created by liuchengran on 2017/8/18.
 */

public interface ActionCallbackListener <T> {
    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    void onSuccess(T data);

    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    void onFailure(int errorEvent, String message);
}
