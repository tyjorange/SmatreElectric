package com.rejuvee.smartelectric.family.fragment;

import android.view.View;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.custom.AmountView;

import java.math.BigDecimal;

/**
 * 电量类设置
 */
public class SettingDYFragment extends BaseFragment {
    private RangeSeekBar rangeSeekBarGY;
    private AmountView amountGY;
    private RangeSeekBar rangeSeekBarQY;
    private AmountView amountQY;
    private boolean isShowing = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting_dy;
    }

    @Override
    protected void initView(View v) {
        // 过压
        amountGY = v.findViewById(R.id.amount_view_gy);
        amountGY.setVal_min(100);
        amountGY.setVal_max(480);
        amountGY.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarGY.setProgress(amount);
            }
        });
        rangeSeekBarGY = v.findViewById(R.id.seek_bar_gy);
        rangeSeekBarGY.setRange(100, 480);//范围
        rangeSeekBarGY.setTickMarkTextArray(new String[]{"100", "480"});//刻度
        rangeSeekBarGY.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
        rangeSeekBarGY.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                amountGY.setAmount(v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        // 欠压
        amountQY = v.findViewById(R.id.amount_view_qy);
        amountQY.setVal_min(50);
        amountQY.setVal_max(320);
        amountQY.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarQY.setProgress(amount);
            }
        });
        rangeSeekBarQY = v.findViewById(R.id.seek_bar_qy);
        rangeSeekBarQY.setRange(50, 320);//范围
        rangeSeekBarQY.setTickMarkTextArray(new String[]{"50", "320"});//刻度
        rangeSeekBarQY.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
        rangeSeekBarQY.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                amountQY.setAmount(v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }

    @Override
    protected void initData() {
        isShowing = true;
    }

    public String getParamID() {
        return "00000005," + // 过压阀值
                "0000000D,"; // 欠压阀值
    }

    /**
     * 设置过压阀值
     *
     * @param paramValue
     */
    public void setGY(float paramValue) {
        rangeSeekBarGY.setProgress(paramValue);
        amountGY.setAmount(paramValue);
    }

    /**
     * 设置欠压阀值
     *
     * @param paramValue
     */
    public void setQY(float paramValue) {
        rangeSeekBarQY.setProgress(paramValue);
        amountQY.setAmount(paramValue);
    }

    public String getValString() {
        String res = "";
        rangeSeekBarGY.setProgress(amountGY.getAmount());
        BigDecimal gyfz = BigDecimal.valueOf(rangeSeekBarGY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
        rangeSeekBarQY.setProgress(amountQY.getAmount());
        BigDecimal qyfz = BigDecimal.valueOf(rangeSeekBarQY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
        res += ",00000005:" + gyfz + // 过压阀值
                ",0000000D:" + qyfz; // 欠压阀值
        return res;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isShowing) {
//            Log.e("VpAdapter", "setUserVisibleHint: " + position);
            listener.onDYShow();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private OnShowingListener listener;

    public SettingDYFragment setOnShowingListener(OnShowingListener onShowingListener) {
        listener = onShowingListener;
        return this;
    }

    public interface OnShowingListener {
        void onDYShow();
    }
}
