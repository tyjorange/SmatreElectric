package com.rejuvee.smartelectric.family.activity.energy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.frame.greenandroid.wheel.view.WheelDateTime;
import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.LanguageUtil;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchStatement;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;

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
    private ListView lvStatement;
    private BaseAdapter adapter;
    private List<SwitchStatement> switchStatementList;
    private WheelDateTime dateSelector;
    private TextView tvDate;
    private boolean isDay;
    private TextView tvEQuantity;
    private TextView tvECharge;
    private int iyear;
    private int imonth;
    private int iday;
    private List<CollectorBean> collectorBeanList;
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
        waitDialog = new LoadingDlg(this, -1);
        NativeLine.init(this);
        ImageView img_cancel = (ImageView) findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(this);
        lvStatement = (ListView) findViewById(R.id.lv_statement);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvEQuantity = (TextView) findViewById(R.id.tv_electric_quantity);
        tvECharge = (TextView) findViewById(R.id.tv_electric_charge);

        mTabLayout = (TabLayout) findViewById(R.id.tab_day_month);
        mTabLayout.addTab(mTabLayout.newTab().setText("按日"));
        mTabLayout.addTab(mTabLayout.newTab().setText("按月"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        isDay = true;
                        dateSelector.setDateItemVisiable(true, false, false);
                        changeDate(0);
                        getBreakerStatement();
                        break;
                    case 1:
                        isDay = false;
                        dateSelector.setDateItemVisiable(false, false, false);
                        changeDate(0);
                        getBreakerStatement();
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

        initAdapter();
        lvStatement.setAdapter(adapter);
        findViewById(R.id.img_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatementActivity.this, CostCalculationActivity.class));
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void initTimeSelect() {
        isDay = true;
        changeDate(0);

        dateSelector = new WheelDateTime(StatementActivity.this,
                new WheelDateTime.OnWheelListener() {

                    @Override
                    public void onWheel(Boolean isSubmit, String year, String month, String day, String hour, String minute) {
                        iyear = Integer.parseInt(year);
                        imonth = Integer.parseInt(month);
                        if (isDay) {
                            iday = Integer.parseInt(day);
                        }
                        changeTvDate();
                    }
                }, true,
                new SimpleDateFormat("yyyy").format(new Date()),
                new SimpleDateFormat("MM").format(new Date()),
                new SimpleDateFormat("dd").format(new Date()),
                getString(R.string.select_date), getString(R.string.cancel), getString(R.string.ensure));
        dateSelector.setDateItemVisiable(true, false, false);
    }

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
        getBreakerStatement();
        String date;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, imonth - 1);
        calendar.set(Calendar.YEAR, iyear);
        calendar.set(Calendar.DATE, iday);
        SimpleDateFormat dateFormat;
        Locale locale = Locale.getDefault();
        if (LanguageUtil.getLangType(this) == 1) {
            locale = Locale.ENGLISH;
        }
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
    }

   /* private void getCollector() {
        Core.instance(this).getCollector(utils.USER_KRY, new ActionCallbackListener<List<CollectorBean>>() {
            @Override
            public void onSuccess(List<CollectorBean> data) {
                collectorBeanList = new ArrayList<>();
                collectorBeanList.addAll(data);
                currentCollectorBean = collectorBeanList.get(0);
                tvDevice.setText(currentCollectorBean.getDeviceName());
                initLvAdapter();
                lvCollector.setAdapter(lvAdapter);
                getBreakerStatement();
            }

            @Override
            public void onFailure(int errorEvent, String message) {

                if (message.contains(getResources().getString(R.string.server_error_message_no_result))) {
                    message = getString(R.string.local_error_message_no_data);
                }
                CustomToast.showCustomToast(StatementActivity.this, message);
            }
        });
    }*/

    /*private Dialog dialog;

    private void initDialog() {
        View contentView = View.inflate(this, R.layout.view_dialog_collector, null);
        lvCollector = (ListView) contentView.findViewById(R.id.lv_collector);
        lvCollector.setAdapter(lvAdapter);
        lvCollector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                currentCollectorBean = collectorBeanList.get(position);
                tvDevice.setText(currentCollectorBean.getDeviceName());
                getBreakerStatement();
            }
        });
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WindowManager.LayoutParams lp = window.getAttributes(); // 获取对话框当前的参数值  
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度  
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度  
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
    }*/

    private void getBreakerStatement() {

        if (currentCollectorBean == null) {
            return;
        }
        String time = iyear + "-" + (imonth < 10 ? "0" + imonth : imonth);
        if (isDay) {
            time = time + "-" + (iday < 10 ? "0" + iday : iday);
        }
        if (isDay) {
            getTotalPowerByDay(time);
        } else {
            getTotalPowerByMonth(time);
        }


    }

    private void changeTotal() {
        double totalQuantity = 0;
        double totalCharge = 0;

        for (SwitchStatement switchStatement : switchStatementList) {
            totalQuantity += switchStatement.getShowValue();
            totalCharge += switchStatement.getShowPrice();
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

        Core.instance(this).getTotalPowerByDay(currentCollectorBean.getCode(), time, new ActionCallbackListener<List<SwitchStatement>>() {
            @Override
            public void onSuccess(List<SwitchStatement> data) {
                waitDialog.dismiss();
                switchStatementList.clear();
                switchStatementList.addAll(data);
                adapter.notifyDataSetChanged();
                changeTotal();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                if (switchStatementList != null) {
                    switchStatementList.clear();
                    adapter.notifyDataSetChanged();
                }

                if (errorEvent == 12) {
                    message = getString(R.string.local_error_message_no_data);
                    CustomToast.showCustomErrorToast(StatementActivity.this, message);
                } else {
                    message = getString(R.string.get_data_fail);
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
        Core.instance(this).getTotalPowerByMonth(currentCollectorBean.getCode(), time, new ActionCallbackListener<List<SwitchStatement>>() {
            @Override
            public void onSuccess(List<SwitchStatement> data) {
                waitDialog.dismiss();
                switchStatementList.clear();
                switchStatementList.addAll(data);
                adapter.notifyDataSetChanged();
                changeTotal();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                if (switchStatementList != null) {
                    switchStatementList.clear();
                    adapter.notifyDataSetChanged();
                }

                if (errorEvent == 12) {
                    message = getString(R.string.local_error_message_no_data);
                } else {
                    message = getString(R.string.get_data_fail);
                }
                CustomToast.showCustomErrorToast(StatementActivity.this, message);

            }
        });
    }


    @Override
    protected void initData() {
        switchStatementList = new ArrayList<>();
        initTimeSelect();
        currentCollectorBean = getIntent().getParcelableExtra("collectorBean");
//        tvDevice.setText(currentCollectorBean.getDeviceName());
        getBreakerStatement();
        changeTotal();
        //getCollector();
    }

    private void initAdapter() {
        switchStatementList = new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return switchStatementList.size();
            }

            @Override
            public Object getItem(int position) {
                return switchStatementList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                SwitchStatement switchStatement = switchStatementList.get(position);

                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = View.inflate(StatementActivity.this, R.layout.item_lv_statement, null);
                    viewHolder.tvName = convertView.findViewById(R.id.tv_name);
                    viewHolder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
                    viewHolder.tvCharge = convertView.findViewById(R.id.tv_charge);
                    viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.ivPicture.setImageResource(NativeLine.LinePictures[switchStatement.getIconType() % NativeLine.LinePictures.length]);
                viewHolder.tvName.setText(switchStatement.getName());

                viewHolder.tvQuantity.setText(String.valueOf(switchStatement.getShowValue()));
                viewHolder.tvCharge.setText(String.valueOf(switchStatement.getShowPrice() + ""));

                return convertView;
            }

            class ViewHolder {
                TextView tvName;
                TextView tvQuantity;
                TextView tvCharge;
                ImageView ivPicture;
            }
        };
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

    }

    /*private void initLvAdapter() {

        lvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return collectorBeanList.size();
            }

            @Override
            public Object getItem(int position) {
                return collectorBeanList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                CollectorBean collectDevice = collectorBeanList.get(position);

                ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = View.inflate(StatementActivity.this, R.layout.item_lv_collector, null);
                    viewHolder = new ViewHolder();
                    viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
                    viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(viewHolder);

                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);

                if (collectDevice.equals(currentCollectorBean)) {
                    viewHolder.ivSelected.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.ivSelected.setVisibility(View.GONE);
                }

                viewHolder.tvName.setText(collectDevice.getDeviceName());
                return convertView;
            }

            class ViewHolder {
                ImageView ivSelected;
                TextView tvName;
            }
        };
    }*/

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
//            case R.id.iv_device:
            //dialog.show();
//                break;
//            case R.id.tv_close:
            //dialog.dismiss();
//                break;
        }
    }
}
