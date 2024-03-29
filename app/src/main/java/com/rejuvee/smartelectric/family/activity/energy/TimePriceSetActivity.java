package com.rejuvee.smartelectric.family.activity.energy;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTimePicker;
import com.rejuvee.smartelectric.family.databinding.ActivityTimePriceSetBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.TimePriceSetViewModel;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

/**
 * 分时计价 设置
 */
public class TimePriceSetActivity extends BaseActivity {
    private DialogTimePicker mTimePicker;
    //    private TextView txtStartTime, txtEndTime;
//    private EditText edtPrice;
    private int mStartTime = -1, mEndTime = -1;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_time_price_set;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivityTimePriceSetBinding mBinding;
    private TimePriceSetViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_price_set);
        mViewModel = ViewModelProviders.of(this).get(TimePriceSetViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        mTimePicker = new DialogTimePicker(this);
        mTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .setCurrentMiniute(0)
                .setListener((hour, miniute, type) -> {
                    if (type == 0) {
//                        txtStartTime.setText(String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, miniute));
                        mViewModel.setStartTime(String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, miniute));
                        mStartTime = hour;
                    } else if (type == 1) {
//                        txtEndTime.setText(String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, miniute));
                        mViewModel.setEndTime(String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, miniute));
                        mEndTime = hour;
                    }
                });
//        txtStartTime = findViewById(R.id.txt_start_time);
//        txtEndTime = findViewById(R.id.txt_end_time);
//        edtPrice = findViewById(R.id.edt_price);

        String s_price = getIntent().getStringExtra("s_price");
        String s_start = getIntent().getStringExtra("s_start");
        String s_end = getIntent().getStringExtra("s_end");
        int i_start = getIntent().getIntExtra("i_start", -1);
        int i_end = getIntent().getIntExtra("i_end", -1);
        // 如果是修改单条记录
        if (s_start != null) {
//            txtStartTime.setText(s_start);
            mViewModel.setStartTime(s_start);
//            txtEndTime.setText(s_end);
            mViewModel.setEndTime(s_end);
//            edtPrice.setText(s_price);
            mViewModel.setPrice(s_price);
            mStartTime = i_start;
            mEndTime = i_end;
            mBinding.txtStartTime.setEnabled(false);
            mBinding.txtEndTime.setEnabled(false);
        } else {
            mBinding.txtStartTime.setEnabled(true);
            mBinding.txtEndTime.setEnabled(true);
        }

//        findViewById(R.id.st_ensure).setOnClickListener(this);

        Currency currency = Currency.getInstance(Locale.getDefault());
        mBinding.txtSymbol.setText(String.format("%s%s", getString(R.string.vs19), currency.getSymbol()));

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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onStartTime(View view) {
            mTimePicker.setType(0).show();
        }

        public void onEndTime(View view) {
            mTimePicker.setType(1).show();
        }

        public void onEnsure(View view) {
            ensure();
        }
    }

    @Override
    protected void dealloc() {

    }


//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.txt_start_time) {
//            mTimePicker.setType(0).show();
//        } else if (id == R.id.txt_end_time) {
//            mTimePicker.setType(1).show();
//        } else if (id == R.id.st_ensure) {
//            ensure();
//        }
//    }

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

        String price = mViewModel.getPrice().getValue();//mBinding.edtPrice.getText().toString();
        if (StringUtil.isEmpty(price)) {
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
