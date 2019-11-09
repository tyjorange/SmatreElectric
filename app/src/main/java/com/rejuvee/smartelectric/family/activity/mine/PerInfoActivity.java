package com.rejuvee.smartelectric.family.activity.mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.SizeUtils;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.utils.GetPhotoUtil;
import com.rejuvee.smartelectric.family.common.utils.ImageUtil;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.databinding.ActivityInformationBinding;
import com.rejuvee.smartelectric.family.databinding.PhotoChooseDialogBinding;
import com.rejuvee.smartelectric.family.model.bean.Headimg;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;

import java.io.File;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 个人信息
 */
public class PerInfoActivity extends BaseActivity implements ImageUtil.CropHandler {
    private static final String TAG = "PerInformationActivity";
    private Dialog dialog;
    //    private String nickname;
//    private String username;
    //    private String headImgurl;
//    private String telephone;
//    private Button bu_img;
    //    private Button bu_getphto;
//    private Button dimiss;
    //    private CircleImageView img_head;
//    private TextView txt_phone;
//    private TextView txt_nickname;
//    private TextView txt_username;
    private File tempFile;
    private Uri tempUri;
    private AccountInfo cacheAccount;


    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_information;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    ActivityInformationBinding mBinding;
    PerInfoViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_information);
        mViewModel = ViewModelProviders.of(this).get(PerInfoViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        img_head = findViewById(R.id.img_head);
//        txt_phone = findViewById(R.id.txt_phone);
//        txt_nickname = findViewById(R.id.txt_nickname);
//        txt_username = findViewById(R.id.txt_username);
//        findViewById(R.id.ll_headimg).setOnClickListener(this);
//        findViewById(R.id.ll_getphone).setOnClickListener(this);
//        findViewById(R.id.ll_nickname).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
//        nickname = intent.getStringExtra("nickname");
        mViewModel.setNickname(intent.getStringExtra("nickname"));
//        username = intent.getStringExtra("username");
        mViewModel.setUsername(intent.getStringExtra("username"));
//        headImgurl = intent.getStringExtra("headImgurl");
        mViewModel.setHeadImgUrl(intent.getStringExtra("headImgurl"));
//        telephone = intent.getStringExtra("telephone");
        mViewModel.setPhone(intent.getStringExtra("telephone"));
//        txt_phone.setText(telephone);
//        txt_nickname.setText(nickname);
//        txt_username.setText(username);

//        if (headImgurl == null) {
//            img_head.setImageResource(R.drawable.icon_user_default);
//        } else {
//            RequestCreator load = Picasso.with(this).load(headImgurl);
//            setHeaderIcon(load);
//        }

        getUserMsg();
        getCacheAccount();
        buildUri();

        Bitmap bmpUserQR = ValidateUtils.createQRcodeImage(mViewModel.getNickname().getValue(), SizeUtils.dp2px(100), SizeUtils.dp2px(100));
//        ImageView ivQcode = findViewById(R.id.iv_qcode);
        mBinding.ivQcode.setImageBitmap(bmpUserQR);
    }

    /**
     * 设置头像
     */
//    private void setHeaderIcon(RequestCreator load) {
//        load.into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                img_head.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                img_head.setImageResource(R.drawable.icon_user_default);
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        });
//    }
    private void getCacheAccount() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.open();
        List<AccountInfo> listAccount = accountInfoRealm.getAccountInfoList();
        if (listAccount != null && listAccount.size() > 0) {
            cacheAccount = listAccount.get(0);
//            username = cacheAccount.getUserName();
            mViewModel.setUsername(cacheAccount.getUserName());
        }
        accountInfoRealm.close();
    }

    private void getUserMsg() {
        Core.instance(this).getUserMsg(new ActionCallbackListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg data) {
//                nickname = data.getNickName();
                mViewModel.setNickname(data.getNickName());
//                telephone = data.getPhone();
                mViewModel.setPhone(data.getPhone());
//                username = data.getUsername();
                mViewModel.setUsername(data.getUsername());
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
//                img_head.setImageResource(R.drawable.icon_user_default);
            }
        });
    }

    private void buildUri() {
        String root = Objects.requireNonNull(getContext().getExternalCacheDir()).getAbsolutePath();
        String dirStr = root + File.separator + mViewModel.getUsername().getValue();
        File dir = new File(dirStr);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        tempFile = new File(dirStr + File.separator + System.currentTimeMillis() + ".png");
        tempUri = Uri.fromFile(tempFile);
//        tempUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName(), tempFile);
    }

