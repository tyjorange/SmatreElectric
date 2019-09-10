package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ControllerId;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.DialogTip;
import com.rejuvee.smartelectric.family.widget.ExpandLayout;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;
import com.rejuvee.smartelectric.family.widget.SnackbarMessageShow;

import java.util.List;

public class YaoKongDetailActivity extends BaseActivity {
    private String TAG = "YaoKongDetailActivity";
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private SwitchBean switchBean;
    private SwitchBean currentSwitchBean;
    private String controllerId;

    private static int currentSearchCount;//重试计数
    private static final int MAX_REQUEST_COUNT = 4;// 最大重新请求次数
    private static final int MAX_REFRESH_COUNT = 7;// 最大刷新请求次数
    private static final int MSG_CMD_RESULT = 0;//开关命令发送是否成功轮询
    private static final int MSG_TIMER = 1;//定时刷新switch state
    private static final int MSG_FILLDATA = 3;// 填充tree数据
    private static final int MSG_SWTCH_REFRESH = 5;// 刷新单个线路
    private boolean targetState;//开关操作的目标状态

    private Context mContext;
    private Handler mHandler;
    private LoadingDlg mWaitDialog;
    private ListView lvProduct;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_yao_kong_detail;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
        switchBean = getIntent().getParcelableExtra("SwitchBean");
        mWaitDialog = new LoadingDlg(this, -1);
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.iv_switch_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBreak(switchBean);
            }
        });
        TextView tv_root_name = findViewById(R.id.tv_root_name);
        tv_root_name.setText("线路：" + switchBean.getName());
        lvProduct = (ListView) findViewById(R.id.lv_products);
        OneExpandAdapter adapter = new OneExpandAdapter(this, mCollectorBean, switchBean, new OneExpandAdapter.ISwitchCheckListen() {
            @Override
            public void onSwitch(SwitchBean cb) {
                switchBreak(cb);
            }

            @Override
            public void onDelete(SwitchBean cb) {

            }

            @Override
            public void onAdd(SwitchBean cb) {

            }

            @Override
            public void onResetChose() {

            }

            @Override
            public void onChose(SwitchBean cb) {

            }
        });
        lvProduct.setAdapter(adapter);
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                currentSearchCount++;
                if (msg.what == MSG_CMD_RESULT) {
                    getResultOfController();
                } else if (msg.what == MSG_TIMER) {
//                    getAllSwitchState(1);
                } else if (msg.what == MSG_FILLDATA) {
//                    fillData();
                } else if (msg.what == MSG_SWTCH_REFRESH) {
//                    getSwitchState();
                }
            }
        };
    }

    /**
     * 手动操作断路器[开、关]
     *
     * @param cb
     */
    private void switchBreak(SwitchBean cb) {
        DialogTip mDialogSwitch = new DialogTip(this);
        if (mCollectorBean.getOnline() == 0) {// 集中器不在线
            CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, "集中器不在线，请刷新集中器");
            return;
        }
        currentSwitchBean = cb;
        int _isSwitch = cb.getSwitchState() == -1 ? 2 : cb.getSwitchState();
        targetState = _isSwitch == 1;
        if (_isSwitch == 2) {
            CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, "请刷当前线路状态再操作");
        } else {
            String title = _isSwitch == 0 ? getString(R.string.open_break) : getString(R.string.close_break);
            String desc = _isSwitch == 0 ?
                    String.format(getString(R.string.break_op_tip), getString(R.string.open), currentSwitchBean.getName())
                    : String.format(getString(R.string.break_op_tip), getString(R.string.close), currentSwitchBean.getName());
            mDialogSwitch.setTitle(title);
            mDialogSwitch.setContent(desc);
            mDialogSwitch.setDialogListener(new DialogTip.onEnsureDialogListener() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onEnsure() {
                    mWaitDialog.show();
                    Core.instance(mContext).controlBreak(currentSwitchBean.getSwitchID(), targetState, new ActionCallbackListener<ControllerId>() {
                        @Override
                        public void onSuccess(ControllerId data) {
                            controllerId = data.getControllerID();
                            currentSearchCount = 0;
                            mHandler.sendEmptyMessageDelayed(MSG_CMD_RESULT, 1000);
//                                waitDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int errorEvent, String message) {
                            CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, message);
                            mWaitDialog.dismiss();
                        }
                    });
                }
            });
            mDialogSwitch.show();
        }
    }

    /**
     * controllerId
     * 查询控制操作状态
     */
    private void getResultOfController() {
        if (TextUtils.isEmpty(controllerId)) {
            return;
        }
        Core.instance(this).getControllerState(controllerId, new ActionCallbackListener<SwitchBean>() {
            @Override
            public void onSuccess(SwitchBean data) {
                currentSearchCount = 0;
//                waitLastResultAfterSwitch();
                checkSwitchSuccess(data);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, message);
                if (currentSearchCount >= MAX_REQUEST_COUNT) {
                    currentSearchCount = 0;
//                    CustomToast.showCustomToast(SwitchTreeActivity.this, getString(R.string.op_fail));
                    SnackbarMessageShow.getInstance().showError(lvProduct, "请求超时，请稍后尝试");
                    mWaitDialog.dismiss();
                } else {
                    mHandler.sendEmptyMessageDelayed(MSG_CMD_RESULT, 1000);
                }
            }
        });
    }

    /**
     * 查询控制操作状态 根据结果进行提示
     *
     * @param cb
     */
    private void checkSwitchSuccess(SwitchBean cb) {
        switch (cb.getRunCode()) {
            case "0": // run code=0 重发
                if (currentSearchCount <= MAX_REQUEST_COUNT) {
                    mHandler.sendEmptyMessageDelayed(MSG_CMD_RESULT, 2000);
                } else {
                    SnackbarMessageShow.getInstance().showError(lvProduct, "请求超时，请稍后尝试");
                    mWaitDialog.dismiss();
                }
                break;
            case "1":
                SnackbarMessageShow.getInstance().showError(lvProduct, "发送操作失败，请确认设备是否在线");
                mWaitDialog.dismiss();
                break;
            case "2":
                switch (cb.getRunResult()) {
                    case "0":
                        currentSearchCount = 0;
                        getSwitchState();
                        break;
                    case "34":
                        SnackbarMessageShow.getInstance().showError(lvProduct, getString(R.string.terminal_net_error));
                        mWaitDialog.dismiss();
                        break;
                    default:
                        SnackbarMessageShow.getInstance().showError(lvProduct, getString(R.string.operator_failure) + cb.getRunResult());
                        currentSearchCount = 0;
                        getSwitchState();
                        break;
                }
                break;
            case "3":
                SnackbarMessageShow.getInstance().showError(lvProduct, "请求失败，多人同时操作冲突");
                mWaitDialog.dismiss();
                break;
        }
    }

    /**
     * 刷新单条线路状态
     */
    private void getSwitchState() {
        Core.instance(this).getSwitchState(currentSwitchBean.getSerialNumber(), new ActionCallbackListener<SwitchBean>() {

            @Override
            public void onSuccess(SwitchBean cb) {
                Log.i(TAG, currentSearchCount + "-flush_count");
                if (currentSearchCount <= MAX_REFRESH_COUNT) {
                    if ((cb.getSwitchState() == 1) == targetState) {// 如果还没有变成开关操作的目标状态 继续查询
                        mHandler.sendEmptyMessageDelayed(MSG_SWTCH_REFRESH, 1000);
                    } else {
                        Log.i(TAG, cb.getSwitchState() + "-flush_success");
//                        findChild(rootNode, currentSwitchBean.getSerialNumber(), cb.getSwitchState(), cb.getFault(), 1);
                        mWaitDialog.dismiss();
                        CustomToast.showCustomToast(YaoKongDetailActivity.this, "操作成功");
                    }
                } else {
                    CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, "查询操作超时，请刷新");
                    mWaitDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, message);
                mWaitDialog.dismiss();
            }
        });
    }

    @Override
    protected void dealloc() {

    }

    /**
     * 点击item展开隐藏部分,再次点击收起
     * 只可展开一条记录
     *
     */
    public static class OneExpandAdapter extends BaseAdapter {
        private Context context;
        private CollectorBean collectorBean;
        private SwitchBean switchBean;
        private List<SwitchBean> list;
        private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
        private ISwitchCheckListen iSwitchCheckListen;

        /**
         * @param context
         * @param collectorBean
         * @param switchBean
         */
        OneExpandAdapter(Context context, CollectorBean collectorBean, SwitchBean switchBean, ISwitchCheckListen iSwitchCheckListen) {
            super();
            this.context = context;
            this.collectorBean = collectorBean;
            this.switchBean = switchBean;
            this.list = switchBean.getChild();
            this.iSwitchCheckListen = iSwitchCheckListen;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_test1, parent, false);
                holder = new ViewHolder();
                holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
                holder.img_line = (ImageView) convertView.findViewById(R.id.img_line);
                holder.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
                holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
                holder.iv_time_clock = (ImageView) convertView.findViewById(R.id.iv_time_clock);
                holder.iv_switch = (ImageView) convertView.findViewById(R.id.iv_switch);
                holder.toggle_icon = (ImageView) convertView.findViewById(R.id.toggle_icon);
                holder.sp_line = (ImageView) convertView.findViewById(R.id.sp_line);
                holder.hideArea = (ExpandLayout) convertView.findViewById(R.id.layout_hideArea);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
            holder.showArea.setTag(position);

            SwitchBean currentSwitchBean = getItem(position);
            holder.img_line.setImageResource(currentSwitchBean.getIcon());
            holder.txt_content.setText("分线：" + currentSwitchBean.getName());
            holder.tv_state.setVisibility(currentSwitchBean.getSwitchState() == 0 ? View.VISIBLE : View.INVISIBLE);// 更新状态文字
            holder.tv_state.setText(SwitchBean.getSwitchFaultState(convertView.getContext(), currentSwitchBean.fault));// 更新状态文字
            holder.tv_code.setText(currentSwitchBean.getSerialNumber());
            holder.iv_time_clock.setVisibility(currentSwitchBean.timerCount > 0 ? View.VISIBLE : View.INVISIBLE);
            holder.iv_switch.setVisibility(View.VISIBLE);
            if (collectorBean.beShared == 0 || collectorBean.enable == 1) {// 不是被分享的 或 有集中器操作权限
                holder.iv_switch.setImageResource(NativeLine.DrawableToggle[switchBean.getSwitchState() == -1 ? 2 : switchBean.getSwitchState()]);
                holder.iv_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iSwitchCheckListen != null) {
//                        holder.ivSwitch.setImageResource(NativeLine.DrawableToggle[2]);
                            iSwitchCheckListen.onSwitch(switchBean);
                        }
                    }
                });
            }

            List<SwitchBean> child = currentSwitchBean.getChild();
            if (child != null) {
                holder.hideArea.removeAllViews();//清除重绘
                for (SwitchBean s : child) {
                    View inflate1 = LayoutInflater.from(context).inflate(R.layout.item_test2, parent, false);
                    ViewHolder holder1 = new ViewHolder();
                    holder1.img_line = (ImageView) inflate1.findViewById(R.id.img_line);
                    holder1.txt_content = (TextView) inflate1.findViewById(R.id.txt_content);
                    holder1.tv_state = (TextView) inflate1.findViewById(R.id.tv_state);
                    holder1.tv_code = (TextView) inflate1.findViewById(R.id.tv_code);
                    holder1.iv_switch = (ImageView) inflate1.findViewById(R.id.iv_switch);
                    holder1.iv_time_clock = (ImageView) inflate1.findViewById(R.id.iv_time_clock);

                    holder1.img_line.setImageResource(s.getIcon());
                    holder1.txt_content.setText("支线：" + s.getName());
                    holder1.tv_state.setVisibility(s.getSwitchState() == 0 ? View.VISIBLE : View.INVISIBLE);// 更新状态文字
                    holder1.tv_state.setText(SwitchBean.getSwitchFaultState(convertView.getContext(), s.fault));// 更新状态文字
                    holder1.tv_code.setText(s.getSerialNumber());
                    holder1.iv_switch.setVisibility(View.VISIBLE);
                    if (collectorBean.beShared == 0 || collectorBean.enable == 1) {// 不是被分享的 或 有集中器操作权限
                        holder1.iv_switch.setImageResource(NativeLine.DrawableToggle[switchBean.getSwitchState() == -1 ? 2 : switchBean.getSwitchState()]);
                        holder1.iv_switch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (iSwitchCheckListen != null) {
//                        holder.ivSwitch.setImageResource(NativeLine.DrawableToggle[2]);
                                    iSwitchCheckListen.onSwitch(switchBean);
                                }
                            }
                        });
                    }
                    holder1.iv_time_clock.setVisibility(s.timerCount > 0 ? View.VISIBLE : View.INVISIBLE);

                    holder.hideArea.addView(inflate1);
                }
            }
            if (child == null) {
                holder.toggle_icon.setVisibility(View.INVISIBLE);
            }
            //切换收缩按钮
            if (currentItem != position) {
                // 其他node 如果展开就收起
                if (holder.hideArea.isExpand()) {
                    holder.hideArea.collapse();
                    holder.sp_line.setVisibility(View.GONE);
                    holder.toggle_icon.setImageResource(R.drawable.toggle_down);
                }
            } else {
                // 当前点击的node 根据情况变化
                if (holder.hideArea.isExpand()) {
                    holder.sp_line.setVisibility(View.VISIBLE);
                    holder.toggle_icon.setImageResource(R.drawable.toggle_up);
                } else {
                    holder.sp_line.setVisibility(View.GONE);
                    holder.toggle_icon.setImageResource(R.drawable.toggle_down);
                }
            }
            ViewHolder finalHolder = holder;
            // 点击事件
            holder.showArea.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //用 currentItem 记录点击位置
                    currentItem = (int) (Integer) view.getTag();
                    finalHolder.hideArea.toggleExpand();
                    //通知adapter数据改变需要重新加载 必须有的一步
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private LinearLayout showArea;

            private ImageView img_line;
            private TextView txt_content;
            private TextView tv_state;
            private TextView tv_code;
            private ImageView iv_time_clock;
            private ImageView iv_switch;
            private ImageView toggle_icon;
            private ImageView sp_line;

            private ExpandLayout hideArea;
        }

        public interface ISwitchCheckListen {
            void onSwitch(SwitchBean cb);

            void onDelete(SwitchBean cb);

            void onAdd(SwitchBean cb);

            void onResetChose();

            void onChose(SwitchBean cb);
        }
    }
}
