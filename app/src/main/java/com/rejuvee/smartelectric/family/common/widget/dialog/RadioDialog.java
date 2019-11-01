package com.rejuvee.smartelectric.family.common.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.rejuvee.smartelectric.family.R;


/**
 * 单选弹窗 （上电配置0：拉闸；1：合闸，2：不动作）
 */
public class RadioDialog extends Dialog implements View.OnClickListener {
    //    private EditText editContent;
    private ImageView iv_item0;
    private ImageView iv_item1;
    private ImageView iv_item2;
    private int sdpz_val = -1;//上电配置值
    private onInputDialogListener mListener;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_cancel) {
            dismiss();
        } else if (view.getId() == R.id.txt_ok) {
            if (mListener != null) {
                mListener.onEnsure(sdpz_val);
            }
            dismiss();
        } else if (view.getId() == R.id.ll_item0) {
            setV0();
        } else if (view.getId() == R.id.ll_item1) {
            setV1();
        } else if (view.getId() == R.id.ll_item2) {
            setV2();
        }
    }

    private void setV0() {
        iv_item0.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_chose_slices));
        iv_item1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_unchose_slices));
        iv_item2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_unchose_slices));
        sdpz_val = 0;
    }

    private void setV1() {
        iv_item0.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_unchose_slices));
        iv_item1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_chose_slices));
        iv_item2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_unchose_slices));
        sdpz_val = 1;
    }

    private void setV2() {
        iv_item0.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_unchose_slices));
        iv_item1.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_unchose_slices));
        iv_item2.setImageDrawable(getContext().getResources().getDrawable(R.drawable.dx_chose_slices));
        sdpz_val = 2;
    }

    public RadioDialog(Context context) {
        super(context, com.base.library.R.style.Dialog);
        init(context);

    }

    public RadioDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    /**
     * 上电配置 设置值
     *
     * @param val
     */
    public void setVal(int val) {
        switch (val) {
            case 0:
                setV0();
                break;
            case 1:
                setV1();
                break;
            case 2:
                setV2();
                break;
        }
        this.sdpz_val = val;
    }

    public void setDialogListener(onInputDialogListener listener) {
        mListener = listener;
    }

    private void init(Context context) {
        setContentView(R.layout.dialog_input);
        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);

        findViewById(R.id.txt_cancel).setOnClickListener(this);
        findViewById(R.id.txt_ok).setOnClickListener(this);

        findViewById(R.id.ll_item0).setOnClickListener(this);
        findViewById(R.id.ll_item1).setOnClickListener(this);
        findViewById(R.id.ll_item2).setOnClickListener(this);
        iv_item0 = findViewById(R.id.iv_item0);
        iv_item1 = findViewById(R.id.iv_item1);
        iv_item2 = findViewById(R.id.iv_item2);

    }

    public interface onInputDialogListener {
        void onEnsure(int content);
    }
}
