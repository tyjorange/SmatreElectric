package com.rejuvee.smartelectric.family.activity.energy;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeDialog;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.databinding.ActivityEnergySavingInformationBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ElequantityBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.viewmodel.EnergySavingInformationViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 节能信息
 */
public class EnergySavingInformationActivity extends BaseActivity {
    private String TAG = "EnergySavingInformationActivity";
    private List<ElequantityBean> res = new ArrayList<>();//所有集中器,线路 同比环比数据
    private CollectorBean collectorBean;
    private SwitchBean switchBean;
    //    private TextView collector_name;
//    private TextView line_name;
//    private TextView tv_lastMonth;
//    private TextView tv_lastYearMonth;
//    private TextView hbjs_val;
//    private TextView tbjs_val;
//    private TextView ydl_val;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_energy_saving_information;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private EnergySavingInformationViewModel mViewModel;

    @Override
    protected void initView() {
        ActivityEnergySavingInformationBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_energy_saving_information);
        mViewModel = ViewModelProviders.of(this).get(EnergySavingInformationViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        collector_name = findViewById(R.id.collector_name);
//        TextView dianxiang_code = findViewById(R.id.dianxiang_code);
//        line_name = findViewById(R.id.line_name);
//        tv_lastMonth = findViewById(R.id.tv_lastMonth);
//        tv_lastYearMonth = findViewById(R.id.tv_lastYearMonth);
//        hbjs_val = findViewById(R.id.hbjs_val);
//        tbjs_val = findViewById(R.id.tbjs_val);
//        ydl_val = findViewById(R.id.ydl_val);
//        dianxiang_code.setText(collectorBean.getCode());
//        LinearLayout img_change = findViewById(R.id.img_change);
//        img_change.setOnClickListener(this);
//        ImageView backBtn = findViewById(R.id.img_cancel);
//        backBtn.setOnClickListener(this);
        getAllSwitchMsg();
    }

    /**
     * 获取 所有集中器,线路 同比环比数据
     */
    public void getAllSwitchMsg() {
        currentCall = Core.instance(this).getAllswitchMsg("nohierarchy", new ActionCallbackListener<List<ElequantityBean>>() {
            @Override
            public void onSuccess(List<ElequantityBean> data) {
                res.clear();
                res.addAll(data);
                getSwitchByCollector();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
            }
        });
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        currentCall = Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                switchBean = data.get(0);//init bean
                findData(switchBean, collectorBean);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(EnergySavingInformationActivity.this, getString(R.string.vs29));
                } else {
                    CustomToast.showCustomErrorToast(EnergySavingInformationActivity.this, message);
                }
            }
        });
    }

    private void findData(SwitchBean switchBean, CollectorBean collectorBean) {
        for (ElequantityBean eb : res) {
            if (eb.getCollector().getCollectorID().equals(collectorBean.getCollectorID())) {
                for (SwitchBean sb : eb.getSwitchs()) {
                    if (sb.getSwitchID() == (switchBean.getSwitchID())) {
                        setData(sb, eb.getCollector());
                    }
                }
            }
        }
    }

    private void setData(SwitchBean switchBean, CollectorBean collectorBean) {
//        collector_name.setText(String.format("%s%s", getString(R.string.vs5), collectorBean.getDeviceName()));
        mViewModel.setCollectorName(String.format("%s%s", getString(R.string.vs5), collectorBean.getDeviceName()));
//        line_name.setText(String.format("%s%s", getString(R.string.vs4), switchBean.getName()));
        mViewModel.setLineName(String.format("%s%s", getString(R.string.vs4), switchBean.getName()));
//        tv_lastMonth.setText(String.format("%s%s", switchBean.getLastMonth(), getString(R.string.statement_unit_electric)));
        mViewModel.setLastMonth(String.format("%s%s", switchBean.getLastMonth(), getString(R.string.statement_unit_electric)));
//        hbjs_val.setText(switchBean.getHbjn().equals("") ? "-" : switchBean.getHbjn());
        mViewModel.setHbjsVal(switchBean.getHbjn().equals("") ? "-" : switchBean.getHbjn());
//        tv_lastYearMonth.setText(String.format("%s%s", switchBean.getLastYearMonth(), getString(R.string.statement_unit_electric)));
        mViewModel.setLastYearMonth(String.format("%s%s", switchBean.getLastYearMonth(), getString(R.string.statement_unit_electric)));
//        tbjs_val.setText(switchBean.getTbjn().equals("") ? "-" : switchBean.getTbjn());
        mViewModel.setTbjsVal(switchBean.getTbjn().equals("") ? "-" : switchBean.getTbjn());
//        ydl_val.setText(String.format("%s%s", switchBean.getMonth(), getString(R.string.statement_unit_electric)));
        mViewModel.setYdlVal(String.format("%s%s", switchBean.getMonth(), getString(R.string.statement_unit_electric)));
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.img_change:
////                Intent intent = new Intent(EnergySavingInformationActivity.this, SwitchTreeDialog.class);
////                intent.putExtra("collectorBean", collectorBean);
////                intent.putExtra("viewType", SwitchTree.JIENENG);
////                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
//                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(this, SwitchTree.JIENENG, collectorBean, s -> findData(s, collectorBean));
//                switchTreeDialog.show();
//                break;
//        }
//    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onChange(View view) {
            SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(view.getContext(), SwitchTree.JIENENG, collectorBean, s -> findData(s, collectorBean));
            switchTreeDialog.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_CHOSE_LINE) {//线路选择 Deprecated
                SwitchBean s = data.getParcelableExtra("switchBean");
                findData(s, collectorBean);
            }
        }
    }
}
