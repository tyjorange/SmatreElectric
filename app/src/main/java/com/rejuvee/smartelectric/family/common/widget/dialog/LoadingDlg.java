package com.rejuvee.smartelectric.family.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;

/**
 * 加载提示弹窗
 */
public class LoadingDlg extends Dialog {
    private TextView txtToast;

    public LoadingDlg(Context context, int tipsRes) {
        super(context, R.style.CustomDialog);

        setContentView(R.layout.layout_toast_loading_data);

        Window window = getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        txtToast = findViewById(R.id.txt_tip);
        if (tipsRes != -1) {
            txtToast.setText(tipsRes);
        }

    }

    public void setLoadingTip(String txtTip) {
        txtToast.setText(txtTip);
    }


}
