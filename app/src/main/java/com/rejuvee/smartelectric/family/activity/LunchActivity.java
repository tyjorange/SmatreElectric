package com.rejuvee.smartelectric.family.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;

import java.util.Locale;

public class LunchActivity extends BaseActivity {
    private String TAG = "LunchActivity";
    private Button mBtnSkip;
    private int count = 3;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lunch;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

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
                lunchActivity.mBtnSkip.setText(String.format(Locale.getDefault(), "%s(%d)", lunchActivity.getString(R.string.vs11), lunchActivity.getCount()));
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
        mBtnSkip = findViewById(R.id.btn_skip);
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LunchActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                handler.removeMessages(101);
                finish();
            }
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
