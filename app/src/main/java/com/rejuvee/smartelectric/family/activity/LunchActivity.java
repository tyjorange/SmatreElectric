package com.rejuvee.smartelectric.family.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;

import java.util.Locale;

public class LunchActivity extends BaseActivity {
    private Button mBtnSkip;
    private int count = 5;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_lunch;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                mBtnSkip.setText(String.format(Locale.getDefault(), "%s%d%s", getString(R.string.vs11), getCount(), getString(R.string.vs12)));
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

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
