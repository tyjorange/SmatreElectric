package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.google.gson.Gson;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.BaseFragment;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivitySwitchSettingBinding;
import com.rejuvee.smartelectric.family.fragment.SettingDL1Fragment;
import com.rejuvee.smartelectric.family.fragment.SettingDL2Fragment;
import com.rejuvee.smartelectric.family.fragment.SettingDYFragment;
import com.rejuvee.smartelectric.family.fragment.SettingOtherFragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.JData;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.VoltageValue;
import com.rejuvee.smartelectric.family.model.viewmodel.SwitchSettingViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

/**
 * 安全阀值设置
 */
public class SwitchSettingActivity extends BaseActivity implements
        SettingDL1Fragment.OnShowingListener,
        SettingDL2Fragment.OnShowingListener,
        SettingOtherFragment.OnShowingListener,
        SettingDYFragment.OnShowingListener {
    private String TAG = "SwitchSettingFragment";
    //    private TabLayout mTabLayout;
//    private ViewPager viewPager;
    private Handler mHandler;
    private CollectorBean collectorBean;
    private SwitchBean currentSwitchBean;
    private LoadingDlg waitDialog;
    private Gson gson;
    //    private Context SwitchSettingActivity.this;
//    private TextView txtLineName;//线路名称
//    private ObservableScrollView scrollView;
//    private SeekBar seekBar;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_switch_setting;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivitySwitchSettingBinding mBinding;
    private SwitchSettingViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_switch_setting);
        mViewModel = ViewModelProviders.of(this).get(SwitchSettingViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        SwitchSettingActivity.this = this;
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        mTabLayout = findViewById(R.id.tab_setting);
//        viewPager = findViewById(R.id.vp_setting);
        waitDialog = new LoadingDlg(this, -1);
        gson = new Gson();
//        seekBar = findViewById(R.id.vrsBar);
//        scrollView = (ObservableScrollView) findViewById(R.id.sv_values);
        // 禁止用户手动垂直滚动
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
        // scrollView绑定滚动条seekBar
//        ScrollBindHelper.bind(seekBar, scrollView);

        //切换线路
//        txtLineName = findViewById(R.id.txt_line_name);
//        LinearLayout img_change = findViewById(R.id.img_change);
//        img_change.setOnClickListener(v -> {
//
//        });
        // 刷新按钮
//        findViewById(R.id.img_flush).setOnClickListener(v -> mHandler.sendEmptyMessageDelayed(MSG_sendGetThreadValueCommand_FLAG, 100));
        // 提交按钮
//        SuperTextView superTextView = findViewById(R.id.stv_commit);
//        superTextView.setOnClickListener(v -> setValues());
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        getSwitchByCollector();
        mHandler = new MyHandler(this);
    }

    private SettingDL1Fragment dl1_fragment;
    private SettingDYFragment dy_fragment;
    private SettingDL2Fragment dl2_fragment;
    private SettingOtherFragment other_fragment;
    private JData _JDataGet;

    @Override
    public void onDL1Show() {
        _JDataGet = new JData();
//        dl1_fragment = fragment;
//        currentParamID = dl1_fragment.getParamID(currentSwitchBean);
        _JDataGet.switchCode = currentSwitchBean.getSerialNumber();
        _JDataGet.paramIDs = dl1_fragment.getParamID(currentSwitchBean);
        mHandler.sendEmptyMessageDelayed(MSG_findSwitchParamBySwitch_FLAG, 100);
    }

    @Override
    public void onDYShow() {
        _JDataGet = new JData();
//        dy_fragment = fragment;
//        currentParamID = dy_fragment.getParamID();
        _JDataGet.switchCode = currentSwitchBean.getSerialNumber();
        _JDataGet.paramIDs = dy_fragment.getParamID();
        mHandler.sendEmptyMessageDelayed(MSG_findSwitchParamBySwitch_FLAG, 100);
    }

    @Override
    public void onDL2Show() {
        _JDataGet = new JData();
//        dl2_fragment = fragment;
//        currentParamID = dl2_fragment.getParamID();
        _JDataGet.switchCode = currentSwitchBean.getSerialNumber();
        _JDataGet.paramIDs = dl2_fragment.getParamID();
        mHandler.sendEmptyMessageDelayed(MSG_findSwitchParamBySwitch_FLAG, 100);
    }

    @Override
    public void onOtherShow() {
        _JDataGet = new JData();
//        other_fragment = fragment;
//        currentParamID = other_fragment.getParamID(currentSwitchBean);
        _JDataGet.switchCode = currentSwitchBean.getSerialNumber();
        _JDataGet.paramIDs = other_fragment.getParamID(currentSwitchBean);
        mHandler.sendEmptyMessageDelayed(MSG_findSwitchParamBySwitch_FLAG, 100);
    }

    //    private static int currentSearchCount;//重试计数
