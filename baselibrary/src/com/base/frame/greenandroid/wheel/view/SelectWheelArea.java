package com.base.frame.greenandroid.wheel.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.base.frame.greenandroid.wheel.AbstractWheelTextAdapter;
import com.base.frame.greenandroid.wheel.OnWheelChangedListener;
import com.base.frame.greenandroid.wheel.WheelView;
import com.base.frame.greenandroid.wheel.WheelViewAdapter;
import com.base.frame.log.LogUtil;
import com.base.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchengran on 2017/6/8.
 */

public class SelectWheelArea extends PopupWindow implements View.OnClickListener {
    private static final String LOGTAG = LogUtil
            .makeLogTag(SelectWheelArea.class);
    private Context mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Button btnSubmit, btnCancel;
    private String age;
    private WheelView province, city, county;
    private String[] dateType;

    private OnWheelListener onWheelListener;
    private List<String> listProvince, listCity, listCounty;

    private WheelViewAdapter provinceAdapter, cityAdapter, countryAdapter;
    private int curProvince = 0, curCity = 0, curCounty = 0;
    private List<GovernmentArea> listArea;
    /**
     * 初始化选择区域对话框
     *
     * @param mContext
     * @param onWheelListener
     * @param listArea
     */
    public SelectWheelArea(Context mContext,
                           OnWheelListener onWheelListener,
                           List<GovernmentArea> listArea) {
        super(mContext);
        this.mContext = mContext;
        this.listArea = listArea;

        listProvince = new ArrayList<>();
        for (GovernmentArea area : listArea) {
            listProvince.add(area.getProvinceName());
        }

        this.onWheelListener = onWheelListener;

        initData(mContext);
        initView();

    }

    private void initData(Context context ) {
        provinceAdapter = new DateAreaAdapter(context, listProvince);
        listCity = listArea.get(curProvince).getListCityName();
        cityAdapter = new DateAreaAdapter(context, listCity);

        listCounty = listArea.get(curProvince).getListCity().get(curCity).getListCounty();
        countryAdapter = new DateAreaAdapter(context, listCounty);

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_wheel_area, null);
        viewfipper = new ViewFlipper(mContext);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        province = (WheelView) mMenuView.findViewById(R.id.select_wheel_province);
        city = (WheelView) mMenuView.findViewById(R.id.select_wheel_city);
        county = (WheelView) mMenuView.findViewById(R.id.select_wheel_county);


        btnSubmit = (Button) mMenuView.findViewById(R.id.select_wheel_submit);
        btnCancel = (Button) mMenuView.findViewById(R.id.select_wheel_cancel);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                Log.d(LOGTAG, "old=" + oldValue + " new=" + newValue);
                if (wheel == province) {
                    curCity = 0;
                    curCounty = 0;
                    curProvince = newValue;
                    initData(mContext);
                } else if (wheel == city) {
                    curCity = newValue;
                    curCounty = 0;
                    listCity = listArea.get(curProvince).getListCityName();
                    listCounty = listArea.get(curProvince).getListCity().get(curCity).getListCounty();
                    countryAdapter = new DateAreaAdapter(mContext, listCounty);
                    county.setViewAdapter(countryAdapter);
                    county.setCurrentItem(0);
                } else if (wheel == county) {
                    curCounty = newValue;
                }
            }
        };

        //province
        province.setViewAdapter(provinceAdapter);
        province.setCurrentItem(curProvince);
        province.addChangingListener(listener);
        //city
        city.setViewAdapter(cityAdapter);
        city.setCurrentItem(curCity);
        city.addChangingListener(listener);
        // county
        county.setViewAdapter(countryAdapter);
        county.setCurrentItem(curCounty);
        county.addChangingListener(listener);


        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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

    /**
     * Adapter for area wheels. Highlights the current value.
     */
    private class DateAreaAdapter extends AbstractWheelTextAdapter {
        List<String> listData;

        /**
         * Constructor
         */
        public DateAreaAdapter(Context context, List<String> listItems) {
            super(context);
            listData = listItems;
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
            return listData.get(index);
        }

        @Override
        public int getItemsCount() {
            return listData.size();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.select_wheel_submit) {
            onWheelListener.onWheel(listProvince.get(curProvince) , listCity.get(curCity), listCounty.get(curCounty));
            dismiss();
        } else if (v.getId() == R.id.select_wheel_cancel) {
            dismiss();
        }
    }

    public interface OnWheelListener {
        /**
         * 获取接口
         *
         */
        public void onWheel(String provinceName, String cityName, String countyName);

        // public void getSlectedDate(String date);
    }
}