//    private void intoHeadview(String headImgurl) {
//        tempFile = new File(headImgurl);
//        Log.i("PATH", tempFile.getPath());
//        if (!tempFile.exists()) {
//            img_head.setImageResource(R.drawable.icon_user_default);
//        } else {
//            String headPath = "file://" + tempFile.getPath();
//            Picasso.with(this).invalidate(headPath);
//            Picasso.with(this).load("file://" + tempFile.getPath()).stableKey(headPath).into(img_head);
//        }
//    }

//    @Override
//    public void onClick(View v) {
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.ll_headimg:
//                getHeadview();
//                break;
//            case R.id.ll_getphone:
//                intent = new Intent();
//                intent.putExtra("phone_num", mViewModel.getPhone().getValue());
//                intent.putExtra("position", 0);
//                intent.setClass(this, SetInfoActivity.class);
////                startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_PHONE_NUMBER);
//                break;
//            case R.id.ll_nickname:
//                intent = new Intent();
//                intent.putExtra("nickname", mViewModel.getNickname().getValue());
//                intent.putExtra("position", 1);
//                intent.setClass(this, SetInfoActivity.class);
////                startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_NICKNAME);
//                break;
////            case R.id.ll_username:
////                intent = new Intent();
////                intent.putExtra("username", username);
////                intent.putExtra("position", 2);
////                intent.setClass(this, SetInfoActivity.class);
////                startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_USERNAME);
////                break;
//            case R.id.btn_img:
//                dialog.dismiss();
//                //选择相册
//                Intent galleryIntent = ImageUtil
//                        .getCropHelperInstance(this)
//                        .buildGalleryIntent();
//                startActivityForResult(galleryIntent, ImageUtil.REQUEST_GALLERY);
//                break;
////            case R.id.bu_getphto:
////                dialog.dismiss();
////                //拍摄照片
////                Intent cameraIntent = ImageUtil
////                        .getCropHelperInstance(this)
////                        .buildCameraIntent();
////                startActivityForResult(cameraIntent, ImageUtil.REQUEST_CAMERA);
////                break;
//            case R.id.dimiss:
//                dialog.dismiss();
//                break;
//        }
//    }

    private void getHeadview(View view) {
        PhotoChooseDialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(view.getContext()), R.layout.photo_choose_dialog, null, false);
        dialogBinding.setPresenter(new Presenter());
        dialogBinding.setLifecycleOwner(this);
