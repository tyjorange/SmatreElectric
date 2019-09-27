package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.base.library.widget.SuperTextView;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.custom.AmountView;
import com.rejuvee.smartelectric.family.custom.AmountViewInt;
import com.rejuvee.smartelectric.family.custom.MyTextWatcher;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;
import com.rejuvee.smartelectric.family.utils.ScrollBindHelper;
import com.rejuvee.smartelectric.family.widget.InputDialog;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;
import com.rejuvee.smartelectric.family.widget.ObservableScrollView;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 安全阀值设置
 */
public class SwitchSettingActivity extends BaseActivity implements View.OnFocusChangeListener {
    private String TAG = "SwitchSettingFragment";
    //    private List<Item> items = new ArrayList<>();
    private final String[] list11 = {"1", "6", "10", "16", "20", "25", "32", "40", "50", "63"};//要填充的数据
    private ListPopupWindow listPopupWindow;
    //    private Spinner spinnerGL;
    private EditText et_GL;
    private EditText et_GL2;
    //    private EditText et_GWFZ;
    //    private Float valueGL;
    private RangeSeekBar rangeSeekBarGY;
    private AmountView amountGY;
    private RangeSeekBar rangeSeekBarQY;
    private AmountView amountQY;
    private RangeSeekBar rangeSeekBarLDL;
    private AmountViewInt amountLDL;
    private RangeSeekBar rangeSeekBarGWFZ;
    private AmountView amountGWFZ;
    private RangeSeekBar rangeSeekBarSXBPH;
    private AmountView amountSXBPH;
    //    private RangeSeekBar rangeSeekBarDL;
    private EditText dl_shangxian;
    private EditText dl_xiaxian;
    // 上电配置
    private TextView tv_sdpz;
    private SuperTextView superTextView;
    //    private ArrayAdapter<Item> adapter;
    //    private BreakEVSetHttpCall mHttpCall;
    private Handler mHandler;
    private CollectorBean collectorBean;
    private SwitchBean currentSwitchBean;
    //    private DecimalFormat formater = new DecimalFormat("000000.00");
    private LoadingDlg waitDialog;
    private Context mContext;
    private TextView txtLineName;//线路名称
    private ObservableScrollView scrollView;
    private SeekBar seekBar;
    private LinearLayout empty_layout;
    private InputDialog inputDialog;
    private int sdpz_val;//上电配置值

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_switch_setting;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        mContext = this;
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inputDialog = new InputDialog(this);
//        inputDialog.setTitle(getString(R.string.fun_share));
//        inputDialog.setHint(getString(R.string.input_share_username));
        inputDialog.setDialogListener(new InputDialog.onInputDialogListener() {

            @Override
            public void onEnsure(int val) {
                setSDPZ(val);
            }
        });
        waitDialog = new LoadingDlg(this, -1);

        seekBar = findViewById(R.id.vrsBar);
        scrollView = (ObservableScrollView) findViewById(R.id.sv_values);
        // 禁止用户手动垂直滚动
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // scrollView绑定滚动条seekBar
        ScrollBindHelper.bind(seekBar, scrollView);

        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        //切换线路
        txtLineName = (TextView) findViewById(R.id.txt_line_name);
        LinearLayout img_change = findViewById(R.id.img_change);
        img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(mContext, SwitchTree.DINGSHI, collectorBean, new SwitchTreeDialog.ChoseCallBack() {

                    @Override
                    public void onChose(SwitchBean s) {
                        currentSwitchBean = s;
//                        getData(switchBean);
                        txtLineName.setText("线路：" + currentSwitchBean.getName());
                        mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 100);
                    }
                });
                switchTreeDialog.show();
            }
        });

        // 过流
        listPopupWindow = new ListPopupWindow(this);
        et_GL = findViewById(R.id.et_sp1);
        et_GL.setOnFocusChangeListener(this);
        et_GL2 = findViewById(R.id.et_sp2);
