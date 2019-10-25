package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.collector.autolink.AutoLinkActivity;
import com.rejuvee.smartelectric.family.activity.energy.EnergySavingInformationActivity;
import com.rejuvee.smartelectric.family.activity.energy.StatementActivity;
import com.rejuvee.smartelectric.family.activity.logger.SecurityInformationActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchModifyActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchStatusActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.YaoKongActivity;
import com.rejuvee.smartelectric.family.activity.report.ReportActivity;
import com.rejuvee.smartelectric.family.activity.share.ShareListActivity;
import com.rejuvee.smartelectric.family.activity.timer.TimerActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorUpgradeInfo;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTipWithoutOkCancel;

/**
 * 集中器设备详情
 */
public class CollectorDetailActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "CollectorDetailActivity";
    private CollectorBean collectorBean;
    private CollectorUpgradeInfo collectorUpgradeInfo;
    private Context mContext;
    private TextView tvUpgrade;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        ImageView backBtn = findViewById(R.id.img_cancel);
        TextView txtShareTip = findViewById(R.id.txt_share_tip);
        TextView collectorName = findViewById(R.id.collector_name);

        TextView tvKongzhi = findViewById(R.id.txt_kongzhi);
        TextView tvShishi = findViewById(R.id.txt_shishi);
        TextView tvBaobiao = findViewById(R.id.txt_baobiao);
        TextView tvDingshi = findViewById(R.id.txt_dingshi);

        TextView tvJieneng = findViewById(R.id.txt_jieneng);
        TextView tvAnquan = findViewById(R.id.txt_anquan);
        tvUpgrade = findViewById(R.id.txt_upgrade);
        TextView tvZhuanye = findViewById(R.id.txt_zhuanye);

        TextView tvDianlu = findViewById(R.id.txt_dianlu);
        TextView tvDianbiao = findViewById(R.id.txt_dianbiao);
        TextView tvDianqi = findViewById(R.id.txt_dianqi);
        TextView tvShare = findViewById(R.id.txt_share);

        TextView tv_wifi = findViewById(R.id.txt_wifi_set);

        ImageView img_remove = findViewById(R.id.img_remove);

        backBtn.setOnClickListener(this);

        tvKongzhi.setOnClickListener(this);
        tvShishi.setOnClickListener(this);
        tvBaobiao.setOnClickListener(this);
        tvDingshi.setOnClickListener(this);

        tvJieneng.setOnClickListener(this);
        tvAnquan.setOnClickListener(this);
        tvUpgrade.setOnClickListener(this);
        tvZhuanye.setOnClickListener(this);

        tvDianlu.setOnClickListener(this);
        tvDianbiao.setOnClickListener(this);
        tvDianqi.setOnClickListener(this);
        tvShare.setOnClickListener(this);

        if (collectorBean.getIoType() == 2) {
            tv_wifi.setOnClickListener(this);
            tv_wifi.setVisibility(View.VISIBLE);
        }

        img_remove.setOnClickListener(this);

//        tvConfig.setEnabled(collectorBean.beShared != 1);
        if (collectorBean.beShared == 1) {
            tvDingshi.setEnabled(false);
            tvDingshi.setTextColor(getResources().getColor(R.color.gray));
            Drawable top1 = getResources().getDrawable(R.drawable.collector_dskg_gray);
            tvDingshi.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);

            tvShare.setEnabled(false);
            tvShare.setTextColor(getResources().getColor(R.color.gray));
            Drawable top2 = getResources().getDrawable(R.drawable.collector_wdfx_gray);
            tvShare.setCompoundDrawablesWithIntrinsicBounds(null, top2, null, null);

            tvUpgrade.setEnabled(false);
            tvUpgrade.setTextColor(getResources().getColor(R.color.gray));
            Drawable top3 = getResources().getDrawable(R.drawable.collector_upgrade_gray);
            tvUpgrade.setCompoundDrawablesWithIntrinsicBounds(null, top3, null, null);

            tvZhuanye.setEnabled(false);
            tvZhuanye.setTextColor(getResources().getColor(R.color.gray));
            Drawable top4 = getResources().getDrawable(R.drawable.collector_zhuanye_gray);
            tvZhuanye.setCompoundDrawablesWithIntrinsicBounds(null, top4, null, null);
        }
//        tvWifi.setEnabled(collectorBean.beShared != 1);


//        if (collectorBean.beShared != 1) {//不是被分享的集中器
//            tvUpgrade.setEnabled(haveNewUpgrade());
//        } else {
//            tvUpgrade.setEnabled(false);
//        }
        collectorName.setText(String.format("%s%s", getString(R.string.vs2), collectorBean.getCode()));
        if (collectorBean.ownerUser != null) {
            if (collectorBean.ownerUser.getNickName() == null) {
                txtShareTip.setText(String.format("%s%s", collectorBean.ownerUser.getUsername(), getString(R.string.vs3)));
            } else {
                txtShareTip.setText(String.format("%s%s", collectorBean.ownerUser.getNickName(), getString(R.string.vs3)));
            }
            img_remove.setVisibility(View.INVISIBLE);
        } else {
            txtShareTip.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initData() {
        //不是被分享的集中器 且有新版本
        if (collectorBean.beShared != 1 && haveNewUpgrade()) {
            getCollectorUpgrade();
        }
    }

//    @Override
//    protected String getToolbarTitle() {
//        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        return collectorBean.getDeviceName();
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }

