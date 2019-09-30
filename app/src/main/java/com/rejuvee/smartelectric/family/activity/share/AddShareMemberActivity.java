package com.rejuvee.smartelectric.family.activity.share;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.PermissionUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.PermisionManage;
import com.rejuvee.smartelectric.family.widget.dialog.WaitDialog;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

/**
 * 添加分享用户
 */
public class AddShareMemberActivity extends BaseActivity {

    private EditText edtScan;
    private WaitDialog mWaitDialog;
    private int request_code_scancode = 1001;
    private String collectId;
    //    private ImageView iv_check;
    private boolean ischeck;
    private int enable = 1;  //1表示允许  0不允许  默认允许

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_share_member;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.rl_scan_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermisionManage.getInstance().startRequest(AddShareMemberActivity.this, new String[]{Manifest.permission.CAMERA}, new PermissionUtils.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            Intent intent = new Intent(AddShareMemberActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, CommonRequestCode.REQUEST_SCAN_CODE);
                        }

                        @Override
                        public void onPermissionDenied(String[] deniedPermissions) {
                            CustomToast.showCustomErrorToast(AddShareMemberActivity.this, getString(R.string.vs46));
                        }
                    });
                } else {
                    Intent intent = new Intent(AddShareMemberActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, request_code_scancode);
                }
            }
        });
        findViewById(R.id.st_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addShareUser();
            }
        });
//        findViewById(R.id.iv_check).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
        edtScan = (EditText) findViewById(R.id.edt_input_device);
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
    }

    @Override
    protected void initData() {
        collectId = getIntent().getStringExtra("collect_id");
    }

    private void addShareUser() {
        String userName = edtScan.getEditableText().toString();
        if (userName.isEmpty()) {
            CustomToast.showCustomToast(this, getString(R.string.input_share_username));
            return;
        }

        mWaitDialog.show();
        Core.instance(this).shareCollector(true, userName, collectId, enable, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddShareMemberActivity.this, getString(R.string.operator_sucess));
                finish();
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(AddShareMemberActivity.this, getString(R.string.op_fail));
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

    @Override
    protected void dealloc() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == request_code_scancode) {
                String code = data.getStringExtra("code");
                edtScan.setText(code);
            }
        }
    }

}