//        et_GL2.setOnFocusChangeListener(this);
//        adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        spinnerGL.setAdapter(adapter);
//        spinnerGL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                valueGL = ((Item) parent.getSelectedItem()).getValue();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        // 过压
        amountGY = findViewById(R.id.amount_view_gy);
        amountGY.setVal_min(50);
        amountGY.setVal_max(380);
        amountGY.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarGY.setProgress(amount);
            }
        });
        rangeSeekBarGY = findViewById(R.id.seek_bar_gy);
        rangeSeekBarGY.setRange(50, 380);//范围
        rangeSeekBarGY.setTickMarkTextArray(new String[]{"50", "380"});//刻度
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
        amountQY = findViewById(R.id.amount_view_qy);
        amountQY.setVal_min(50);
        amountQY.setVal_max(180);
        amountQY.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarQY.setProgress(amount);
            }
        });
        rangeSeekBarQY = findViewById(R.id.seek_bar_qy);
        rangeSeekBarQY.setRange(50, 180);//范围
        rangeSeekBarQY.setTickMarkTextArray(new String[]{"50", "180"});//刻度
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
        // 电量上限
        dl_shangxian = findViewById(R.id.et_dl1);
        MyTextWatcher myTextWatcher1 = new MyTextWatcher(dl_shangxian, "000000.00");
        dl_shangxian.addTextChangedListener(myTextWatcher1);
        dl_shangxian.setOnFocusChangeListener(myTextWatcher1);
        dl_shangxian.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        // 电量下限
        dl_xiaxian = findViewById(R.id.et_dl2);
        MyTextWatcher myTextWatcher2 = new MyTextWatcher(dl_xiaxian, "000000.00");
        dl_xiaxian.addTextChangedListener(myTextWatcher2);
        dl_xiaxian.setOnFocusChangeListener(myTextWatcher2);
        dl_xiaxian.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        // 高温阀值
//        et_GWFZ = findViewById(R.id.et_gwfz);
//        MyTextWatcher myTextWatcher3 = new MyTextWatcher(et_GWFZ, "000.0");
//        et_GWFZ.addTextChangedListener(myTextWatcher3);
//        et_GWFZ.setOnFocusChangeListener(myTextWatcher3);
//        et_GWFZ.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        amountGWFZ = findViewById(R.id.amount_view_gwfz);
        amountGWFZ.setVal_min(0);
        amountGWFZ.setVal_max(85);
//        amountGWFZ.setAmount(0);
        amountGWFZ.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarGWFZ.setProgress(amount);
            }
        });
        rangeSeekBarGWFZ = findViewById(R.id.seek_bar_gwfz);
        rangeSeekBarGWFZ.setRange(0, 85);//范围
        rangeSeekBarGWFZ.setTickMarkTextArray(new String[]{"0", "85"});//刻度
        rangeSeekBarGWFZ.setIndicatorTextDecimalFormat("000.0");//格式化小数位数
        rangeSeekBarGWFZ.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                amountGWFZ.setAmount(v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        // 漏电流阀值
        findViewById(R.id.ll_ldl).setVisibility(View.VISIBLE);
        amountLDL = findViewById(R.id.amount_view_ldl);
        amountLDL.setVal_min(0);
        amountLDL.setVal_max(999);
        amountLDL.setAmount(0);
        amountLDL.setOnAmountChangeListener(new AmountViewInt.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarLDL.setProgress((int) amount);
            }
        });
        rangeSeekBarLDL = findViewById(R.id.seek_bar_ldl);
        rangeSeekBarLDL.setRange(0, 999);//范围
        rangeSeekBarLDL.setTickMarkTextArray(new String[]{"0", "999"});//刻度
        rangeSeekBarLDL.setIndicatorTextDecimalFormat("###");//格式化小数位数
        rangeSeekBarLDL.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
                amountLDL.setAmount((int) v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        //三项不平衡
        findViewById(R.id.ll_sxbph).setVisibility(View.VISIBLE);
        amountSXBPH = findViewById(R.id.amount_view_sxbph);
        amountSXBPH.setVal_min(10);
        amountSXBPH.setVal_max(100);
        amountSXBPH.setAmount(10);
        amountSXBPH.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarSXBPH.setProgress(amount);
            }
        });
        rangeSeekBarSXBPH = findViewById(R.id.seek_bar_sxbph);
        rangeSeekBarSXBPH.setRange(10, 100);//范围
        rangeSeekBarSXBPH.setTickMarkTextArray(new String[]{"10", "100"});//刻度
        rangeSeekBarSXBPH.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
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
        // 上电配置
        tv_sdpz = findViewById(R.id.tv_sdpz);
        findViewById(R.id.ll_sdpz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.setVal(sdpz_val);
                inputDialog.show();
            }
        });
        // 提交按钮
        superTextView = findViewById(R.id.stv_commit);
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues();
            }
        });
