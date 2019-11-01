package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

import java.util.Locale;

/**
 * 集中器参数
 * Created by Administrator on 2018/1/2.
 */
public class CollectorAttrActivity extends BaseActivity implements View.OnClickListener {
    private TextView text_devicename;
    private TextView text_botelv;
    private TextView text_caijilv;
    private TextView text_gaojin;
    private TextView text_guzhangma;
    private TextView text_xintiao;
    private CollectorBean collectorBean;
    private String Baud;
    private String Freq;
    private String Ranges;
    private String devicename;
    private String heartrate;
    private String guzhangma;
    private DialogTip mDialogSwitch;
    private Context mContext;


    public static int[] setNames = new int[]{
            R.string.sets_devicename,
            R.string.sets_botelv,
            R.string.sets_caijipl,
            R.string.sets_gaojingfazhi,
            R.string.sets_guzhangma,
            R.string.sets_xintiaopinglv
    };

    private LoadingDlg loadingDlg;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setup_layout;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");

        findViewById(R.id.img_cancel).setOnClickListener(this);
        findViewById(R.id.re_setname).setOnClickListener(this);
        findViewById(R.id.re_setbotelv).setOnClickListener(this);
        findViewById(R.id.re_setcaiji).setOnClickListener(this);
        findViewById(R.id.re_setgaojin).setOnClickListener(this);
        findViewById(R.id.re_setguzhangma).setOnClickListener(this);
        findViewById(R.id.re_setxintiao).setOnClickListener(this);
        findViewById(R.id.tv_reboot).setOnClickListener(this);
        findViewById(R.id.tv_restore).setOnClickListener(this);
        findViewById(R.id.iv_tips_btl).setOnClickListener(this);
        findViewById(R.id.iv_tips_cjpl).setOnClickListener(this);
        findViewById(R.id.iv_tips_cjfz).setOnClickListener(this);
        findViewById(R.id.iv_tips_gzmpl).setOnClickListener(this);
        findViewById(R.id.iv_tips_xtjg).setOnClickListener(this);
        text_devicename = findViewById(R.id.text_devicename);
        text_botelv = findViewById(R.id.text_botelv);
        text_caijilv = findViewById(R.id.text_caijilv);
        text_gaojin = findViewById(R.id.text_gaojin);
        text_guzhangma = findViewById(R.id.text_guzhangma);
        text_xintiao = findViewById(R.id.text_xintiao);
        TextView tvDeviceId = findViewById(R.id.tv_collect_id);
        tvDeviceId.setText(String.format("%s", collectorBean.getCode()));
        getCollectorByCollectorID(collectorBean.getCollectorID());

        loadingDlg = new LoadingDlg(this, -1);
        mDialogSwitch = new DialogTip(this);
    }

    public void getCollectorByCollectorID(String CollectorID) {
        Log.i("xintiao", "CollectorID=" + CollectorID);
        Core.instance(this).getCollectorByCollectorID(CollectorID, new ActionCallbackListener<CollectorBean>() {
            @Override
            public void onSuccess(CollectorBean data) {
                devicename = data.getDeviceName();
                Baud = data.getBaud() + "";
                Freq = data.getFreq() + "";
                Ranges = data.getRanges() + "";
                guzhangma = data.getFaultFreq() + "";
                heartrate = data.getHeartrate() + "";
                text_devicename.setText(String.format("%s", data.getDeviceName()));
                text_botelv.setText(String.format(Locale.getDefault(), "%dbps", data.getBaud()));
                text_caijilv.setText(String.format(Locale.getDefault(), "%dmin", data.getFreq()));
                text_gaojin.setText(String.format(Locale.getDefault(), "%d%%", data.getRanges()));
                text_guzhangma.setText(String.format(Locale.getDefault(), "%dsec", data.getFaultFreq()));
                text_xintiao.setText(String.format(Locale.getDefault(), "%ds", data.getHeartrate()));
            }

            @Override
            public void onFailure(int errorEvent, String message) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                finish();
                break;
            case R.id.re_setname:
                startactivtiy(devicename, 0);
                break;
            case R.id.re_setbotelv:
                startactivtiy(Baud, 1);
                break;
            case R.id.re_setcaiji:
                startactivtiy(Freq, 2);
                break;
            case R.id.re_setgaojin:
                startactivtiy(Ranges, 3);
                break;
            case R.id.re_setguzhangma:
                startactivtiy(guzhangma, 4);
                break;
            case R.id.re_setxintiao:
                startactivtiy(heartrate, 5);
                break;
            case R.id.tv_reboot:
//                collectSet(0);
                break;
            case R.id.tv_restore:
//                collectSet(1);
                break;
            case R.id.iv_tips_btl:
                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_botelv)).setContent(getString(R.string.tips_btl)).show();
                break;
            case R.id.iv_tips_cjpl:
                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_caijipl)).setContent(getString(R.string.tips_cjpl)).show();
                break;
            case R.id.iv_tips_cjfz:
                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.vs87)).setContent(getString(R.string.tips_cjfz)).show();
                break;
            case R.id.iv_tips_gzmpl:
                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_guzhangma)).setContent(getString(R.string.tips_gzmpl)).show();
                break;
            case R.id.iv_tips_xtjg:
                new DialogTipWithoutOkCancel(this).setTitle(getString(R.string.sets_xintiaopinglv)).setContent(getString(R.string.tips_xtjg)).show();
                break;
        }
    }

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
                Core.instance(mContext).CollectorReboot(type, collectorBean.getCollectorID(), new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        CustomToast.showCustomToast(CollectorAttrActivity.this, getString(R.string.operator_sucess));
                        loadingDlg.dismiss();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomErrorToast(CollectorAttrActivity.this, getString(R.string.operator_failure));
                        loadingDlg.dismiss();
                    }
                });
            }
        });
        mDialogSwitch.show();
    }


    public void startactivtiy(String partext, int position) {
        Intent intent = new Intent();
        intent.putExtra("title", getResources().getString(setNames[position]));
        intent.putExtra("position", position);
        intent.putExtra("partext", partext);
        intent.putExtra("CollectorID", collectorBean.getCollectorID());
        intent.setClass(CollectorAttrActivity.this, CollectorAttrSetActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void initData() {

    }

//    @Override
//    protected String getToolbarTitle() {
//        return getResources().getString(R.string.settings_title);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getCollectorByCollectorID(collectorBean.getCollectorID());
//            MainPageNewFragment.NEED_REFRESH = true;
        }
    }

}
