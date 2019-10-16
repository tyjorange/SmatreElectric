package com.rejuvee.smartelectric.family.fragment;

import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.greenandroid.wheel.view.WheelDateTime;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.custom.AmountViewInt;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 电流类设置
 */
public class SettingDL1Fragment extends BaseFragment implements View.OnFocusChangeListener {
    private View view;
    private final String[] listVal = {"1", "6", "10", "16", "20", "25", "32", "40", "50", "63"};//要填充的数据
    private ListPopupWindow listPopupWindow;
    private EditText et_GL1;
    private EditText et_GL2;
    private ImageView iv_bhsn;
    private ImageView iv_zjsn;
    private TextView tv_zj_time;
    private RangeSeekBar rangeSeekBarLDL;
    private AmountViewInt amountLDL;
    private int bhsn = 0;//保护使能
    private int zjsn = 0;//自检使能
    private int dd = 0;
    private int hh = 0;

    private WheelDateTime dateSelector;
    //    private Handler mHandler;
    private boolean isShowing = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting_dl1;
    }

    @Override
    protected void initView(View v) {
        view = v;
        // 过流1 过流2
        listPopupWindow = new ListPopupWindow(v.getContext());
        et_GL1 = v.findViewById(R.id.et_sp1);
        et_GL1.setOnFocusChangeListener(this);
        et_GL2 = v.findViewById(R.id.et_sp2);
        // et_GL2.setOnFocusChangeListener(this);
        // 漏电保护、自检使能
        iv_bhsn = v.findViewById(R.id.iv_bhsn);
        iv_bhsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bhsn == 0) {
                    bhsn = 1;
                } else {
                    bhsn = 0;
                }
                updateSwitchStatus(v);
            }
        });
        iv_zjsn = v.findViewById(R.id.iv_zjsn);
        iv_zjsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zjsn == 0) {
                    zjsn = 1;
                } else {
                    zjsn = 0;
                }
                updateSwitchStatus(v);
            }
        });
        // 漏电自检时间
        tv_zj_time = v.findViewById(R.id.tv_zj_time);
        v.findViewById(R.id.ll_zj_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelector.show(v);
            }
        });
        // 漏电流阀值
