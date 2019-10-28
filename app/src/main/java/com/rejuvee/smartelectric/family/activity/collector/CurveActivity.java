package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.base.frame.greenandroid.wheel.view.WheelDateTime;
import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.LanguageUtil;
import com.base.library.widget.CustomToast;
import com.google.android.material.tabs.TabLayout;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeDialog;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.fragment.CurveFragment;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SignalType;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;


/**
 * 曲线
 */
public class CurveActivity extends BaseActivity implements CurveFragment.OnShowingListener, View.OnClickListener {
    private TabLayout tlCurve;
    private ViewPager vpCurve;
    //    private GridView gvBreaker;
    private FragmentPagerAdapter fpAdapter;
    //    private BaseAdapter gvAdapter;
//    private List<CollectorBean> collectorBeanList;
    private List<SwitchBean> switchBeanList = new ArrayList<>();
    private List<SignalType> signalDayTypeList, signalMonthTypeList;
    private WheelDateTime dateSelector;
    private TextView tvDate;
    private TextView tvDevice;
    private TextView cur_line;
    private SwitchBean currentSwitchBean;
    private CollectorBean currentCollectorBean;
    private SignalType currentSignalType;
    private CollectorBean collectorBean;
//    private ListView lvCollector;
//    private BaseAdapter lvAdapter;

    private boolean isDay;
    private int iyear;
    private int imonth;
    private int iday;
//    private static int[] DATE_UNITS = {R.string.date_unit_year, R.string.date_unit_month, R.string.date_unit_day};

    private LoadingDlg waitDialog;
    private Call<?> currentCall;
    private TabLayout mTabLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_curve;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        findViewById(R.id.img_cancel).setOnClickListener(this);
        findViewById(R.id.img_change).setOnClickListener(this);
        waitDialog = new LoadingDlg(this, -1);

        cur_line = findViewById(R.id.cur_line);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvDevice = (TextView) findViewById(R.id.tv_device);

        mTabLayout = (TabLayout) findViewById(R.id.tab_day_month);
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