//        mHttpCall = new BreakEVSetHttpCall(this);
    }

    private static int currentSearchCount;//重试计数
    private static final int MAX_REQUEST_COUNT = 10;// 最大重新请求次数
    private static final int findSwitchParamBySwitch = 1221;//findParam
    private static final int sendGetThreadValueCommand = 1223;//sendGetParam

    //    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        getSwitchByCollector();
        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 100);
    }

    private static class MyHandler extends Handler {
        WeakReference<SwitchSettingActivity> activityWeakReference;

        MyHandler(SwitchSettingActivity activity) {
            activityWeakReference = new WeakReference<SwitchSettingActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SwitchSettingActivity theActivity = activityWeakReference.get();
            currentSearchCount++;
            if (msg.what == sendGetThreadValueCommand) {
                theActivity.flushValues();
            } else if (msg.what == findSwitchParamBySwitch) {
                theActivity.getValues();
            }
        }
    }

    /**
     * 根据线路型号改变显示
     */
    private void changeView() {
        // 漏电流阀值显示
        if (currentSwitchBean.getModelMajor() == 1 && currentSwitchBean.getModelMinor() == 5) {
            findViewById(R.id.ll_ldl).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ll_ldl).setVisibility(View.GONE);
        }
        // 三项不平衡显示
        if (currentSwitchBean.getModelMajor() == 2 && currentSwitchBean.getModelMinor() == 1) {
            findViewById(R.id.ll_sxbph).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.ll_sxbph).setVisibility(View.GONE);
        }
        // 这里刷新滑块显示
