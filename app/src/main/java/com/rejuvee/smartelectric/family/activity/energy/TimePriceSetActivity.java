package com.rejuvee.smartelectric.family.activity.energy;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTimePicker;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

/**
 * 分时计价 设置
 */
public class TimePriceSetActivity extends BaseActivity implements View.OnClickListener {
    private DialogTimePicker mTimePicker;
    private TextView txtStartTime, txtEndTime;
    private EditText edtPrice;
    private int mStartTime = -1, mEndTime = -1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_time_price_set;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        mTimePicker = new DialogTimePicker(this);
        mTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setCurrentMiniute(0)
                .setListener(new DialogTimePicker.TimeSelectedListen() {
                    @Override
                    public void onTimeSelected(int hour, int miniute, int type) {
                        if (type == 0) {
                            txtStartTime.setText(String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, miniute));
                            mStartTime = hour;
                        } else if (type == 1) {
                            txtEndTime.setText(String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, miniute));
                            mEndTime = hour;
                        }
                    }
                });
        txtStartTime = findViewById(R.id.txt_start_time);
        txtEndTime = findViewById(R.id.txt_end_time);
        edtPrice = findViewById(R.id.edt_price);

        String s_price = getIntent().getStringExtra("s_price");
        String s_start = getIntent().getStringExtra("s_start");
        String s_end = getIntent().getStringExtra("s_end");
        int i_start = getIntent().getIntExtra("i_start", -1);
        int i_end = getIntent().getIntExtra("i_end", -1);
        // 如果是修改单条记录
        if (s_start != null) {
            txtStartTime.setText(s_start);
            txtEndTime.setText(s_end);
            edtPrice.setText(s_price);
            mStartTime = i_start;
            mEndTime = i_end;
        } else {
            txtStartTime.setOnClickListener(this);
            txtEndTime.setOnClickListener(this);
        }

        findViewById(R.id.st_ensure).setOnClickListener(this);

        Currency currency = Currency.getInstance(Locale.getDefault());
        TextView txtSymbol = findViewById(R.id.txt_symbol);
        txtSymbol.setText(String.format("%s%s", getString(R.string.vs19), currency.getSymbol()));

    }

    @Override
    protected void initData() {

    }

//    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.time_price_edit);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txt_start_time) {
            mTimePicker.setType(0).show();
        } else if (id == R.id.txt_end_time) {
            mTimePicker.setType(1).show();
        } else if (id == R.id.st_ensure) {
            ensure();
        }
    }

    private void ensure() {
        if (mStartTime == -1 || mEndTime == -1) {
            CustomToast.showCustomErrorToast(this, getString(R.string.please_input_start_end_time));
            return;
        }
        if (mEndTime <= mStartTime) {
            if (mStartTime - mEndTime < 12) {
                CustomToast.showCustomErrorToast(this, getString(R.string.endtime_early_starttime));
                return;
            }
        }

        String price = edtPrice.getText().toString();
        if (price.length() == 0) {
            CustomToast.showCustomErrorToast(this, getString(R.string.please_input_price));
            return;
        }

        Intent intent = getIntent();
        intent.putExtra("start", mStartTime);
        intent.putExtra("end", mEndTime);
        intent.putExtra("price", price);
        setResult(RESULT_OK, intent);
        finish();
    }
}
