package com.rejuvee.smartelectric.family.activity.login;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.ActivityFragmentManager;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityReLoginBinding;

public class ReLoginActivity extends BaseActivity {

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_re_login;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityReLoginBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_re_login);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        findViewById(R.id.btn_ok).setOnClickListener(v -> restartApp());
    }

    public class Presenter {
        public void onOk(View view) {
            restartApp();
        }

    }
    @Override
    protected void dealloc() {

    }

    private void restartApp() {
        //销毁所有活动
        ActivityFragmentManager.removeAll();
        ActivityFragmentManager.finishAll();
        //启动登陆活动
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //在广播中启动活动，需要添加如下代码
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getApplicationContext().startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