//        ScrollBindHelper.resetThumb();
        ScrollBindHelper.bind(seekBar, scrollView);
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        waitDialog.show();
        Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                currentSwitchBean = data.get(0);//init bean
                txtLineName.setText("线路：" + currentSwitchBean.getName());
                mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 1000);
                changeView();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(SwitchSettingActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(SwitchSettingActivity.this, message);
                }
                finish();
            }
        });
    }

    /**
     * 发送阀值刷新命令
     */
    private void flushValues() {
        if (currentSwitchBean == null) {
            return;
        }
        listPopupWindow.dismiss();
        waitDialog.show();
        Core.instance(this).sendGetThreadValueCommand(currentSwitchBean.getSerialNumber(), paramID, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d(TAG, "发送刷新命令成功, threadId = " + Thread.currentThread().getId());
                        currentSearchCount = 0;
                        mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 1000);
                        scrollView.setVisibility(View.VISIBLE);
                        superTextView.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        Log.e(TAG, "发送刷新命令失败");
                        waitDialog.dismiss();
                        CustomToast.showCustomErrorToast(mContext, message);
                        scrollView.setVisibility(View.INVISIBLE);
                        superTextView.setVisibility(View.INVISIBLE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private String paramID = "00000011," + // 过流阀值
            "00000005," + // 过压阀值
            "0000000D," + // 欠压阀值
            "00000018," + // 电量下限
            "00000019," + // 电量上限
            "0000001A," + // （瞬时）过流阀值2
            "0000001B," + // 漏电阀值
            "0000001C," + // 漏电自检使能
            "0000001D," + // 漏电自检时间 xxxx(ddhh)
            "0000001E," + // 温度阀值
            "0000001F," + // （上电配置0：拉闸；1：合闸，2：不动作）
            "00000020,"; // 三相不平衡阀值
    /**
     * 获取刷新命令后的阀值
     */
    private void getValues() {
        if (currentSwitchBean == null) {
            return;
        }
        Core.instance(this).findSwitchParamBySwitch(currentSwitchBean.getSwitchID(), paramID, new ActionCallbackListener<List<VoltageValue>>() {

                    @Override
                    public void onSuccess(List<VoltageValue> valueList) {
                        Log.i(TAG, "findSwitchParamBySwitch=" + currentSearchCount);
                        if (valueList == null) {
                            CustomToast.showCustomToast(mContext, "获取配置失败");
                            waitDialog.dismiss();
                            return;
                        }
                        if (currentSearchCount <= MAX_REQUEST_COUNT) {
                            mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 1000);
                            return;
                        }
                        // 设置值
                        for (VoltageValue vv : valueList) {
                            float paramValue = vv.getParamValue();
//                            if (paramValue == null) {
//                                CustomToast.showCustomToast(mContext, "读取配置失败");
//                                continue;
//                            }
                            resetValue();
                            switch (vv.getParamID()) {
                                case 0x00000005: // 过压阀值
                                    rangeSeekBarGY.setProgress(Float.valueOf(paramValue));
                                    amountGY.setAmount(Float.valueOf(paramValue));
                                    break;
                                case 0x0000000D:// 欠压阀值
                                    rangeSeekBarQY.setProgress(Float.valueOf(paramValue));
                                    amountQY.setAmount(Float.valueOf(paramValue));
                                    break;
                                case 0x00000011:// 过流阀值
                                    BigDecimal s = BigDecimal.valueOf(Float.valueOf(paramValue)).setScale(0, BigDecimal.ROUND_HALF_UP);
                                    et_GL.setText(s.toString());
                                    break;
                                case 0x00000018:// 电量下限
                                    dl_xiaxian.setText(new DecimalFormat("000000.00").format(paramValue));
                                    break;
                                case 0x00000019:// 电量上限
                                    dl_shangxian.setText(new DecimalFormat("000000.00").format(paramValue));
                                    break;
                                case 0x0000001A:// 瞬时过流阀值
                                    BigDecimal s2 = BigDecimal.valueOf(Float.valueOf(paramValue)).setScale(0, BigDecimal.ROUND_HALF_UP);
                                    et_GL2.setText(s2.toString());
                                    break;
                                case 0x0000001B:// 漏电阀值
                                    rangeSeekBarLDL.setProgress(paramValue);
                                    amountLDL.setAmount((int) paramValue);
                                    break;
                                case 0x0000001E:// 温度阀值
                                    rangeSeekBarGWFZ.setProgress(Float.valueOf(paramValue));
                                    amountGWFZ.setAmount(Float.valueOf(paramValue));
                                    break;
                                case 0x0000001F:// 上电配置
                                    setSDPZ((int) paramValue);
                                    break;
                                case 0x00000020:// 三相不平衡
                                    rangeSeekBarSXBPH.setProgress(Float.valueOf(paramValue));
                                    amountSXBPH.setAmount(Float.valueOf(paramValue));
                                    break;
                                case 0x00000021:// 故障电弧
                                    //暂无断路器支持电弧类设置，所以不显示。
                                    break;
                                case 0x00000022:// 防雷阀值
                                    //暂无断路器支持雷击类设置，所以不显示。
                                    break;
                            }
                        }
                        waitDialog.dismiss();
                        scrollView.setVisibility(View.VISIBLE);
                        superTextView.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                        changeView();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomErrorToast(mContext, message);
                        scrollView.setVisibility(View.INVISIBLE);
                        superTextView.setVisibility(View.INVISIBLE);
                        empty_layout.setVisibility(View.VISIBLE);
                        waitDialog.dismiss();
                    }
                });
    }

    /**
     * 温度有时查不到
     */
    private void resetValue() {
        rangeSeekBarGWFZ.setProgress(0);
        amountGWFZ.setAmount(0);
    }
    /**
     * 上电配置 设置值
     *
     * @param paramValue
     */
    private void setSDPZ(int paramValue) {
        switch (paramValue) {
            case 0:
                tv_sdpz.setText("拉闸");
                sdpz_val = 0;
                break;
            case 1:
                tv_sdpz.setText("合闸");
                sdpz_val = 1;
                break;
            case 2:
                tv_sdpz.setText("不动作");
                sdpz_val = 2;
                break;
        }
    }
    /**
     * 设置阀值 批量提交
     */
    private void setValues() {
        BigDecimal b1 = BigDecimal.valueOf(rangeSeekBarGY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
        BigDecimal b2 = BigDecimal.valueOf(rangeSeekBarQY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//                BigDecimal b3 = BigDecimal.valueOf(rangeSeekBarDL.getLeftSeekBar().getProgress()).setScale(0, BigDecimal.ROUND_HALF_UP);
//                BigDecimal b4 = BigDecimal.valueOf(rangeSeekBarDL.getRightSeekBar().getProgress()).setScale(0, BigDecimal.ROUND_HALF_UP);
        String b3 = dl_shangxian.getEditableText().toString();
        String b4 = dl_xiaxian.getEditableText().toString();
//        String b5 = et_GWFZ.getEditableText().toString();
        BigDecimal b5 = BigDecimal.valueOf(rangeSeekBarGWFZ.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
        String b6 = et_GL.getEditableText().toString();
        BigDecimal b7 = BigDecimal.valueOf(rangeSeekBarSXBPH.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
        String b8 = et_GL2.getEditableText().toString();
//                System.out.println(valueGL);
//                System.out.println(b1);
//                System.out.println(b2);
//                System.out.println(b3);
//                System.out.println(b4);
        if (b3.isEmpty() || b4.isEmpty()) {
            CustomToast.showCustomToast(mContext, "请设置电量");
            return;
        }
        String values = "00000011:" + b6 + // 过流阀值
                ",0000001A:" + b8 + // 过流阀值2
                ",00000005:" + b1 + // 过压阀值
                ",0000000D:" + b2 + // 欠压阀值
                ",00000018:" + b4 + // 电量下限
                ",00000019:" + b3 + // 电量上限
                ",0000001E:" + b5 + // 温度阀值
                ",0000001F:" + sdpz_val + // 上电配置
                ",00000020" + b7; // 三项不平衡
//        amountGY.setAmount(b1.floatValue());
//        amountQY.setAmount(b2.floatValue());
        if (currentSwitchBean == null) {
            return;
        }
        Core.instance(mContext).sendSetThreadValueCommand(currentSwitchBean.getSerialNumber(), values, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(mContext, "设置成功");
                finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(mContext, message);
            }
        });
//                mHttpCall.setLimitValue(switchBean.getSerialNumber(), values, new BreakEVSetHttpCall.IBreakSetCallback() {
//                    @Override
//                    public void setCallback(boolean isSuccess) {
//                        if (isSuccess) {
//                            CustomToast.showCustomToast(this, "设置成功");
//                        } else {
//                            CustomToast.showCustomToast(this, "设置失败");
//                        }
//                    }
//                });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showListPopulWindow(); //调用显示PopuWindow 函数
        }
    }

    private void showListPopulWindow() {
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list11));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(et_GL);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_GL.setText(list11[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    @Override
    protected void dealloc() {
//        mHandler.removeMessages(findSwitchParamBySwitch);
    }

    class Item {
        private String key;
        private Float value;

        Item(String key, Float value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Float getValue() {
            return value;
        }

        public void setValue(Float value) {
            this.value = value;
        }

        //to display object as a string in spinnerGL
        @Override
        public String toString() {
            return key;
        }

    }
}
