package com.rejuvee.smartelectric.family.activity.energy;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.greenandroid.wheel.view.WheelDateTime;
import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.google.android.material.tabs.TabLayout;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.SimpleTreeAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchStatementBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 我的电表
 * <p>
 * 报表统计
 */
public class StatementActivity extends BaseActivity implements View.OnClickListener {
    //    private RadioGroup rgDate;
    private Context mContext;
    private ListView lvStatement;
    private SimpleTreeAdapter mAdapter;
    private List<SwitchStatementBean> switchStatementBeanList = new ArrayList<>();
    private WheelDateTime dateSelector;
    private WheelDateTime dateSelectorSS;
    private WheelDateTime dateSelectorEE;
    private TextView tvDate;
    private TextView tvDateSS;
    private TextView tvDateEE;
    private int selectType;// 0按日 1按月 2按时段
    private TextView tvEQuantity;
    private TextView tvECharge;
    private int iyear;
    private int imonth;
    private int iday;
    private int ihour;
    //    private List<CollectorBean> collectorBeanList;
    private CollectorBean currentCollectorBean;
    private TabLayout mTabLayout;
    //    private ListView lvCollector;
//    private BaseAdapter lvAdapter;
    //    private TextView tvDevice;
    private LoadingDlg waitDialog;

//    private static int[] DATE_UNITS = {R.string.date_unit_year, R.string.date_unit_month, R.string.date_unit_day};

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_statement;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        waitDialog = new LoadingDlg(this, -1);
        NativeLine.init(this);
        ImageView img_cancel = findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(this);
        lvStatement = findViewById(R.id.lv_statement);
        tvDate = findViewById(R.id.tv_date);
        tvDateSS = findViewById(R.id.tv_date_ss);
        tvDateEE = findViewById(R.id.tv_date_ee);
        tvEQuantity = findViewById(R.id.tv_electric_quantity);
        tvECharge = findViewById(R.id.tv_electric_charge);

