package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.base.frame.greenandroid.wheel.view.WheelDateTime;
import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.base.library.widget.SuperTextView;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.fragment.SettingDL1Fragment;
import com.rejuvee.smartelectric.family.fragment.SettingDL2Fragment;
import com.rejuvee.smartelectric.family.fragment.SettingDYFragment;
import com.rejuvee.smartelectric.family.fragment.SettingOtherFragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;
import com.rejuvee.smartelectric.family.utils.ScrollBindHelper;
import com.rejuvee.smartelectric.family.widget.ObservableScrollView;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 安全阀值设置
 */
public class SwitchSettingActivity extends BaseActivity implements
        SettingDL1Fragment.OnShowingListener,
        SettingDL2Fragment.OnShowingListener,
        SettingOtherFragment.OnShowingListener,
        SettingDYFragment.OnShowingListener {
    private String TAG = "SwitchSettingFragment";
    //    private List<Item> items = new ArrayList<>();
//    private final String[] listVal = {"1", "6", "10", "16", "20", "25", "32", "40", "50", "63"};//要填充的数据
//    private ListPopupWindow listPopupWindow;
    private TabLayout mTabLayout;
    private ViewPager viewPager;


    //    private Spinner spinnerGL;
//    private EditText et_GL1;
//    private EditText et_GL2;
    //    private EditText et_GWFZ;
    //    private Float valueGL;
//    private RangeSeekBar rangeSeekBarGY;
//    private AmountView amountGY;
//    private RangeSeekBar rangeSeekBarQY;
//    private AmountView amountQY;
    //    private RangeSeekBar rangeSeekBarLDL;
//    private AmountViewInt amountLDL;
//    private RangeSeekBar rangeSeekBarWDFZ;
//    private AmountView amountWDFZ;
//    private RangeSeekBar rangeSeekBarSXBPH;
//    private AmountView amountSXBPH;
    //    private ImageView iv_bhsn;
//    private ImageView iv_zjsn;
//    private TextView tv_zj_time;
    //    private RangeSeekBar rangeSeekBarDL;
//    private EditText dl_shangxian;
//    private EditText dl_xiaxian;
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
    //    private LinearLayout empty_layout;
//    private TextView tv_sdpz;
    //    private int sdpz_val;//上电配置值
//    private InputDialog inputDialog;
    private WheelDateTime dateSelector;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_switch_setting;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    //    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        mContext = this;
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        inputDialog = new InputDialog(this);
//        inputDialog.setTitle(getString(R.string.fun_share));
//        inputDialog.setHint(getString(R.string.input_share_username));
//        inputDialog.setDialogListener(new InputDialog.onInputDialogListener() {
//
//            @Override
//            public void onEnsure(int val) {
//                setSDPZ(val);
//            }
//        });
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
                        txtLineName.setText(String.format("%s%s", mContext.getString(R.string.vs14), currentSwitchBean.getName()));
                        mTabLayout.getTabAt(0).select();
                        paramID = dl1_fragment.getParamID(currentSwitchBean);
                        mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 100);
                    }
                });
                switchTreeDialog.show();
            }
        });

        findViewById(R.id.img_flush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 100);
            }
        });

        initV();
    }

    private void initV() {
        // 过流
//        listPopupWindow = new ListPopupWindow(this);
//        et_GL1 = findViewById(R.id.et_sp1);
//        et_GL1.setOnFocusChangeListener(this);
//        et_GL2 = findViewById(R.id.et_sp2);
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
//        // 过压
//        amountGY = findViewById(R.id.amount_view_gy);
//        amountGY.setVal_min(100);
//        amountGY.setVal_max(480);
//        amountGY.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
//            @Override
//            public void onAmountChange(View view, float amount) {
//                rangeSeekBarGY.setProgress(amount);
//            }
//        });
//        rangeSeekBarGY = findViewById(R.id.seek_bar_gy);
//        rangeSeekBarGY.setRange(100, 480);//范围
//        rangeSeekBarGY.setTickMarkTextArray(new String[]{"100", "480"});//刻度
//        rangeSeekBarGY.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
//        rangeSeekBarGY.setOnRangeChangedListener(new OnRangeChangedListener() {
//            @Override
//            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
//                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//                amountGY.setAmount(v1);
//            }
//
//            @Override
//            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//        });
//        // 欠压
//        amountQY = findViewById(R.id.amount_view_qy);
//        amountQY.setVal_min(50);
//        amountQY.setVal_max(320);
//        amountQY.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
//            @Override
//            public void onAmountChange(View view, float amount) {
//                rangeSeekBarQY.setProgress(amount);
//            }
//        });
//        rangeSeekBarQY = findViewById(R.id.seek_bar_qy);
//        rangeSeekBarQY.setRange(50, 320);//范围
//        rangeSeekBarQY.setTickMarkTextArray(new String[]{"50", "320"});//刻度
//        rangeSeekBarQY.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
//        rangeSeekBarQY.setOnRangeChangedListener(new OnRangeChangedListener() {
//            @Override
//            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
//                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//                amountQY.setAmount(v1);
//            }
//
//            @Override
//            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//        });
        // 电量上限
//        dl_shangxian = findViewById(R.id.et_dl1);
//        MyTextWatcher myTextWatcher1 = new MyTextWatcher(dl_shangxian, "000000.00");
//        dl_shangxian.addTextChangedListener(myTextWatcher1);
//        dl_shangxian.setOnFocusChangeListener(myTextWatcher1);
//        dl_shangxian.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
//        // 电量下限
//        dl_xiaxian = findViewById(R.id.et_dl2);
//        MyTextWatcher myTextWatcher2 = new MyTextWatcher(dl_xiaxian, "000000.00");
//        dl_xiaxian.addTextChangedListener(myTextWatcher2);
//        dl_xiaxian.setOnFocusChangeListener(myTextWatcher2);
//        dl_xiaxian.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        // 温度阀值
//        amountWDFZ = findViewById(R.id.amount_view_gwfz);
//        amountWDFZ.setVal_min(0);
//        amountWDFZ.setVal_max(85);
////        amountGWFZ.setAmountInt(0);
//        amountWDFZ.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
//            @Override
//            public void onAmountChange(View view, float amount) {
//                rangeSeekBarWDFZ.setProgress(amount);
//            }
//        });
//        rangeSeekBarWDFZ = findViewById(R.id.seek_bar_gwfz);
//        rangeSeekBarWDFZ.setRange(0, 85);//范围
//        rangeSeekBarWDFZ.setTickMarkTextArray(new String[]{"0", "85"});//刻度
//        rangeSeekBarWDFZ.setIndicatorTextDecimalFormat("000.0");//格式化小数位数
//        rangeSeekBarWDFZ.setOnRangeChangedListener(new OnRangeChangedListener() {
//            @Override
//            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
//                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//                amountWDFZ.setAmount(v1);
//            }
//
//            @Override
//            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//        });
//         漏电保护、自检使能
//        iv_bhsn = findViewById(R.id.iv_bhsn);
//        iv_bhsn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bhsn == 0) {
//                    bhsn = 1;
//                } else {
//                    bhsn = 0;
//                }
//                updateSwitchStatus();
//            }
//        });
//        iv_zjsn = findViewById(R.id.iv_zjsn);
//        iv_zjsn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (zjsn == 0) {
//                    zjsn = 1;
//                } else {
//                    zjsn = 0;
//                }
//                updateSwitchStatus();
//            }
//        });
//        // 漏电自检时间
//        tv_zj_time = findViewById(R.id.tv_zj_time);
//        findViewById(R.id.ll_zj_time).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dateSelector.show(v);
//            }
//        });
//        // 漏电流阀值
//        findViewById(R.id.ll_ldl).setVisibility(View.VISIBLE);
//        amountLDL = findViewById(R.id.amount_view_ldl);
//        amountLDL.setVal_min(0);
//        amountLDL.setVal_max(999);
//        amountLDL.setAmountInt(0);
//        amountLDL.setOnAmountChangeListener(new AmountViewInt.OnAmountChangeListener() {
//            @Override
//            public void onAmountChange(View view, float amount) {
//                rangeSeekBarLDL.setProgress((int) amount);
//            }
//        });
//        rangeSeekBarLDL = findViewById(R.id.seek_bar_ldl);
//        rangeSeekBarLDL.setRange(0, 999);//范围
//        rangeSeekBarLDL.setTickMarkTextArray(new String[]{"0", "999"});//刻度
//        rangeSeekBarLDL.setIndicatorTextDecimalFormat("###");//格式化小数位数
//        rangeSeekBarLDL.setOnRangeChangedListener(new OnRangeChangedListener() {
//            @Override
//            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
//                float v1 = BigDecimal.valueOf(leftValue).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
//                amountLDL.setAmountInt((int) v1);
//            }
//
//            @Override
//            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//        });
//        //三项不平衡
//        findViewById(R.id.ll_sxbph).setVisibility(View.VISIBLE);
//        amountSXBPH = findViewById(R.id.amount_view_sxbph);
//        amountSXBPH.setVal_min(10);
//        amountSXBPH.setVal_max(100);
//        amountSXBPH.setAmount(10);
//        amountSXBPH.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
//            @Override
//            public void onAmountChange(View view, float amount) {
//                rangeSeekBarSXBPH.setProgress(amount);
//            }
//        });
//        rangeSeekBarSXBPH = findViewById(R.id.seek_bar_sxbph);
//        rangeSeekBarSXBPH.setRange(10, 100);//范围
//        rangeSeekBarSXBPH.setTickMarkTextArray(new String[]{"10", "100"});//刻度
//        rangeSeekBarSXBPH.setIndicatorTextDecimalFormat("###.0");//格式化小数位数
//        rangeSeekBarSXBPH.setOnRangeChangedListener(new OnRangeChangedListener() {
//            @Override
//            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
//                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
//                amountSXBPH.setAmount(v1);
//            }
//
//            @Override
//            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
//
//            }
//        });
        // 上电配置
//        tv_sdpz = findViewById(R.id.tv_sdpz);
//        findViewById(R.id.ll_sdpz).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inputDialog.setVal(sdpz_val);
//                inputDialog.show();
//            }
//        });
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

    //    private static int currentSearchCount;//重试计数
//    private static final int MAX_REQUEST_COUNT = 2;// 最大重新请求次数
    private static final int findSwitchParamBySwitch = 1221;//findParam
    private static final int sendGetThreadValueCommand = 1223;//sendGetParam

    @Override
    protected void initData() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        getSwitchByCollector();
        mHandler = new MyHandler(this);
//        mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 100);
        // 日期选择器
//        dateSelector = new WheelDateTime(SwitchSettingActivity.this,
//                new WheelDateTime.OnWheelListener() {
//
//                    @Override
//                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
////                        iyear = Integer.parseInt(year);
////                        imonth = Integer.parseInt(month);
////                        if (isDay) {
////                            iday = Integer.parseInt(day);
////                        }
////                        changeTvDate();
//                        dd = Integer.parseInt(day);
//                        hh = Integer.parseInt(hour);
//                        tv_zj_time.setText(String.format(Locale.getDefault(), getString(R.string.vs190), dd, hh));
//                    }
//                }, true,
//                new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()),
//                new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()),
//                new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()),
//                getString(R.string.vs191), getString(R.string.cancel), getString(R.string.ensure));
//        dateSelector.setDateItemVisiable(false, false, true, true, false);
    }

    private SettingDL1Fragment dl1_fragment;
    private SettingDYFragment dy_fragment;
    private SettingDL2Fragment dl2_fragment;
    private SettingOtherFragment other_fragment;

    @Override
    public void onShowDL1() {
//        dl1_fragment = fragment;
        paramID = dl1_fragment.getParamID(currentSwitchBean);
        mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 100);
    }

    @Override
    public void onShowDY() {
//        dy_fragment = fragment;
        paramID = dy_fragment.getParamID();
        mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 100);
    }

    @Override
    public void onShowDL2() {
//        dl2_fragment = fragment;
        paramID = dl2_fragment.getParamID();
        mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 100);
    }

    @Override
    public void onShowOther() {
//        other_fragment = fragment;
        paramID = other_fragment.getParamID(currentSwitchBean);
        mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 100);
    }

    private static class MyHandler extends Handler {
        WeakReference<SwitchSettingActivity> activityWeakReference;

        MyHandler(SwitchSettingActivity activity) {
            activityWeakReference = new WeakReference<SwitchSettingActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SwitchSettingActivity theActivity = activityWeakReference.get();
//            currentSearchCount++;
            if (msg.what == sendGetThreadValueCommand) {
                theActivity.flushValues();
            } else if (msg.what == findSwitchParamBySwitch) {
                theActivity.getValues();
            }
        }
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
                txtLineName.setText(String.format("%s%s", mContext.getString(R.string.vs14), currentSwitchBean.getName()));
//                mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 1000);
//                changeView();
                initFragment();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(SwitchSettingActivity.this, getString(R.string.vs29));
                } else {
                    CustomToast.showCustomErrorToast(SwitchSettingActivity.this, message);
                }
