package com.rejuvee.smartelectric.family.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;

import com.base.library.utils.SizeUtils;
import com.rejuvee.smartelectric.family.R;

/**
 * Created by liuchengran on 2018/1/5.
 */
public class CustomGridViewWithSplitLine extends GridView {
    private String TAG = "CustomGridViewWithSplitLine";
    private boolean isScrollToTop = true;

    public CustomGridViewWithSplitLine(Context context) {
        super(context);
    }

    public CustomGridViewWithSplitLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.d(TAG, "scrollState=" + scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    View firstChild = getChildAt(0);
                    isScrollToTop = firstChild != null && firstChild.getTop() > -10 && firstChild.getTop() <= 10;
                } else {
                    isScrollToTop = false;
                }

            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
       /* Log.d(TAG, "getScrollX=" + getScrollX() + " isScrollTop=" + isScrollToTop);
        requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                if (!isScrollToTop) {
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                break;
        }
        boolean result = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatch=" + result);
        return result;*/
        requestDisallowInterceptTouchEvent(!isScrollToTop);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int childCount = getChildCount();
        if (childCount == 0)
            return;

        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();

        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(SizeUtils.dp2px(1));
        localPaint.setColor(getContext().getResources().getColor(R.color.white));
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);
            if ((i + 1) % column == 0) {//最后一列绘制底部线
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            } else if ((i + 1) > (childCount - (childCount % column))) {//最后一行，绘制右边竖直线
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
            } else {
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);//绘制右边竖直线
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);//绘制底部分割线
            }
        }
        if (childCount % column != 0) {
            for (int j = 0; j < (column - childCount % column); j++) {//绘制未填满格子的右边竖直线
                View lastView = getChildAt(childCount - 1);
                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j, lastView.getBottom(), localPaint);
            }

            View lastView = getChildAt(childCount - childCount % column);
            canvas.drawLine(lastView.getLeft(), lastView.getBottom(), getRight(), lastView.getBottom(), localPaint);//绘制最后一行底部分割线

        }
    }
}
