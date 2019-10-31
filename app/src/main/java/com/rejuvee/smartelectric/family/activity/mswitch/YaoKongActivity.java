package com.rejuvee.smartelectric.family.activity.mswitch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.AddDeviceOrSwitchActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 主线路
 */
public class YaoKongActivity extends BaseActivity implements SwitchTree {
    private Context mContext;
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private LoadingDlg waitDialog;
    private List<SwitchBean> mListData = new ArrayList<>();
    private MyAdapter adapter;
    private int viewType;
    private DialogTipWithoutOkCancel d;

    private DialogTip mDialogTip;

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
        mContext = this;
        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
        viewType = getIntent().getIntExtra("viewType", -1);
        waitDialog = new LoadingDlg(this, -1);
        d = new DialogTipWithoutOkCancel(this);
        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        TextView tv_title = findViewById(R.id.tv_title);
        switch (viewType) {
            case YaoKongActivity.YAOKONG:
                tv_title.setText(getString(R.string.remote_control));
                findViewById(R.id.ll_xianlu_paizhao).setVisibility(View.GONE);
                findViewById(R.id.type_yaokong).setVisibility(View.VISIBLE);
                findViewById(R.id.type_xianluweihu).setVisibility(View.GONE);
                break;
            case YaoKongActivity.XIANLU_WEIHU:
                tv_title.setText(getString(R.string.vs117));
                findViewById(R.id.type_yaokong).setVisibility(View.GONE);
                findViewById(R.id.type_xianluweihu).setVisibility(View.VISIBLE);
                View iv_switch_add = findViewById(R.id.iv_add_child_switch);
                iv_switch_add.setOnClickListener(v -> {
                    addSwitch(null);//添加至集中器
                });
                iv_switch_add.setVisibility(View.VISIBLE);
                View iv_switch_remove = findViewById(R.id.iv_switch_remove_toggle);
                iv_switch_remove.setOnClickListener(v -> toggleDelIcon());
                iv_switch_remove.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_xianlu_paizhao).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_xianlu_paizhao).setOnClickListener(v -> {
                    d.hiddenTitle();
                    d.showImg();
                    d.setContent(getString(R.string.vs30));
                    d.show();
                });
                break;
        }
        ListView lvProduct = findViewById(R.id.lv_products);
        lvProduct.setEmptyView(findViewById(R.id.empty_layout));
        adapter = new MyAdapter(this, mCollectorBean, viewType, mListData, s -> deleteSwitch(s.getSwitchID()));
        lvProduct.setAdapter(adapter);
    }


    @Override
    protected void initData() {
//        getSwitchByCollector();
    }

    /**
     * 切换删除按钮
     */
    private void toggleDelIcon() {
        // 线路
        for (SwitchBean taskBean : mListData) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
                // 隐藏空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);

//                mHandler.sendEmptyMessageDelayed(MSG_FILLDATA, 10);
                waitDialog.dismiss();
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
                    CustomToast.showCustomErrorToast(YaoKongActivity.this, getString(R.string.vs29));
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

    /**
     * 删除断路器(switch)
     */
    private void deleteSwitch(int switchId) {
        mDialogTip = new DialogTip(mContext);
        mDialogTip.setTitle(getString(R.string.deletexianlu));
        mDialogTip.setContent(getString(R.string.xianlu_issure));
        mDialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                mDialogTip.dismiss();
            }

            @Override
            public void onEnsure() {
                mDialogTip.dismiss();
                waitDialog.show();
                Core.instance(mContext).deleteBreak(switchId, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
//                mListData.remove(position);
//                mAdapter.notifyDataSetChanged();
                        getSwitchByCollector();
                        CustomToast.showCustomToast(YaoKongActivity.this, getString(R.string.operator_sucess));
                        waitDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomToast(YaoKongActivity.this, message);
                        waitDialog.dismiss();
                    }
                });
            }
        });
        mDialogTip.show();

    }

    /**
     * 跳转添加断路器(switch)界面
     *
     * @param mSwitch null 添加至电箱
     *                notNull 添加至mSwitch
     */
    private void addSwitch(SwitchBean mSwitch) {
        Intent intent = new Intent(mContext, AddDeviceOrSwitchActivity.class);
        intent.setExtrasClassLoader(getClass().getClassLoader());
        intent.putExtra("collectorBean", mCollectorBean);
        intent.putExtra("switch", mSwitch);
        intent.putExtra("add_type", AddDeviceOrSwitchActivity.BREAK_ADD);
        startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_LINE_ROOT);
    }

    @Override
    protected void dealloc() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSwitchByCollector();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_ADD_LINE) {
                //添加后 刷新数据
//                getSwitchByCollector();
                finish();//TODO 添加/删除完分线支线直接结束界面
            } else if (requestCode == CommonRequestCode.REQUEST_ADD_LINE_ROOT) {
                getSwitchByCollector();
            }
        }
    }

    public static class MyAdapter extends BaseAdapter {
        private Context context;
        private CollectorBean collectorBean;
        private List<SwitchBean> list;
        private int viewType;
        private ISwitchCheckListen iSwitchCheckListen;

        MyAdapter(Context context, CollectorBean collectorBean, int viewType, List<SwitchBean> list, ISwitchCheckListen iSwitchCheckListen) {
            super();
            this.context = context;
            this.collectorBean = collectorBean;
            this.viewType = viewType;
            this.iSwitchCheckListen = iSwitchCheckListen;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SwitchBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_test0, parent, false);
                holder = new ViewHolder();
                holder.img_line = convertView.findViewById(R.id.img_line);
                holder.txt_content = convertView.findViewById(R.id.txt_content);
                holder.tv_state = convertView.findViewById(R.id.tv_state);
                holder.tv_code = convertView.findViewById(R.id.tv_code);
                holder.iv_time_clock = convertView.findViewById(R.id.iv_time_clock);
                holder.img_right = convertView.findViewById(R.id.img_right);
                holder.iv_del_switch = convertView.findViewById(R.id.iv_del_switch);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SwitchBean switchBean = getItem(position);
            holder.img_line.setImageResource(switchBean.getIcon());
            holder.txt_content.setText(String.format("%s%s", context.getString(R.string.vs14), switchBean.getName()));
//            holder.tv_state.setVisibility(switchBean.getSwitchState() == 0 ? View.VISIBLE : View.INVISIBLE);// 更新状态文字
//            holder.tv_state.setText(SwitchBean.getSwitchFaultState(convertView.getContext(), switchBean.fault));// 更新状态文字
            holder.tv_state.setText(String.format(Locale.getDefault(), "%d%s", switchBean.getChild().size(), context.getString(R.string.vs28)));
            holder.tv_code.setText(switchBean.getSerialNumber());
            holder.iv_time_clock.setVisibility(switchBean.timerCount > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.iv_del_switch.setVisibility(switchBean.showDelIcon);

            if (switchBean.getChild() != null) {
                holder.img_right.setVisibility(View.VISIBLE);
                convertView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, YaoKongDetailActivity.class);
                    intent.putExtra("collectorBean", collectorBean);
                    intent.putExtra("SwitchBean", switchBean);
                    intent.putExtra("viewType", viewType);
//                        intent.putExtra("datas", datas);
                    ((Activity) context).startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_LINE);
                });
            }
            holder.iv_del_switch.setOnClickListener(v -> iSwitchCheckListen.onDelete(switchBean));
            return convertView;
        }

        private class ViewHolder {
            private ImageView img_line;
            private TextView txt_content;
            private TextView tv_state;
            private TextView tv_code;
            private ImageView iv_time_clock;
            private ImageView img_right;
            private ImageView iv_del_switch;
        }

        public interface ISwitchCheckListen {

            void onDelete(SwitchBean cb);

        }
    }
}
