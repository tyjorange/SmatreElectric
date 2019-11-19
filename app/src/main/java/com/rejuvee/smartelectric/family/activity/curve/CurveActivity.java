package com.rejuvee.smartelectric.family.activity.curve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.greenandroid.wheel.view.WheelDateTime;
import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.google.android.material.tabs.TabLayout;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeDialog;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityCurveBinding;
import com.rejuvee.smartelectric.family.fragment.CurveFragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SignalType;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.viewmodel.CurveViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;

/**
 * 曲线
 */
public class CurveActivity extends BaseActivity implements CurveFragment.OnShowingListener {
    //    private TabLayout tabCurve;
//    private ViewPager vpCurve;
    //    private GridView gvBreaker;
    private FragmentPagerAdapter fpAdapter;
    //    private BaseAdapter gvAdapter;
//    private List<CollectorBean> collectorBeanList;
    private List<SwitchBean> switchBeanList = new ArrayList<>();
    private List<SignalType> signalDayTypeList, signalMonthTypeList;
    private WheelDateTime dateSelector;
    //    private TextView tvDate;
    //    private TextView tvDevice;
    //    private TextView cur_line;
    private SwitchBean currentSwitchBean;
    private SignalType currentSignalType;
    private CollectorBean collectorBean;
//    private CollectorBean currentCollectorBean;
//    private ListView lvCollector;
//    private BaseAdapter lvAdapter;

    private boolean isDay;
    private int iyear;
    private int imonth;
    private int iday;
    private int lastPos;// 切换日期前最后的tab页面
//    private static int[] DATE_UNITS = {R.string.date_unit_year, R.string.date_unit_month, R.string.date_unit_day};

    private LoadingDlg waitDialog;
    private Call<?> currentCall;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_curve;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivityCurveBinding mBinding;
    private CurveViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_curve);
        mViewModel = ViewModelProviders.of(this).get(CurveViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        findViewById(R.id.img_cancel).setOnClickListener(this);
//        findViewById(R.id.img_change).setOnClickListener(this);
        waitDialog = new LoadingDlg(this, -1);

//        cur_line = findViewById(R.id.cur_line);
//        tvDate = findViewById(R.id.tv_date);
//        tvDevice = findViewById(R.id.tv_device);

        TabLayout mTabLayout = mBinding.tabDayMonth;
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.curve_by_day)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.curve_by_month)));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        isDay = true;
                        dateSelector.setDateItemVisiable(true, true, true, false, false);
                        changeDate(0);
                        break;
                    case 1:
                        isDay = false;
                        dateSelector.setDateItemVisiable(true, true, false, false, false);
                        changeDate(0);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        RadioGroup rgDate = (RadioGroup) findViewById(R.id.rg_date);
//        rgDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_date_day:
//                        isDay = true;
//                        dateSelector.setDateItemVisiable(true, false, false);
//                        changeDate(0);
//                        break;
//                    case R.id.rb_date_month:
//                        isDay = false;
//                        dateSelector.setDateItemVisiable(false, false, false);
//                        changeDate(0);
//                        break;
//                }
//            }
//        });


//        vpCurve = mBinding.vpCurve;
//        tabCurve = mBinding.tlCurve;
        mBinding.tlCurve.setupWithViewPager(mBinding.vpCurve);
//        mBinding.tlCurve.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                lastPos = tab.getPosition();
//                System.out.println("lastPos --" + lastPos);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        gvBreaker = (GridView) findViewById(R.id.gv_breaker);
//        initGvAdapter();
//        gvBreaker.setAdapter(gvAdapter);
//        gvBreaker.setOnItemClickListener(new AdapterView.CallBack() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                currentSwitchBean = switchBeanList.get(position);
//                gvAdapter.notifyDataSetChanged();
//                notifyFpAdapter();
//            }
//        });
        initTimeSelect();
        initFpAdapter();
