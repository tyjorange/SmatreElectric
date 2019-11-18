package com.rejuvee.smartelectric.family.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.common.custom.AmountView;
import com.rejuvee.smartelectric.family.common.widget.dialog.RadioDialog;
import com.rejuvee.smartelectric.family.databinding.FragmentSettingOtherBinding;
import com.rejuvee.smartelectric.family.model.bean.PP;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 其他类设置
 */
public class SettingOtherFragment extends BaseFragment {
    //    private View view;
    private RangeSeekBar rangeSeekBarWDFZ;
    private AmountView amountWDFZ;
    private RangeSeekBar rangeSeekBarSXBPH;
    private AmountView amountSXBPH;
    private TextView tv_sdpz;
    private int sdpz_val;//上电配置值
    private RadioDialog radioDialog;
//    private boolean isShowing = false;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.fragment_setting_other;
//    }

    private FragmentSettingOtherBinding mBinding;

    @Override
    protected View initView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_setting_other, null, false);
//        view = v;
        // 温度阀值
        amountWDFZ = mBinding.amountViewGwfz;//v.findViewById(R.id.amount_view_gwfz);
        amountWDFZ.setVal_min(0);
        amountWDFZ.setVal_max(85);
        amountWDFZ.setAmount(84);
        amountWDFZ.setOnAmountChangeListener((view, amount) -> rangeSeekBarWDFZ.setProgress(amount));
        rangeSeekBarWDFZ = mBinding.seekBarGwfz;//v.findViewById(R.id.seek_bar_gwfz);
        rangeSeekBarWDFZ.setRange(0, 85);//范围
        rangeSeekBarWDFZ.setTickMarkTextArray(new String[]{"0", "85"});//刻度
        rangeSeekBarWDFZ.setIndicatorTextDecimalFormat("000.0");//格式化小数位数
        rangeSeekBarWDFZ.setProgress(84);
        rangeSeekBarWDFZ.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                amountWDFZ.setAmount(v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        // 上电配置
        radioDialog = new RadioDialog(getContext());
        setSDPZ(2);
        radioDialog.setDialogListener(this::setSDPZ);
        tv_sdpz = mBinding.tvSdpz;//v.findViewById(R.id.tv_sdpz);
        mBinding.llSdpz.setOnClickListener(v12 -> {
            radioDialog.setVal(sdpz_val);
            radioDialog.show();
        });
        //三项不平衡
        mBinding.llSxbph.setVisibility(View.VISIBLE);
        amountSXBPH = mBinding.amountViewSxbph;//v.findViewById(R.id.amount_view_sxbph);
        amountSXBPH.setVal_min(10);
        amountSXBPH.setVal_max(100);
        amountSXBPH.setAmount(10);
        amountSXBPH.setOnAmountChangeListener((view, amount) -> rangeSeekBarSXBPH.setProgress(amount));
        rangeSeekBarSXBPH = mBinding.seekBarSxbph;//v.findViewById(R.id.seek_bar_sxbph);
        rangeSeekBarSXBPH.setRange(10, 100);//范围
        rangeSeekBarSXBPH.setTickMarkTextArray(new String[]{"10", "100"});//刻度
        rangeSeekBarSXBPH.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
        rangeSeekBarSXBPH.setProgress(10);
        rangeSeekBarSXBPH.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                amountSXBPH.setAmount(v1);
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

    public List<String> getParamID(SwitchBean currentSwitchBean) {
        List<String> list = new ArrayList<>();
        // 判断三项不平衡是否显示
        if (currentSwitchBean.getModelMajor() == 2 && currentSwitchBean.getModelMinor() == 1) {
            mBinding.llSxbph.setVisibility(View.VISIBLE);
//            return "0000001E," + // 温度阀值
//                    "00000020," + // 三相不平衡
//                    "0000001F,"; // 上电配置
            list.add("0000001E");
            list.add("00000020");
            list.add("0000001F");
            return list;
        } else {
            mBinding.llSxbph.setVisibility(View.GONE);
//            return "0000001E," + // 温度阀值
//                    "0000001F,"; // 上电配置
            list.add("0000001E");
            list.add("0000001F");
            return list;
        }
    }

    /**
     * 设置温度阀值
     *
     * @param paramValue
     */
    public void setWDFZ(float paramValue) {
        if (rangeSeekBarWDFZ != null) {
            rangeSeekBarWDFZ.setProgress(paramValue);
        }
        if (amountWDFZ != null) {
            amountWDFZ.setAmount(paramValue);
        }
    }

    /**
     * 设置上电配置
     *
     * @param paramValue
     */
    public void setSDPZ(int paramValue) {
        if (tv_sdpz == null) {
            return;
        }
        switch (paramValue) {
            case 0:
                tv_sdpz.setText(getString(R.string.vs75));
                sdpz_val = 0;
                break;
            case 1:
                tv_sdpz.setText(getString(R.string.vs74));
                sdpz_val = 1;
                break;
            case 2:
                tv_sdpz.setText(getString(R.string.vs84));
                sdpz_val = 2;
                break;
        }
    }

    /**
     * 设置三相不平衡
     *
     * @param paramValue
     */
    public void setSXBPH(float paramValue) {
        if (rangeSeekBarSXBPH != null) {
            rangeSeekBarSXBPH.setProgress(paramValue);
        }
        if (amountSXBPH != null) {
            amountSXBPH.setAmount(paramValue);
        }

    }

    /**
     * @return
     */
    public List<PP> getValString() {
        List<PP> list = new ArrayList<>();
        String res = "";
        rangeSeekBarWDFZ.setProgress(amountWDFZ.getAmount());
        BigDecimal wdfz = BigDecimal.valueOf(rangeSeekBarWDFZ.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        res += "0000001E:" + wdfz + // 温度阀值
        list.add(new PP("0000001E", wdfz + ""));
//                ",0000001F:" + sdpz_val; // 上电配置
        list.add(new PP("0000001F", sdpz_val + ""));
        if (mBinding.llSxbph.getVisibility() == View.VISIBLE) {
            rangeSeekBarSXBPH.setProgress(amountSXBPH.getAmount());
            BigDecimal sxbph = BigDecimal.valueOf(rangeSeekBarSXBPH.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//            res += ",00000020:" + sxbph.intValue(); // 三项不平衡
            list.add(new PP("00000020", sxbph.intValue() + ""));
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onOtherShow();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser && isShowing) {
//            Log.e("VpAdapter", "setUserVisibleHint: " + position);
//            listener.onOtherShow();
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    private OnShowingListener listener;

    public SettingOtherFragment setOnShowingListener(OnShowingListener onShowingListener) {
        listener = onShowingListener;
        return this;
    }

    public interface OnShowingListener {
        void onOtherShow();
    }
}