        mTabLayout = findViewById(R.id.tab_day_month);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.curve_by_day)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.curve_by_month)));
        mTabLayout.addTab(mTabLayout.newTab().setText("时段"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        selectType = 0;
                        dateSelector.setDateItemVisiable(true, true, true, false, false);
                        changeDate(0);
                        findViewById(R.id.day_month).setVisibility(View.VISIBLE);
                        findViewById(R.id.hour_hour).setVisibility(View.GONE);
                        break;
                    case 1:
                        selectType = 1;
                        dateSelector.setDateItemVisiable(true, true, false, false, false);
                        changeDate(0);
                        findViewById(R.id.day_month).setVisibility(View.VISIBLE);
                        findViewById(R.id.hour_hour).setVisibility(View.GONE);
                        break;
                    case 2:
                        selectType = 2;
                        changeTextViewSS();
                        changeTextViewEE();
                        getBreakerStatement();
                        findViewById(R.id.hour_hour).setVisibility(View.VISIBLE);
                        findViewById(R.id.day_month).setVisibility(View.GONE);
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
//        rgDate = (RadioGroup) findViewById(R.id.rg_date);
//        rgDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_date_day:
//                        isDay = true;
//                        dateSelector.setDateItemVisiable(true, false, false);
//                        changeDate(0);
//                        getBreakerStatement();
//                        break;
//                    case R.id.rb_date_month:
//                        isDay = false;
//                        dateSelector.setDateItemVisiable(false, false, false);
//                        changeDate(0);
//                        getBreakerStatement();
//                        break;
//                }
//            }
//        });
//        tvDevice = (TextView) findViewById(R.id.tv_device);
        //initDialog();

//        initAdapter();
        mAdapter = new SimpleTreeAdapter<>(lvStatement, this, switchStatementBeanList, 0);
        lvStatement.setAdapter(mAdapter);
        findViewById(R.id.img_price).setOnClickListener(v -> startActivity(new Intent(StatementActivity.this, TimePriceActivity.class)));
    }

    private void initTimeSelect() {
//        changeDate(0);
        dateSelector = new WheelDateTime(StatementActivity.this,
                new WheelDateTime.OnWheelListener() {

                    @Override
                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
                        iyear = Integer.parseInt(year);
                        imonth = Integer.parseInt(month);
                        if (selectType == 0) {
                            iday = Integer.parseInt(day);
                        }
                        changeTextView();
                        getBreakerStatement();
                    }
                }, true,
                new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()),
                getString(R.string.select_date), getString(R.string.cancel), getString(R.string.ensure));
        dateSelector.setDateItemVisiable(true, true, true, false, false);
        dateSelectorSS = new WheelDateTime(StatementActivity.this,
                new WheelDateTime.OnWheelListener() {

                    @Override
                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
                        iyear = Integer.parseInt(year);
                        imonth = Integer.parseInt(month);
                        iday = Integer.parseInt(day);
                        ihour = Integer.parseInt(hour);
                        changeTextViewSS();
                        getBreakerStatement();
                    }
                }, true,
                new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()),
                "起始时间", getString(R.string.cancel), getString(R.string.ensure));
        dateSelectorSS.setDateItemVisiable(true, true, true, true, false);
        dateSelectorEE = new WheelDateTime(StatementActivity.this,
                new WheelDateTime.OnWheelListener() {

                    @Override
                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
                        iyear = Integer.parseInt(year);
                        imonth = Integer.parseInt(month);
                        iday = Integer.parseInt(day);
                        ihour = Integer.parseInt(hour);
                        changeTextViewEE();
                        getBreakerStatement();
                    }
                }, true,
                new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()),
                new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()),
                "结束时间", getString(R.string.cancel), getString(R.string.ensure));
        dateSelectorEE.setDateItemVisiable(true, true, true, true, false);
    }

    /**
     * 日期+1 或-1
     *
     * @param c
     */
    private void changeDate(int c) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        boolean canChange = true;
        switch (c) {
            case 1:
                if (selectType == 0) {
                    if (iyear == year && imonth == month && iday == day) {
                        canChange = false;
                    }
                } else if (selectType == 1) {
                    if (iyear == year && imonth == month) {
                        canChange = false;
                    }
                }
            case -1:
                if (canChange) {
                    calendar.set(iyear, imonth - 1, iday);
                    if (selectType == 0) {
                        calendar.add(Calendar.DAY_OF_MONTH, c);
                    } else if (selectType == 1) {
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
        changeTextView();
        getBreakerStatement();
    }

    /**
     * 按日按月
     */
    private void changeTextView() {
//        getBreakerStatement();
        String date = "";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, imonth - 1);
        calendar.set(Calendar.YEAR, iyear);
        calendar.set(Calendar.DATE, iday);
        SimpleDateFormat dateFormat;
//        Locale locale = Locale.getDefault();
//        if (LanguageUtil.getLangType(this) == 1) {
//            locale = Locale.ENGLISH;
//        }
        if (selectType == 0) {
            dateFormat = new SimpleDateFormat(getString(R.string.daily_date_format), Locale.getDefault());
            date = dateFormat.format(calendar.getTime());
            //date = imonth + getResources().getString(DATE_UNITS[1]) + iday + getResources().getString(DATE_UNITS[2]);
        } else if (selectType == 1) {
            dateFormat = new SimpleDateFormat(getString(R.string.monthly_date_format), Locale.getDefault());
            date = dateFormat.format(calendar.getTime());
            //date = iyear + getResources().getString(DATE_UNITS[0]) + imonth + getResources().getString(DATE_UNITS[1]);
        }
        tvDate.setText(date);
    }

    /**
     * 按时段
     */
    private void changeTextViewSS() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, imonth - 1);
        calendar.set(Calendar.YEAR, iyear);
        calendar.set(Calendar.DATE, iday);
        calendar.set(Calendar.HOUR_OF_DAY, ihour);
        calendar.add(Calendar.MONTH, -1);// 起始时间设为上个月
        // 改变显示
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());
        tvDateSS.setText(date);
        // 准备要提交的值
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault());
        resStart = dateFormat2.format(calendar.getTime());
    }

    private String resStart;

    /**
     * 按时段
     */
    private void changeTextViewEE() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, imonth - 1);
        calendar.set(Calendar.YEAR, iyear);
        calendar.set(Calendar.DATE, iday);
        calendar.set(Calendar.HOUR_OF_DAY, ihour);
        // 改变显示
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());
        tvDateEE.setText(date);
        // 准备要提交的值
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault());
        resEnd = dateFormat2.format(calendar.getTime());
    }

    private String resEnd;
//    private void getCollector() {
//        Core.instance(this).getCollector(utils.USER_KRY, new ActionCallbackListener<List<CollectorBean>>() {
//            @Override
//            public void onSuccess(List<CollectorBean> data) {
//                collectorBeanList = new ArrayList<>();
//                collectorBeanList.addAll(data);
//                currentCollectorBean = collectorBeanList.get(0);
//                tvDevice.setText(currentCollectorBean.getDeviceName());
//                initLvAdapter();
//                lvCollector.setAdapter(lvAdapter);
//                getBreakerStatement();
//            }
//
//            @Override
//            public void onFailure(int errorEvent, String message) {
//
//                if (message.contains(getResources().getString(R.string.server_error_message_no_result))) {
//                    message = getString(R.string.local_error_message_no_data);
//                }
//                CustomToast.showCustomToast(StatementActivity.this, message);
//            }
//        });
//    }