//        currentCollectorBean = getIntent().getParcelableExtra("collectorBean");
//        tvDevice.setText(currentCollectorBean.getDeviceName());
        getBreakers();
    }

    /**
     * 获取断路器
     */
    private void getBreakers() {
        if (collectorBean == null) {
            return;
        }
        waitDialog.show();
        currentCall = Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                waitDialog.dismiss();
                switchBeanList.clear();
                switchBeanList.addAll(data);
                currentSwitchBean = switchBeanList.get(0);
//                cur_line.setText(String.format("%s%s", getString(R.string.vs4), currentSwitchBean.getName()));
                mViewModel.setCurrentSwitchBeanName(String.format("%s%s", getString(R.string.vs4), currentSwitchBean.getName()));
//                gvAdapter.notifyDataSetChanged();
                notifyFpAdapter();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
//                if (errorEvent == 12) {
//                    CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.local_error_message_no_data));
//                } else {
//                    CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.get_data_fail));
//                }
                CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.get_data_fail));

            }
        });
    }

    private void notifyFpAdapter() {
        if (isDay) {
            if (signalDayTypeList != null && signalDayTypeList.size() > 0) {
//                initFpAdapter();
                mBinding.vpCurve.setAdapter(fpAdapter);
                mBinding.vpCurve.setCurrentItem(lastPos);
                fpAdapter.notifyDataSetChanged();
            } else {
                getSignalsType("day");
            }
        } else {
            if (signalMonthTypeList != null && signalMonthTypeList.size() > 0) {
                System.out.println("lastPos  " + lastPos);
//                initFpAdapter();
                mBinding.vpCurve.setAdapter(fpAdapter);
                mBinding.vpCurve.setCurrentItem(lastPos);
                fpAdapter.notifyDataSetChanged();
            } else {
                getSignalsType("month");
            }
        }
    }

    private void getSignalsType(final String type) {
        if (currentSwitchBean == null) {
            return;
        }
        waitDialog.show();
        currentCall = Core.instance(this).getSignalsType(type, new ActionCallbackListener<List<SignalType>>() {
            @Override
            public void onSuccess(List<SignalType> data) {
                waitDialog.dismiss();
                if (type.equals("day")) {
                    signalDayTypeList = new ArrayList<>();
                    signalDayTypeList.addAll(data);
                } else {
                    signalMonthTypeList = new ArrayList<>();
                    signalMonthTypeList.addAll(data);
                }
                notifyFpAdapter();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
//                if (errorEvent == 12) {
////                    CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.local_error_message_no_data));
//                } else {
//                    CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.get_data_fail));
//                }
                CustomToast.showCustomErrorToast(CurveActivity.this, getString(R.string.get_data_fail));
            }
        });
    }

    private Bundle getBundle() {
        if (currentSignalType == null) {
            return null;
        }

        Bundle bundle = new Bundle();
        bundle.putCharSequence("signalTypeId", currentSignalType.getSignalsTypeID());
        bundle.putInt("switchId", currentSwitchBean.getSwitchID());

        String time = iyear + "-" + (imonth < 10 ? "0" + imonth : imonth);
        if (isDay) {
            time = time + "-" + (iday < 10 ? "0" + iday : iday);
        }

        bundle.putCharSequence("time", time);
        bundle.putCharSequence("unit", currentSignalType.getUnit());
        bundle.putBoolean("isDay", isDay);
        bundle.putCharSequence("tag", currentSignalType.getTypeName());

        return bundle;
    }

    private void initTimeSelect() {
        isDay = true;
        changeDate(0);

        dateSelector = new WheelDateTime(CurveActivity.this,
                (isSubmit, year, month, day, hour, minute) -> {
                    Log.e("onWheel", "onWheel: " + year);
                    Log.e("onWheel", "onWheel: " + month);
                    Log.e("onWheel", "onWheel: " + day);
                    iyear = Integer.parseInt(year);
                    imonth = Integer.parseInt(month);
                    if (isDay) {
                        iday = Integer.parseInt(day);
                    }
                    changeTvDate();
                }, true,
                new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()),
                getString(R.string.select_date), getString(R.string.cancel), getString(R.string.ensure));
        dateSelector.setDateItemVisiable(true, true, true, false, false);
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return false;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onDate(View view) {
            dateSelector.show(view);
        }

        public void onMinus(View view) {
            changeDate(-1);
        }

        public void onAdd(View view) {
            changeDate(1);
        }

        public void onChange(View view) {
            //                Intent intent = new Intent(CurveActivity.this, SwitchTreeDialog.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.QUXIAN);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
            SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(view.getContext(), SwitchTree.QUXIAN, collectorBean, s -> {
                //添加后 刷新数据
                currentSwitchBean = s;
                mViewModel.setCurrentSwitchBeanName(currentSwitchBean.getName());
//                gvAdapter.notifyDataSetChanged();
                notifyFpAdapter();
            });
            switchTreeDialog.show();
        }
    }

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }


    private void initFpAdapter() {
        mBinding.tlCurve.setTabMode(isDay ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        fpAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                Log.i("VpAdapter", "getItem: " + position);
                List<SignalType> signalTypeList = isDay ? signalDayTypeList : signalMonthTypeList;
                CurveFragment curveFragment = new CurveFragment();
                curveFragment.setPosition(position);
                curveFragment.setOnShowingListener(CurveActivity.this);
                if (getCount() > 0) {
                    currentSignalType = signalTypeList.get(position);
                    curveFragment.setArguments(getBundle());
                }
                return curveFragment;
            }

            @Override
            public int getCount() {
                List<SignalType> signalTypeList = isDay ? signalDayTypeList : signalMonthTypeList;
                return signalTypeList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                String title = "";
                if (getCount() <= 0) {
                    return title;
                }
                List<SignalType> signalTypeList = isDay ? signalDayTypeList : signalMonthTypeList;
                currentSignalType = signalTypeList.get(position);
                title = getResources().getString(currentSignalType.getTabTitle());
                return title;
            }
        };
    }

    private void changeDate(int c) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        lastPos = mBinding.tlCurve.getSelectedTabPosition();
        boolean canChange = true;
        switch (c) {
            case 1:// +1
                if (isDay) {
                    if (iyear == year && imonth == month && iday == day) {
                        canChange = false;//不能超过当天
                    }
                } else {
                    if (iyear == year && imonth == month) {
                        canChange = false;//不能超过当月
                    }
                }
//                break;
            case -1:// -1
                if (canChange) {
                    calendar.set(iyear, imonth - 1, iday);
                    if (isDay) {
                        calendar.add(Calendar.DAY_OF_MONTH, c);
                    } else {
                        calendar.add(Calendar.MONTH, c);
                    }
                    imonth = calendar.get(Calendar.MONTH) + 1;
                    iday = calendar.get(Calendar.DATE);
                    iyear = calendar.get(Calendar.YEAR);
                    changeTvDate();
                }
                break;
            case 0:
                iyear = year;
                imonth = month;
                iday = day;
                changeTvDate();
                break;
        }
    }

    private Calendar today = Calendar.getInstance();

    private void changeTvDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, imonth - 1);
        calendar.set(Calendar.YEAR, iyear);
        calendar.set(Calendar.DATE, iday);
        SimpleDateFormat dateFormat;
        String date;
        if (isDay) {
            dateFormat = new SimpleDateFormat(getString(R.string.daily_date_format), Locale.getDefault());
            date = dateFormat.format(calendar.getTime());
            //date = imonth + getResources().getString(DATE_UNITS[1]) + iday + getResources().getString(DATE_UNITS[2]);
        } else {
            dateFormat = new SimpleDateFormat(getString(R.string.monthly_date_format), Locale.getDefault());
            date = dateFormat.format(calendar.getTime());
            //date = iyear + getResources().getString(DATE_UNITS[0]) + imonth + getResources().getString(DATE_UNITS[1]);
        }
        mViewModel.setCurrentDate(date);
