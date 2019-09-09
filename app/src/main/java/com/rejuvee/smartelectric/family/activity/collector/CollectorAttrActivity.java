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
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.widget.DialogTip;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;

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
    private TextView tvDeviceId;
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
        text_devicename = (TextView) findViewById(R.id.text_devicename);
        text_botelv = (TextView) findViewById(R.id.text_botelv);
        text_caijilv = (TextView) findViewById(R.id.text_caijilv);
        text_gaojin = (TextView) findViewById(R.id.text_gaojin);
        text_guzhangma = (TextView) findViewById(R.id.text_guzhangma);
        text_xintiao = (TextView) findViewById(R.id.text_xintiao);
        tvDeviceId = (TextView) findViewById(R.id.tv_collect_id);
        tvDeviceId.setText(collectorBean.getCode() + "");
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
                guzhangma = "";
                heartrate = data.getHeartrate() + "";
                text_devicename.setText(data.getDeviceName() + "");
                text_botelv.setText(data.getBaud() + "bps");
                text_caijilv.setText(data.getFreq() + "min");
                text_gaojin.setText(data.getRanges() + "%");
                text_guzhangma.setText(data.getFault() + "sec");
                text_xintiao.setText(data.getHeartrate() + "s");
                Log.i("xintiao", data.getHeartrate() + "------");
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
                collectSet(0);
                break;
            case R.id.tv_restore:
                collectSet(1);
                break;
        }
    }

    private void collectSet(int type) {
        mDialogSwitch.setTitle(type == 1 ? "恢复出厂设置" : "重启集中器");
        mDialogSwitch.setContent(type == 1 ? "确认恢复出厂设置?" : "确认重启集中器?");
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getCollectorByCollectorID(collectorBean.getCollectorID());
//            MainPageNewFragment.NEED_REFRESH = true;
        }
    }

}
