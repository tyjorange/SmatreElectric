package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.AddDeviceActivity;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.custom.FlushTimeTask;
import com.rejuvee.smartelectric.family.custom.MyNodeViewHolder;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorState;
import com.rejuvee.smartelectric.family.model.bean.ControllerId;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.SnackbarMessageShow;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * 线路 树状图
 */
@Deprecated
public class SwitchTreeActivity extends BaseActivity implements SwitchTree {
    private String TAG = "SwitchTreeActivity";
    private int viewType;// 1 遥控开关 2 实时情况 3 定时开关 4 节能信息 5 线路修改（名称图标）6 曲线 7 安全设置 8 线路维护
    //    private SwipeRefreshLayout refreshLayout;
    private LinearLayout linearLayout;
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private Handler mHandler;
    private Context mContext;
    private List<SwitchBean> mListData = new ArrayList<>();
    // 树状图组件
    private AndroidTreeView treeView;
    // 树状图node 断路器item长按事件
//    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
//        @Override
//        public boolean onLongClick(TreeNode node, Object value) {
//            try {
//                showPopupMenu(node.getViewHolder().getView(), (SwitchBean) value);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//    };
    private MyNodeViewHolder myNodeViewHolder;
    private MyNodeViewHolder.ISwitchCheckListen iSwitchCheckListen = new MyNodeViewHolder.ISwitchCheckListen() {

        @Override
        public void onSwitch(SwitchBean cb) {
            switchBreak(cb);
        }

        @Override
        public void onDelete(SwitchBean cb) {
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
                    deleteBreak(cb.getSwitchID());
                }
            });
            mDialogTip.show();
        }

        @Override
        public void onAdd(SwitchBean cb) {
            addSwitch(cb);
        }

        @Override
        public void onResetChose() {
            //Nothing
        }

        @Override
        public void onChose(SwitchBean cb) {
            //Nothing
        }

    };
    private LoadingDlg waitDialog;
    private DialogTip mDialogTip;
    private DialogTip mDialogSwitch;

    //    public static boolean NEED_REFRESH = false;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_switch_tree;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        viewType = getIntent().getIntExtra("viewType", -1);
        mCollectorBean = getIntent().getParcelableExtra("collectorBean");
        mDialogSwitch = new DialogTip(this);
        waitDialog = new LoadingDlg(this, -1);

        treeView = new AndroidTreeView(this, TreeNode.root());
        treeView.setDefaultAnimation(true);
        treeView.setDefaultViewHolder(MyNodeViewHolder.class);
//        treeView.setDefaultNodeLongClickListener(nodeLongClickListener);
//        refreshLayout = findViewById(R.id.swipeLayout);
        linearLayout = findViewById(R.id.ll_tree_view);
        ImageView backBtn = findViewById(R.id.img_cancel);
        TextView title = findViewById(R.id.tree_title);
        linearLayout.addView(treeView.getView());
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title.setText(getViewTitle());
        LinearLayout ll_choce_tip = findViewById(R.id.ll_choce_tip);
        if (viewType == SwitchTree.YAOKONG || viewType == SwitchTree.XIANLU_XIUGAI || viewType == SwitchTree.ANQUAN_SHEZHI || viewType == SwitchTree.XIANLU_WEIHU) {
            ll_choce_tip.setVisibility(View.GONE);
        } else {
            ll_choce_tip.setVisibility(View.VISIBLE);
        }
        if (viewType == SwitchTree.XIANLU_WEIHU) {
            ImageView img_add = findViewById(R.id.img_add);
            img_add.setVisibility(View.VISIBLE);
            img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSwitch(null);
                }
            });
            ImageView img_remove = findViewById(R.id.img_remove);
            img_remove.setVisibility(View.VISIBLE);
            img_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAllSwitchState(2);
                }
            });
        }
        EventBus.getDefault().register(this);
    }

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

    //    @SuppressLint("HandlerLeak")
    @Override
    protected void initData() {
        NativeLine.init(this);
        mHandler = new MyHandler(this);
        getSwitchByCollector();
    }

    private static class MyHandler extends Handler {
        WeakReference<SwitchTreeActivity> activityWeakReference;

        MyHandler(SwitchTreeActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SwitchTreeActivity settingActivity = activityWeakReference.get();
            currentSearchCount++;
            if (msg.what == MSG_CMD_RESULT) {
                settingActivity.getResultOfController();
            } else if (msg.what == MSG_TIMER) {
                settingActivity.getAllSwitchState(1);
            } else if (msg.what == MSG_FILLDATA) {
                settingActivity.fillData();
            } else if (msg.what == MSG_SWTCH_REFRESH) {
                settingActivity.getSwitchState();
            }
        }
    }
    private String getViewTitle() {
        switch (viewType) {
            case SwitchTree.YAOKONG:
                return getString(R.string.remote_control);
            case SwitchTree.XIANLU_XIUGAI:
                return getString(R.string.vs145);
            case SwitchTree.ANQUAN_SHEZHI:
                return getString(R.string.vs95);
            case SwitchTree.XIANLU_WEIHU:
                return getString(R.string.vs117);
        }
        return "";
    }
    //    @Deprecated