//        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
//        bu_img = view.findViewById(R.id.btn_img);
//        bu_getphto = (Button) view.findViewById(R.id.bu_getphto);
//        dimiss = view.findViewById(R.id.dimiss);
//        bu_img.setOnClickListener(this);
//        bu_getphto.setOnClickListener(this);
//        dimiss.setOnClickListener(this);

        dialog = new Dialog(view.getContext(), R.style.transparentFrameWindowStyle);
        dialog.setContentView(dialogBinding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        //设置显示动画
        if (window != null) {
            window.setWindowAnimations(R.style.main_menu_animstyle);
        }
        WindowManager.LayoutParams wl = null;
        if (window != null) {
            wl = window.getAttributes();
        }
        if (wl != null) {
            wl.x = 0;
            wl.y = getWindowManager().getDefaultDisplay().getHeight();
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            //修改电话号码
            if (requestCode == CommonRequestCode.REQUEST_UPDATE_PHONE_NUMBER) {
                String telephone = data.getExtras().getString("exchangphone");
                updateUserMes(null, null, null, telephone);
                //修改昵称
            } else if (requestCode == CommonRequestCode.REQUEST_UPDATE_NICKNAME) {
                String nickname = data.getExtras().getString("exchangnick");
                updateUserMes(null, nickname, null, null);
                //修改用户名
            } else if (requestCode == CommonRequestCode.REQUEST_UPDATE_USERNAME) {
                String username = data.getExtras().getString("exchangname");
                updateUserMes(username, null, null, null);
            }
        }
        if (requestCode == 161) {// ??
            tempUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName(), tempFile);
        }
        //更新头像 or 获得图片
        ImageUtil.getCropHelperInstance(this).sethandleResultListerner(PerInfoActivity.this, requestCode, resultCode, data, tempUri);
    }

    //更新数据
    private void updateUserMes(final String userName, final String nickName, String headImg, final String phone) {
        Core.instance(this).updateUserMsg(userName, nickName, headImg, phone, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(PerInfoActivity.this, getString(R.string.vs187));
//                if (nickName != null) {
//                    txt_nickname.setText(nickname);
//                } else if (phone != null) {
//                    txt_phone.setText(telephone);
//                } else if (userName != null) {
//                    txt_username.setText(username);
//                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(PerInfoActivity.this, getString(R.string.vs188) + message);
            }
        });
    }

    @Override
    public void onPhotoCropped(Bitmap imgUrl, int requestCode) {
        switch (requestCode) {
            case ImageUtil.RE_GALLERY:
                addHeadimg(imgUrl);
                break;
            case ImageUtil.RE_CAMERA:
                addHeadimg(imgUrl);
                break;
        }
    }

    //添加图片
    private void addHeadimg(final Bitmap imgUrl) {
        Core.instance(this).uploadHeadImg(getPhotoPart(), new ActionCallbackListener<Headimg>() {
            @Override
            public void onSuccess(Headimg data) {
//                String oldheadImgurl = data.getHeadImg();
//                img_head.setImageBitmap(imgUrl);
                CustomToast.showCustomToast(PerInfoActivity.this, getString(R.string.vs138));
                Log.i(TAG, data.getHeadImg());
                updateUserMes(null, null, data.getHeadImg(), null);
                /*  if (headImgurl.equals("")) {
                        updateUserMes(userkey, null, data.getHeadImg(), null);
                    }else{
                        updateUserMes(userkey, null, data.getHeadImg(), null);
                        //保存服务器  添加修改图片
                }*/
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(PerInfoActivity.this, getString(R.string.vs144));
            }
        });
//        MineFragment.NEED_REFRESH = true;
    }

    private MultipartBody.Part getPhotoPart() {
        if (tempUri == null)
            return null;
        File file1 = new File(Objects.requireNonNull(GetPhotoUtil.getPath(this, tempUri)));
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file1);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        return MultipartBody.Part.createFormData("photo", file1.getName(), requestFile);
    }

    @Override
    public void onCropCancel() {
        Log.i(TAG, "onCropCancel");
    }

    @Override
    public void onCropFailed(String message) {
        Log.i(TAG, message);
    }

    @Override
    public Activity getContext() {
        return PerInfoActivity.this;
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.information);
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

        public void onChangeHead(View view) {
            getHeadview(view);
        }

        public void onChangePhone(View view) {
            Intent intent = new Intent();
            intent.putExtra("phone_num", mViewModel.getPhone().getValue());
            intent.putExtra("position", 0);
            intent.setClass(view.getContext(), SetInfoActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_PHONE_NUMBER);
        }

        public void onChangeNick(View view) {
            Intent intent = new Intent();
            intent.putExtra("nickname", mViewModel.getNickname().getValue());
            intent.putExtra("position", 1);
            intent.setClass(view.getContext(), SetInfoActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_NICKNAME);
        }

        public void onChosePhoto(View view) {
            dialog.dismiss();
            //选择相册
            Intent galleryIntent = ImageUtil
                    .getCropHelperInstance(view.getContext())
                    .buildGalleryIntent();
            startActivityForResult(galleryIntent, ImageUtil.REQUEST_GALLERY);
        }

        public void noChosePhoto(View view) {
            dialog.dismiss();
        }
    }

    @Override
    protected void dealloc() {
    }

}
