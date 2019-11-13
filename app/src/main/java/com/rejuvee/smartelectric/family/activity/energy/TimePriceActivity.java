package com.rejuvee.smartelectric.family.activity.energy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListSetingItem;
import com.rejuvee.smartelectric.family.adapter.SettingAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityCostCalculationBinding;
import com.rejuvee.smartelectric.family.model.bean.TimePrice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * 分时计价
 */
public class TimePriceActivity extends BaseActivity {
    private String TAG = "TimePriceActivity";
    //    private ListView mListView;
    private SettingAdapter mAdapter;
    //    private EditText mEditText;
    private List<ListSetingItem> mListData = new ArrayList<>();
    private static Handler mHandler;

    private static int MESSAGE_UPDATE_PRICE = 100;
    private String[] timeOfUsePrice = null;
    private String currencySymbol;// 货币符号
    //    private String defaultPrice;
    private LoadingDlg mWaitDialog;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_cost_calculation;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityCostCalculationBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cost_calculation);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        Currency currency = Currency.getInstance(Locale.getDefault());
//        TextView txtSymbol = findViewById(R.id.txt_symbol);
//        txtSymbol.setText(currency.getSymbol());
        currencySymbol = Currency.getInstance(Locale.getDefault()).getSymbol();
//        mEditText = findViewById(R.id.edt_default_price);
//        mListView = mBinding.listSections;
//        mListView.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(TimePriceActivity.this, TimePriceSetActivity.class);
//            intent.putExtra("s_price", timeOfUsePrice[position]);
//            String[] split = mListData.get(position).getContent().split(" - ");
//            intent.putExtra("s_start", split[0]);
//            intent.putExtra("s_end", split[1]);
//            intent.putExtra("i_start", position);
//            intent.putExtra("i_end", position + 1);
//            startActivityForResult(intent, 1000);
//        });
        readPriceFromShared();//读取本地的
//        mEditText.setText(defaultPrice);
//        mEditText.addTextChangedListener(textWatcher);

//        findViewById(R.id.img_edit_section).setOnClickListener(v -> startActivityForResult(new Intent(TimePriceActivity.this, TimePriceSetActivity.class), 1000));
        mHandler = new MyHandler(this);

        mWaitDialog = new LoadingDlg(this, -1);
        getPrice();//读取服务器的数据


//        if (mAdapter == null) {
        mAdapter = new SettingAdapter(this);
        mBinding.listSections.setLayoutManager(new LinearLayoutManager(this));
        mBinding.listSections.setAdapter(mAdapter);
        mAdapter.setSetListener(new SettingAdapter.onSettingClickListener() {
            @Override
            public void onRemove(ListSetingItem item, int position) {
                //nothing
            }

            @Override
            public void onSwitch(ListSetingItem item, int isEnable) {
                //nothing
            }

            @Override
            public void onBeanClick(int position) {
                Intent intent = new Intent(TimePriceActivity.this, TimePriceSetActivity.class);
                intent.putExtra("s_price", timeOfUsePrice[position]);
                String[] split = mListData.get(position).getContent().getValue().split(" - ");
                intent.putExtra("s_start", split[0]);
                intent.putExtra("s_end", split[1]);
                intent.putExtra("i_start", position);
                intent.putExtra("i_end", position + 1);
                startActivityForResult(intent, 1000);
            }
        });
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }
        setDataType();
    }

    private void setDataType() {
        mListData.clear();
        for (int i = 0; i < timeOfUsePrice.length; i++) {
            ListSetingItem listSetingItem = new ListSetingItem();
            listSetingItem.setContent(String.format(Locale.getDefault(), "%02d", i) + ":00 - " + String.format(Locale.getDefault(), "%02d", i + 1) + ":00");
            listSetingItem.setDesc(currencySymbol + timeOfUsePrice[i]);
            listSetingItem.setViewType(ListSetingItem.ITEM_VIEW_TYPE_LINEDETAIL1);
            mListData.add(listSetingItem);
        }
        mAdapter.addAll(mListData);
    }

    private static class MyHandler extends Handler {
        WeakReference<TimePriceActivity> activityWeakReference;

        MyHandler(TimePriceActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TimePriceActivity theActivity = activityWeakReference.get();
            if (msg.what == MESSAGE_UPDATE_PRICE) {
//                theActivity.updatePrice(0, 23, theActivity.mEditText.getText().toString());
            }
        }
    }

    private void updatePrice(int start, int end, String price) {
        if (end < start) {
            for (int i = 0; i <= end; i++) {
                mListData.get(i).setDesc(currencySymbol + price);
                timeOfUsePrice[i] = price;
            }
            for (int i = start; i < 24; i++) {
                mListData.get(i).setDesc(currencySymbol + price);
                timeOfUsePrice[i] = price;
            }
        } else {
            for (int i = start; i <= end; i++) {
                mListData.get(i).setDesc(currencySymbol + price);
                timeOfUsePrice[i] = price;
            }
        }
        mAdapter.addAll(mListData);
    }


