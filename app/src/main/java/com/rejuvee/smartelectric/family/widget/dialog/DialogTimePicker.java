package com.rejuvee.smartelectric.family.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.rejuvee.smartelectric.family.R;

import java.lang.reflect.Field;

/**
 * Created by liuchengran on 2018/3/23.
 * <p>
 * 整点时间选择器
 * <p>
 * 可加工变为通用时间选择器
 */
public class DialogTimePicker implements View.OnClickListener {
    private Dialog mDialog;
    private NumberPicker mPickerHour, mPickMinute;
    private TextView txtOk, txtCancel;
    private TimeSelectedListen mListener;
    private Context mContext;
    private int mType = 0;
    private int currentHour, currentMiniute;
//    private Handler mHandler = new Handler();

    public interface TimeSelectedListen {
        void onTimeSelected(int hour, int miniute, int type);
    }

    public DialogTimePicker(@NonNull Context context) {
        mDialog = new Dialog(context);
        mContext = context;
        init(context);
    }

    public DialogTimePicker setListener(TimeSelectedListen listener) {
        mListener = listener;
        return this;
    }

    public DialogTimePicker setType(int type) {
        mType = type;
        return this;
    }

    public DialogTimePicker setCurrentHour(int hour) {
        currentHour = hour;
        mPickerHour.setValue(hour);
        return this;
    }

    public DialogTimePicker setCurrentMiniute(int miniute) {
        currentMiniute = miniute;
        mPickMinute.setValue(miniute);
        return this;
    }

    public void show() {
        mDialog.show();

    }

    private void init(Context context) {
        View contentView = View.inflate(context, R.layout.dialog_time_picker, null);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);// 设置背景透明
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (d.widthPixels);
        dialogWindow.setAttributes(lp);
        txtCancel = contentView.findViewById(R.id.txt_cancel);
        txtOk = contentView.findViewById(R.id.txt_ok);

        txtOk.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        mPickerHour = contentView.findViewById(R.id.hour);
        mPickMinute = contentView.findViewById(R.id.minute);
        mPickMinute.setOnLongPressUpdateInterval(100);
        mPickMinute.setFormatter(formater);
        mPickMinute.setMinValue(0);
        mPickMinute.setMaxValue(59);
        mPickMinute.setEnabled(false);

        mPickerHour.setOnLongPressUpdateInterval(100);
        mPickerHour.setFormatter(formater);
        mPickerHour.setMinValue(0);
        mPickerHour.setMaxValue(24);
        mPickerHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                currentHour = newVal;
            }
        });

        setNumberPickerDividerColor(mPickerHour);
        setNumberPickerDividerColor(mPickMinute);
    }

    private NumberPickerFormater formater = new NumberPickerFormater();

    private class NumberPickerFormater implements NumberPicker.Formatter {

        @Override
        public String format(int value) {
            return String.format("%02d", value);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txt_cancel) {
            mDialog.dismiss();
        } else if (id == R.id.txt_ok) {
            mDialog.dismiss();
            if (mListener != null) {
                mListener.onTimeSelected(currentHour, currentMiniute, mType);
            }
        }
    }

    /**
     * 自定义滚动框分隔线颜色
     */
    private void setNumberPickerDividerColor(NumberPicker number) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(number, new ColorDrawable(ContextCompat.getColor(mContext, R.color.def_color)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
