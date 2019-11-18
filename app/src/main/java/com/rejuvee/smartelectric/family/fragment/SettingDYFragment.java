package com.rejuvee.smartelectric.family.fragment;

import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.common.custom.AmountView;
import com.rejuvee.smartelectric.family.databinding.FragmentSettingDyBinding;
import com.rejuvee.smartelectric.family.model.bean.PP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 电压类设置
 */
public class SettingDYFragment extends BaseFragment {
    private RangeSeekBar rangeSeekBarGY;
    private AmountView amountGY;
    private RangeSeekBar rangeSeekBarQY;
    private AmountView amountQY;
//    private boolean isShowing = false;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_setting_dy;
//    }

    @Override
    protected View initView() {
        FragmentSettingDyBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_setting_dy, null, false);
        // 过压
        amountGY = mBinding.amountViewGy;//v.findViewById(R.id.amount_view_gy);
        amountGY.setVal_min(100);
        amountGY.setVal_max(480);
//        amountQY.setAmount(275);
        amountGY.setOnAmountChangeListener((view, amount) -> rangeSeekBarGY.setProgress(amount));
        rangeSeekBarGY = mBinding.seekBarGy;//v.findViewById(R.id.seek_bar_gy);
        rangeSeekBarGY.setRange(100, 480);//范围
        rangeSeekBarGY.setTickMarkTextArray(new String[]{"100", "480"});//刻度
//        rangeSeekBarQY.setProgress(275);
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
        amountQY = mBinding.amountViewQy;//v.findViewById(R.id.amount_view_qy);
        amountQY.setVal_min(50);
        amountQY.setVal_max(320);
//        amountQY.setAmount(160);
        amountQY.setOnAmountChangeListener((view, amount) -> rangeSeekBarQY.setProgress(amount));
        rangeSeekBarQY = mBinding.seekBarQy;//v.findViewById(R.id.seek_bar_qy);
        rangeSeekBarQY.setRange(50, 320);//范围
        rangeSeekBarQY.setTickMarkTextArray(new String[]{"50", "320"});//刻度
//        rangeSeekBarQY.setProgress(160);
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
        return mBinding.getRoot();
    }

    public List<String> getParamID() {
        List<String> list = new ArrayList<>();
//        return "00000005," + // 过压阀值
//                "0000000D,"; // 欠压阀值
        list.add("00000005");
        list.add("0000000D");
        return list;
    }

    /**
     * 设置过压阀值
     *
     * @param paramValue
     */
    public void setGY(float paramValue) {
        if (rangeSeekBarGY != null)
            rangeSeekBarGY.setProgress(paramValue);
        if (amountGY != null)
            amountGY.setAmount(paramValue);
    }

    /**
     * 设置欠压阀值
     *
     * @param paramValue
     */
    public void setQY(float paramValue) {
        if (rangeSeekBarQY != null)
            rangeSeekBarQY.setProgress(paramValue);
        if (amountQY != null)
            amountQY.setAmount(paramValue);
    }

    public List<PP> getValString() {
        List<PP> list = new ArrayList<>();
//        String res = "";
        rangeSeekBarGY.setProgress(amountGY.getAmount());
        BigDecimal gyfz = BigDecimal.valueOf(rangeSeekBarGY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
        rangeSeekBarQY.setProgress(amountQY.getAmount());
        BigDecimal qyfz = BigDecimal.valueOf(rangeSeekBarQY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        res += "00000005:" + gyfz + // 过压阀值
        list.add(new PP("00000005", gyfz + ""));
//                ",0000000D:" + qyfz; // 欠压阀值
        list.add(new PP("0000000D", qyfz + ""));
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onDYShow();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser && isShowing) {
//            Log.e("VpAdapter", "setUserVisibleHint: " + position);
//            listener.onDYShow();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    private OnShowingListener listener;

    public SettingDYFragment setOnShowingListener(OnShowingListener onShowingListener) {
        listener = onShowingListener;
        return this;
    }

    public interface OnShowingListener {
        void onDYShow();
    }
}
