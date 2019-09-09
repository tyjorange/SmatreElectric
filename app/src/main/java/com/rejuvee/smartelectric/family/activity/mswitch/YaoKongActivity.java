package com.rejuvee.smartelectric.family.activity.mswitch;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

public class YaoKongActivity extends BaseActivity {
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private LoadingDlg waitDialog;
    private List<SwitchBean> mListData = new ArrayList<>();

    //线路
    private List<SwitchBean> xianluListData = new ArrayList<>();


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_yao_kong;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
        waitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        getSwitchByCollector();
    }

    /**
     * 获取集中器下的线路(switch)
     */
    private void getSwitchByCollector() {
        waitDialog.show();
        Core.instance(this).getSwitchByCollector(mCollectorBean.getCode(), "hierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                mListData.clear();
                mListData.addAll(data);
                // 隐藏空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);

//                mHandler.sendEmptyMessageDelayed(MSG_FILLDATA, 10);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                String messageTip = "";
//                if (errorEvent == 12) {
//                    messageTip = getString(R.string.local_error_message_no_data);
//                } else {
//                    messageTip = getString(R.string.operator_failure);
//                }
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(YaoKongActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(YaoKongActivity.this, message);
                }
                // 显示空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.GONE);

                waitDialog.dismiss();
//                refreshLayout.setRefreshing(false);
            }
        });
        // 初始化线路状态
//        Core.instance(YaoKongActivity.this).getAllSwitchState(mCollectorBean.getCode(), new ActionCallbackListener<CollectorState>() {
//            @Override
//            public void onSuccess(CollectorState data) {
//                switchState = data.getSwitchState();
//            }
//
//            @Override
//            public void onFailure(int errorEvent, String message) {
//                CustomToast.showCustomErrorToast(YaoKongActivity.this, message);
//            }
//        });
    }

    private void doS() {
        for (SwitchBean c : mListData) {

        }
    }

    @Override
    protected void dealloc() {

    }

}
