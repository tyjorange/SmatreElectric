package com.rejuvee.smartelectric.family.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuvee.smartelectric.family.R;


/**
 * Created by liuchengran on 2017/8/31.
 */

public class InputDialog extends Dialog implements View.OnClickListener {
    private TextView txtTitle, txtCancel, txtOk;
    private EditText editContent;
    private onInputDialogListener mListener;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_cancel) {
            if (mListener != null)
                mListener.onCancel();
        } else if (view.getId() == R.id.txt_ok) {
            String content = editContent.getEditableText().toString();
            if (content == null || content.isEmpty()) {
                Toast.makeText(getContext(), getContext().getString(R.string.prompt), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mListener != null)
                mListener.onEnsure(content);
        }

        dismiss();
    }

    public interface onInputDialogListener {
        void onCancel();

        void onEnsure(String content);
    }

    public InputDialog(Context context) {
        super(context, com.base.library.R.style.Dialog);
        init(context);

    }

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public void setDialogListener(onInputDialogListener listener) {
        mListener = listener;
    }

    private void init(Context context) {
        setContentView(R.layout.dialog_input);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtCancel = (TextView) findViewById(R.id.txt_cancel);
        txtOk = (TextView) findViewById(R.id.txt_ok);

        editContent = (EditText) findViewById(R.id.edit_content);

        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        txtOk.setOnClickListener(this);
        txtCancel.setOnClickListener(this);

    }

    public void setInputType(int inputType) {
        editContent.setInputType(inputType);
    }

    public void setHint(String hint) {
        editContent.setHint(hint);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setContent(String content) {
        editContent.setText(content);
    }

}
