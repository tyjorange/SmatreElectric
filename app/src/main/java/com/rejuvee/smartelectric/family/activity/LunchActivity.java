package com.rejuvee.smartelectric.family.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityLunchBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.LunchViewModel;

import java.util.Locale;

public class LunchActivity extends BaseActivity {
    private static final String TAG = "LunchActivity";
    private static final int countDownFlag = 1502;
    //    private Button mBtnSkip;
    private int count = 3;
    private ActivityLunchBinding mBinding;

//    @Override
//    protected int getLayoutResId() {
//        return 0;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    private Handler handler;

    private static class MyHandler extends Handler {
//        SoftReference<LunchActivity> activitySoftReference;

        LunchActivity lunchActivity;

        MyHandler(LunchActivity activity) {
//            activitySoftReference = new SoftReference<LunchActivity>(activity);
            lunchActivity = activity;
        }


        @Override
        public void handleMessage(Message msg) {
//            LunchActivity theActivity = activitySoftReference.get();
            if (msg.what == countDownFlag) {
                int count = lunchActivity.getCount();
                Log.i(TAG, String.valueOf(count));
                lunchActivity.mBinding.btnSkip.setText(String.format(Locale.getDefault(), "%s(%d)", lunchActivity.getString(R.string.vs11), count));
                lunchActivity.handler.sendEmptyMessageDelayed(countDownFlag, 1000);
            }
        }
    }

    private int getCount() {
        count--;
        if (count == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.top_in, R.anim.top_out);
            finish();
        }
        return count;
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lunch);
        LunchViewModel mViewModel = ViewModelProviders.of(this).get(LunchViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        mBtnSkip = findViewById(R.id.btn_skip);
//        mBinding.btnSkip.setOnClickListener(view -> {
//            startActivity(new Intent(LunchActivity.this, LoginActivity.class));
//            overridePendingTransition(R.anim.top_in, R.anim.top_out);
//            handler.removeMessages(countDownFlag);
//            finish();
//        });
        handler = new MyHandler(this);
        handler.sendEmptyMessageDelayed(countDownFlag, 1000);
    }

    public class Presenter {
        public void onSkip(View view) {
            Log.i(TAG, "onSkip");
            startActivity(new Intent(view.getContext(), LoginActivity.class));
            overridePendingTransition(R.anim.top_in, R.anim.top_out);
            handler.removeMessages(countDownFlag);
            finish();
        }
    }

    @Override
    protected void dealloc() {

    }
}
