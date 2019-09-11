package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.rejuvee.smartelectric.family.activity.AddDeviceActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.ControllerId;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.utils.utils;
import com.rejuvee.smartelectric.family.widget.DialogTip;
import com.rejuvee.smartelectric.family.widget.ExpandLayout;
import com.rejuvee.smartelectric.family.widget.LoadingDlg;
import com.rejuvee.smartelectric.family.widget.SnackbarMessageShow;

import java.util.List;

public class YaoKongDetailActivity extends BaseActivity {
    private String TAG = "YaoKongDetailActivity";
    private int viewType;
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private SwitchBean rootSwitchBean;
    private List<SwitchBean> childList;
    private SwitchBean currentSwitchBean;
    private OneExpandAdapter adapter;
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
    private DialogTip mDialogTip;
    private ImageView iv_switch_root;

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
        rootSwitchBean = getIntent().getParcelableExtra("SwitchBean");
        viewType = getIntent().getIntExtra("viewType", -1);
        mWaitDialog = new LoadingDlg(this, -1);
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (viewType) {
            case SwitchTree.YAOKONG:
                iv_switch_root = findViewById(R.id.iv_switch_root);
                iv_switch_root.setImageResource(NativeLine.DrawableToggle[rootSwitchBean.getSwitchState() == -1 ? 2 : rootSwitchBean.getSwitchState()]);
                iv_switch_root.setVisibility(View.VISIBLE);
                iv_switch_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isRootSwitch = true;
                        switchBreak(rootSwitchBean);
                    }
                });
                break;
            case SwitchTree.XIANLU_WEIHU:
                ImageView iv_switch_add = findViewById(R.id.iv_add_child_switch);
                iv_switch_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSwitch(rootSwitchBean);
                    }
                });
                iv_switch_add.setVisibility(View.VISIBLE);
                ImageView iv_switch_remove_toggle = findViewById(R.id.iv_switch_remove_toggle);
                iv_switch_remove_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleDelIcon();
                    }
                });
                iv_switch_remove_toggle.setVisibility(View.VISIBLE);
                break;
        }
        TextView tv_root_name = findViewById(R.id.tv_root_name);
        tv_root_name.setText("线路：" + rootSwitchBean.getName());
        lvProduct = (ListView) findViewById(R.id.lv_products);
        lvProduct.setEmptyView(findViewById(R.id.empty_layout));
        childList = rootSwitchBean.getChild();
        adapter = new OneExpandAdapter(this, mCollectorBean, viewType, childList, new OneExpandAdapter.ISwitchCheckListen() {
            @Override
            public void onSwitch(SwitchBean cb) {
                isRootSwitch = false;
                switchBreak(cb);
            }

            @Override
            public void onDelete(SwitchBean cb) {
                deleteSwitch(cb.getSwitchID());
            }

            @Override
            public void onAdd(SwitchBean cb) {
                addSwitch(cb);
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
                    getSwitchStateOne();
                }
            }
        };
        getSwitchByCollector();
    }

    /**
     * 切换删除按钮
     */
    private void toggleDelIcon() {
        //分线
        for (SwitchBean taskBean : rootSwitchBean.getChild()) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
            //支线
            for (SwitchBean s : taskBean.getChild()) {
                if (s.showDelIcon == View.GONE) {
                    s.showDelIcon = View.VISIBLE;
                } else {
                    s.showDelIcon = View.GONE;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取集中器下的线路(switch)
     */
    private void getSwitchByCollector() {
        mWaitDialog.show();
        Core.instance(this).getSwitchByCollector(mCollectorBean.getCode(), "hierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                for (SwitchBean ss : data) {
                    // 取当前线路的支线
                    if (ss.getSwitchID().equals(rootSwitchBean.getSwitchID())) {
                        childList.clear();
                        childList.addAll(ss.getChild());
                        break;
                    }
                }
                // 隐藏空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);

//                mHandler.sendEmptyMessageDelayed(MSG_FILLDATA, 10);
//                mHandler.sendEmptyMessageDelayed(MSG_SWTCH_REFRESH, 1000);
                adapter.notifyDataSetChanged();
                mWaitDialog.dismiss();
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
                    CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, message);
                }
                // 显示空提示界面
//                findViewById(R.id.ll_empty_layout).setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.GONE);

                mWaitDialog.dismiss();
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
    private void deleteSwitch(String switchId) {
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
                mWaitDialog.show();
                Core.instance(mContext).deleteBreak(switchId, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
//                mListData.remove(position);
//                mAdapter.notifyDataSetChanged();
//                        getSwitchByCollector();
                        CustomToast.showCustomToast(YaoKongDetailActivity.this, getString(R.string.operator_sucess));
                        mWaitDialog.dismiss();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomToast(YaoKongDetailActivity.this, message);
                        mWaitDialog.dismiss();
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
        Intent intent = new Intent(mContext, AddDeviceActivity.class);
        intent.setExtrasClassLoader(getClass().getClassLoader());
        intent.putExtra("collectorBean", mCollectorBean);
        intent.putExtra("switch", mSwitch);
        intent.putExtra("add_type", AddDeviceActivity.BREAK_ADD);
        startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_LINE);
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
        targetState = (_isSwitch == 1);
        if (_isSwitch == 2) {
            CustomToast.showCustomErrorToast(YaoKongDetailActivity.this, "请刷当前线路状态再操作");
        } else {
            String title = !targetState ?
                    getString(R.string.open_break) :
                    getString(R.string.close_break);
            String desc = !targetState ?
                    String.format(getString(R.string.break_op_tip), getString(R.string.open), currentSwitchBean.getName()) :
                    String.format(getString(R.string.break_op_tip), getString(R.string.close), currentSwitchBean.getName());
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

    private boolean isRootSwitch;// 是否操作的分线

    /**
     * 刷新单条线路状态
     */
    private void getSwitchStateOne() {
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
                        if (isRootSwitch) {
                            iv_switch_root.setImageResource(NativeLine.DrawableToggle[cb.getSwitchState() == -1 ? 2 : cb.getSwitchState()]);// 更新开关图片
                        } else {

                        }
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
                        getSwitchStateOne();
                        break;
                    case "34":
                        SnackbarMessageShow.getInstance().showError(lvProduct, getString(R.string.terminal_net_error));
                        mWaitDialog.dismiss();
                        break;
                    default:
                        SnackbarMessageShow.getInstance().showError(lvProduct, getString(R.string.operator_failure) + cb.getRunResult());
                        currentSearchCount = 0;
                        getSwitchStateOne();
                        break;
                }
                break;
            case "3":
                SnackbarMessageShow.getInstance().showError(lvProduct, "请求失败，多人同时操作冲突");
                mWaitDialog.dismiss();
                break;
        }
    }

    @Override
    protected void dealloc() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_ADD_LINE) {
                //添加后 刷新数据
//                getSwitchByCollector();
                setResult(RESULT_OK);
                finish();//TODO 添加/删除完分线支线直接结束界面
            }
        }
    }
    /**
     * 分线 数据适配器
     *
     * 点击item展开隐藏部分,再次点击收起
     * 只可展开一条记录
     *
     */
    public static class OneExpandAdapter extends BaseAdapter {
        private Context context;
        private CollectorBean collectorBean;
        //分线
        private List<SwitchBean> switchBeanList;
        private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心
        private ISwitchCheckListen iSwitchCheckListen;
        private int viewType;
        //支线
//        private SubAdapter subAdapter;
//        private List<SwitchBean> switchSubBeanList;

        /**
         * @param context
         * @param collectorBean
         * @param list
         * @param viewType
         */
        OneExpandAdapter(Context context, CollectorBean collectorBean, int viewType, List<SwitchBean> list, ISwitchCheckListen iSwitchCheckListen) {
            this.context = context;
            this.collectorBean = collectorBean;
            this.switchBeanList = list;
            this.iSwitchCheckListen = iSwitchCheckListen;
            this.viewType = viewType;
        }

        @Override
        public int getCount() {
            return switchBeanList.size();
        }

        @Override
        public SwitchBean getItem(int position) {
            return switchBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            // ##分线
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
                holder.sub_list = (ListView) convertView.findViewById(R.id.sub_list);
                holder.iv_del_switch = (ImageView) convertView.findViewById(R.id.iv_del_switch);
                holder.iv_add_sub_switch = (ImageView) convertView.findViewById(R.id.iv_add_sub_switch);
                holder.toggle_ll = (LinearLayout) convertView.findViewById(R.id.toggle_ll);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，为了记录点击的 position，我们正好用 position 设置 Tag
            holder.showArea.setTag(position);
            holder.toggle_ll.setTag(position);

            SwitchBean currentSwitchBean = getItem(position);
            holder.img_line.setImageResource(currentSwitchBean.getIcon());
            holder.txt_content.setText("分线：" + currentSwitchBean.getName());
//            holder.tv_state.setVisibility(currentSwitchBean.getSwitchState() == 0 ? View.VISIBLE : View.INVISIBLE);// 更新状态文字
//            holder.tv_state.setText(SwitchBean.getSwitchFaultState(context, currentSwitchBean.fault));// 更新状态文字
            holder.tv_state.setText(currentSwitchBean.getChild().size() + "条支线");
            holder.tv_code.setText(currentSwitchBean.getSerialNumber());
            holder.iv_time_clock.setVisibility(currentSwitchBean.timerCount > 0 ? View.VISIBLE : View.INVISIBLE);
            List<SwitchBean> child = currentSwitchBean.getChild();
            // ##支线
            SubAdapter sa = new SubAdapter(context, collectorBean, viewType, child, new SubAdapter.ISwitchCheckListen() {
                @Override
                public void onSwitch(SwitchBean cb) {

                }

                @Override
                public void onDelete(SwitchBean cb) {
                    iSwitchCheckListen.onDelete(cb);
                }

                @Override
                public void onAdd(SwitchBean cb) {
                    iSwitchCheckListen.onAdd(cb);
                }

                @Override
                public void onResetChose() {

                }

                @Override
                public void onChose(SwitchBean cb) {

                }
            });
            holder.sub_list.setAdapter(sa);
            utils.setListViewHeightBasedOnChildren(holder.sub_list);
//            holder.hideArea.setViewHeight(holder.sub_list.getMeasuredHeight());

            switch (viewType) {
                case YaoKongActivity.YAOKONG:
                    holder.iv_switch.setVisibility(View.VISIBLE);
                    if (collectorBean.beShared == 0 || collectorBean.enable == 1) {// 不是被分享的 或 有集中器操作权限
                        holder.iv_switch.setImageResource(NativeLine.DrawableToggle[currentSwitchBean.getSwitchState() == -1 ? 2 : currentSwitchBean.getSwitchState()]);
                        holder.iv_switch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (iSwitchCheckListen != null) {
                                    iSwitchCheckListen.onSwitch(currentSwitchBean);
                                }
                            }
                        });
                    }
                    break;
                case YaoKongActivity.XIANLU_WEIHU:
                    holder.iv_add_sub_switch.setVisibility(View.VISIBLE);
                    holder.iv_add_sub_switch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iSwitchCheckListen.onAdd(currentSwitchBean);
                        }
                    });
                    holder.iv_del_switch.setVisibility(currentSwitchBean.showDelIcon);
                    holder.iv_del_switch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iSwitchCheckListen.onDelete(currentSwitchBean);
                        }
                    });
                    break;
            }

            if (child != null && child.size() == 0) {// 没有支线
//                holder.toggle_icon.setVisibility(View.INVISIBLE);
                holder.toggle_ll.setVisibility(View.GONE);
                holder.sp_line.setVisibility(View.INVISIBLE);
            } else {// 有支线
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
                // 点击事件
                holder.toggle_ll.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //用 currentItem 记录点击位置
                        currentItem = (int) (Integer) view.getTag();
                        finalHolder.hideArea.toggleExpand();
                        //通知adapter数据改变需要重新加载 必须有的一步
                        notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }

        private class ViewHolder {
            private LinearLayout showArea;

            private ImageView iv_del_switch;
            private ImageView img_line;
            private TextView txt_content;
            private TextView tv_state;
            private TextView tv_code;
            private ImageView iv_time_clock;
            private ImageView iv_switch;
            private ImageView toggle_icon;
            private ImageView sp_line;
            private ImageView iv_add_sub_switch;
            private LinearLayout toggle_ll;

            private ExpandLayout hideArea;
            private ListView sub_list;
        }
        public interface ISwitchCheckListen {
            void onSwitch(SwitchBean cb);

            void onDelete(SwitchBean cb);

            void onAdd(SwitchBean cb);

            void onResetChose();

            void onChose(SwitchBean cb);
        }
    }

    /**
     * 支线 数据适配器
     */
    static class SubAdapter extends BaseAdapter {
        private Context context;
        private CollectorBean collectorBean;
        private List<SwitchBean> list;
        private ISwitchCheckListen iSwitchCheckListen;
        private int viewType;

        SubAdapter(Context context, CollectorBean collectorBean, int viewType, List<SwitchBean> list, ISwitchCheckListen iSwitchCheckListen) {
            this.context = context;
            this.collectorBean = collectorBean;
            this.viewType = viewType;
            this.list = list;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            SubHolder holder1 = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_test2, parent, false);
                holder1 = new SubHolder();
                holder1.img_line = (ImageView) convertView.findViewById(R.id.img_line);
                holder1.txt_content = (TextView) convertView.findViewById(R.id.txt_content);
                holder1.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
                holder1.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
                holder1.iv_switch = (ImageView) convertView.findViewById(R.id.iv_switch);
                holder1.iv_time_clock = (ImageView) convertView.findViewById(R.id.iv_time_clock);
                holder1.iv_del_switch = (ImageView) convertView.findViewById(R.id.iv_del_switch);
//                    holder1.iv_add_sub_switch = (ImageView) inflate1.findViewById(R.id.iv_add_sub_switch);

                convertView.setTag(holder1);
            } else {
                holder1 = (SubHolder) convertView.getTag();
            }
            SwitchBean currentSubSwitchBean = getItem(position);
            holder1.img_line.setImageResource(currentSubSwitchBean.getIcon());
            holder1.txt_content.setText("支线：" + currentSubSwitchBean.getName());
