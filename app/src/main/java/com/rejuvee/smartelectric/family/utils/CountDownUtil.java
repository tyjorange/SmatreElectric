package com.rejuvee.smartelectric.family.utils;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 秒数倒计时
 */
public class CountDownUtil {
    private String TAG = "CountDownUtil";
    private int secondsCount = 0;
    private Timer mTimer;
    private ITimeUpListener mListener;

    private boolean isStart = false;
    private Handler mHandler;
    public CountDownUtil(int seconds, ITimeUpListener listener) {
        secondsCount = seconds;
        mTimer = new Timer();
        mHandler = new Handler();
        mListener = listener;
    }

    //开始计时
    public void start() {
        if (isStart) {
            return;
        }
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            private int seconds = secondsCount;
            @Override
            public void run() {
                seconds--;
                if (mListener != null) {
                    mHandler.post(() -> mListener.onTimeUp(seconds));

                }

                if (seconds == 0) {
                    stop();
                }

            }
        }, 0, 1000);

        isStart = true;
    }

    private void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        isStart = false;
    }

    public interface ITimeUpListener {
        void onTimeUp(int seconds);
    }
}
