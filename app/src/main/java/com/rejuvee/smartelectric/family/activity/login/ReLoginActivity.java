package com.rejuvee.smartelectric.family.activity.login;

import android.content.Context;
import android.content.Intent;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.ActivityFragmentManager;
import com.rejuvee.smartelectric.family.common.BaseActivity;

public class ReLoginActivity extends BaseActivity {
    private Context mContext;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_re_login;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        findViewById(R.id.btn_ok).setOnClickListener(v -> restartApp(mContext));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dealloc() {

    }

    private void restartApp(Context context) {
        //销毁所有活动
        ActivityFragmentManager.removeAll();
        ActivityFragmentManager.finishAll();
        //启动登陆活动
        Intent intent = new Intent(context, LoginActivity.class);
        //在广播中启动活动，需要添加如下代码
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
