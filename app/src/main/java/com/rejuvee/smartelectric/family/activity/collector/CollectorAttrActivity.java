package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivitySetupLayoutBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.viewmodel.CollectorAttrViewModel;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;

/**
 * 集中器参数
 * Created by Administrator on 2018/1/2.
 */
public class CollectorAttrActivity extends BaseActivity {
    private String TAG = "CollectorAttrActivity";
    //    private TextView text_devicename;
//    private TextView text_botelv;
//    private TextView text_caijilv;
//    private TextView text_gaojin;
//    private TextView text_guzhangma;
//    private TextView text_xintiao;
    private CollectorBean collectorBean;
    //    private String devicename;
//    private String Baud;
//    private String Freq;
//    private String Ranges;
//    private String guzhangma;
//    private String heartrate;
    private DialogTip mDialogSwitch;
    private CollectorAttrViewModel mViewModel;
    public static int[] setNames = new int[]{
            R.string.sets_devicename,
            R.string.sets_botelv,
            R.string.sets_caijipl,
            R.string.sets_gaojingfazhi,
            R.string.sets_guzhangma,
            R.string.sets_xintiaopinglv
    };

    private LoadingDlg loadingDlg;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_setup_layout;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        //    private Context mContext;
        ActivitySetupLayoutBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setup_layout);
        mViewModel = ViewModelProviders.of(this).get(CollectorAttrViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        findViewById(R.id.img_cancel).setOnClickListener(this);
//        findViewById(R.id.re_setname).setOnClickListener(this);
//        findViewById(R.id.re_setbotelv).setOnClickListener(this);
//        findViewById(R.id.re_setcaiji).setOnClickListener(this);
//        findViewById(R.id.re_setgaojin).setOnClickListener(this);
//        findViewById(R.id.re_setguzhangma).setOnClickListener(this);
//        findViewById(R.id.re_setxintiao).setOnClickListener(this);
//        findViewById(R.id.tv_reboot).setOnClickListener(this);
//        findViewById(R.id.tv_restore).setOnClickListener(this);
//        findViewById(R.id.iv_tips_btl).setOnClickListener(this);
//        findViewById(R.id.iv_tips_cjpl).setOnClickListener(this);
//        findViewById(R.id.iv_tips_cjfz).setOnClickListener(this);
//        findViewById(R.id.iv_tips_gzmpl).setOnClickListener(this);
//        findViewById(R.id.iv_tips_xtjg).setOnClickListener(this);
//        text_devicename = findViewById(R.id.text_devicename);
//        text_botelv = findViewById(R.id.text_botelv);
//        text_caijilv = findViewById(R.id.text_caijilv);
//        text_gaojin = findViewById(R.id.text_gaojin);
//        text_guzhangma = findViewById(R.id.text_guzhangma);
//        text_xintiao = findViewById(R.id.text_xintiao);
//        TextView tvDeviceId = findViewById(R.id.tv_collect_id);
//        tvDeviceId.setText(String.format("%s", collectorBean.getCode()));

        getCollectorByCollectorID(Objects.requireNonNull(collectorBean).getCollectorID());

        loadingDlg = new LoadingDlg(this, -1);
        mDialogSwitch = new DialogTip(this);
    }

    public void getCollectorByCollectorID(String CollectorID) {
        Log.i(TAG, "CollectorID=" + CollectorID);
        currentCall = Core.instance(this).getCollectorByCollectorID(CollectorID, new ActionCallbackListener<CollectorBean>() {
            @Override
            public void onSuccess(CollectorBean data) {
                mViewModel.setCollectorBeanCode(data.getCode());
//                devicename = data.getDeviceName();
                mViewModel.setDevicename(String.format("%s", data.getDeviceName()));
//                Baud = data.getBaud() + "";
                mViewModel.setBaud(String.format(Locale.getDefault(), "%dbps", data.getBaud()));
//                Freq = data.getFreq() + "";
                mViewModel.setFreq(String.format(Locale.getDefault(), "%dmin", data.getFreq()));
//                Ranges = data.getRanges() + "";
                mViewModel.setRanges(String.format(Locale.getDefault(), "%d%%", data.getRanges()));
//                guzhangma = data.getFaultFreq() + "";
                mViewModel.setGuzhangma(String.format(Locale.getDefault(), "%dsec", data.getFaultFreq()));
//                heartrate = data.getHeartrate() + "";
                mViewModel.setHeartrate(String.format(Locale.getDefault(), "%ds", data.getHeartrate()));
//                text_devicename.setText(String.format("%s", data.getDeviceName()));
//                text_botelv.setText(String.format(Locale.getDefault(), "%dbps", data.getBaud()));
//                text_caijilv.setText(String.format(Locale.getDefault(), "%dmin", data.getFreq()));
//                text_gaojin.setText(String.format(Locale.getDefault(), "%d%%", data.getRanges()));
//                text_guzhangma.setText(String.format(Locale.getDefault(), "%dsec", data.getFaultFreq()));
//                text_xintiao.setText(String.format(Locale.getDefault(), "%ds", data.getHeartrate()));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.re_setname:
//                startActivtiy(devicename, 0);
//                break;
//            case R.id.re_setbotelv:
//                startActivtiy(Baud, 1);
//                break;
//            case R.id.re_setcaiji:
//                startActivtiy(Freq, 2);
//                break;
//            case R.id.re_setgaojin:
//                startActivtiy(Ranges, 3);
//                break;
//            case R.id.re_setguzhangma:
//                startActivtiy(guzhangma, 4);
//                break;
//            case R.id.re_setxintiao:
//                startActivtiy(heartrate, 5);
//                break;
//            case R.id.tv_reboot:
////                collectSet(0);
//                break;
//            case R.id.tv_restore:
////                collectSet(1);
//                break;
//            case R.id.iv_tips_btl:
//                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_botelv)).setContent(getString(R.string.tips_btl)).show();
//                break;
//            case R.id.iv_tips_cjpl:
//                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_caijipl)).setContent(getString(R.string.tips_cjpl)).show();
//                break;
//            case R.id.iv_tips_cjfz:
//                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.vs87)).setContent(getString(R.string.tips_cjfz)).show();
//                break;
//            case R.id.iv_tips_gzmpl:
//                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_guzhangma)).setContent(getString(R.string.tips_gzmpl)).show();
//                break;
//            case R.id.iv_tips_xtjg:
//                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_xintiaopinglv)).setContent(getString(R.string.tips_xtjg)).show();
//                break;
//        }
//    }

    @Deprecated
    private void collectSet(int type) {
        mDialogSwitch.setTitle(type == 1 ? getString(R.string.vs180) : getString(R.string.vs181));
        mDialogSwitch.setContent(type == 1 ? getString(R.string.vs182) : getString(R.string.vs183));
        mDialogSwitch.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onEnsure() {
                loadingDlg.show();
                currentCall = Core.instance(CollectorAttrActivity.this).CollectorReboot(type, collectorBean.getCollectorID(), new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        CustomToast.showCustomToast(CollectorAttrActivity.this, getString(R.string.operator_sucess));
                        loadingDlg.dismiss();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomErrorToast(CollectorAttrActivity.this, message);
                        loadingDlg.dismiss();
                    }
                });
            }
        });
        mDialogSwitch.show();
    }


    private void startActivtiy(String partext, int position) {
        Intent intent = new Intent();
        intent.putExtra("title", getResources().getString(setNames[position]));
        intent.putExtra("position", position);
        intent.putExtra("partext", partext);
        intent.putExtra("CollectorID", collectorBean.getCollectorID());
        intent.setClass(CollectorAttrActivity.this, CollectorAttrSetActivity.class);
        startActivityForResult(intent, 100);
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onSetName(View view) {
            startActivtiy(mViewModel.getDevicename().getValue(), 0);
        }

        public void onSetbotelv(View view) {
            startActivtiy(mViewModel.getBaud().getValue(), 1);
        }

        public void onSetcaiji(View view) {
            startActivtiy(mViewModel.getFreq().getValue(), 2);
        }

        public void onSetgaojin(View view) {
            startActivtiy(mViewModel.getRanges().getValue(), 3);
        }

        public void onSetguzhangma(View view) {
            startActivtiy(mViewModel.getGuzhangma().getValue(), 4);
        }

        public void onSetxintiao(View view) {
            startActivtiy(mViewModel.getHeartrate().getValue(), 5);
        }

        public void onTips_btl(View view) {
            new DialogTipWithoutOkCancel(view.getContext()).setTitle(getString(R.string.sets_botelv)).setContent(getString(R.string.tips_btl)).show();
        }

        public void onTips_cjpl(View view) {
            new DialogTipWithoutOkCancel(view.getContext()).setTitle(getString(R.string.sets_caijipl)).setContent(getString(R.string.tips_cjpl)).show();
        }

        public void onTips_cjfz(View view) {
            new DialogTipWithoutOkCancel(view.getContext()).setTitle(getString(R.string.vs87)).setContent(getString(R.string.tips_cjfz)).show();
        }

        public void onTips_gzmpl(View view) {
            new DialogTipWithoutOkCancel(view.getContext()).setTitle(getString(R.string.sets_guzhangma)).setContent(getString(R.string.tips_gzmpl)).show();
        }

        public void onTips_xtjg(View view) {
            new DialogTipWithoutOkCancel(view.getContext()).setTitle(getString(R.string.sets_xintiaopinglv)).setContent(getString(R.string.tips_xtjg)).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getCollectorByCollectorID(collectorBean.getCollectorID());
//            MainPageNewFragment.NEED_REFRESH = true;
        }
    }

}