//        v.findViewById(R.id.ll_ldl).setVisibility(View.VISIBLE);
        amountLDL = v.findViewById(R.id.amount_view_ldl);
        amountLDL.setVal_min(0);
        amountLDL.setVal_max(999);
        amountLDL.setAmountInt(0);
        amountLDL.setOnAmountChangeListener(new AmountViewInt.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarLDL.setProgress((int) amount);
            }
        });
        rangeSeekBarLDL = v.findViewById(R.id.seek_bar_ldl);
        rangeSeekBarLDL.setRange(0, 999);//范围
        rangeSeekBarLDL.setTickMarkTextArray(new String[]{"0", "999"});//刻度
        rangeSeekBarLDL.setIndicatorTextDecimalFormat("###");//格式化小数位数
        rangeSeekBarLDL.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
                amountLDL.setAmountInt((int) v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        // 日期选择器
        dateSelector = new WheelDateTime(v.getContext(),
                new WheelDateTime.OnWheelListener() {

                    @Override
                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
//                        iyear = Integer.parseInt(year);
//                        imonth = Integer.parseInt(month);
//                        if (isDay) {
//                            iday = Integer.parseInt(day);
//                        }
//                        changeTvDate();
                        dd = Integer.parseInt(day);
                        hh = Integer.parseInt(hour);
                        tv_zj_time.setText(String.format(Locale.getDefault(), getString(R.string.vs190), dd, hh));
                    }
                }, true,
                new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()),
                getString(R.string.vs191), getString(R.string.cancel), getString(R.string.ensure));
        dateSelector.setDateItemVisiable(false, false, true, true, false);

    }

    /**
     * 00 01 10 11
     * BIT0=漏电自检使能 BIT1=漏电保护使能
     */
    private void updateSwitchStatus(View v) {
        iv_bhsn.setImageDrawable(v.getResources().getDrawable(bhsn == 0 ? R.drawable.icon_switch_off : R.drawable.icon_switch_on));
        iv_zjsn.setImageDrawable(v.getResources().getDrawable(zjsn == 0 ? R.drawable.icon_switch_off : R.drawable.icon_switch_on));
    }

    @Override
    protected void initData() {
        isShowing = true;
        listener.onDL1Show();// 第一个TAB
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showListPopulWindow(v); //调用显示PopuWindow 函数
        }
    }

    /**
     * 预设值下拉选
     */
    private void showListPopulWindow(View v) {
        listPopupWindow.setAdapter(new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1, listVal));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(et_GL1);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_GL1.setText(listVal[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }


    public String getParamID(SwitchBean currentSwitchBean) {
        // 判断漏电类是否显示
        if (currentSwitchBean.getModelMajor() == 1 && currentSwitchBean.getModelMinor() == 5) {
            view.findViewById(R.id.ll_ldl).setVisibility(View.VISIBLE);
            return "0000001C," + // 漏电自检/保护使能
                    "0000001D," + // 漏电自检时间 xxxx(ddhh) 0000
                    "0000001B," + // 漏电阀值
                    "00000011," + // 过流阀值(1)
                    "0000001A,"; // 瞬时过流阀值(2)
        } else {
            view.findViewById(R.id.ll_ldl).setVisibility(View.GONE);
            return "00000011," + // 过流阀值(1)
                    "0000001A,"; // 瞬时过流阀值(2)
        }
    }

    /**
     * 设置过流1
     *
     * @param paramValue
     */
    public void setGL1(float paramValue) {
        BigDecimal s1 = BigDecimal.valueOf(paramValue).setScale(0, BigDecimal.ROUND_HALF_UP);
        et_GL1.setText(String.format(Locale.getDefault(), "%.0f", s1));
    }

    /**
     * 设置过流2 （瞬时）
     *
     * @param paramValue
     */
    public void setGL2(float paramValue) {
        BigDecimal s2 = BigDecimal.valueOf(paramValue).setScale(0, BigDecimal.ROUND_HALF_UP);
        et_GL2.setText(String.format(Locale.getDefault(), "%.0f", s2));
    }

    /**
     * 设置漏电自检/保护使能
     *
     * @param paramValue
     */
    public void setBHSN(float paramValue) {
        String s = Integer.toBinaryString((int) paramValue);
        switch (s) {
            case "0":
                bhsn = 0;
                zjsn = 0;
                break;
            case "1":
                bhsn = 0;
                zjsn = 1;
                break;
            case "10":
                bhsn = 1;
                zjsn = 0;
                break;
            case "11":
                bhsn = 1;
                zjsn = 1;
                break;
        }
        updateSwitchStatus(view);
    }

    /**
     * 设置漏电自检时间
     *
     * @param paramValue
     */
    public void setzjtime(float paramValue) {
//        String format = String.format("%04x", (int) paramValue);
        dd = ((int) paramValue) / 256;
        hh = ((int) paramValue) % 256;
        tv_zj_time.setText(String.format(Locale.getDefault(), getString(R.string.vs190), dd, hh));
    }

    /**
     * 设置漏电阀值
     *
     * @param paramValue
     */
    public void setVal(float paramValue) {
        rangeSeekBarLDL.setProgress(paramValue);
        amountLDL.setAmountInt((int) paramValue);
    }

    /**
     * @return
     */
    public String getValString() {
        String res = "";
        String glfz = et_GL1.getEditableText().toString();
        String ssglfz = et_GL2.getEditableText().toString();
        res += "00000011:" + glfz; // 过流阀值(1)
        res += ",0000001A:" + ssglfz;// 瞬时过流阀值(2)
        if (view.findViewById(R.id.ll_ldl).getVisibility() == View.VISIBLE) {
            res += ",0000001C:" + Integer.valueOf(bhsn + "" + zjsn, 2); // 漏电自检/保护使能
            res += ",0000001D:" + (dd * 256 + hh); // 漏电自检时间
            rangeSeekBarLDL.setProgress(amountLDL.getAmountInt());
            BigDecimal ldfz = BigDecimal.valueOf(rangeSeekBarLDL.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
            res += ",0000001B:" + ldfz.intValue(); // 漏电阀值
        }
        return res;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isShowing) {
//            Log.e("VpAdapter", "setUserVisibleHint: " + position);
            listener.onDL1Show();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private OnShowingListener listener;

    public SettingDL1Fragment setOnShowingListener(OnShowingListener onShowingListener) {
        listener = onShowingListener;
        return this;
    }

    public interface OnShowingListener {
        void onDL1Show();
    }
}
