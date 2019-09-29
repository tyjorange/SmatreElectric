package com.rejuvee.smartelectric.family.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;

/**
 * Created by liuchengran on 2019/1/4.
 */

public class DialogTipWithoutOkCancel extends Dialog {
    private TextView txtTitle, txtCancel, txtOk;
    private TextView txtContent;
    private DialogTip.onEnsureDialogListener mListener;


    public DialogTipWithoutOkCancel(Context context) {
        super(context, com.base.library.R.style.Dialog);
        init(context);

    }

    public DialogTipWithoutOkCancel(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }


    private void init(Context context) {
        setContentView(R.layout.dialog_tip);

        txtTitle = (TextView) findViewById(R.id.tv_tip_title);
        txtContent = (TextView) findViewById(R.id.txt_tip_desc);
        findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.9
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);

    }

    public DialogTipWithoutOkCancel setTitle(String title) {
        txtTitle.setText(title);
        return this;
    }

    public void hiddenTitle() {
        txtTitle.setVisibility(View.GONE);
    }

    public DialogTipWithoutOkCancel setContent(String content) {
        txtContent.setText(content);
        return this;
    }

    public void showImg() {
        findViewById(R.id.iv_kaifazhong).setVisibility(View.VISIBLE);
    }
}