//    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            mHandler.removeMessages(MESSAGE_UPDATE_PRICE);
//            mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PRICE, 2000);
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onEdit(View view) {
            startActivityForResult(new Intent(view.getContext(), TimePriceSetActivity.class), 1000);
        }

    }

    @Override
    protected void dealloc() {
//        mEditText.removeTextChangedListener(textWatcher);
        savePriceToShared();
        mHandler.removeMessages(MESSAGE_UPDATE_PRICE);
        mHandler = null;
    }

    private void savePriceToShared() {
        SharedPreferences sharedPreferences = getSharedPreferences("time-of-use pricing", MODE_PRIVATE);
        StringBuilder price = new StringBuilder();
        for (String s : timeOfUsePrice) {
            price.append(s).append(",");
        }
        sharedPreferences.edit().putString("prices", price.toString()).apply();
//        sharedPreferences.edit().putString("default", mEditText.getText().toString()).apply();
    }

    private void readPriceFromShared() {
        SharedPreferences sharedPreferences = getSharedPreferences("time-of-use pricing", MODE_PRIVATE);
        String price = sharedPreferences.getString("prices", null);
//        defaultPrice = sharedPreferences.getString("default", "");
        if (price == null) {
            timeOfUsePrice = new String[24];
            for (int i = 0; i < 24; i++) {
                timeOfUsePrice[i] = "0.0";
            }
            return;
        }

        timeOfUsePrice = price.split(",");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                int startTime = data.getIntExtra("start", 0);
                int endTime = data.getIntExtra("end", 0);
                String price = data.getStringExtra("price");
                updatePrice(startTime, endTime - 1, price);
                uploadPrice();
            }
        }
    }

    private void uploadPrice() {
        StringBuilder price = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            price.append(i).append(",").append(timeOfUsePrice[i]).append(";");
        }
        Log.d(TAG, "upload price = " + price);
        mWaitDialog.show();
        Core.instance(this).addTimeOfUsePrice(price.toString(), new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                mWaitDialog.dismiss();
                CustomToast.showCustomToast(TimePriceActivity.this, getString(R.string.operator_sucess));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(TimePriceActivity.this, message);
            }
        });
    }

    private void getPrice() {
        mWaitDialog.show();
        Core.instance(this).getTimeOfUsePrice(new ActionCallbackListener<List<TimePrice>>() {
            @Override
            public void onSuccess(List<TimePrice> data) {
                mWaitDialog.dismiss();
                if (timeOfUsePrice == null) {
                    timeOfUsePrice = new String[24];
                }
                for (int i = 0; i < data.size(); i++) {
                    timeOfUsePrice[i] = data.get(i).getPrice();
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                mWaitDialog.dismiss();
            }
        });
    }
}
