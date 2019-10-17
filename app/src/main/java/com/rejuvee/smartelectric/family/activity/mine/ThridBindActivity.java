package com.rejuvee.smartelectric.family.activity.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.ThirdPartyInfo;
import com.rejuvee.smartelectric.family.utils.thrid.QQLoginHelper;
import com.rejuvee.smartelectric.family.utils.thrid.WXHelper;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 第三方绑定
 */
public class ThridBindActivity extends BaseActivity {
    private String TAG = "ThridBindActivity";
    private LinearLayout ll_wx;
    private LinearLayout ll_qq;
    private TextView tv_wx;
    private TextView tv_qq;
    private Context context;
    private DialogTip mDialogSwitch;
    private LoadingDlg waitDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bind;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        context = this;
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDialogSwitch = new DialogTip(this);
        waitDialog = new LoadingDlg(this, -1);
        ll_wx = findViewById(R.id.ll_wx);
        ll_qq = findViewById(R.id.ll_qq);
        tv_wx = findViewById(R.id.tv_wx);
        tv_qq = findViewById(R.id.tv_qq);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        initWX();
        initQQ();
    }

    private void initWX() {
        String wechatUnionID = getIntent().getStringExtra("wechatUnionID");
        if (wechatUnionID == null || wechatUnionID.isEmpty()) {
            tv_wx.setText(R.string.mine_unbound);
            ll_wx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Snackbar.make(v, wechatUnionID, Snackbar.LENGTH_SHORT).show();
                    //TODO 拉起第三方
                    WXHelper.startWxBind(context);
                }
            });
        } else {
            tv_wx.setText(R.string.mine_isbound);
            ll_wx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Snackbar.make(v, wechatUnionID, Snackbar.LENGTH_SHORT).show();
                    //TODO 提示解绑
                    popDialog(getString(R.string.vs167), getString(R.string.vs168), "WX");
                }
            });
        }
    }

    private void initQQ() {
        String qqUnionID = getIntent().getStringExtra("qqUnionID");
        if (qqUnionID == null || qqUnionID.isEmpty()) {
            tv_qq.setText(R.string.mine_unbound);
            ll_qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Snackbar.make(v, qqOpenID, Snackbar.LENGTH_SHORT).show();
                    //TODO 拉起第三方
                    QQLoginHelper.getInstance().qqBind((Activity) context);
                }
            });
        } else {
            tv_qq.setText(R.string.mine_isbound);
            ll_qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Snackbar.make(v, qqOpenID, Snackbar.LENGTH_SHORT).show();
                    //TODO 提示解绑
                    popDialog(getString(R.string.vs167), getString(R.string.vs169), "QQ");
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThirdPartBind(final ThirdPartyInfo thirdPartyBind) {
        if (thirdPartyBind.isSucess) {
            Core.instance(context).ThirdPartBind(thirdPartyBind, new ActionCallbackListener<Void>() {
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
                    CustomToast.showCustomErrorToast(ThridBindActivity.this, message);
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
                waitDialog.show();
                if (popType.equals("WX")) {
                    Core.instance(context).unBindWX(new ActionCallbackListener<Void>() {

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
                    Core.instance(context).unBindQQ(new ActionCallbackListener<Void>() {

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
        });
        mDialogSwitch.show();
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

    @Override
    protected void dealloc() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        QQLoginHelper.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
