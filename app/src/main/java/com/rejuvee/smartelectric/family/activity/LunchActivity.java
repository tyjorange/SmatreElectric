package com.rejuvee.smartelectric.family.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityLunchBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.LunchViewModel;

import java.util.Locale;

public class LunchActivity extends BaseActivity {
    private String TAG = "LunchActivity";
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
            if (msg.what == 101) {
                lunchActivity.mBinding.btnSkip.setText(String.format(Locale.getDefault(), "%s(%d)", lunchActivity.getString(R.string.vs11), lunchActivity.getCount()));
                lunchActivity.handler.sendEmptyMessageDelayed(101, 1000);
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
        mBinding.setAct(this);
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);

//        mBtnSkip = findViewById(R.id.btn_skip);
        mBinding.btnSkip.setOnClickListener(view -> {
            startActivity(new Intent(LunchActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.top_in, R.anim.top_out);
            handler.removeMessages(101);
            finish();
        });
        handler = new MyHandler(this);
        handler.sendEmptyMessageDelayed(101, 1000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dealloc() {

    }
}
