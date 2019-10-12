package com.rejuvee.smartelectric.family.activity.electric;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeDialog;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.custom.DeviceEventMsg;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.MyEleApplianceBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 电器 添加修改
 * Created by Administrator on 2018/11/15.
 */
@Deprecated
public class ModifyorAddEleapplianceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ModifyorAddEleActivity";
    private TextView txt_appliance_linename,
            txt_appliance_name,
            txt_appliance_power;
    //    private String FALE_APPLIANCENAME = "appliance_name";
//    private String FALE_APPLIANCEPOWER = "appliance_power";
    //    private DialogTip dialogTip;
    private String switchID;
    private TextView text_title;
    private MyEleApplianceBean eleappliance;
    private LoadingDlg mWaitDialog;
    private CollectorBean collectorBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_addeleappliance;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
//        dialogTip = new DialogTip(this, 0);

        org.greenrobot.eventbus.EventBus.getDefault().register(this);

        txt_appliance_linename = (TextView) findViewById(R.id.appliance_line_name);
        txt_appliance_name = (TextView) findViewById(R.id.txt_appliance_name);
        txt_appliance_power = (TextView) findViewById(R.id.txt_appliance_power);
        text_title = (TextView) findViewById(R.id.ele_title);
        findViewById(R.id.save_appliance).setOnClickListener(this);
        findViewById(R.id.re_addappliance).setOnClickListener(this);
        findViewById(R.id.re_insetname).setOnClickListener(this);
        findViewById(R.id.re_insetpower).setOnClickListener(this);
        findViewById(R.id.img_cancel).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        eleappliance = getIntent().getParcelableExtra("eleappliance");
        collectorBean = getIntent().getParcelableExtra("collectorBean");

        if (eleappliance != null) { //修改电器信息
            text_title.setText(R.string.modify_appliance);
            txt_appliance_linename.setText(eleappliance.getLinename());
            switchID = eleappliance.getSwitchID();
            txt_appliance_name.setText(eleappliance.getElename());
            txt_appliance_power.setText(String.valueOf(eleappliance.getGonglv()));
            Log.i(TAG, eleappliance.getGonglv() + "=gonglv");
            findViewById(R.id.re_addappliance).setVisibility(View.GONE);
        }
        //else 添加电器信息
    }

//    @Override
//    protected String getToolbarTitle() {
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return false;
//    }

    @Override
    protected void dealloc() {
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
    }

  /*  private void dialogInput(String dialogTitle, String desc, final String flag) {
        dialogTip.setContent(desc).setTitle(dialogTitle).setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
            }

            @Override
            public void onEnsure() {
                if (flag == FALE_APPLIANCENAME) {
                    conname = dialogTip.getContent();
                    txt_appliance_name.setText(dialogTip.getContent());
                } else if (flag == FALE_APPLIANCEPOWER) {
                    conpower = dialogTip.getContent();
                    txt_appliance_power.setText(dialogTip.getContent());
                }
            }
        }).show();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_appliance:
                String conname = txt_appliance_name.getText().toString();
                String conpower = txt_appliance_power.getText().toString();
                //将数据保存到数据库  返回上一个界面
                if (switchID == null || conname.isEmpty() || conpower.isEmpty()) {
                    CustomToast.showCustomErrorToast(ModifyorAddEleapplianceActivity.this, getString(R.string.ple_select_line));
                } else {
                    if (eleappliance != null) { //修改电器
                        addOrUpdateElet(this, eleappliance.getId(), switchID, conname, Double.valueOf(conpower), true);
                    } else {//添加电器
                        addOrUpdateElet(this, null, switchID, conname, Double.valueOf(conpower), true);
                    }
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            case R.id.re_addappliance:
//                LineSelectActivity.startLineSelectActivity(this, null);
//                Intent intent = new Intent(ModifyorAddEleapplianceActivity.this, SwitchTreeDialog.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", SwitchTreeDialog.JIENENG);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.JIENENG, collectorBean, new SwitchTreeDialog.ChoseCallBack() {

                    @Override
                    public void onChose(SwitchBean s) {
                        if (s != null) {
                            txt_appliance_linename.setText(s.getName());
                            switchID = s.getSwitchID();
                            Log.i(TAG, switchID + "");
                        }
                    }
                });
                switchTreeDialog.show();
                break;
            case R.id.re_insetname:
//                dialogInput(getString(R.string.inset_name), "", "appliance_name");
                break;
            case R.id.re_insetpower:
//                dialogInput(getString(R.string.inset_power), "", "appliance_power");
                break;
            case R.id.img_cancel:
                finish();
                break;
        }
    }

    //添加修改电器
    public void addOrUpdateElet(Context context, String electricalEquipmentID, String switchID, String name, Double gonglv, boolean showLoading) {
        if (showLoading) {
            mWaitDialog = new LoadingDlg(context, -1);
            mWaitDialog.show();
        }
        Core.instance(context).addOrUpdateEE(electricalEquipmentID, switchID, name, gonglv, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                postResult(DeviceEventMsg.EVENT_ADD_UPDATEELET, data, "ok", true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                postResult(DeviceEventMsg.EVENT_ADD_UPDATEELET, null, message, false);
            }
        });
    }

    private void postResult(int eventType, Object result, String errorMsg, boolean isSucess) {
        DeviceEventMsg eventMsg = new DeviceEventMsg(eventType);
        eventMsg.setEventMsg(result);
        eventMsg.setSucess(isSucess);
        eventMsg.setErrorMessage(errorMsg);
        mWaitDialog.dismiss();
        EventBus.getDefault().post(eventMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddOrUpdateElet(DeviceEventMsg eventMsg) {
        if (eventMsg.eventType == DeviceEventMsg.EVENT_ADD_UPDATEELET) {
            if (eventMsg.isSucess()) {
                CustomToast.showCustomToast(ModifyorAddEleapplianceActivity.this, getString(R.string.add_eleappliances_sucess));
            } else {
                CustomToast.showCustomErrorToast(ModifyorAddEleapplianceActivity.this, getString(R.string.get_data_fail));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_CHOSE_LINE) {
                SwitchBean infoBean = data.getParcelableExtra("switchBean");
                if (infoBean != null) {
                    txt_appliance_linename.setText(infoBean.getName());
                    switchID = infoBean.getSwitchID();
                    Log.i(TAG, switchID + "");
                }
            }
        }
    }
}