//    private Dialog dialog;
//
//    private void initDialog() {
//        View contentView = View.inflate(this, R.layout.view_dialog_collector, null);
//        lvCollector = (ListView) contentView.findViewById(R.id.lv_collector);
//        lvCollector.setAdapter(lvAdapter);
//        lvCollector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                dialog.dismiss();
//                currentCollectorBean = collectorBeanList.get(position);
//                tvDevice.setText(currentCollectorBean.getDeviceName());
//                getBreakerStatement();
//            }
//        });
//        dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(contentView);
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        WindowManager.LayoutParams lp = window.getAttributes(); // 获取对话框当前的参数值
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//        window.setAttributes(lp);
//        dialog.setCanceledOnTouchOutside(true);
//    }

    /**
     * 根据selectType调用不同接口
     */
    private void getBreakerStatement() {
        if (currentCollectorBean == null) {
            return;
        }
        String time = iyear + "-" + (imonth < 10 ? "0" + imonth : imonth);
        if (selectType == 0) {
            time = time + "-" + (iday < 10 ? "0" + iday : iday);
            getTotalPowerByDay(time);
        } else if (selectType == 1) {
            getTotalPowerByMonth(time);
        } else if (selectType == 2) {
            getTotalPowerByTime(resStart, resEnd);
        }
    }

    private void changeTotal() {
        double totalQuantity = 0;
        double totalCharge = 0;

        // 计算电量电费和
        for (SwitchStatementBean switchStatementBean : switchStatementBeanList) {
            if (switchStatementBean.getPid() == 0) {// 只计算根节点
                totalQuantity += switchStatementBean.getShowValue();
                totalCharge += switchStatementBean.getShowPrice();
            }
        }

        totalQuantity = new BigDecimal(totalQuantity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        totalCharge = new BigDecimal(totalCharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
       /* String gonglv = String.format(getResources().getString(R.string.cur_gonglv),
                "<font color='#0d77ff'>", item.getValue(), "</font>");*/
        tvECharge.setText(Html.fromHtml(String.valueOf(totalCharge)));
        tvEQuantity.setText(Html.fromHtml(String.valueOf(totalQuantity)));
    }

    private void getTotalPowerByDay(String time) {
        if (currentCollectorBean == null) {
            return;
        }
        waitDialog.show();

        Core.instance(this).getTotalPowerByDay("nohierarchy", currentCollectorBean.getCode(), time, new ActionCallbackListener<List<SwitchStatementBean>>() {
            @Override
            public void onSuccess(List<SwitchStatementBean> data) {
                waitDialog.dismiss();
                switchStatementBeanList.clear();
                switchStatementBeanList.addAll(data);
//                mAdapter.notifyDataSetChanged();
                mAdapter = new SimpleTreeAdapter<>(lvStatement, mContext, switchStatementBeanList, 0);
                lvStatement.setAdapter(mAdapter);
                changeTotal();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                if (switchStatementBeanList != null) {
                    switchStatementBeanList.clear();
//                    mAdapter.notifyDataSetChanged();
                    mAdapter = new SimpleTreeAdapter<>(lvStatement, mContext, switchStatementBeanList, 0);
                    lvStatement.setAdapter(mAdapter);
                }

                if (errorEvent == 12) {
//                    message = getString(R.string.local_error_message_no_data);
                    CustomToast.showCustomErrorToast(StatementActivity.this, message);
                } else {
//                    message = getString(R.string.get_data_fail);
                    CustomToast.showCustomErrorToast(StatementActivity.this, message);
                }

            }
        });
    }

    private void getTotalPowerByMonth(String time) {
        if (currentCollectorBean == null) {
            return;
        }
        waitDialog.show();
        Core.instance(this).getTotalPowerByMonth("nohierarchy", currentCollectorBean.getCode(), time, new ActionCallbackListener<List<SwitchStatementBean>>() {
            @Override
            public void onSuccess(List<SwitchStatementBean> data) {
                waitDialog.dismiss();
                switchStatementBeanList.clear();
                switchStatementBeanList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mAdapter = new SimpleTreeAdapter<>(lvStatement, mContext, switchStatementBeanList, 0);
                lvStatement.setAdapter(mAdapter);
                changeTotal();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                if (switchStatementBeanList != null) {
                    switchStatementBeanList.clear();
                    mAdapter.notifyDataSetChanged();
                    mAdapter = new SimpleTreeAdapter<>(lvStatement, mContext, switchStatementBeanList, 0);
                    lvStatement.setAdapter(mAdapter);
                }
//                if (errorEvent == 12) {
////                    message = getString(R.string.local_error_message_no_data);
//                } else {
////                    message = getString(R.string.get_data_fail);
//                }
                CustomToast.showCustomErrorToast(StatementActivity.this, message);
            }
        });
    }

    private void getTotalPowerByTime(String startStr, String endStr) {
        if (currentCollectorBean == null) {
            return;
        }
        waitDialog.show();
        Core.instance(this).getTotalPowerByTime("nohierarchy", currentCollectorBean.getCode(), startStr, endStr, new ActionCallbackListener<List<SwitchStatementBean>>() {
            @Override
            public void onSuccess(List<SwitchStatementBean> data) {
                waitDialog.dismiss();
                switchStatementBeanList.clear();
                switchStatementBeanList.addAll(data);
                mAdapter.notifyDataSetChanged();
                mAdapter = new SimpleTreeAdapter<>(lvStatement, mContext, switchStatementBeanList, 0);
                lvStatement.setAdapter(mAdapter);
                changeTotal();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                if (switchStatementBeanList != null) {
                    switchStatementBeanList.clear();
                    mAdapter.notifyDataSetChanged();
                    mAdapter = new SimpleTreeAdapter<>(lvStatement, mContext, switchStatementBeanList, 0);
                    lvStatement.setAdapter(mAdapter);
                }
//                if (errorEvent == 12) {
////                    message = getString(R.string.local_error_message_no_data);
//                } else {
////                    message = getString(R.string.get_data_fail);
//                }
                CustomToast.showCustomErrorToast(StatementActivity.this, message);
            }
        });
    }

    @Override
    protected void initData() {
        switchStatementBeanList = new ArrayList<>();
        currentCollectorBean = getIntent().getParcelableExtra("collectorBean");
//        tvDevice.setText(currentCollectorBean.getDeviceName());
        initTimeSelect();
        getBreakerStatement();
        changeTotal();
        //getCollector();
    }

//    private void initAdapter() {
////        switchStatementBeanList = new ArrayList<>();
//        adapter = new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return switchStatementBeanList.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return switchStatementBeanList.get(position);
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
//                SwitchStatementBean switchStatementBean = switchStatementBeanList.get(position);
//
//                ViewHolder viewHolder;
//                if (convertView == null) {
//                    viewHolder = new ViewHolder();
//                    convertView = View.inflate(StatementActivity.this, R.layout.item_lv_statement, null);
//                    viewHolder.tvName = convertView.findViewById(R.id.tv_name);
//                    viewHolder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
//                    viewHolder.tvCharge = convertView.findViewById(R.id.tv_charge);
//                    viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
//
//                    convertView.setTag(viewHolder);
//                } else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
//                viewHolder.ivPicture.setImageResource(NativeLine.LinePictures[switchStatementBean.getIconType() % NativeLine.LinePictures.length]);
//                viewHolder.tvName.setText(switchStatementBean.getName());
//                viewHolder.tvQuantity.setText(String.valueOf(switchStatementBean.getShowValue()));
//                viewHolder.tvCharge.setText(String.valueOf(switchStatementBean.getShowPrice() + ""));
//
//                return convertView;
//            }
//
//            class ViewHolder {
//                TextView tvName;
//                TextView tvQuantity;
//                TextView tvCharge;
//                ImageView ivPicture;
//            }
//        };
//    }

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

    }

//    private void initLvAdapter() {
//
//        lvAdapter = new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return collectorBeanList.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return collectorBeanList.get(position);
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
//                CollectorBean collectDevice = collectorBeanList.get(position);
//
//                ViewHolder viewHolder;
//                if (convertView == null) {
//                    convertView = View.inflate(StatementActivity.this, R.layout.item_lv_collector, null);
//                    viewHolder = new ViewHolder();
//                    viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
//                    viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//                    convertView.setTag(viewHolder);
//
//                } else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
//                viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
//                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//
//                if (collectDevice.equals(currentCollectorBean)) {
//                    viewHolder.ivSelected.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.ivSelected.setVisibility(View.GONE);
//                }
//
//                viewHolder.tvName.setText(collectDevice.getDeviceName());
//                return convertView;
//            }
//
//            class ViewHolder {
//                ImageView ivSelected;
//                TextView tvName;
//            }
//        };
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                finish();
                break;
            case R.id.iv_add:
                changeDate(1);
                break;
            case R.id.iv_minus:
                changeDate(-1);
                break;
            case R.id.tv_date:
                dateSelector.show(v);
                break;
            case R.id.tv_date_ss:
                dateSelectorSS.show(v);
                break;
            case R.id.tv_date_ee:
                dateSelectorEE.show(v);
                break;
        }
    }
}
