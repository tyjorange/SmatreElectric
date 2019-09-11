package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.rejuvee.smartelectric.family.custom.MyTextWatcher;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;

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
    //    private Float valueGL;
    private RangeSeekBar rangeSeekBarGY;
    private AmountView amountGY;
    private RangeSeekBar rangeSeekBarQY;
    private AmountView amountQY;
    private RangeSeekBar rangeSeekBarLDL;
    private AmountView amountLDL;
    //    private RangeSeekBar rangeSeekBarDL;
    private EditText dl_shangxian;
    private EditText dl_xiaxian;
    private SuperTextView superTextView;
    //    private ArrayAdapter<Item> adapter;
//    private BreakEVSetHttpCall mHttpCall;
    private Handler mHandler;
    private CollectorBean collectorBean;
    private SwitchBean currentSwitchBean;
    private DecimalFormat formater = new DecimalFormat("000000.00");
    private LoadingDlg waitDialog;
    private Context mContext;
    private TextView txtLineName;//线路名称
    private ScrollView scrollView;
    private LinearLayout empty_layout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_switch_setting;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        waitDialog = new LoadingDlg(this, -1);
        scrollView = (ScrollView) findViewById(R.id.sv_values);
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
        rangeSeekBarGY.setIndicatorTextDecimalFormat("000.0");//格式化小数位数
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
        rangeSeekBarQY.setIndicatorTextDecimalFormat("000.0");//格式化小数位数
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
        // 电量上下限
        dl_shangxian = findViewById(R.id.et_dl1);
        MyTextWatcher myTextWatcher1 = new MyTextWatcher(dl_shangxian);
        dl_shangxian.addTextChangedListener(myTextWatcher1);
        dl_shangxian.setOnFocusChangeListener(myTextWatcher1);
        dl_xiaxian = findViewById(R.id.et_dl2);
        MyTextWatcher myTextWatcher2 = new MyTextWatcher(dl_xiaxian);
        dl_xiaxian.addTextChangedListener(myTextWatcher2);
        dl_xiaxian.setOnFocusChangeListener(myTextWatcher2);
        // 漏电流
        amountLDL = findViewById(R.id.amount_view_ldl);
        amountLDL.setVal_min(50);
        amountLDL.setVal_max(180);
        amountLDL.setAmount(50);
        amountLDL.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, float amount) {
                rangeSeekBarLDL.setProgress(amount);
            }
        });
        rangeSeekBarLDL = findViewById(R.id.seek_bar_ldl);
        rangeSeekBarLDL.setRange(50, 180);//范围
        rangeSeekBarLDL.setTickMarkTextArray(new String[]{"50", "180"});//刻度
        rangeSeekBarLDL.setIndicatorTextDecimalFormat("000.0");//格式化小数位数
        rangeSeekBarLDL.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                float v1 = BigDecimal.valueOf(leftValue).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                amountLDL.setAmount(v1);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
        // 提交按钮
        superTextView = findViewById(R.id.stv_commit);
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigDecimal b1 = BigDecimal.valueOf(rangeSeekBarGY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
                BigDecimal b2 = BigDecimal.valueOf(rangeSeekBarQY.getLeftSeekBar().getProgress()).setScale(1, BigDecimal.ROUND_HALF_UP);
//                BigDecimal b3 = BigDecimal.valueOf(rangeSeekBarDL.getLeftSeekBar().getProgress()).setScale(0, BigDecimal.ROUND_HALF_UP);
//                BigDecimal b4 = BigDecimal.valueOf(rangeSeekBarDL.getRightSeekBar().getProgress()).setScale(0, BigDecimal.ROUND_HALF_UP);
                String b3 = dl_shangxian.getEditableText().toString();
                String b4 = dl_xiaxian.getEditableText().toString();
//                System.out.println(valueGL);
//                System.out.println(b1);
//                System.out.println(b2);
//                System.out.println(b3);
//                System.out.println(b4);
                if (b3.isEmpty() || b4.isEmpty()) {
                    CustomToast.showCustomToast(mContext, "请设置电量");
                    return;
                }
                String values = "00000011:" + et_GL.getEditableText().toString() + ",00000005:" + b1 + ",0000000D:" + b2 + ",00000018:" + b4 + ",00000019:" + b3;
                System.out.println(values);
                amountGY.setAmount(b1.floatValue());
                amountQY.setAmount(b2.floatValue());
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
                        CustomToast.showCustomToast(mContext, "设置失败");
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
        });
