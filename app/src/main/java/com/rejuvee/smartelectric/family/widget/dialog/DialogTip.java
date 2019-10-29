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
 * 确认提示弹出
 * Created by liuchengran on 2017/12/19.
 */
public class DialogTip extends Dialog implements View.OnClickListener {
    private TextView txtTitle, txtCancel, txtOk;
    private TextView txtContent;
    private onEnsureDialogListener mListener;

    @Override
    public void onClick(View view) {
        dismiss();
        if (view.getId() == R.id.txt_cancel) {
            if (mListener != null)
                mListener.onCancel();
        } else if (view.getId() == R.id.txt_ok) {
            if (mListener != null)
                mListener.onEnsure();
        }
    }

    public interface onEnsureDialogListener {
        void onCancel();

        void onEnsure();
    }

    public DialogTip(Context context) {
        super(context, com.base.library.R.style.Dialog);
        init(context, true);

    }

    public DialogTip(Context context, int themeResId) {
        super(context, themeResId);
        init(context, true);
    }

    public DialogTip(Context context, boolean isBottom) {
        super(context);
        init(context, isBottom);
    }

    public DialogTip setDialogListener(onEnsureDialogListener listener) {
        mListener = listener;
        return this;
    }

    private void init(Context context, boolean isBottom) {
        setContentView(R.layout.dialog_ensure);
        txtTitle = findViewById(R.id.txt_tip_title);
        txtCancel = findViewById(R.id.txt_cancel);
        txtOk = findViewById(R.id.txt_ok);
        txtOk.setBackground(getContext().getDrawable(R.drawable.btn_def));

        txtContent = findViewById(R.id.txt_tip_desc);

        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        dialogWindow.setAttributes(lp);
        if (isBottom) {
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
//        else {
//            txtOk.setText(getContext().getString(R.string.intall_now));
//            txtCancel.setText(getContext().getString(R.string.vs215));
//        }
        txtOk.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    public DialogTip setTitle(String title) {
        txtTitle.setText(title);
        return this;
    }

    public DialogTip setContent(String content) {
        txtContent.setText(content);
        return this;
    }

    public DialogTip setOkTxt(String okTxt) {
        txtOk.setText(okTxt);
        return this;
    }

    public DialogTip setCancelTxt(String cancelTxt) {
        txtCancel.setText(cancelTxt);
        return this;
    }
    public DialogTip setRedBtn() {
        txtOk.setBackground(getContext().getDrawable(R.drawable.btn_red));
        return this;
    }
}
