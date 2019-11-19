package com.rejuvee.smartelectric.family.activity.share;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.manager.PermissionManage;
import com.rejuvee.smartelectric.family.common.widget.dialog.WaitDialog;
import com.rejuvee.smartelectric.family.databinding.ActivityAddShareMemberBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.AddShareMemberViewModel;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import retrofit2.Call;

/**
 * 添加分享用户
 */
public class AddShareMemberActivity extends BaseActivity {
    private String TAG = "AddShareMemberActivity";
    //    private EditText edtScan;
    private WaitDialog mWaitDialog;
    //    private int request_code_scancode = 1001;
    private String collectId;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_add_share_member;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    private AddShareMemberViewModel mViewModel;

    @Override
    protected void initView() {
        ActivityAddShareMemberBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_share_member);
        mViewModel = ViewModelProviders.of(this).get(AddShareMemberViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        PermissionManage.getInstance().setCallBack(new PermissionManage.PermissionCallBack() {

            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied() {
                CustomToast.showCustomErrorToast(AddShareMemberActivity.this, getString(R.string.vs46));
            }
        }).hasCamera(this);

//        findViewById(R.id.ll_img_cancel).setOnClickListener(v -> finish());
//        findViewById(R.id.rl_scan_add).setOnClickListener(v -> {
//            Intent intent = new Intent(AddShareMemberActivity.this, CaptureActivity.class);
//            startActivityForResult(intent, request_code_scancode);
//        });
//        findViewById(R.id.st_finish).setOnClickListener(view -> addShareUser());
//        findViewById(R.id.iv_check).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//        edtScan = findViewById(R.id.edt_input_share);
//        iv_check = (ImageView) findViewById(R.id.iv_check);
//        ischeck = true;
//
//        iv_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ischeck == true) {   //默认点击  勾选     enable  1表示允许  0不允许
//                    ischeck = false;
//                    enable = 0;
//                    iv_check.setImageResource(R.drawable.weixuan);
//                } else {
//                    ischeck = true;
//                    enable = 1;
//                    iv_check.setImageResource(R.drawable.yixuan);
//                }
//            }
//        });
        mWaitDialog = new WaitDialog(this);
        collectId = getIntent().getStringExtra("collect_id");
    }

    private void addShareUser() {
        String userName = mViewModel.getShareName().getValue();//edtScan.getEditableText().toString();
        if (userName == null || userName.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getString(R.string.input_share_username));
            return;
        }

        mWaitDialog.show();
        //    private ImageView iv_check;
        //    private boolean ischeck;
        //1表示允许  0不允许  默认允许
        int enable = 1;
        currentCall = Core.instance(this).shareCollector(true, userName, collectId, enable, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddShareMemberActivity.this, getString(R.string.operator_sucess));
                finish();
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(AddShareMemberActivity.this, message);
                mWaitDialog.dismiss();
            }
        });
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.add_share);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onScanAdd(View view) {
            Intent intent = new Intent(view.getContext(), CaptureActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_SCAN_CODE);
        }

        public void onCommit(View view) {
            addShareUser();
        }

    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_SCAN_CODE) {
                String code = data.getStringExtra("code");
//                edtScan.setText(code);
                mViewModel.setShareName(code);

            }
        }
    }

}
