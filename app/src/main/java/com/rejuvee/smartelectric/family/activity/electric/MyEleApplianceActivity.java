package com.rejuvee.smartelectric.family.activity.electric;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.EleapplianceAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.api.ErrorEvent;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.custom.DeviceEventMsg;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.EleBean;
import com.rejuvee.smartelectric.family.model.bean.MyEleApplianceBean;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 电器
 * Created by Administrator on 2018/11/5.
 */
@Deprecated
public class MyEleApplianceActivity extends BaseActivity implements EleapplianceAdapter.OnDeletEleListener {
    private static final String TAG = "MyEleApplianceActivity";
    private ArrayList<MyEleApplianceBean> arreleappliance = new ArrayList<>();
    private EleapplianceAdapter eleapplianceAdapter;
    private DialogTip dialogTip;
    private LoadingDlg mWaitDialog;
    private CollectorBean collectorBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myeleappliance;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.img_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDelIcon();
            }
        });
        ListView list_myelec = (ListView) findViewById(R.id.list_myelec);
        //    private DeviceHelper deviceHelper;
        ImageView add_appliance = findViewById(R.id.add_appliance);
        dialogTip = new DialogTip(this);

//        deviceHelper = new DeviceHelper();
        EventBus.getDefault().register(this);

        eleapplianceAdapter = new EleapplianceAdapter(MyEleApplianceActivity.this, arreleappliance, this);
        list_myelec.setAdapter(eleapplianceAdapter);
        list_myelec.setEmptyView(findViewById(R.id.empty_layout));

        //点击修改电器
        list_myelec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MyEleApplianceActivity.this, ModifyorAddEleapplianceActivity.class);
                Log.i(TAG, "gonglv=" + arreleappliance.get(position).getGonglv() + "");
                intent.putExtra("eleappliance", arreleappliance.get(position));
                intent.putExtra("collectorBean", collectorBean);
                startActivityForResult(intent, CommonRequestCode.REQUEST_MODIFYORUPDATE_ELES);
            }
        });

        //点击添加电器
        findViewById(R.id.add_appliance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyEleApplianceActivity.this, ModifyorAddEleapplianceActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_ELES);
            }
        });
    }

    @Override
    protected void initData() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        //获取电器数据
        getAllEles(this, true);
    }

    //获取所有电器
    public void getAllEles(Context context, boolean showLoading) {
        if (showLoading) {
            mWaitDialog = new LoadingDlg(context, -1);
            mWaitDialog.show();
        }
        Core.instance(context).getEEsByCollector(collectorBean.getCollectorID(), new ActionCallbackListener<List<EleBean>>() {
            @Override
            public void onSuccess(List<EleBean> data) {
                postResult(DeviceEventMsg.EVENT_GET_ALLELES, data, "ok", true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                postResult(DeviceEventMsg.EVENT_GET_ALLELES, errorEvent, message, false);
                CustomToast.showCustomErrorToast(MyEleApplianceActivity.this, message);
                mWaitDialog.dismiss();
            }
        });
    }

    //删除电器
    public void DeleteEle(Context context, String eleId, boolean showLoading) {
        Core.instance(context).deleteEE(eleId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                postResult(DeviceEventMsg.EVENT_DELET_ELES, data, "ok", true);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                postResult(DeviceEventMsg.EVENT_DELET_ELES, null, message, false);
                CustomToast.showCustomErrorToast(MyEleApplianceActivity.this, message);
                mWaitDialog.dismiss();
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
    public void onGetAllEles(DeviceEventMsg eventMsg) {
        if (eventMsg.eventType == DeviceEventMsg.EVENT_GET_ALLELES) {
            if (eventMsg.isSucess()) {
                arreleappliance.clear();
                List<EleBean> meventMsg = (List<EleBean>) eventMsg.getEventMsg();
                for (EleBean EleBean : meventMsg) {
//                    ArrayList<MyEleBean.allSwitchs> mswitchs = MyEleBean.getSwitchs();
//                    if (mswitchs != null) {
//                        for (MyEleBean.allSwitchs mswitch : mswitchs) {
//                            String switchID = mswitch.getSwitchID();
//                            String linename = mswitch.getLinename();
//                            ArrayList<MyEleBean.allEle> listeles = mswitch.getEes();
//                            if (listeles != null) {
//                                for (MyEleBean.allEle listele : listeles) {
                    arreleappliance.add(new MyEleApplianceBean(EleBean.getSwitchName(), EleBean.getName(), EleBean.getGonglv(), String.valueOf(EleBean.getId()), String.valueOf(EleBean.getSwitchID())));
//                                }
//                            }
//                        }
//                    }
                }
                eleapplianceAdapter.notifyDataSetChanged();
            } else {
                ErrorEvent.ParseAndShowGetDataError(this, (int) eventMsg.eventMsg);
                //CustomToast.showCustomErrorToast(MyEleApplianceActivity.this, getString(R.string.get_data_fail));
            }
        } else if (eventMsg.eventType == DeviceEventMsg.EVENT_DELET_ELES) {
            if (eventMsg.isSucess()) {
                CustomToast.showCustomToast(MyEleApplianceActivity.this, getString(R.string.delete_eleappliances_sucess));
            } else {
                CustomToast.showCustomErrorToast(MyEleApplianceActivity.this, getString(R.string.get_data_fail));
            }
        }
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.My_ele_appliance);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }
    private void toggleDelIcon() {
        for (MyEleApplianceBean taskBean : arreleappliance) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        eleapplianceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void dealloc() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            getAllEles(this, true);
    }

    @Override
    public void onDelete(final MyEleApplianceBean MyEleApplianceBean) {
        dialogTip.setTitle(getString(R.string.deletewarn));
        dialogTip.setContent(getString(R.string.vs154));
        dialogTip.show();
        dialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
            }

            @Override
            public void onEnsure() {
                Iterator<MyEleApplianceBean> eles = arreleappliance.iterator();
                while (eles.hasNext()) {
                    MyEleApplianceBean mele = eles.next();
                    Log.i(TAG, "id=" + mele.getId() + "---" + MyEleApplianceBean.getId());
                    if (mele.getId().equals(MyEleApplianceBean.getId())) {
                        eles.remove();//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
                        eleapplianceAdapter.notifyDataSetChanged();
                    }
                }
                dialogTip.dismiss();
                DeleteEle(MyEleApplianceActivity.this, MyEleApplianceBean.getId(), true);
            }
        });

    }
}
