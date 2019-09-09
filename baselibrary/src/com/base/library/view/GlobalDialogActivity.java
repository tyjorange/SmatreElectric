package com.base.library.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.base.frame.os.ActivityStack;
import com.base.library.R;
import com.base.library.core.AbstractBaseToolbarActivity;



/**
 * Created by shaxiaoning on 12/12/16.
 */

public class GlobalDialogActivity extends AbstractBaseToolbarActivity {
    private TextView btnConfirm = null;
    private TextView btnCancel = null;
    private TextView txtTitle = null;
    private WindowManager.LayoutParams lp;
    private int percentageH = 4;
    private int percentageW = 8;

    /**
     * 返回当前Activity布局文件的id
     *
     * @return
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_global_dialog;
    }

    /**
     * initView
     */
    @Override
    protected void initView() {
        getSupportActionBar().hide();
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0.6f; // 去背景遮盖
        lp.alpha = 1.0f;
        int[] wh = initWithScreenWidthAndHeight(this);
        lp.width = wh[0] - wh[0] / percentageW;
        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        btnCancel = (TextView) this.findViewById(R.id.activity_global_dialog_btn_cancel);
        btnConfirm = (TextView) this.findViewById(R.id.activity_global_dialog_btn_confirm);
        txtTitle = (TextView) this.findViewById(R.id.activity_global_dialog_title);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /**
     * initData
     */
    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String btnDes = bundle.getString("btnDes");
        String btnCancelDes = bundle.getString("cancelDes");
        int isHideCancel = bundle.getInt("isHideCancel");
        if (isHideCancel == 1) btnCancel.setVisibility(View.GONE);
        btnConfirm.setText(btnDes);
        btnCancel.setText(btnCancelDes);
        txtTitle.setText(title);
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    /**
     * 获取当前window width,height
     *
     * @param context
     * @return
     */
    private static int[] initWithScreenWidthAndHeight(Context context) {
        int[] wh = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        wh[0] = dm.widthPixels;
        wh[1] = dm.heightPixels;
        return wh;
    }

    /**
     * 是否显示返回键
     *
     * @return
     */
    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.activity_global_dialog_btn_confirm) {
//            this.finish();
            callback();

        } else if (i == R.id.activity_global_dialog_btn_cancel) {//重登录
//            this.finish();
//            callback();
            loginAgain();

        }
    }

    @Override
    protected void dealloc() {

    }

    /**
     * login
     */
    private void loginAgain() {
        ActivityStack.getActivityManage().removeAllActivity();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(GlobalDialogActivity.this, LoginActivity.class);
//                startActivity(intent);
//                GlobalDialogActivity.this.finish();
                overridePendingTransition(R.anim.popup_bottom_out, 0);
            }
        }, 500);


    }

    /**
     *
     */
    private void callback() {
        setResult(2001);
        this.finish();
        overridePendingTransition(R.anim.popup_bottom_out, 0);
        ActivityStack.getActivityManage().removeAllActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            callback();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