//        mHttpCall = new BreakEVSetHttpCall(this);
    }

    private static int currentSearchCount;//重试计数
    private static final int MAX_REQUEST_COUNT = 10;// 最大重新请求次数
    private static final int findSwitchParamBySwitch = 0;//findParam
    private static final int sendGetThreadValueCommand = 1;//sendGetParam

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                currentSearchCount++;
                if (msg.what == sendGetThreadValueCommand) {
                    getData1();
                } else if (msg.what == findSwitchParamBySwitch) {
                    getData2();
                }
            }
        };
//        getData();
        getSwitchByCollector();
        mHandler.sendEmptyMessageDelayed(sendGetThreadValueCommand, 100);
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
//                getBreakSignalValue(curBreaker);
//                judgSwitchstate(curBreaker);
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
     * 发送刷新命令
     */
    private void getData1() {
        if (currentSwitchBean == null) {
            return;
        }
        listPopupWindow.dismiss();
        waitDialog.show();
        Core.instance(this).sendGetThreadValueCommand(currentSwitchBean.getSerialNumber(), "00000011,00000005,0000000D,00000018,00000019,0000001B,0000001C,0000001D,0000001E",
                new ActionCallbackListener<Void>() {
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

    /**
     * 获取法师刷新命令后的值
     */
    private void getData2() {
        if (currentSwitchBean == null) {
            return;
        }
        Core.instance(this).findSwitchParamBySwitch(currentSwitchBean.getSwitchID(), "00000011,00000005,0000000D,00000018,00000019,0000001B,0000001C,0000001D,0000001E",
                new ActionCallbackListener<List<VoltageValue>>() {

                    @Override
                    public void onSuccess(List<VoltageValue> valueList) {
                        if (valueList == null) {
                            CustomToast.showCustomToast(mContext, "获取配置失败");
                            waitDialog.dismiss();
                            return;
                        }
                        if (valueList.size() != 5) {
                            if (currentSearchCount != MAX_REQUEST_COUNT) {
                                mHandler.sendEmptyMessageDelayed(findSwitchParamBySwitch, 1000);
                                return;
                            }
                        }
                        // 设置值
                        for (VoltageValue vv : valueList) {
                            String paramValue = vv.getParamValue();
                            if (paramValue == null) {
                                CustomToast.showCustomToast(mContext, "读取配置失败");
                                continue;
                            }
                            switch (vv.getParamID()) {
                                case "5": // 过压
                                    rangeSeekBarGY.setProgress(Float.valueOf(paramValue));
                                    amountGY.setAmount(Float.valueOf(paramValue));
                                    break;
                                case "13":// 欠压
                                    rangeSeekBarQY.setProgress(Float.valueOf(paramValue));
                                    amountQY.setAmount(Float.valueOf(paramValue));
                                    break;
                                case "17":// 过流
                                    BigDecimal s = BigDecimal.valueOf(Float.valueOf(paramValue)).setScale(0, BigDecimal.ROUND_HALF_UP);
//                            spinnerGL.setSelection(items.indexOf(getSelect(s.floatValue())));
                                    et_GL.setText(s.toString());
                                    break;
                                case "24":// 下限
                                    dl_xiaxian.setText(formater.format(Double.valueOf(paramValue.isEmpty() ? "0" : paramValue)));
                                    break;
                                case "25":// 上限
                                    dl_shangxian.setText(formater.format(Double.valueOf(paramValue.isEmpty() ? "0" : paramValue)));
                                    break;
                            }
                        }
                        waitDialog.dismiss();
                        scrollView.setVisibility(View.VISIBLE);
                        superTextView.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
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
