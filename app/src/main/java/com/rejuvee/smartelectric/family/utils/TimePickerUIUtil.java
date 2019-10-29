package com.rejuvee.smartelectric.family.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.core.content.ContextCompat;

import com.rejuvee.smartelectric.family.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TimePickerUIUtil {

    private static final String TAG = TimePickerUIUtil.class.getSimpleName();

    public static void setNumberPickerTextSize(ViewGroup viewGroup) {
        List<NumberPicker> npList = findNumberPicker(viewGroup);
        if (null != npList) {
            for (NumberPicker np : npList) {
                EditText et = findEditText(np);
                et.setFocusable(false);
                et.setGravity(Gravity.CENTER);
                et.setTextSize(30);
            }
        }
    }

    public static void setTimepickerTextColour(TimePicker time_picker, Context context) {
        Resources system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = time_picker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = time_picker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = time_picker.findViewById(ampm_numberpicker_id);

        setNumberpickerTextColour(hour_numberpicker, context);
        setNumberpickerTextColour(minute_numberpicker, context);
        setNumberpickerTextColour(ampm_numberpicker, context);

        setNumberPickerDividerColor(hour_numberpicker, Color.rgb(255, 255, 255));
        setNumberPickerDividerColor(minute_numberpicker, Color.rgb(255, 255, 255));
        setNumberPickerDividerColor(ampm_numberpicker, Color.rgb(255, 255, 255));

        setPickerSize(hour_numberpicker, 70, context);
        setPickerSize(minute_numberpicker, 70, context);
        setPickerSize(ampm_numberpicker, 69, context);
    }

    private static void setNumberpickerTextColour(NumberPicker number_picker, Context context) {
        final int count = number_picker.getChildCount();
        //这里就是要设置的颜色，修改一下作为参数传入会更好
        final int color = ContextCompat.getColor(context, R.color.black);

        for (int i = 0; i < count; i++) {
            View child = number_picker.getChildAt(i);

            try {
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);
                ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText) child).setTextColor(color);
                number_picker.invalidate();
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                Log.i(TAG, "setNumberpickerTextColour: " + e);
            }
        }
    }

    private static void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field SelectionDividerField : pickerFields) {
            if (SelectionDividerField.getName().equals("mSelectionDivider")) {
                SelectionDividerField.setAccessible(true);
                try {
                    SelectionDividerField.set(numberPicker, new ColorDrawable(color));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    //此方法将dp值转换为px值，以保证适配不同分辨率机型

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    //这个方法是改变NumberPicker大小的方法，传入的参数为要修                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  改的NumberPicker和NumberPicker的宽度值

    private static void setPickerSize(NumberPicker np, int widthDpValue, Context context) {
        int widthPxValue = dp2px(context, widthDpValue);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPxValue, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);//这儿参数可根据需要进行更改
        np.setLayoutParams(params);
    }

    private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;

        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }

        return npList;
    }

    private static EditText findEditText(NumberPicker np) {
        if (null != np) {
            for (int i = 0; i < np.getChildCount(); i++) {
                View child = np.getChildAt(i);

                if (child instanceof EditText) {
                    return (EditText) child;
                }
            }
        }

        return null;
    }
}
