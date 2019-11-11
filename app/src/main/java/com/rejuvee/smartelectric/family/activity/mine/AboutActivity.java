package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity {
//    private Context mContext;
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_about;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityAboutBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        mBinding.txtCurrentVision.setText(String.format("%s%s", getString(R.string.vs20), packageCode(this)));
    }

    private String packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        String code = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    @Override
    protected void initData() {

    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }
    }

    @Override
    protected void dealloc() {

    }
}