//    private static final int MAX_REQUEST_COUNT = 5;// 最大重新请求次数
    private static final int MSG_findSwitchParamBySwitch_FLAG = 1221;//findParam 读取
    private static final int MSG_sendGetThreadValueCommand_FLAG = 1223;//sendGetParam 刷新

    /**
     * MyHandler
     */
    private static class MyHandler extends Handler {
        WeakReference<SwitchSettingActivity> activityWeakReference;

        MyHandler(SwitchSettingActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SwitchSettingActivity theActivity = activityWeakReference.get();
//            currentSearchCount++;
            if (msg.what == MSG_sendGetThreadValueCommand_FLAG) {
                theActivity.flushValues();
            } else if (msg.what == MSG_findSwitchParamBySwitch_FLAG) {
                theActivity.getValues();
            }
        }
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        waitDialog.show();
        currentCall = Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                currentSwitchBean = data.get(0);//init bean
//                txtLineName.setText(String.format("%s%s", SwitchSettingActivity.this.getString(R.string.vs14), currentSwitchBean.getName()));
                mViewModel.setTxtLineName(String.format("%s%s", SwitchSettingActivity.this.getString(R.string.vs14), currentSwitchBean.getName()));
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
        String content = gson.toJson(_JDataGet);// 转JSON字符串
        currentCall = Core.instance(this).sendGetThreadValueCommand(content, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Log.d(TAG, getString(R.string.vs153) + " threadId = " + Thread.currentThread().getId());
//                currentSearchCount = 0;
                mHandler.sendEmptyMessageDelayed(MSG_findSwitchParamBySwitch_FLAG, 5000); // 等待5秒
//                scrollView.setVisibility(View.VISIBLE);
//                superTextView.setVisibility(View.VISIBLE);
//                empty_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
                waitDialog.dismiss();
                CustomToast.showCustomErrorToast(SwitchSettingActivity.this, message);
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
//        if (currentSearchCount <= MAX_REQUEST_COUNT) {
//            return;
//        }
        if (!waitDialog.isShowing()) {
            waitDialog.show();
        }
        String content = gson.toJson(_JDataGet);// 转JSON字符串
        currentCall = Core.instance(this).findSwitchParamBySwitch(content, new ActionCallbackListener<List<VoltageValue>>() {

            @Override
            public void onSuccess(List<VoltageValue> valueList) {
                if (valueList == null) {
                    CustomToast.showCustomToast(SwitchSettingActivity.this, getString(R.string.vs142));
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
                Log.e(TAG, message);
//                CustomToast.showCustomErrorToast(SwitchSettingActivity.this, message); // 无结果
//                        scrollView.setVisibility(View.INVISIBLE);
//                        superTextView.setVisibility(View.INVISIBLE);
//                        empty_layout.setVisibility(View.VISIBLE);
//                curentSwitchHaveValue = false;
                setDefaultValue();
                waitDialog.dismiss();
            }
        });
    }

    /**
     * 初始化成默认值
     */
    private void setDefaultValue() {
//        case 0x00000005: // 过压阀值 275
        dy_fragment.setGY(275);
//        case 0x0000000D:// 欠压阀值 160
        dy_fragment.setQY(160);
//        case 0x00000011:// 过流阀值 1 30
        dl1_fragment.setGL1(30);
//        case 0x00000018:// 电量下限 999999.99
        dl2_fragment.setXX(000000.00f);
//        case 0x00000019:// 电量上限 999999.99
        dl2_fragment.setSX(999999.99f);
//        case 0x0000001A:// 瞬时过流阀值 2 200
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
    }

//    private String currentParamID = "00000011," + // 过流阀值(1)
//            "00000005," + // 过压阀值
//            "0000000D," + // 欠压阀值
//            "00000018," + // 电量下限
//            "00000019," + // 电量上限
//            "0000001A," + // 瞬时过流阀值(2)
//            "0000001B," + // 漏电阀值
//            "0000001C," + // 漏电自检/保护使能 [0000 0001 0100 0101] 对应 [0 1 2 3]
//            "0000001D," + // 漏电自检时间 xxxx(ddhh) 0000
//            "0000001E," + // 温度阀值
//            "0000001F," + // 上电配置 0：拉闸；1：合闸，2：不动作
//            "00000020"; // 三相不平衡阀值

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
//        String dlsx = dl_shangxian.getEditableText().toString();
//        String dlxx = dl_xiaxian.getEditableText().toString();
//        String glfz = et_GL1.getEditableText().toString();
//        String ssglfz = et_GL2.getEditableText().toString();
//        String dds = Integer.toHexString(dd);
//        String hhs = Integer.toHexString(hh);
//        if (dlsx.isEmpty() || dlxx.isEmpty()) {
//            CustomToast.showCustomErrorToast(SwitchSettingActivity.this, getString(R.string.vs151));
//            return;
//        }
//        if (Double.valueOf(dlsx) <= Double.valueOf(dlxx)) {
//            CustomToast.showCustomErrorToast(SwitchSettingActivity.this, getString(R.string.vs189));
//            return;
//        }
//                "00000011:" + glfz + // 过流阀值(1)
//                ",00000005:" + gyfz + // 过压阀值
//                ",0000000D:" + qyfz + // 欠压阀值
//                ",00000018:" + dlxx + // 电量下限
//                ",00000019:" + dlsx + // 电量上限
//                ",0000001A:" + ssglfz + // 瞬时过流阀值(2)
//                ",0000001B:" + ldfz.intValue() + // 漏电阀值
//                ",0000001C:" + Integer.valueOf(bhsn + "" + zjsn, 2) + // 漏电自检/保护使能
//                ",0000001D:" + (dd * 256 + hh) + // 漏电自检时间
//                ",0000001E:" + wdfz + // 温度阀值
//                ",0000001F:" + sdpz_val + // 上电配置
//                ",00000020:" + sxbph.intValue(); // 三项不平衡
        if (currentSwitchBean == null) {
            return;
        }
        JData _JDataSet = new JData();
        _JDataSet.switchCode = currentSwitchBean.getSerialNumber();
//        String jdata = "";
        int selectedTabPosition = mBinding.tabSetting.getSelectedTabPosition();
        switch (selectedTabPosition) {
            case 0:
                _JDataSet.params = (dl1_fragment.getValList());
                break;
            case 1:
                _JDataSet.params = (dy_fragment.getValList());
                break;
            case 2:
                _JDataSet.params = (dl2_fragment.getValList());
                break;
            case 3:
                _JDataSet.params = (other_fragment.getValList());
                break;
        }
        if (_JDataSet.params.isEmpty()) {
            return;
        }
        String content = gson.toJson(_JDataSet);// 转JSON字符串
        System.out.println(content);
        currentCall = Core.instance(SwitchSettingActivity.this).sendSetThreadValueCommand(content, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(SwitchSettingActivity.this, getString(R.string.vs213));
                mHandler.sendEmptyMessageDelayed(MSG_sendGetThreadValueCommand_FLAG, 100);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(SwitchSettingActivity.this, message);
            }
        });
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onChange(View view) {
            SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(SwitchSettingActivity.this, SwitchTree.DINGSHI, collectorBean, s -> {
                currentSwitchBean = s;
//                        getData(switchBean);
//                txtLineName.setText(String.format("%s%s", SwitchSettingActivity.this.getString(R.string.vs14), currentSwitchBean.getName()));
                mViewModel.setTxtLineName(String.format("%s%s", SwitchSettingActivity.this.getString(R.string.vs14), currentSwitchBean.getName()));
                Objects.requireNonNull(mBinding.tabSetting.getTabAt(0)).select();// 重置为第一个TAB
//                currentParamID = dl1_fragment.getParamID(currentSwitchBean);
                _JDataGet.switchCode = currentSwitchBean.getSerialNumber();
                _JDataGet.paramIDs = dl1_fragment.getParamID(currentSwitchBean);
                mHandler.sendEmptyMessageDelayed(MSG_findSwitchParamBySwitch_FLAG, 100);
            });
            switchTreeDialog.show();
        }

        public void onFlush(View view) {
            mHandler.sendEmptyMessageDelayed(MSG_sendGetThreadValueCommand_FLAG, 100);
        }

        public void onCommit(View view) {
            setValues();
        }
    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
        mHandler.removeMessages(MSG_sendGetThreadValueCommand_FLAG);
        mHandler.removeMessages(MSG_findSwitchParamBySwitch_FLAG);
    }

    private void initFragment() {
        MyFragmentAdapter mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mBinding.vpSetting.setAdapter(mAdapter);
        mBinding.tabSetting.setupWithViewPager(mBinding.vpSetting, true);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {
        //        private Bundle bundle;
        private List<BaseFragment> listFragments = new ArrayList<>();

        @SuppressLint("WrongConstant")
        MyFragmentAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            dl1_fragment = new SettingDL1Fragment().setOnShowingListener(SwitchSettingActivity.this);
            dy_fragment = new SettingDYFragment().setOnShowingListener(SwitchSettingActivity.this);
            dl2_fragment = new SettingDL2Fragment().setOnShowingListener(SwitchSettingActivity.this);
            other_fragment = new SettingOtherFragment().setOnShowingListener(SwitchSettingActivity.this);
            listFragments.add(dl1_fragment);
            listFragments.add(dy_fragment);
            listFragments.add(dl2_fragment);
            listFragments.add(other_fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.statistical_2);
            } else if (position == 1) {
                return getString(R.string.statistical_1);
            } else if (position == 2) {
                return getString(R.string.electric_quantity);
            } else if (position == 3) {
                return getString(R.string.vs255);
            }
            return super.getPageTitle(position);
        }
    }
}