//            holder1.tv_state.setVisibility(currentSubSwitchBean.getSwitchState() == 0 ? View.VISIBLE : View.INVISIBLE);// 更新状态文字
//            holder1.tv_state.setText(SwitchBean.getSwitchFaultState(context, currentSubSwitchBean.fault));// 更新状态文字
            holder1.tv_code.setText(currentSubSwitchBean.getSerialNumber());
            holder1.iv_time_clock.setVisibility(currentSubSwitchBean.timerCount > 0 ? View.VISIBLE : View.INVISIBLE);

            switch (viewType) {
                case YaoKongActivity.YAOKONG:
                    holder1.iv_switch.setVisibility(View.VISIBLE);
                    if (collectorBean.beShared == 0 || collectorBean.enable == 1) {// 不是被分享的 或 有集中器操作权限
                        holder1.iv_switch.setImageResource(NativeLine.DrawableToggle[currentSubSwitchBean.getSwitchState() == -1 ? 2 : currentSubSwitchBean.getSwitchState()]);
                        holder1.iv_switch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iSwitchCheckListen.onSwitch(currentSubSwitchBean);
                            }
                        });
                    }
                    break;
                case YaoKongActivity.XIANLU_WEIHU:
//                            holder1.iv_add_sub_switch.setVisibility(View.VISIBLE);
//                            holder1.iv_add_sub_switch.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
                    holder1.iv_del_switch.setVisibility(currentSubSwitchBean.showDelIcon);
                    holder1.iv_del_switch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iSwitchCheckListen.onDelete(currentSubSwitchBean);
                        }
                    });
                    break;
            }
            return convertView;
        }

        private class SubHolder {
            private ImageView iv_del_switch;
            private ImageView img_line;
            private TextView txt_content;
            private TextView tv_state;
            private TextView tv_code;
            private ImageView iv_time_clock;
            private ImageView iv_switch;
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