//                finish();
//                scrollView.setVisibility(View.INVISIBLE);
//                superTextView.setVisibility(View.INVISIBLE);
//                curentSwitchHaveValue = false;
//                setDefaultValue();
                waitDialog.dismiss();
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
//        listPopupWindow.dismiss();
        waitDialog.show();
        Core.instance(this).sendGetThreadValueCommand(currentSwitchBean.getSerialNumber(), paramID, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Log.d(TAG, getString(R.string.vs153) + " threadId = " + Thread.currentThread().getId());
//                currentSearchCount = 0;
//                mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 1000);
//                scrollView.setVisibility(View.VISIBLE);
//                superTextView.setVisibility(View.VISIBLE);
//                empty_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, getString(R.string.vs152));
                waitDialog.dismiss();
                CustomToast.showCustomErrorToast(mContext, message);
//                scrollView.setVisibility(View.INVISIBLE);
//                superTextView.setVisibility(View.INVISIBLE);
//                empty_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 获取刷新命令后的阀值
     */
    private void getValues() {
        if (currentSwitchBean == null) {
            return;
        }
        waitDialog.show();
        Core.instance(this).findSwitchParamBySwitch(currentSwitchBean.getSwitchID(), paramID, new ActionCallbackListener<List<VoltageValue>>() {

            @Override
            public void onSuccess(List<VoltageValue> valueList) {
//                Log.i(TAG, "findSwitchParamBySwitchCount=" + currentSearchCount);
                if (valueList == null) {
                    CustomToast.showCustomToast(mContext, getString(R.string.vs142));
                    waitDialog.dismiss();
                    return;
                }
                if (valueList.size() == 0) {
                    setDefaultValue();
                    waitDialog.dismiss();
                    return;
                }
                // 设置值
                for (VoltageValue vv : valueList) {
                    float paramValue = vv.getParamValue();
//                            if (paramValue == null) {
//                                CustomToast.showCustomToast(mContext, "读取配置失败");
//                                continue;
//                            }
                    switch (vv.getParamID()) {
                        case 0x00000005: // 过压阀值
                            dy_fragment.setGY(paramValue);
                            break;
                        case 0x0000000D:// 欠压阀值
                            dy_fragment.setQY(paramValue);
                            break;
                        case 0x00000011:// 过流阀值
                            dl1_fragment.setGL1(paramValue);
                            break;
                        case 0x00000018:// 电量下限
                            dl2_fragment.setXX(paramValue);
                            break;
                        case 0x00000019:// 电量上限
                            dl2_fragment.setSX(paramValue);
                            break;
                        case 0x0000001A:// 瞬时过流阀值
                            dl1_fragment.setGL2(paramValue);
                            break;
                        case 0x0000001B:// 漏电阀值
                            dl1_fragment.setVal(paramValue);
                            break;
                        case 0x0000001C:// 漏电自检/保护使能
                            dl1_fragment.setBHSN(paramValue);
                            break;
                        case 0x0000001D:// 漏电自检时间
                            dl1_fragment.setzjtime(paramValue);
                            break;
                        case 0x0000001E:// 温度阀值
                            other_fragment.setWDFZ(paramValue);
                            break;
                        case 0x0000001F:// 上电配置
                            other_fragment.setSDPZ((int) paramValue);
                            break;
                        case 0x00000020:// 三相不平衡
                            other_fragment.setSXBPH(paramValue);
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
//                        scrollView.setVisibility(View.VISIBLE);
//                        superTextView.setVisibility(View.VISIBLE);
//                        empty_layout.setVisibility(View.GONE);
//                changeView();
//                curentSwitchHaveValue = true;
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                CustomToast.showCustomErrorToast(mContext, message); // 无结果
//                        scrollView.setVisibility(View.INVISIBLE);
//                        superTextView.setVisibility(View.INVISIBLE);
//                        empty_layout.setVisibility(View.VISIBLE);
                setDefaultValue();
//                curentSwitchHaveValue = false;
                waitDialog.dismiss();
            }
        });
    }

//    boolean curentSwitchHaveValue = false;// 当前线路是否有值（防止虚假线路）
//    private int bhsn = 0;//保护使能
//    private int zjsn = 0;//自检使能
//    private int dd = 0;
//    private int hh = 0;

    /**
     * 00 01 10 11
     * BIT0=漏电自检使能 BIT1=漏电保护使能
     */
//    private void updateSwitchStatus() {
//        iv_bhsn.setImageDrawable(getDrawable(bhsn == 0 ? R.drawable.icon_switch_off : R.drawable.icon_switch_on));
//        iv_zjsn.setImageDrawable(getDrawable(zjsn == 0 ? R.drawable.icon_switch_off : R.drawable.icon_switch_on));
//    }

    /**
     * 初始化成默认值
     */
    private void setDefaultValue() {
        // 无值则重置成默认值
//        if (!curentSwitchHaveValue) {
//            case 0x00000005: // 过压阀值 275
        dy_fragment.setGY(275);
//        case 0x0000000D:// 欠压阀值 160
        dy_fragment.setQY(160);
//        case 0x00000011:// 过流阀值 30
        dl1_fragment.setGL1(30);
//        case 0x00000018:// 电量下限 999999.99
        dl2_fragment.setXX(999999.99f);
//        case 0x00000019:// 电量上限 999999.99
        dl2_fragment.setSX(999999.99f);
//        case 0x0000001A:// 瞬时过流阀值 200
        dl1_fragment.setGL2(200);
//        case 0x0000001B:// 漏电阀值 30
        dl1_fragment.setVal(30);
//        case 0x0000001C:// 漏电自检/保护使能 0 0
        dl1_fragment.setBHSN(0);
//        case 0x0000001D:// 漏电自检时间 0 0
        dl1_fragment.setzjtime(0);
//        case 0x0000001E:// 温度阀值 84
        other_fragment.setWDFZ(84);
//        case 0x0000001F:// 上电配置 2
        other_fragment.setSDPZ(2);
//        case 0x00000020:// 三相不平衡 10?
        other_fragment.setSXBPH(10);
//        }
//        changeView();
    }

    /**
     * 103 104 105
     * 根据线路型号改变显示项目
     */
//    private void changeView() {
//        // 漏电流阀值显示
//        if (currentSwitchBean.getModelMajor() == 1 && currentSwitchBean.getModelMinor() == 5) {
//            findViewById(R.id.ll_ldl).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.ll_ldl).setVisibility(View.GONE);
//        }
//        // 三项不平衡显示
//        if (currentSwitchBean.getModelMajor() == 2 && currentSwitchBean.getModelMinor() == 1) {
//            findViewById(R.id.ll_sxbph).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.ll_sxbph).setVisibility(View.GONE);
//        }
//        // 这里刷新右侧滑块显示
////        ScrollBindHelper.resetThumb();
//        ScrollBindHelper.bind(seekBar, scrollView);
//    }

    /**
     * 设置上电配置
     *
     * @param paramValue
     */
//    private void setSDPZ(int paramValue) {
//        switch (paramValue) {
//            case 0:
//                tv_sdpz.setText(getString(R.string.vs75));
//                sdpz_val = 0;
//                break;
//            case 1:
//                tv_sdpz.setText(getString(R.string.vs74));
//                sdpz_val = 1;
//                break;
//            case 2:
//                tv_sdpz.setText(getString(R.string.vs84));
//                sdpz_val = 2;
//                break;
//        }
//    }

    private String paramID = "00000011," + // 过流阀值(1)
            "00000005," + // 过压阀值
            "0000000D," + // 欠压阀值
            "00000018," + // 电量下限
            "00000019," + // 电量上限
            "0000001A," + // 瞬时过流阀值(2)
            "0000001B," + // 漏电阀值
            "0000001C," + // 漏电自检/保护使能 [0000 0001 0100 0101] 对应 [0 1 2 3]
            "0000001D," + // 漏电自检时间 xxxx(ddhh) 0000
            "0000001E," + // 温度阀值
            "0000001F," + // 上电配置 0：拉闸；1：合闸，2：不动作
            "00000020"; // 三相不平衡阀值

    /**
     * 设置阀值 批量提交
     */
    private void setValues() {
//        rangeSeekBarGY.setProgress(amountGY.getAmount());
//        BigDecimal gyfz = BigDecimal.valueOf(rangeSeekBarGY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        rangeSeekBarQY.setProgress(amountQY.getAmount());
//        BigDecimal qyfz = BigDecimal.valueOf(rangeSeekBarQY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        rangeSeekBarWDFZ.setProgress(amountWDFZ.getAmount());
//        BigDecimal wdfz = BigDecimal.valueOf(rangeSeekBarWDFZ.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        rangeSeekBarSXBPH.setProgress(amountSXBPH.getAmount());
//        BigDecimal sxbph = BigDecimal.valueOf(rangeSeekBarSXBPH.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        rangeSeekBarLDL.setProgress(amountLDL.getAmountInt());
//        BigDecimal ldfz = BigDecimal.valueOf(rangeSeekBarLDL.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//        amountLDL.setAmountInt(ldfz.intValue());
//        String dlsx = dl_shangxian.getEditableText().toString();
//        String dlxx = dl_xiaxian.getEditableText().toString();
//        String glfz = et_GL1.getEditableText().toString();
//        String ssglfz = et_GL2.getEditableText().toString();
//        String dds = Integer.toHexString(dd);
//        String hhs = Integer.toHexString(hh);
//        if (dlsx.isEmpty() || dlxx.isEmpty()) {
//            CustomToast.showCustomErrorToast(mContext, getString(R.string.vs151));
//            return;
//        }
//        if (Double.valueOf(dlsx) <= Double.valueOf(dlxx)) {
//            CustomToast.showCustomErrorToast(mContext, getString(R.string.vs189));
//            return;
//        }
        String values = "";
//                "00000011:" + glfz + // 过流阀值(1)
//                ",00000005:" + gyfz + // 过压阀值
//                        ",0000000D:" + qyfz + // 欠压阀值
//                        ",00000018:" + dlxx + // 电量下限
//                        ",00000019:" + dlsx + // 电量上限
//                ",0000001A:" + ssglfz + // 瞬时过流阀值(2)
//                ",0000001B:" + ldfz.intValue() + // 漏电阀值
//                ",0000001C:" + Integer.valueOf(bhsn + "" + zjsn, 2) + // 漏电自检/保护使能
//                ",0000001D:" + (dd * 256 + hh) + // 漏电自检时间
//                        ",0000001E:" + wdfz + // 温度阀值
//                        ",0000001F:" + sdpz_val + // 上电配置
//                        ",00000020:" + sxbph.intValue(); // 三项不平衡
        if (currentSwitchBean == null) {
            return;
        }
        Core.instance(mContext).sendSetThreadValueCommand(currentSwitchBean.getSerialNumber(), values, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(mContext, getString(R.string.vs213));
//                finish();
//                mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 100);
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

    //    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        if (hasFocus) {
////            showListPopulWindow(); //调用显示PopuWindow 函数
//        }
//    }

    /**
     * 预设值下拉选
     */
//    private void showListPopulWindow() {
//        listPopupWindow.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listVal));//用android内置布局，或设计自己的样式
//        listPopupWindow.setAnchorView(et_GL1);//以哪个控件为基准，在该处以mEditText为基准
//        listPopupWindow.setModal(true);
//
//        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                et_GL1.setText(listVal[i]);//把选择的选项内容展示在EditText上
//                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
//            }
//        });
//        listPopupWindow.show();//把ListPopWindow展示出来
//    }
    @Override
    protected void dealloc() {
//        mHandler.removeMessages(findSwitchParamBySwitch);
    }

    private void initFragment() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_setting);
        viewPager = findViewById(R.id.vp_setting);
        MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(viewPager, true);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        //        private Bundle bundle;
        private List<BaseFragment> listFragments = new ArrayList<>();

        MyFragmentAdapter(FragmentManager fm) {
            super(fm);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("currentSwitchBean", currentSwitchBean);
            dl1_fragment = new SettingDL1Fragment().setOnShowingListener(SwitchSettingActivity.this);
//            s1.setArguments(bundle);
            dy_fragment = new SettingDYFragment().setOnShowingListener(SwitchSettingActivity.this);
//            s2.setArguments(bundle);
            dl2_fragment = new SettingDL2Fragment().setOnShowingListener(SwitchSettingActivity.this);
//            s3.setArguments(bundle);
            other_fragment = new SettingOtherFragment().setOnShowingListener(SwitchSettingActivity.this);
//            s4.setArguments(bundle);
            listFragments.add(dl1_fragment);
            listFragments.add(dy_fragment);
            listFragments.add(dl2_fragment);
            listFragments.add(other_fragment);
        }

        @Override
        public Fragment getItem(int position) {
//            Class fragment = listFragments.get(position);
//            return Fragment.instantiate(SwitchSettingActivity.this, fragment.getName(), bundle);
            //            Bundle bundle = new Bundle();
//            bundle.putParcelable("currentSwitchBean", currentSwitchBean);
//            baseFragment.setArguments(bundle);
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "电流";
            } else if (position == 1) {
                return "电压";
            } else if (position == 2) {
                return "电量";
            } else if (position == 3) {
                return "其他";
            }
            return super.getPageTitle(position);
        }
    }
//    class Item {
//        private String key;
//        private Float value;
//
//        Item(String key, Float value) {
//            this.key = key;
//            this.value = value;
//        }
//
//        public String getKey() {
//            return key;
//        }
//
//        public void setKey(String key) {
//            this.key = key;
//        }
//
//        public Float getValue() {
//            return value;
//        }
//
//        public void setValue(Float value) {
//            this.value = value;
//        }
//
//        //to display object as a string in spinnerGL
//        @Override
//        public String toString() {
//            return key;
//        }
//
//    }
}
