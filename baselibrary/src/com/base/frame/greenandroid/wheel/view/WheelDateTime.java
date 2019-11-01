package com.base.frame.greenandroid.wheel.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.base.frame.greenandroid.wheel.NumericWheelAdapter;
import com.base.frame.greenandroid.wheel.OnWheelChangedListener;
import com.base.frame.greenandroid.wheel.WheelView;
import com.base.frame.log.LogUtil;
import com.base.library.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 选择日期控件
 *
 * @author shaxiaoning
 */
public class WheelDateTime extends PopupWindow implements OnClickListener {
    private static final String LOGTAG = LogUtil.makeLogTag(WheelDateTime.class);
    private Context mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Button btnSubmit, btnCancel;
    private String age;
    private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter,
            hourAdapter, minuteAdapter;
    private WheelView year, month, day, hour, minute;
    private int mCurYear = 80, mCurMonth = 5, mCurDay = 14;
    private String[] dateType;

    private int lastYear, lastMonth, lastDay, lastHour, lastMinute;

    private boolean isHaveDayWheel;

    private OnWheelListener onWheelListener;

    // 可以设置时间选择器的title文案
    private TextView txtTitle;
    private String title = "", btnLeft = "", btnRight = "";

    /**
     * 初始化选择日期对话框
     *
     * @param mContext
     * @param onWheelListener
     * @param isHaveDayWheel1 是否显示 日
     * @param yearInt
     * @param monthInt
     * @param dayInt
     */
    public WheelDateTime(Context mContext, OnWheelListener onWheelListener,
                         boolean isHaveDayWheel1, String yearInt, String monthInt,
                         String dayInt) {
        super(mContext);
        isHaveDayWheel = isHaveDayWheel1;
        this.mContext = mContext;
        this.onWheelListener = onWheelListener;
        this.age = yearInt + "-" + monthInt + "-" + dayInt;
        this.title = "select date";
        initView();
    }

    public WheelDateTime(Context mContext, OnWheelListener onWheelListener,
                         boolean isHaveDayWheel1, String yearInt, String monthInt,
                         String dayInt, String title, String leftBtn, String rightBtn) {
        super(mContext);
        isHaveDayWheel = isHaveDayWheel1;
        this.mContext = mContext;
        this.onWheelListener = onWheelListener;
        this.age = yearInt + "-" + monthInt + "-" + dayInt;
        this.title = title;
        this.btnLeft = leftBtn;
        this.btnRight = rightBtn;
        initView();
    }

