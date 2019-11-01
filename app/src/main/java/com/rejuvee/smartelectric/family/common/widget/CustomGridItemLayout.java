package com.rejuvee.smartelectric.family.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by liuchengran on 2018/1/5.
 * <p>
 * Grid item 宽高相等
 */
public class CustomGridItemLayout extends FrameLayout {

    public CustomGridItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomGridItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridItemLayout(Context context) {
        super(context);
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

//        int childWidthSize = getMeasuredWidth();
//        int childHeightSize = getMeasuredHeight();
        //宽高相等
//        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