//    private boolean isAuthorized(int id) {
//        if (collectorBean.beShared == 0) {
//            return true;
//        }
//        return id == R.id.txt_quxian || id == R.id.txt_baobiao;
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (!isAuthorized(id)) {
//            CustomToast.showCustomErrorToast(this, getString(R.string.permission_error));
//            return;
//        }
        Intent intent;
        switch (id) {
            case R.id.img_cancel://返回
                finish();
                break;
            case R.id.txt_kongzhi:
                intent = new Intent(this, YaoKongActivity.class);
//                intent = new Intent(this, SwitchTreeActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                intent.putExtra("viewType", YaoKongActivity.YAOKONG);
                startActivity(intent);
                break;
            case R.id.txt_shishi:
                intent = new Intent(this, SwitchStatusActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_baobiao:
                intent = new Intent(this, ReportActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_dingshi:
                intent = new Intent(this, TimerActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_jieneng:
                intent = new Intent(this, EnergySavingInformationActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_anquan:
                intent = new Intent(this, SecurityInformationActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_upgrade:
                if (haveNewUpgrade()) {
                    intent = new Intent(this, UpgradeDialogActivity.class);
                    intent.putExtra("collectorBean", collectorBean);
                    intent.putExtra("collectorUpgradeInfo", collectorUpgradeInfo);
                    startActivityForResult(intent, CommonRequestCode.REQUEST_COLLECTOR_UPGRADE);
                } else {
                    CustomToast.showCustomToast(mContext, "电箱已是最新版本:" + collectorBean.getVerMajor() + "." + collectorBean.getVerMinor());
                }
                break;
            case R.id.txt_zhuanye:
                intent = new Intent(this, CollectorDetail2Activity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_dianlu:
                intent = new Intent(this, SwitchModifyActivity.class);
                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.XIANLU_XIUGAI);
                startActivity(intent);
                break;
            case R.id.txt_dianbiao:
                intent = new Intent(this, StatementActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_dianqi:
                DialogTipWithoutOkCancel d = new DialogTipWithoutOkCancel(this);
                d.hiddenTitle();
                d.showImg();
                d.setContent(getString(R.string.vs30));
                d.show();
//                intent = new Intent(this, MyEleApplianceActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
                break;
            case R.id.txt_share:
                intent = new Intent(this, ShareListActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_wifi_set:
                intent = new Intent(this, AutoLinkActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.img_remove:
                onDel();
                break;
            default:
                break;
        }
    }

    /**
     * 查询集中器版本升级情况
     */
    private void getCollectorUpgrade() {
        Core.instance(CollectorDetailActivity.this).getCollectorUpgrade(
                collectorBean.getCollectorID(),
                collectorBean.getVerMajorNew(),
                collectorBean.getVerMinorNew(),
                collectorBean.getFileID(),
                new ActionCallbackListener<CollectorUpgradeInfo>() {

                    @Override
                    public void onSuccess(CollectorUpgradeInfo data) {
                        collectorUpgradeInfo = data;
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        Log.w(TAG, message);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_COLLECTOR_UPGRADE) {
                getCollectorUpgrade();
            }
        }
    }

    /**
     * 判断是否有集中器软件版本升级
     *
     * @return
     */
    private boolean haveNewUpgrade() {
        return collectorBean.getVerMajorNew() * 256 + collectorBean.getVerMinorNew() > collectorBean.getVerMajor() * 256 + collectorBean.getVerMinor();
    }

    /**
     * 解绑电箱
     */
    public void onDel() {
        DialogTip dialogTip = new DialogTip(mContext, false);
        dialogTip.setTitle(mContext.getResources().getString(R.string.vs231)).setRedBtn()
                .setContent(mContext.getResources().getString(R.string.delete_device))
                .setDialogListener(new DialogTip.onEnsureDialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onEnsure() {
                        Core.instance(mContext).unbindDevice(collectorBean.getCode(), new ActionCallbackListener<Void>() {
                            @Override
                            public void onSuccess(Void data) {
//                                listDeviceData.remove(position);
//                                mDeviceAdapter.notifyDataSetChanged();
//                                mDeviceAdapter.setEditMode(false);
//                                tv_collector_count.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.vs13), listDeviceData.size()));
                                CustomToast.showCustomToast(mContext, getString(R.string.vs66));
                                finish();
                            }

                            @Override
                            public void onFailure(int errorEvent, String message) {
                                CustomToast.showCustomErrorToast(mContext, message);
                            }
                        });

                    }
                }).show();
    }
}
