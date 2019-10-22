package com.rejuvee.smartelectric.family.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;

import java.lang.ref.WeakReference;
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

    private Handler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<LunchActivity> activityWeakReference;

        MyHandler(LunchActivity activity) {
            activityWeakReference = new WeakReference<LunchActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LunchActivity theActivity = activityWeakReference.get();
            if (msg.what == 0) {
                theActivity.mBtnSkip.setText(String.format(Locale.getDefault(), "%s(%d)", theActivity.getString(R.string.vs11), theActivity.getCount()));
                theActivity.handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    }

    public int getCount() {
        count--;
        if (count == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
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
                handler.removeMessages(0);
                finish();
            }
        });
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dealloc() {

    }
}
