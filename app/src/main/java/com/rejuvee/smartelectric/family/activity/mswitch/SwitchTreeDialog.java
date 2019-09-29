package com.rejuvee.smartelectric.family.activity.mswitch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.base.library.widget.SuperTextView;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.custom.MyNodeViewHolder;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.CollectorState;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路 树状图
 * <p>
 * 其他功能公用的线路选择界面  浮动对话框样式
 */
public class SwitchTreeDialog extends Dialog implements SwitchTree {
    private String TAG = "SwitchTreeActivity";
    private int viewType;// 1 遥控开关 2 实时情况 3 定时开关 4 节能信息 5 线路修改（名称图标） 6 曲线 7 安全设置 8 线路维护
    //    private SwipeRefreshLayout refreshLayout;
    private LinearLayout linearLayout;
    // 集中器 collector
    private CollectorBean mCollectorBean;
    private Handler mHandler;
    private List<SwitchBean> mListData = new ArrayList<>();
    // 线路
    private SwitchBean currentSwitch;
    // 树状图组件
    private AndroidTreeView treeView;
    private MyNodeViewHolder myNodeViewHolder;
    private MyNodeViewHolder.ISwitchCheckListen iSwitchCheckListen = new MyNodeViewHolder.ISwitchCheckListen() {
        @Override
        public void onSwitch(SwitchBean cb) {
            //Nothing
        }

        @Override
        public void onDelete(SwitchBean cb) {
            //Nothing
        }

        @Override
        public void onAdd(SwitchBean cb) {
            //Nothing
        }

        @Override
        public void onResetChose() {
            resetChose();
        }

        @Override
        public void onChose(SwitchBean cb) {
            currentSwitch = cb;
        }
    };
    private LoadingDlg waitDialog;
    private ChoseCallBack choseCallBack;

    public SwitchTreeDialog(Context context) {
        super(context);
    }

    public SwitchTreeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SwitchTreeDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public SwitchTreeDialog(Context context, int viewType, CollectorBean mCollectorBean, ChoseCallBack callBack) {
        super(context, R.style.MyDialogStyle);
        this.viewType = viewType;
        this.mCollectorBean = mCollectorBean;
        this.choseCallBack = callBack;
        initView(context);
        initData();
    }

    protected void initView(Context context) {
        setContentView(R.layout.activity_switch_tree);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        lp.height = (int) (d.heightPixels * 0.7);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);

        waitDialog = new LoadingDlg(getContext(), -1);

        treeView = new AndroidTreeView(getContext(), TreeNode.root());
        treeView.setDefaultAnimation(true);
        treeView.setDefaultViewHolder(MyNodeViewHolder.class);

        linearLayout = findViewById(R.id.ll_tree_view);
        linearLayout.addView(treeView.getView());

        //选择确认按钮
        SuperTextView superTextView = findViewById(R.id.st_wancheng);
        superTextView.setVisibility(View.VISIBLE);
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSwitch != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra("switchBean", currentSwitch);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
                    if (choseCallBack != null) {
                        choseCallBack.onChose(currentSwitch);
                        SwitchTreeDialog.this.dismiss();
                    }
                }
            }
        });

        LinearLayout title = findViewById(R.id.ll_tree_title);
        title.setVisibility(View.GONE);

        LinearLayout ll_choce_tip = findViewById(R.id.ll_choce_tip);
        ll_choce_tip.setVisibility(viewType != SwitchTree.YAOKONG ? View.VISIBLE : View.GONE);

        // 返回
        Toolbar backBtn = findViewById(R.id.ll_img_cancel);
        backBtn.setVisibility(View.GONE);

        // 取消
        ImageView choce_cancel = findViewById(R.id.choce_cancel);
        choce_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                SwitchTreeDialog.this.dismiss();
            }
        });
//        EventBus.getDefault().register(this);
    }

    private static final int MSG_FILLDATA = 3;// 填充tree数据

    @SuppressLint("HandlerLeak")
//    @Override
    protected void initData() {
//        NativeLine.init(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MSG_FILLDATA) {
                    fillData();
                }
            }
        };
        getSwitchByCollector();
    }

    /**
     * 获取集中器下的线路(switch)
     */
    private void getSwitchByCollector() {
        waitDialog.show();
        Core.instance(getContext()).getSwitchByCollector(mCollectorBean.getCode(), "hierarchy", new ActionCallbackListener<List<SwitchBean>>() {
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
                if (errorEvent == 12) {//无结果
                    CustomToast.showCustomErrorToast(getContext(), "请先添加线路");
//                    finish();
                } else {
                    CustomToast.showCustomErrorToast(getContext(), message);
                }
                // 显示空提示界面
                findViewById(R.id.ll_empty_layout).setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);

                waitDialog.dismiss();
//                refreshLayout.setRefreshing(false);
            }
        });
        // 初始化线路状态
        Core.instance(getContext()).getAllSwitchState(mCollectorBean.getCode(), new ActionCallbackListener<CollectorState>() {
            @Override
            public void onSuccess(CollectorState data) {
                switchState = data.getSwitchState();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(getContext(), message);
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
        myNodeViewHolder = new MyNodeViewHolder(getContext(), mCollectorBean, iSwitchCheckListen, viewType);
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

    private List<SwitchBean> switchState;

    /**
     * 重置所有node为未选择状态
     */
    private void resetChose() {
        if (switchState == null) {
            Log.e(TAG, "List<SwitchBean> switchState = null");
            return;
        }
        for (SwitchBean cb : switchState) {
            findChild(rootNode, cb.getSerialNumber());
        }
    }

    /**
     * 递归 寻找节点 重置 chose -> unchose
     *
     * @param parent 父节点
     * @param code   线路编码
     */
    private void findChild(TreeNode parent, String code) {
        if (parent != null) {
            SwitchBean bb = (SwitchBean) parent.getValue();
            if (bb != null && bb.getSerialNumber().equals(code)) {
                ImageView delImg = parent.getViewHolder().getView().findViewById(R.id.img_node_chose);
                delImg.setImageDrawable(parent.getViewHolder().getView().getContext().getDrawable(R.drawable.dx_unchose_slices));
            }
            for (TreeNode td : parent.getChildren()) {
                findChild(td, code);
            }
        }
    }

    public interface ChoseCallBack {
        void onChose(SwitchBean switchBean);
    }
}