//    public static class MyHandler extends Handler {
//        private WeakReference<Activity> reference;
//
//        MyHandler(Activity activity) {
//            reference = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            currentSearchCount++;
//            if (msg.what == MSG_CMD_RESULT) {
////                getResultOfController();
//            } else if (msg.what == MSG_SWITCH_RESULT) {
////                waitLastResultAfterSwitch();
//            } else if (msg.what == MSG_COLLECTOR_REFRESH) {
////                waitLastResultAfterRefresh();
//            } else if (msg.what == MSG_FILLDATA) {
////                fillData();
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (NEED_REFRESH) {
//            NEED_REFRESH = false;
//            getSwitchByCollector();
//        }
        runTask = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        runTask = false;
    }

    private FlushTimeTask flushTimeTask;
    private final int flushTimeMill = 3000;//刷新间隔
    private boolean runTask = true;

    /**
     * 开始定时任务
     */
    private void startTimer() {
        flushTimeTask = new FlushTimeTask(flushTimeMill, new TimerTask() {
            @Override
            public void run() {
                if (runTask && viewType == SwitchTree.YAOKONG) {
                    Log.d(TAG, "FlushTimeTask run");
                    mHandler.sendEmptyMessage(MSG_TIMER);
                }
            }
        });
        flushTimeTask.start();
    }

    /**
     * 停止定时任务
     */
    private void stopTimer() {
        flushTimeTask.stop();
    }

    /**
     * 长按弹出菜单
     *
     * @param view
     * @param cb
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
//    @SuppressLint("RestrictedApi")
//    private void showPopupMenu(View view, SwitchBean cb) throws NoSuchFieldException, IllegalAccessException {
//        View viewById = view.findViewById(R.id.img_right);//弹出显示位置的锚点view
//        // 这里的view代表popupMenu需要依附的view
//        PopupMenu popupMenu = new PopupMenu(this, viewById, Gravity.END);
//        // 显示图标
//        Field mpopup = popupMenu.getClass().getDeclaredField("mPopup");
//        mpopup.setAccessible(true);
//        MenuPopupHelper mPopup = (MenuPopupHelper) mpopup.get(popupMenu);
//        mPopup.setForceShowIcon(true);
//        // 获取布局文件
//        getMenuInflater().inflate(R.menu.sample_menu, popupMenu.getMenu());
//        popupMenu.show();
//        // 通过上面这几行代码，就可以把控件显示出来了
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                // 控件每一个item的点击事件
//                switch (item.getItemId()) {
//                    case R.id.add:
////                        Toast.makeText(mContext, "add: " + value.getAlias(), Toast.LENGTH_SHORT).show();
//                        addSwitch(cb);
//                        break;
//                    case R.id.del:
////                        Toast.makeText(mContext, "del: " + value.getAlias(), Toast.LENGTH_SHORT).show();
//                        iSwitchCheckListen.onDelete(cb);
//                        break;
//                }
//                return true;
//            }
//        });
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                // 控件消失时的事件
//            }
//        });
//    }

    /**
     * 手动操作断路器[开、关]
     *
     * @param cb
     */
    private void switchBreak(SwitchBean cb) {
        if (mCollectorBean.getOnline() == 0) {// 集中器不在线
            CustomToast.showCustomErrorToast(SwitchTreeActivity.this, getString(R.string.vs159));
            return;
        }
        currentSwitchBean = cb;
        int _isSwitch = cb.getSwitchState() == -1 ? 2 : cb.getSwitchState();
        targetState = _isSwitch == 1;
        if (_isSwitch == 2) {
            CustomToast.showCustomErrorToast(SwitchTreeActivity.this, getString(R.string.vs160));
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
                    waitDialog.show();
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
                            CustomToast.showCustomErrorToast(SwitchTreeActivity.this, message);
                            waitDialog.dismiss();
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
                CustomToast.showCustomErrorToast(SwitchTreeActivity.this, message);
                if (currentSearchCount >= MAX_REQUEST_COUNT) {
                    currentSearchCount = 0;
//                    CustomToast.showCustomToast(SwitchTreeActivity.this, getString(R.string.op_fail));
                    SnackbarMessageShow.getInstance().showError(linearLayout, getString(R.string.vs156));
                    waitDialog.dismiss();
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
                    SnackbarMessageShow.getInstance().showError(linearLayout, getString(R.string.vs156));
                    waitDialog.dismiss();
                }
                break;
            case "1":
                SnackbarMessageShow.getInstance().showError(linearLayout, getString(R.string.vs157));
                waitDialog.dismiss();
                break;
            case "2":
                switch (cb.getRunResult()) {
                    case "0":
                        currentSearchCount = 0;
                        getSwitchState();
                        break;
                    case "34":
                        SnackbarMessageShow.getInstance().showError(linearLayout, getString(R.string.terminal_net_error));
                        waitDialog.dismiss();
                        break;
                    default:
                        SnackbarMessageShow.getInstance().showError(linearLayout, getString(R.string.operator_failure) + cb.getRunResult());
                        currentSearchCount = 0;
                        getSwitchState();
                        break;
                }
                break;
            case "3":
                SnackbarMessageShow.getInstance().showError(linearLayout, getString(R.string.vs158));
                waitDialog.dismiss();
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
                        findChild(rootNode, currentSwitchBean.getSerialNumber(), cb.getSwitchState(), cb.getFault(), 1);
                        waitDialog.dismiss();
                        CustomToast.showCustomToast(SwitchTreeActivity.this, getString(R.string.operator_sucess));
                    }
                } else {
                    CustomToast.showCustomErrorToast(SwitchTreeActivity.this, getString(R.string.vs155));
                    waitDialog.dismiss();
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(SwitchTreeActivity.this, message);
                waitDialog.dismiss();
            }
        });
    }

    private List<SwitchBean> switchState;

    /**
     * 刷新集中器下的所有线路状态
     *
     * @param type 1 刷新开关状态 2 切换删除按钮(线路维护)
     */
    private void getAllSwitchState(int type) {
        if (mListData.size() == 0) {
            return;
        }
        if (type == 1) {
            Core.instance(mContext).getAllSwitchState(mCollectorBean.getCode(), new ActionCallbackListener<CollectorState>() {
                @Override
                public void onSuccess(CollectorState data) {
                    switchState = data.getSwitchState();
                    for (SwitchBean cb : switchState) {
                        findChild(rootNode, cb.getSerialNumber(), cb.getSwitchState(), cb.getFault(), type);
                    }
                }

                @Override
                public void onFailure(int errorEvent, String message) {
                    CustomToast.showCustomErrorToast(SwitchTreeActivity.this, message);
                    waitDialog.dismiss();
                }
            });
        } else if (type == 2) {
            for (SwitchBean cb : switchState) {
                findChild(rootNode, cb.getSerialNumber(), cb.getSwitchState(), cb.getFault(), type);
            }
        }
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
                findViewById(R.id.ll_empty_layout).setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                mHandler.sendEmptyMessageDelayed(MSG_FILLDATA, 10);
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
                    CustomToast.showCustomErrorToast(SwitchTreeActivity.this, getString(R.string.vs29));
                } else {
                    CustomToast.showCustomErrorToast(SwitchTreeActivity.this, message);
                }
                // 显示空提示界面
                findViewById(R.id.ll_empty_layout).setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);

                waitDialog.dismiss();
