package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.MainApplication;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.login.LoginActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;

/**
 * APP设置
 * Created by SH on 2017/12/22.
 */
public class SettingsActivity extends BaseActivity {
    private String[] permissions = new String[]{"android.permission.CHANGE_CONFIGURATION"};
    //    private TextView tvClean;
    private DialogTip mDialogSwitch;
    private Context mContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_settings;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContent = this;

//        setToolbarHide(true);
//        tvClean = (TextView) findViewById(R.id.tv_clean);
        ((TextView) findViewById(R.id.txt_current_vision)).setText("v" + packageCode(this));
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
        permissionCheck();
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

    @Override
    protected void dealloc() {

    }

    //    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_clean:
                //Snackbar.make(v, "清除缓存", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.ll_suggestion:
                //Snackbar.make(v, "意见反馈", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.ll_change_password:
//              Snackbar.make(v, "修改密码", Snackbar.LENGTH_SHORT).show();
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;
            case R.id.tv_exit://退出账号
                logOut();
                break;
//            case R.id.ll_language_switch:
//                int type = LanguageUtil.getLangType(this);
//                if (type == 0) {
//                    LanguageUtil.setConfigueType(this, 1);
//                    Log.d("SettingsActivity", "中文");
//                } else if (type == 1) {
//                    LanguageUtil.setConfigueType(this, 0);
//                    Log.d("SettingsActivity", "英文");
//                }
//                CustomToast.showCustomToast(getContext(), getString(R.string.settings_language));
//                LanguageUtil.SwitchLang(this);
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    }
//                }, 1000);
//                break;
        }
//        super.onClick(v);
    }

    private void logOut() {
        mDialogSwitch = new DialogTip(this);
        String title = "";
        String desc = "确定要退出么？";
        mDialogSwitch.setTitle(title);
        mDialogSwitch.setContent(desc);
        mDialogSwitch.setRedBtn();
        mDialogSwitch.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onEnsure() {
                AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
                accountInfoRealm.deleteRealm();
//                unbindAccount();
                MainApplication.unbindAliCloud();
                Intent intent = new Intent(mContent, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        mDialogSwitch.show();
    }

    private void permissionCheck() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }
}
