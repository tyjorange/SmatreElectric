package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.AutoUpgrade;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.databinding.ActivitySettingsBinding;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;

/**
 * APP设置
 * Created by SH on 2017/12/22.
 */
public class SettingsActivity extends BaseActivity {
//    private String[] permissions = new String[]{"android.permission.CHANGE_CONFIGURATION"};
    //    private TextView tvClean;


//    private Context mContext;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_settings;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivitySettingsBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        mContext = this;
//        setToolbarHide(true);
//        tvClean = (TextView) findViewById(R.id.tv_clean);
        mBinding.txtCurrentVision.setText(String.format("v%s", packageCode(this)));
//        findViewById(R.id.ll_check_new).setOnClickListener(v -> AutoUpgrade.getInstacne(this).startWithTip());
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

    //    @Override
//    protected String getToolbarTitle() {
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return false;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onChangePassword(View view) {
//              Snackbar.make(v, "修改密码", Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(view.getContext(), ModifyPasswordActivity.class));
        }

        public void onCheckVersion(View view) {
            AutoUpgrade.getInstacne(view.getContext()).startWithTip();
        }

        public void onEnLogOut(View view) {
            enLogOut();
        }
    }

    @Override
    protected void dealloc() {

    }

    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.ll_clean:
//                //Snackbar.make(v, "清除缓存", Snackbar.LENGTH_SHORT).show();
//                break;
//            case R.id.ll_suggestion:
//                //Snackbar.make(v, "意见反馈", Snackbar.LENGTH_SHORT).show();
//                break;
//            case R.id.ll_change_password:
////              Snackbar.make(v, "修改密码", Snackbar.LENGTH_SHORT).show();
//                startActivity(new Intent(this, ModifyPasswordActivity.class));
//                break;
//            case R.id.tv_exit://退出账号
//                enLogOut();
//                break;
//        }
//    }

    private void enLogOut() {
        DialogTip mDialogSwitch = new DialogTip(this);
        String title = "";
        String desc = getString(R.string.vs186);
        mDialogSwitch.setTitle(title);
        mDialogSwitch.setContent(desc);
        mDialogSwitch.setRedBtn();
        mDialogSwitch.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onEnsure() {
                logOut();
            }
        });
        mDialogSwitch.show();
    }

    private void logOut() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.deleteRealm();
        MainApplication.unbindAliCloud();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
//    private void permissionCheck() {
//        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // 检查该权限是否已经获取
//            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
//            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//            if (i != PackageManager.PERMISSION_GRANTED) {
//                // 如果没有授予该权限，就去提示用户请求
//                ActivityCompat.requestPermissions(this, permissions, 321);
//            }
//        }
//    }
}