        vpCurve = (ViewPager) findViewById(R.id.vp_curve);
        tlCurve = (TabLayout) findViewById(R.id.tl_curve);
        tlCurve.setupWithViewPager(vpCurve);

//        gvBreaker = (GridView) findViewById(R.id.gv_breaker);
//        initGvAdapter();
//        gvBreaker.setAdapter(gvAdapter);
//        gvBreaker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                currentSwitchBean = switchBeanList.get(position);
//                gvAdapter.notifyDataSetChanged();
//                notifyFpAdapter();
//            }
//        });

    }

    @Override
    protected void initData() {
        initTimeSelect();
        currentCollectorBean = getIntent().getParcelableExtra("collectorBean");
        tvDevice.setText(currentCollectorBean.getDeviceName());
        getBreakers();
    }

    /**
     * 获取断路器
     */
    private void getBreakers() {
        if (currentCollectorBean == null) {
            return;
        }
        waitDialog.show();
        currentCall = Core.instance(this).getSwitchByCollector(currentCollectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                waitDialog.dismiss();
                switchBeanList.clear();
                switchBeanList.addAll(data);
                currentSwitchBean = switchBeanList.get(0);
                cur_line.setText(String.format("%s%s", getString(R.string.vs4), currentSwitchBean.getName()));
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
                initVpAdapter();
                vpCurve.setAdapter(fpAdapter);
                fpAdapter.notifyDataSetChanged();
            } else {
                signalDayTypeList = new ArrayList<>();
                getSignalsType("day");
            }
        } else {
            if (signalMonthTypeList != null && signalMonthTypeList.size() > 0) {
                initVpAdapter();
                vpCurve.setAdapter(fpAdapter);
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
        bundle.putCharSequence("switchId", currentSwitchBean.getSwitchID());

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
                new WheelDateTime.OnWheelListener() {

                    @Override
                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
                        Log.e("onWheel", "onWheel: " + year);
                        Log.e("onWheel", "onWheel: " + month);
                        Log.e("onWheel", "onWheel: " + day);
                        iyear = Integer.parseInt(year);
                        imonth = Integer.parseInt(month);
                        if (isDay) {
                            iday = Integer.parseInt(day);
                        }
                        changeTvDate();
                    }
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

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
//        System.gc();
    }


    private void initVpAdapter() {
        tlCurve.setTabMode(isDay ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        fpAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                Log.e("VpAdapter", "getItem: " + position);
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

//    private void initGvAdapter() {
//        switchBeanList = new ArrayList<>();
//
//        gvAdapter = new BaseAdapter() {
//
//            @Override
//            public int getCount() {
//                return switchBeanList.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return switchBeanList.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                SwitchBean switchBean = switchBeanList.get(position);
//
//                ViewHolder viewHolder;
//                if (convertView == null) {
//                    viewHolder = new ViewHolder();
//                    convertView = View.inflate(CurveActivity.this, R.layout.item_gv_breaker, null);
//                    viewHolder.ivPicture = (ImageView) convertView.findViewById(R.id.iv_picture);
//                    viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//                    viewHolder.llBreaker = (LinearLayout) convertView.findViewById(R.id.ll_breaker);
//                    convertView.setTag(viewHolder);
//                } else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
//
//                viewHolder.ivPicture.setImageResource(switchBean.getIcon());
//                viewHolder.tvName.setText(switchBean.getName());
//                if (switchBean.equals(currentSwitchBean)) {
//                    viewHolder.llBreaker.setBackgroundResource(R.drawable.fuxuankuang);
//                } else {
//                    viewHolder.llBreaker.setBackgroundResource(R.drawable.weixuanzhong);
//                }
//                return convertView;
//            }
//
//            class ViewHolder {
//                ImageView ivPicture;
//                TextView tvName;
//                LinearLayout llBreaker;
//            }
//        };
//
//    }

    private void changeDate(int c) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        boolean canChange = true;
        switch (c) {
            case 1:
                if (isDay) {
                    if (iyear == year && imonth == month && iday == day) {
                        canChange = false;
                    }
                } else {
                    if (iyear == year && imonth == month) {
                        canChange = false;
                    }
                }
            case -1:
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
                }
                break;
            case 0:
                iyear = year;
                imonth = month;
                iday = day;
                break;
        }
        changeTvDate();
    }

    private void changeTvDate() {
        notifyFpAdapter();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, imonth - 1);
        calendar.set(Calendar.YEAR, iyear);
        calendar.set(Calendar.DATE, iday);
        SimpleDateFormat dateFormat;
        Locale locale = Locale.getDefault();
        if (LanguageUtil.getLangType(this) == 1) {
            locale = Locale.ENGLISH;
        }
        String date;
        if (isDay) {
            dateFormat = new SimpleDateFormat(getString(R.string.daily_date_format), locale);
            date = dateFormat.format(calendar.getTime());
            //date = imonth + getResources().getString(DATE_UNITS[1]) + iday + getResources().getString(DATE_UNITS[2]);
        } else {
            dateFormat = new SimpleDateFormat(getString(R.string.monthly_date_format), locale);
            date = dateFormat.format(calendar.getTime());
            //date = iyear + getResources().getString(DATE_UNITS[0]) + imonth + getResources().getString(DATE_UNITS[1]);
        }
        tvDate.setText(date);
//        tvDate.setText(date);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_cancel:
                finish();
                break;
            case R.id.tv_date:
                dateSelector.show(v);
                break;
            case R.id.iv_minus:
                changeDate(-1);
                break;
            case R.id.iv_add:
                changeDate(1);
                break;
            case R.id.iv_device:
                //dialog.show();
                break;
            case R.id.img_change://改变线路
//                Intent intent = new Intent(CurveActivity.this, SwitchTreeDialog.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.QUXIAN);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.QUXIAN, collectorBean, new SwitchTreeDialog.ChoseCallBack() {

                    @Override
                    public void onChose(SwitchBean s) {
                        //添加后 刷新数据
                        currentSwitchBean = s;
                        cur_line.setText(String.format("%s%s", getString(R.string.vs4), currentSwitchBean.getName()));
//                gvAdapter.notifyDataSetChanged();
                        notifyFpAdapter();
                    }
                });
                switchTreeDialog.show();
                break;
//            case R.id.tv_close:
//                //dialog.dismiss();
//                break;
        }
//        super.onClick(v);
    }

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
                cur_line.setText(String.format("%s%s", getString(R.string.vs4), currentSwitchBean.getName()));
//                gvAdapter.notifyDataSetChanged();
                notifyFpAdapter();
            }
        }
    }
}
