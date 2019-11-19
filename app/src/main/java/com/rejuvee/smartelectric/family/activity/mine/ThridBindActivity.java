package com.rejuvee.smartelectric.family.activity.mine;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.utils.thrid.QQLoginHelper;
import com.rejuvee.smartelectric.family.common.utils.thrid.WXHelper;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityBindBinding;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

/**
 * 第三方绑定
 */
public class ThridBindActivity extends BaseActivity {
    private String TAG = "ThridBindActivity";
    //    private LinearLayout ll_wx;
//    private LinearLayout ll_qq;
//    private TextView tv_wx;
//    private TextView tv_qq;
    //    private Context context;
    private DialogTip mDialogSwitch;
    private LoadingDlg waitDialog;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_bind;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivityBindBinding mBinding;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bind);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        context = this;
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        mDialogSwitch = new DialogTip(this);
        waitDialog = new LoadingDlg(this, -1);
//        ll_wx = findViewById(R.id.ll_wx);
//        ll_qq = findViewById(R.id.ll_qq);
//        tv_wx = findViewById(R.id.tv_wx);
//        tv_qq = findViewById(R.id.tv_qq);
//        findViewById(R.id.stv_commit).setOnClickListener(v -> {  });
        EventBus.getDefault().register(this);

        //        initWX();
        //        initQQ();
        // 未绑定就去微信绑定
        String wechatUnionID = getIntent().getStringExtra("wechatUnionID");
        if (wechatUnionID == null || wechatUnionID.isEmpty()) {
            //TODO 拉起第三方
            WXHelper.startWxBind(this);
        }
    }

    @Deprecated
    private void initWX() {
        String wechatUnionID = getIntent().getStringExtra("wechatUnionID");
        if (wechatUnionID == null || wechatUnionID.isEmpty()) {
            mBinding.tvWx.setText(R.string.mine_unbound);
            mBinding.llWx.setOnClickListener(v -> {
                //TODO 拉起第三方
                WXHelper.startWxBind(v.getContext());
            });
        } else {
            mBinding.tvWx.setText(R.string.mine_isbound);
            mBinding.llWx.setOnClickListener(v -> {
                //TODO 提示解绑
                popDialog(getString(R.string.vs167), getString(R.string.vs168), "WX");
            });
        }
    }

    @Deprecated
    private void initQQ() {
        String qqUnionID = getIntent().getStringExtra("qqUnionID");
        if (qqUnionID == null || qqUnionID.isEmpty()) {
            mBinding.tvQq.setText(R.string.mine_unbound);
            mBinding.llQq.setOnClickListener(v -> {
                //TODO 拉起第三方
                QQLoginHelper.getInstance().qqBind((Activity) v.getContext());
            });
        } else {
            mBinding.tvQq.setText(R.string.mine_isbound);
            mBinding.llQq.setOnClickListener(v -> {
                //TODO 提示解绑
                popDialog(getString(R.string.vs167), getString(R.string.vs169), "QQ");
            });
        }
    }

    /**
     * 绑定后的回调
     *
     * @param thirdPartyBind
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThirdPartBind(final ThirdPartyInfo thirdPartyBind) {
        if (thirdPartyBind.isSucess) {
            Core.instance(this).ThirdPartBind(thirdPartyBind, new ActionCallbackListener<Void>() {
                @Override
                public void onSuccess(Void data) {
                    if (thirdPartyBind.bindType == ThirdPartyInfo.BIND_WEIXIN) {
                        CustomToast.showCustomToast(ThridBindActivity.this, getString(R.string.vs170));
                        finish();
                    } else if (thirdPartyBind.bindType == ThirdPartyInfo.BIND_QQ) {
                        CustomToast.showCustomToast(ThridBindActivity.this, getString(R.string.vs171));
                        finish();
                    }
                }

                @Override
                public void onFailure(int errorEvent, String message) {
//                    if (errorEvent != 15) {// 此微信已注册登录或已被其他账号绑定 忽略
                    CustomToast.showCustomErrorToast(ThridBindActivity.this, message);
//                    }
                }
            });
        }
    }

    private void popDialog(String title, String desc, final String popType) {
        mDialogSwitch.setTitle(title);
        mDialogSwitch.setContent(desc);
        mDialogSwitch.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onEnsure() {
                unBind(popType);
            }
        });
        mDialogSwitch.show();
    }

    private void toWXGZH() {
        // 复制到剪贴板
        ClipboardManager tvCopy = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN, getResources().getString(R.string.vs221));
        Objects.requireNonNull(tvCopy).setPrimaryClip(myClip);
        CustomToast.showCustomToast(getApplicationContext(), getString(R.string.vs223));
        // 跳转到微信
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            CustomToast.showCustomErrorToast(this, getString(R.string.vs224));
        }
    }

    private void unBind(String popType) {
        waitDialog.show();
        if (popType.equals("WX")) {
            Core.instance(this).unBindWX(new ActionCallbackListener<Void>() {

                @Override
                public void onSuccess(Void data) {
                    CustomToast.showCustomToast(ThridBindActivity.this, getString(R.string.vs172));
                    waitDialog.dismiss();
                    finish();
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    CustomToast.showCustomErrorToast(ThridBindActivity.this, getString(R.string.vs173));
                    waitDialog.dismiss();
                }
            });
        } else if (popType.equals("QQ")) {
            Core.instance(this).unBindQQ(new ActionCallbackListener<Void>() {

                @Override
                public void onSuccess(Void data) {
                    CustomToast.showCustomToast(ThridBindActivity.this, getString(R.string.vs174));
                    waitDialog.dismiss();
                    finish();
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    CustomToast.showCustomErrorToast(ThridBindActivity.this, getString(R.string.vs175));
                    waitDialog.dismiss();
                }
            });
        }
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return getResources().getString(R.string.mine_subtitle_bind);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onCommit(View view) {
            toWXGZH();
        }
    }

    @Override
    protected void dealloc() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QQLoginHelper.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
