package com.rejuvee.smartelectric.family.common.custom;

import android.os.Handler;
import android.os.Message;

/**
 * 只响应最后一次操作的基类
 */
public abstract class LastMsgHandler extends Handler {

    //标记是第几次count
    private int count = 0;

    /**
     * 增加Count数。
     */
    private synchronized void increaseCount() {
        count++;
    }

    //直接发送消息
    public final void sendMsg() {
        sendMsgDelayed(0);
    }

    //增加count数后发送延时消息
    //如果延时小于或者等于0则直接发送。
    protected final void sendMsgDelayed(long delay) {
        increaseCount();
        if (delay <= 0) {
            sendEmptyMessage(0);
        } else {
            sendEmptyMessageDelayed(0, delay);
        }
    }

    //清空所有count和消息
    public synchronized final void clearAll() {
        count = 0;
        removeCallbacksAndMessages(null);
    }

    @Override
    public synchronized final void handleMessage(Message msg) {
        super.handleMessage(msg);
        count--;

        //确保count数不会异常
        if (count < 0) {
            throw new IllegalStateException("count数异常");
        }

        //当count为0时说明是最后一次请求
        if (count == 0) {
            handleLastMessage(msg);
        }
    }

    //响应最后一次请求
    protected abstract void handleLastMessage(Message msg);
}