package com.rejuvee.smartelectric.family.activity.login;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.AppGlobalConfig;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.databinding.ActiveHiddenDialogBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.HiddenDialogViewModel;

/**
 * 设置IP 本地调试
 */
public class HiddenDialogActivity extends BaseActivity {
    private final String BASE_URL = "rejuvee.net";
    private final String URL_START_SSL = "https://";
    private final String URL_END_SSL = "/";
    private final String URL_START = "http://";
    private final String URL_END = ":8080/";
//    private EditText etIP;
//    private Button btnReset;
//    private Button btnSave;
//    private Button btnCancel;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.active_hidden_dialog;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private HiddenDialogViewModel mViewModel;

    @Override
    protected void initView() {
        ActiveHiddenDialogBinding mBinding = DataBindingUtil.setContentView(this, R.layout.active_hidden_dialog);
        mViewModel = ViewModelProviders.of(this).get(HiddenDialogViewModel.class);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        setToolbarHide(true);
//        etIP = findViewById(R.id.et_ip);
//        btnReset = findViewById(R.id.btn_reset);
//        btnSave = findViewById(R.id.btn_save);
//        btnCancel = findViewById(R.id.btn_cancel);
//        etIP.setText("192.168.1.182");
        mViewModel.setIp("192.168.1.182");
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onReset(View view) {
            AppGlobalConfig.HTTP_URL = URL_START_SSL + BASE_URL + URL_END_SSL;
//            etIP.setText(BASE_URL);
            mViewModel.setIp(BASE_URL);
            CustomToast.showCustomToast(HiddenDialogActivity.this, "已还原，可关闭窗口");
        }

        public void onSave(View view) {
            String str = mViewModel.getIp().getValue();// etIP.getEditableText().toString();
            if (ValidateUtils.isIP(str)) {
                AppGlobalConfig.HTTP_URL = URL_START + str + URL_END;
//                etIP.setText(str);
                mViewModel.setIp(BASE_URL);
                CustomToast.showCustomToast(HiddenDialogActivity.this, "设置成功，可关闭窗口");
            } else {
                CustomToast.showCustomErrorToast(HiddenDialogActivity.this, "请输入IP");
            }
        }
    }

    @Override
    protected void dealloc() {

    }
}
