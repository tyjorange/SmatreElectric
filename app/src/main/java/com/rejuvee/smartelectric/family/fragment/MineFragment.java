package com.rejuvee.smartelectric.family.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.core.AbstractBaseFragment;
import com.base.library.utils.SizeUtils;
import com.base.library.view.CircleImageView;
import com.google.android.material.snackbar.Snackbar;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.energy.TimePriceActivity;
import com.rejuvee.smartelectric.family.activity.mine.PerInfoActivity;
import com.rejuvee.smartelectric.family.activity.mine.SettingsActivity;
import com.rejuvee.smartelectric.family.activity.mine.ThridBindActivity;
import com.rejuvee.smartelectric.family.activity.mine.ThridPushActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.common.widget.PopwindowQCode;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfo;
import com.rejuvee.smartelectric.family.model.nativedb.AccountInfoRealm;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * 我的 - 个人信息 TAB
 */
@Deprecated
public class MineFragment extends AbstractBaseFragment implements View.OnClickListener {
    private static final String TAG = "MineFragment";
    private CircleImageView ivHead;//头像view
    private TextView tvNick;//昵称view
    private TextView tvUsername;//用户名view
    private String nickname;
    private String username;
    private String telephone;
    private String headImgurl;
    private String wechatUnionID;
    private String qqUnionID;
    private SharedPreferences spinformation;
    private File tempFile;
    private Uri tempUri;
    private AccountInfo cacheAccount;

    private ImageView ivUserQCode;
    private PopwindowQCode popwindowQCode;

    public static boolean NEED_REFRESH = false;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View v) {

        tvUsername = v.findViewById(R.id.tv_phone);
        tvNick = v.findViewById(R.id.tv_nick_name);
        ivHead = v.findViewById(R.id.iv_head);
        ivUserQCode = v.findViewById(R.id.iv_scan_code);
        getUserMsg();
        /*getCacheAccount();
        buildUri();
        intiData();*/

        v.findViewById(R.id.ll_about).setOnClickListener(this);
        v.findViewById(R.id.ll_question).setOnClickListener(this);
        v.findViewById(R.id.ll_bind).setOnClickListener(this);
        v.findViewById(R.id.ll_push).setOnClickListener(this);
        v.findViewById(R.id.ll_push).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.ll_set).setOnClickListener(this);
        v.findViewById(R.id.ll_user_info).setOnClickListener(this);
        v.findViewById(R.id.ll_time_price).setOnClickListener(this);
        v.findViewById(R.id.iv_scan_code).setOnClickListener(this);

        popwindowQCode = new PopwindowQCode(getContext());
    }


    public void getUserMsg() {
        //显示在该界面
        Core.instance(getContext()).getUserMsg(new ActionCallbackListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg data) {
                telephone = data.getPhone();
                nickname = data.getNickName();
                username = data.getUsername();
                headImgurl = data.getHeadImg();
                wechatUnionID = data.getWechatUnionID();
                qqUnionID = data.getQqUnionID();
                tvNick.setText(nickname);
                tvUsername.setText(username);
                if (headImgurl.equals("")) {
                    headImgurl = null;
                    ivHead.setImageResource(R.drawable.icon_user_default);
                } else {
                    if (!headImgurl.startsWith("https://")) {
                        headImgurl = "https://" + headImgurl;
                    }
                    Picasso.with(getContext()).load(headImgurl).into(ivHead);
                }
                setQCodeUserName();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
                ivHead.setImageResource(R.drawable.icon_user_default);
            }
        });
    }

    private void setQCodeUserName() {
        Bitmap bmpUser = ValidateUtils.createQRcodeImage(username, SizeUtils.dp2px(100), SizeUtils.dp2px(100));
        if (bmpUser != null) {
            ivUserQCode.setImageBitmap(bmpUser);
            popwindowQCode.setQCodeImageBitmap(bmpUser);
        }
    }
  /*  public void buildUri() {
        String root = getContext().getExternalCacheDir().getAbsolutePath();
        String dirStr = root + File.separator + userName;
        File dir = new File(dirStr);
        if(!dir.exists()){
            dir.mkdirs();
        }
        tempFile = new File(dirStr + File.separator + "head_img" + ".jpg");
        tempUri = Uri.fromFile(tempFile);
    }*/

    private void getCacheAccount() {
        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
        accountInfoRealm.open();
        List<AccountInfo> listAccount = accountInfoRealm.getAccountInfoList();
        if (listAccount != null && listAccount.size() > 0) {
            cacheAccount = listAccount.get(0);
            username = cacheAccount.getUserName();
        }
        accountInfoRealm.close();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_about:
                Snackbar.make(getView(), R.string.mine_subtitle_about, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.ll_question:
                Snackbar.make(getView(), R.string.mine_subtitle_question, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.ll_bind:
                intent = new Intent(getContext(), ThridBindActivity.class);
                intent.putExtra("wechatUnionID", wechatUnionID);
                intent.putExtra("qqUnionID", qqUnionID);
//                startActivity(intent);
                startActivityForResult(intent, CommonRequestCode.REQUEST_THIRD_BIND);
                break;
            case R.id.ll_push:
                intent = new Intent(getContext(), ThridPushActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_set:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.ll_user_info:
                intent = new Intent();
                intent.putExtra("telephone", telephone);
                intent.putExtra("nickname", nickname);
                intent.putExtra("username", username);
                intent.putExtra("headImgurl", headImgurl);
                intent.setClass(getContext(), PerInfoActivity.class);
                startActivityForResult(intent, CommonRequestCode.REQUEST_USER_INFO);
                break;
            case R.id.ll_time_price:
                startActivity(new Intent(getContext(), TimePriceActivity.class));
                break;
            case R.id.iv_scan_code:
                if (username != null && !username.isEmpty()) {
                    popwindowQCode.show();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NEED_REFRESH) {
            NEED_REFRESH = false;
            getUserMsg();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonRequestCode.REQUEST_THIRD_BIND || requestCode == CommonRequestCode.REQUEST_USER_INFO) {
            Log.i(TAG, getString(R.string.vs166));
            getUserMsg();
        }
    }
}