//        tvDate.setText(date);
        notifyFpAdapter();
    }

//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.tv_date:
//                dateSelector.show(v);
//                break;
//            case R.id.iv_minus:
//                changeDate(-1);
//                break;
//            case R.id.iv_add:
//                changeDate(1);
////                break;
////            case R.id.iv_device:
//                //dialog.show();
//                break;
//            case R.id.img_change://改变线路
////                Intent intent = new Intent(CurveActivity.this, SwitchTreeDialog.class);
////                intent.putExtra("collectorBean", collectorBean);
////                intent.putExtra("viewType", SwitchTree.QUXIAN);
////                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
//                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.QUXIAN, collectorBean, s -> {
//                    //添加后 刷新数据
//                    currentSwitchBean = s;
//                    mViewModel.setCurrentSwitchBeanName(currentSwitchBean.getName());
////                gvAdapter.notifyDataSetChanged();
//                    notifyFpAdapter();
//                });
//                switchTreeDialog.show();
//                break;
////            case R.id.tv_close:
////                //dialog.dismiss();
////                break;
//        }
////        super.onClick(v);
//    }

    @Override
    public void onShowing(CurveFragment curveFragment) {
        Log.e("VpAdapter", "onShowing");
        List<SignalType> signalTypeList = isDay ? signalDayTypeList : signalMonthTypeList;
        currentSignalType = signalTypeList.get(curveFragment.getPosition());
        curveFragment.change(getBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_CHOSE_LINE) {//添加后 刷新数据 Deprecated
                currentSwitchBean = data.getParcelableExtra("switchBean");
                mViewModel.setCurrentSwitchBeanName(Objects.requireNonNull(currentSwitchBean).getName());
//                gvAdapter.notifyDataSetChanged();
                notifyFpAdapter();
            }
        }
    }
}