//                refreshLayout.setRefreshing(false);
            }
        });
        // 初始化线路状态
        Core.instance(mContext).getAllSwitchState(mCollectorBean.getCode(), new ActionCallbackListener<CollectorState>() {
            @Override
            public void onSuccess(CollectorState data) {
                switchState = data.getSwitchState();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(SwitchTreeActivity.this, message);
            }
        });
    }

    private TreeNode rootNode;

    /**
     * 查询结果填充NodeTree
     */
    private void fillData() {
        rootNode = TreeNode.root();
        listToNode(rootNode, mListData, viewType);
        treeView.setRoot(rootNode);
        linearLayout.removeAllViews();
        linearLayout.addView(treeView.getView());
        treeView.expandAll();
        waitDialog.dismiss();
    }

    /**
     * 递归
     *
     * @param parent
     * @param mListData
     * @param viewType
     */
    private void listToNode(TreeNode parent, List<SwitchBean> mListData, int viewType) {
        myNodeViewHolder = new MyNodeViewHolder(mContext, mCollectorBean, iSwitchCheckListen, viewType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mListData.forEach(c -> {
                TreeNode treeNode = new TreeNode(c).setViewHolder(myNodeViewHolder);
                parent.addChild(treeNode);
                List<SwitchBean> child = c.getChild();
                if (child != null) {
                    listToNode(treeNode, child, viewType);
                }
            });
        } else {
            for (SwitchBean c : mListData) {
                TreeNode treeNode = new TreeNode(c).setViewHolder(myNodeViewHolder);
                parent.addChild(treeNode);
                List<SwitchBean> child = c.getChild();
                if (child != null) {
                    listToNode(treeNode, child, viewType);
                }
            }
        }
    }

    /**
     * 递归 寻找节点修改switch -> currentState
     *
     * @param parent       父节点
     * @param code         线路编码
     * @param currentState 开合状态
     * @param fault        状态文字 错误码
     * @param type         1 刷新开关状态 2 切换删除按钮
     */
    private void findChild(TreeNode parent, String code, int currentState, int fault, int type) {
        if (parent != null) {
            SwitchBean bb = (SwitchBean) parent.getValue();
            if (bb != null && bb.getSerialNumber().equals(code)) {
                if (type == 1) {
                    ImageView imageView = parent.getViewHolder().getView().findViewById(R.id.iv_switch);
                    imageView.setImageResource(NativeLine.DrawableToggle[currentState == -1 ? 2 : currentState]);// 更新开关图片
                    TextView textView = parent.getViewHolder().getView().findViewById(R.id.tv_state);
                    textView.setVisibility(currentState == 0 ? View.VISIBLE : View.GONE);// 更新状态文字
                    textView.setText(SwitchBean.getSwitchFaultState(mContext, fault));// 更新状态文字
                    bb.setSwitchState(currentState == -1 ? 2 : currentState);// 更新状态值
                } else if (type == 2) {
                    ImageView delImg = parent.getViewHolder().getView().findViewById(R.id.iv_del_switch);
                    if (delImg.getVisibility() == View.VISIBLE) {
                        delImg.setVisibility(View.GONE);
                    } else {
                        delImg.setVisibility(View.VISIBLE);
                    }
                }
            }
            for (TreeNode td : parent.getChildren()) {
                findChild(td, code, currentState, fault, type);
            }
        }
    }

    /**
     * 右上角菜单
     *
     * @param menu
     * @return
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_right_add, menu);
//        MenuItem addItem = menu.findItem(R.id.menu_add);
//        MenuItem refresh = menu.findItem(R.id.menu_refresh);
//        // 添加线路
//        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                addSwitch(null);
//                return false;
//            }
//        });
//        // 刷新线路
//        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                getAllSwitchState(true);
//                return false;
//            }
//        });
//        return true;
//    }

    /**
     * 跳转添加断路器(switch)界面
     *
     * @param mSwitch
     */
    private void addSwitch(SwitchBean mSwitch) {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        intent.setExtrasClassLoader(getClass().getClassLoader());
        intent.putExtra("collectorBean", mCollectorBean);
        intent.putExtra("switch", mSwitch);
        intent.putExtra("add_type", AddDeviceActivity.BREAK_ADD);
        startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_COLLECTOR);
    }

    /**
     * 删除断路器(switch)
     */
    private void deleteBreak(String switchId) {
        waitDialog.show();
        Core.instance(this).deleteBreak(switchId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
//                mListData.remove(position);
//                mAdapter.notifyDataSetChanged();
                getSwitchByCollector();
                CustomToast.showCustomToast(SwitchTreeActivity.this, getString(R.string.operator_sucess));
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomToast(SwitchTreeActivity.this, message);
                waitDialog.dismiss();
            }
        });
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return "控制";
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }
    @Override
    protected void dealloc() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(SwitchBean switchBean) {
        Log.d(TAG, "收到时间总线 线路更新的消息");
        getSwitchByCollector();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == CommonRequestCode.REQUEST_ADD_LINE) {
//                //添加后 刷新数据
//                getSwitchByCollector();
//            } else if (requestCode == CommonRequestCode.REQUEST_MODIFY_LINE) {
//                //修改后 刷新数据
//                getSwitchByCollector();
//            }
            getSwitchByCollector();
        }
    }
}
