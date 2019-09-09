package com.rejuvee.smartelectric.family.activity.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.AppGlobalConfig;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.utils.utils;

/**
 * 设置IP 本地调试
 */
public class HiddenDialogActivity extends BaseActivity {
    private final String BASE_URL = "rejuvee.net";
    private final String URL_START_SSL = "https://";
    private final String URL_END_SSL = "/";
    private final String URL_START = "http://";
    private final String URL_END = ":8080/";
    private EditText etIP;
    private Button btnReset;
    private Button btnSave;
    private Button btnCancel;

    @Override
    protected int getLayoutResId() {
        return R.layout.active_hidden_dialog;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
//        setToolbarHide(true);
        etIP = (EditText) findViewById(R.id.et_ip);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
    }

    @Override
    protected void initData() {
        etIP.setText("192.168.1.182");
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGlobalConfig.HTTP_URL = URL_START_SSL + BASE_URL + URL_END_SSL;
                etIP.setText(BASE_URL);
                CustomToast.showCustomToast(HiddenDialogActivity.this, "已还原，可关闭窗口");
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etIP.getEditableText().toString();
                if (utils.isIP(str)) {
                    AppGlobalConfig.HTTP_URL = URL_START + str + URL_END;
                    etIP.setText(str);
                    CustomToast.showCustomToast(HiddenDialogActivity.this, "设置成功，可关闭窗口");
                } else {
                    CustomToast.showCustomErrorToast(HiddenDialogActivity.this, "请输入IP");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void dealloc() {

    }
}
