package com.rejuvee.smartelectric.family.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;


public class WaitDialog extends Dialog {
    private Context context = null;
    private static WaitDialog customProgressDialog = null;
    private boolean cancelAble;

    public WaitDialog(Context context) {
        super(context);
        this.context = context;
        setCanceledOnTouchOutside(false);
    }

    public WaitDialog(Context context, int theme) {
        super(context, theme);
        cancelAble = true;
        this.context = context;
        setCanceledOnTouchOutside(false);
    }

    public static WaitDialog createDialog(Context context) {
        customProgressDialog = new WaitDialog(context, R.style.Dialog);
        customProgressDialog.setContentView(R.layout.dialog_wait);


        Window window = customProgressDialog.getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        TextView tvMsg = customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
        tvMsg.setText(context.getString(R.string.please_wait));

        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }
	        /*ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImg);
	        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
	        animationDrawable.start();*/
    }

    public WaitDialog setTitile(String strTitle) {
        return customProgressDialog;
    }


    public WaitDialog setMessage(String strMessage) {
        TextView tvMsg = customProgressDialog.findViewById(R.id.id_tv_loadingmsg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }

        return customProgressDialog;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setCancelable(boolean flag) {
        // TODO Auto-generated method stub
        super.setCancelable(flag);
        cancelAble = flag;
    }


}

