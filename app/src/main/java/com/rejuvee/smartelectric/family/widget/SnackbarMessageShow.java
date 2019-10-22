package com.rejuvee.smartelectric.family.widget;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.base.library.widget.topsnackbar.TSnackbar;
import com.rejuvee.smartelectric.family.R;

/**
 * Created by liuchengran on 2018/3/20.
 */

public class SnackbarMessageShow {
    private TSnackbar snackbar;
    private static SnackbarMessageShow instance;

    private SnackbarMessageShow() {

    }

    public static SnackbarMessageShow getInstance() {
        if (instance == null) {
            instance = new SnackbarMessageShow();
        }

        return instance;
    }

    public void showError(View view, String errorText) {
        snackbar = TSnackbar.make(view, errorText, TSnackbar.LENGTH_INDEFINITE);
//        snackbar.setIconRight(R.drawable.global_delete_logo_icon, 24); //Resize to bigger dp
        snackbar.setMaxWidth(3000);
        snackbar.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#000000"));
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }

    public void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public boolean isSnackbarShow() {
        if (snackbar != null) {
            return snackbar.isShown();
        }
        return false;
    }

}