    public WheelDateTime(Context mContext, OnWheelListener onWheelListener,
                         boolean isHaveDayWheel1, String yearInt, String monthInt,
                         String dayInt, String HoursInt, String title, String leftBtn, String rightBtn) {
        super(mContext);
        isHaveDayWheel = isHaveDayWheel1;
        this.mContext = mContext;
        this.onWheelListener = onWheelListener;
        this.age = yearInt + "-" + monthInt + "-" + dayInt + "-" + HoursInt;
        this.title = title;
        this.btnLeft = leftBtn;
        this.btnRight = rightBtn;
        initView();
    }


    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_wheel_date_time, null);
        viewfipper = new ViewFlipper(mContext);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        txtTitle = mMenuView.findViewById(R.id.wheel_date_time_title);
        year = mMenuView.findViewById(R.id.select_wheel_year);
        month = mMenuView.findViewById(R.id.select_wheel_month);
        day = mMenuView.findViewById(R.id.select_wheel_day);
        hour = mMenuView.findViewById(R.id.select_wheel_hour);
        minute = mMenuView.findViewById(R.id.select_wheel_minute);

        if (!TextUtils.isEmpty(this.title)) {
            txtTitle.setText(this.title);
        }

        if (isHaveDayWheel)
            day.setVisibility(View.VISIBLE);
        else
            day.setVisibility(View.GONE);

        btnSubmit = mMenuView.findViewById(R.id.select_wheel_submit);
        btnCancel = mMenuView.findViewById(R.id.select_wheel_cancel);
        if (!TextUtils.isEmpty(this.btnLeft)) {
            btnCancel.setText(this.btnLeft);
        }
        if (!TextUtils.isEmpty(this.btnRight)) {
            btnSubmit.setText(this.btnRight);
        }
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day, hour, minute);
            }
        };
        int curYear = calendar.get(Calendar.YEAR);
        if (age != null && age.contains("-")) {
            String[] str = age.split("-");
            mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
            mCurMonth = Integer.parseInt(str[1]) - 1;
            mCurDay = Integer.parseInt(str[2]) - 1;

        }
        dateType = mContext.getResources().getStringArray(R.array.date_wheel);
        monthAdapter = new DateNumericAdapter(mContext, 1, 12, 5);
        monthAdapter.setTextType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year

        yearAdapter = new DateNumericAdapter(mContext, curYear - 20,
                curYear + 50, 0);
        mCurYear = 20;
        /*yearAdapter = new DateNumericAdapter(mContext, curYear, curYear + 50,
                100 - 20);*/
        yearAdapter.setTextType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);

        int curhour = calendar.get(Calendar.HOUR_OF_DAY);
        hourAdapter = new DateNumericAdapter(mContext, 0, 23, 23);
        hourAdapter.setTextType(dateType[3]);
        hour.setViewAdapter(hourAdapter);
        hour.setCurrentItem(curhour);
        hour.addChangingListener(listener);

        int curminute = calendar.get(Calendar.MINUTE);
        minuteAdapter = new DateNumericAdapter(mContext, 0, 59, 59);
        minuteAdapter.setTextType(dateType[4]);
        minute.setViewAdapter(minuteAdapter);
        minute.setCurrentItem(curminute);

        minute.addChangingListener(listener);

        // day

        updateDays(year, month, day, hour, minute);
        day.setCurrentItem(mCurDay);
        updateDays(year, month, day, hour, minute);
        day.addChangingListener(listener);

        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.update();
    }

    /**
     * @param parentView
     */
    public void show(View parentView) {
        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }


    public void setDateItemVisiable(boolean showYear, boolean showMonth, boolean showDay, boolean showHour, boolean showMin) {
        year.setVisibility(showYear ? View.VISIBLE : View.GONE);
        month.setVisibility(showMonth ? View.VISIBLE : View.GONE);
        day.setVisibility(showDay ? View.VISIBLE : View.GONE);
        hour.setVisibility(showHour ? View.VISIBLE : View.GONE);
        minute.setVisibility(showMin ? View.VISIBLE : View.GONE);
    }

    private void updateDays(WheelView year, WheelView month, WheelView day,
                            WheelView hour, WheelView minute) {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 20 + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter = new DateNumericAdapter(mContext, 1, maxDays,
                calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setTextType(dateType[2]);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        // int years = calendar.get(Calendar.YEAR) - 20;
        int years = calendar.get(Calendar.YEAR);

        lastYear = years;
        lastMonth = month.getCurrentItem() + 1;
        lastDay = day.getCurrentItem() + 1;
        lastHour = hour.getCurrentItem();
        lastMinute = minute.getCurrentItem();

        age = years + "-" + (month.getCurrentItem() + 1) + "-"
                + (day.getCurrentItem() + 1);
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue,
                                  int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(20);
            setTextColor(context.getResources().getColor(
                    R.color.wheel_text_color));

        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
            // view.setTextColor(color);
        }

        public CharSequence getItemText(int index) {
            currentItem = index;
            return super.getItemText(index);
        }

    }

    public void onClick(View v) {
        String month = "";
        String day = "";
        String hour = "";
        String minute = "";
        try {
            Date dates = new SimpleDateFormat("MM", Locale.getDefault()).parse(String
                    .valueOf(lastMonth));
            month = new SimpleDateFormat("MM", Locale.getDefault()).format(dates);
            Date days = new SimpleDateFormat("dd", Locale.getDefault()).parse(String
                    .valueOf(lastDay));
            day = new SimpleDateFormat("dd", Locale.getDefault()).format(days);
            Date hours = new SimpleDateFormat("HH", Locale.getDefault()).parse(String
                    .valueOf(lastHour));
            hour = new SimpleDateFormat("HH", Locale.getDefault()).format(hours);
            Date minutes = new SimpleDateFormat("mm", Locale.getDefault()).parse(String
                    .valueOf(lastMinute));
            minute = new SimpleDateFormat("mm", Locale.getDefault()).format(minutes);
        } catch (ParseException e) {
            // e.printStackTrace();
            month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
            day = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
            hour = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
            minute = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
        }
        int id = v.getId();
        if (id == R.id.select_wheel_submit) {
            onWheelListener.onWheel(true, "" + lastYear, month, "" + day, ""
                    + hour, "" + minute);
            this.dismiss();
        } else if (id == R.id.select_wheel_cancel) {
            // onWheelListener.onWheel(false, "" + lastYear, month, "" + day);
            this.dismiss();
        } else {
        }

    }

    public interface OnWheelListener {
        /**
         * 获取日期接口
         */
        void onWheel(Boolean isSubmit, String year, String month,
                     String day, String hour, String minute);

        // public void getSlectedDate(String date);
    }

}
