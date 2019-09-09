package com.rejuvee.smartelectric.family.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.rejuvee.smartelectric.family.R;


/**
 * 查看密码 组件
 * Created by SH on 2017/12/19.
 */
public class CheckableImageView extends android.support.v7.widget.AppCompatImageView implements Checkable {

    private boolean check;
    private OnCheckChangeListener listener;
    private int checkedDrawable;
    private int uncheckDrawable;

    public CheckableImageView(Context context) {
        super(context);
        init();
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        check = false;
        checkedDrawable = R.drawable.eye;
        uncheckDrawable = R.drawable.eye_close;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked == check) {
            return;
        }
        check = checked;

        if (listener != null) {
            listener.onCheckChanged(checked);
        }

        setImageResource(checked ? checkedDrawable : uncheckDrawable);
    }

    @Override
    public boolean isChecked() {
        return check;
    }

    @Override
    public void toggle() {
        setChecked(!check);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckChangeListener {
        void onCheckChanged(boolean checked);
    }
}
