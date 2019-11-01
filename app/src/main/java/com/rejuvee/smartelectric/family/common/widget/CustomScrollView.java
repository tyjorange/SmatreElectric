package com.rejuvee.smartelectric.family.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by liuchengran on 2017/12/19.
 */

public class CustomScrollView extends ScrollView {
    private int touchSlop = 0;
    private GestureDetector mGestureDetector;

    @SuppressWarnings("deprecation")
    public CustomScrollView(Context context, AttributeSet attrs) {

        super(context,attrs);

        mGestureDetector= new GestureDetector(new YScrollDetector());

        setFadingEdgeLength(0);
        touchSlop = ViewConfiguration.getTouchSlop();

    }

    //通过手势判断，来判断是否拦截触摸事件。
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) || mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > touchSlop && Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}
