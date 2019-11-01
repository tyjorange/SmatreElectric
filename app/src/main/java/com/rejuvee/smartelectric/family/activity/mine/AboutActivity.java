package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;

public class AboutActivity extends BaseActivity {
    private Context mContext;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.txt_current_vision)).setText(String.format("%s%s", getString(R.string.vs20), packageCode(mContext)));
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

    @Override
    protected void dealloc() {

    }
}
