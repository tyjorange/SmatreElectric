package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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
import com.rejuvee.smartelectric.family.activity.web.ChartsActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.databinding.ActivityDeviceDetailBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorUpgradeInfo;
import com.rejuvee.smartelectric.family.model.viewmodel.CollectorDetailViewModel;

import java.util.Objects;

/**
 * 集中器设备详情
 */
public class CollectorDetailActivity extends BaseActivity {
    private String TAG = "CollectorDetailActivity";
    private CollectorBean collectorBean;
    private CollectorUpgradeInfo collectorUpgradeInfo;
    //    private Context mContext;
//    private TextView tvUpgrade;

    //    @Override
    //    protected int getLayoutResId() {
    //        return R.layout.activity_device_detail;
    //    }
    //
    //    @Override
    //    protected int getMyTheme() {
    //        return 0;
    //    }
    @Override
    protected void initView() {
        ActivityDeviceDetailBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_detail);
        CollectorDetailViewModel mViewModel = ViewModelProviders.of(this).get(CollectorDetailViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        ImageView backBtn = findViewById(R.id.img_cancel);
//        TextView txtShareTip = findViewById(R.id.txt_share_tip);
//        TextView collectorName = findViewById(R.id.collector_name);

//        TextView tvKongzhi = findViewById(R.id.txt_kongzhi);
//        TextView tvShishi = findViewById(R.id.txt_shishi);
//        TextView tvBaobiao = findViewById(R.id.txt_baobiao);
//        TextView tvDingshi = findViewById(R.id.txt_dingshi);

//        TextView tvJieneng = findViewById(R.id.txt_jieneng);
//        TextView tvAnquan = findViewById(R.id.txt_anquan);
//        tvUpgrade = findViewById(R.id.txt_upgrade);
//        TextView tvZhuanye = findViewById(R.id.txt_zhuanye);

//        TextView tvDianlu = findViewById(R.id.txt_dianlu);
//        TextView tvDianbiao = findViewById(R.id.txt_dianbiao);
//        TextView tvDianqi = findViewById(R.id.txt_dianqi);
//        TextView tvShare = findViewById(R.id.txt_share);

//        TextView tv_wifi = findViewById(R.id.txt_wifi_set);
//        TextView txt_cha = findViewById(R.id.txt_cha);

//        ImageView img_remove = findViewById(R.id.img_remove);

//        backBtn.setOnClickListener(this);

//        tvKongzhi.setOnClickListener(this);
//        tvShishi.setOnClickListener(this);
//        tvBaobiao.setOnClickListener(this);
//        tvDingshi.setOnClickListener(this);

//        tvJieneng.setOnClickListener(this);
//        tvAnquan.setOnClickListener(this);
//        tvUpgrade.setOnClickListener(this);
//        tvZhuanye.setOnClickListener(this);

//        tvDianlu.setOnClickListener(this);
//        tvDianbiao.setOnClickListener(this);
//        tvDianqi.setOnClickListener(this);
//        tvShare.setOnClickListener(this);

        if (Objects.requireNonNull(collectorBean).getIoType() == R.drawable.io_wifi) {
            //            mBinding.txtWifiSet.setOnClickListener(this);
            mBinding.txtWifiSet.setVisibility(View.VISIBLE);
        }
//        txt_cha.setOnClickListener(this);

//        img_remove.setOnClickListener(this);

//        tvConfig.setEnabled(collectorBean.beShared != 1);
        if (collectorBean.beShared == 1) {
            mBinding.txtDingshi.setEnabled(false);
            mBinding.txtDingshi.setTextColor(getResources().getColor(R.color.gray));
            Drawable top1 = getResources().getDrawable(R.drawable.collector_dskg_gray);
            mBinding.txtDingshi.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);

            mBinding.txtShare.setEnabled(false);
            mBinding.txtShare.setTextColor(getResources().getColor(R.color.gray));
            Drawable top2 = getResources().getDrawable(R.drawable.collector_wdfx_gray);
            mBinding.txtShare.setCompoundDrawablesWithIntrinsicBounds(null, top2, null, null);

            mBinding.txtUpgrade.setEnabled(false);
            mBinding.txtUpgrade.setTextColor(getResources().getColor(R.color.gray));
            Drawable top3 = getResources().getDrawable(R.drawable.collector_upgrade_gray);
            mBinding.txtUpgrade.setCompoundDrawablesWithIntrinsicBounds(null, top3, null, null);

            mBinding.txtZhuanye.setEnabled(false);
            mBinding.txtZhuanye.setTextColor(getResources().getColor(R.color.gray));
            Drawable top4 = getResources().getDrawable(R.drawable.collector_zhuanye_gray);
            mBinding.txtZhuanye.setCompoundDrawablesWithIntrinsicBounds(null, top4, null, null);
        }
//        tvWifi.setEnabled(collectorBean.beShared != 1);
//        if (collectorBean.beShared != 1) {//不是被分享的集中器
//            tvUpgrade.setEnabled(haveNewUpgrade());
//        } else {
//            tvUpgrade.setEnabled(false);
//        }
//        collectorName.setText(String.format("%s%s", getString(R.string.vs2), collectorBean.getCode()));
        mViewModel.setCollectorBeanCode(String.format("%s%s", getString(R.string.vs2), collectorBean.getCode()));
        if (collectorBean.ownerUser != null) {
            if (collectorBean.ownerUser.getNickName() == null) {
//                txtShareTip.setText(String.format("%s%s", collectorBean.ownerUser.getUsername(), getString(R.string.vs3)));
                mViewModel.setSharedName(String.format("%s%s", collectorBean.ownerUser.getUsername(), getString(R.string.vs3)));
            } else {
//                txtShareTip.setText(String.format("%s%s", collectorBean.ownerUser.getNickName(), getString(R.string.vs3)));
                mViewModel.setSharedName(String.format("%s%s", collectorBean.ownerUser.getNickName(), getString(R.string.vs3)));
            }
            mBinding.imgRemove.setVisibility(View.INVISIBLE);
        } else {
            mBinding.txtShareTip.setVisibility(View.INVISIBLE);

        }
        //不是被分享的集中器 且有新版本
        if (collectorBean.beShared != 1 && haveNewUpgrade()) {
            getCollectorUpgrade();
        }
        if (!haveNewUpgrade()) {
            mBinding.txtUpgrade.setEnabled(false);
            mBinding.txtUpgrade.setTextColor(getResources().getColor(R.color.gray));
            Drawable top3 = getResources().getDrawable(R.drawable.collector_upgrade_gray);
            mBinding.txtUpgrade.setCompoundDrawablesWithIntrinsicBounds(null, top3, null, null);
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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onKongzhi(View view) {
            Intent intent = new Intent(view.getContext(), YaoKongActivity.class);
//                intent = new Intent(this, SwitchTreeActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            intent.putExtra("viewType", YaoKongActivity.YAOKONG);
            startActivity(intent);
        }

        public void onShishi(View view) {
            Intent intent = new Intent(view.getContext(), SwitchStatusActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onBaoBiao(View view) {
            Intent intent = new Intent(view.getContext(), ReportActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onDingshi(View view) {
            Intent intent = new Intent(view.getContext(), TimerActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onJieneng(View view) {
            Intent intent = new Intent(view.getContext(), EnergySavingInformationActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onAnquan(View view) {
            Intent intent = new Intent(view.getContext(), SecurityInformationActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onUpgrade(View view) {
            if (haveNewUpgrade()) {
                Intent intent = new Intent(view.getContext(), UpgradeDialogActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                intent.putExtra("collectorUpgradeInfo", collectorUpgradeInfo);
                startActivityForResult(intent, CommonRequestCode.REQUEST_COLLECTOR_UPGRADE);
//            } else {
//                CustomToast.showCustomToast(view.getContext(), "电箱已是最新版本:" + collectorBean.getVerMajor() + "." + collectorBean.getVerMinor());
            }
        }

        public void onZhuanye(View view) {
            Intent intent = new Intent(view.getContext(), CollectorDetail2Activity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onDianlu(View view) {
            Intent intent = new Intent(view.getContext(), SwitchModifyActivity.class);
            intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTree.XIANLU_XIUGAI);
            startActivity(intent);
        }

        public void onDianbiao(View view) {
            Intent intent = new Intent(view.getContext(), StatementActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onDianqi(View view) {
            DialogTipWithoutOkCancel d = new DialogTipWithoutOkCancel(view.getContext());
            d.hiddenTitle();
            d.showImg();
            d.setContent(getString(R.string.vs30));
            d.show();
        }

        public void onShare(View view) {
            Intent intent = new Intent(view.getContext(), ShareListActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onWifiSet(View view) {
            Intent intent = new Intent(view.getContext(), AutoLinkActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onCeshi(View view) {
            Intent intent = new Intent(view.getContext(), ChartsActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onRemove(View view) {
            onDel();
        }


    }

    @Override
    protected void dealloc() {

    }

//    private boolean isAuthorized(int id) {
//        if (collectorBean.beShared == 0) {
//            return true;
//        }
//        return id == R.id.txt_quxian || id == R.id.txt_baobiao;
//    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
////        if (!isAuthorized(id)) {
////            CustomToast.showCustomErrorToast(this, getString(R.string.permission_error));
////            return;
////        }
//        Intent intent;
//        switch (id) {
//            case R.id.img_cancel://返回
//                finish();
//                break;
//            case R.id.txt_kongzhi:
//                intent = new Intent(this, YaoKongActivity.class);
////                intent = new Intent(this, SwitchTreeActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", YaoKongActivity.YAOKONG);
//                startActivity(intent);
//                break;
//            case R.id.txt_shishi:
//                intent = new Intent(this, SwitchStatusActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_baobiao:
//                intent = new Intent(this, ReportActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_dingshi:
//                intent = new Intent(this, TimerActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_jieneng:
//                intent = new Intent(this, EnergySavingInformationActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_anquan:
//                intent = new Intent(this, SecurityInformationActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_upgrade:
//                if (haveNewUpgrade()) {
//                    intent = new Intent(this, UpgradeDialogActivity.class);
//                    intent.putExtra("collectorBean", collectorBean);
//                    intent.putExtra("collectorUpgradeInfo", collectorUpgradeInfo);
//                    startActivityForResult(intent, CommonRequestCode.REQUEST_COLLECTOR_UPGRADE);
//                } else {
//                    CustomToast.showCustomToast(this, "电箱已是最新版本:" + collectorBean.getVerMajor() + "." + collectorBean.getVerMinor());
//                }
//                break;
//            case R.id.txt_zhuanye:
//                intent = new Intent(this, CollectorDetail2Activity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_dianlu:
//                intent = new Intent(this, SwitchModifyActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
////                intent.putExtra("viewType", SwitchTree.XIANLU_XIUGAI);
//                startActivity(intent);
//                break;
//            case R.id.txt_dianbiao:
//                intent = new Intent(this, StatementActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_dianqi:
//                DialogTipWithoutOkCancel d = new DialogTipWithoutOkCancel(this);
//                d.hiddenTitle();
//                d.showImg();
//                d.setContent(getString(R.string.vs30));
//                d.show();
////                intent = new Intent(this, MyEleApplianceActivity.class);
////                intent.putExtra("collectorBean", collectorBean);
////                startActivity(intent);
//                break;
//            case R.id.txt_share:
//                intent = new Intent(this, ShareListActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_wifi_set:
//                intent = new Intent(this, AutoLinkActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.img_remove:
//                onDel();
//                break;
//            case R.id.txt_cha:
//                intent = new Intent(this, ChartsActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }

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
                        Log.e(TAG, message);
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
     * @return true 有可用升级
     */
    private boolean haveNewUpgrade() {
        return collectorBean.getVerMajorNew() * 256 + collectorBean.getVerMinorNew() > collectorBean.getVerMajor() * 256 + collectorBean.getVerMinor();
    }

    /**
     * 解绑电箱
     */
    public void onDel() {
        DialogTip dialogTip = new DialogTip(this, false);
        dialogTip.setTitle(getResources().getString(R.string.vs231)).setRedBtn()
                .setContent(getResources().getString(R.string.delete_device))
                .setDialogListener(new DialogTip.onEnsureDialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onEnsure() {
                        onApi();
                    }
                }).show();
    }

    public void onApi() {
        Core.instance(this).unbindDevice(collectorBean.getCode(), new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
//                                listDeviceData.remove(position);
//                                mDeviceAdapter.notifyDataSetChanged();
//                                mDeviceAdapter.setEditMode(false);
//                                tv_collector_count.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.vs13), listDeviceData.size()));
                CustomToast.showCustomToast(CollectorDetailActivity.this, getString(R.string.vs66));
                finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(CollectorDetailActivity.this, message);
            }
        });
    }
}
