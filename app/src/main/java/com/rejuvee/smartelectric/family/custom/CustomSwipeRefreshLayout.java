package com.rejuvee.smartelectric.family.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Created by liuchengran on 2018/6/1.
 */

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private String TAG = "CustomSwipeRefreshLayout";
    private boolean isDisallowIntercept = false;
    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        isDisallowIntercept = b;
//        if (isDisallowIntercept) {
//            Log.d(TAG, "不要拦截我！");
//        } else {
//            Log.e(TAG, "你可以拦截我了");
//        }
        super.requestDisallowInterceptTouchEvent(b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isDisallowIntercept = false;
        }
        boolean isOnIterceptTouch = super.onInterceptTouchEvent(ev);
        isOnIterceptTouch = isOnIterceptTouch && !isDisallowIntercept;
//        if (isOnIterceptTouch) {
//            Log.e(TAG, "我拦截事件了。别怪我");
//        } else {
//            Log.d(TAG, "我没有拦截哦。别甩锅");
//        }
        return isOnIterceptTouch;
    }
}
