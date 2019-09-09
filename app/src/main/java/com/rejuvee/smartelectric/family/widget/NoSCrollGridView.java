package com.rejuvee.smartelectric.family.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 滚动布局
 * Created by Administrator on 2017/12/15.
 */
public class NoSCrollGridView extends GridView {


    public NoSCrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSCrollGridView(Context context) {
        super(context);
    }

    public NoSCrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
