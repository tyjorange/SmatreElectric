package com.rejuvee.smartelectric.family.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.rejuvee.smartelectric.family.R;

import java.util.Objects;

/**
 * 二维码弹窗
 * Created by liuchengran on 2018/12/29.
 */
@Deprecated
public class PopwindowQCode extends Dialog {
    private PopupWindow popupWindow;
    private ImageView ivQcode;

    public PopwindowQCode(Context context) {
        super(context, com.base.library.R.style.Dialog);
        View contentView = View.inflate(context, R.layout.pop_qcode, null);

        setContentView(contentView);
        setCanceledOnTouchOutside(true);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = Objects.requireNonNull(dialogWindow).getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.6); // 高度设置为屏幕的0.6
        lp.height = (int) (d.widthPixels * 0.6);
        dialogWindow.setAttributes(lp);

/*
        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(contentView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });*/

        ivQcode = contentView.findViewById(R.id.iv_qcode);

    }

    /*public void show(View parentView) {
        //popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }*/

    public void setQCodeImageBitmap(Bitmap bmpQcode) {
        ivQcode.setImageBitmap(bmpQcode);
    }


}
