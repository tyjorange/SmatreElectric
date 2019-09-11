package com.rejuvee.smartelectric.family.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchModifyActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchSettingActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTree;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.unnamed.b.atv.model.TreeNode;

/**
 * treeNode Adapter
 */
public class MyNodeViewHolder extends TreeNode.BaseNodeViewHolder<SwitchBean> {
    private int viewType;// 1 遥控开关 2 实时情况 3 定时开关 4 节能信息 5 线路修改（名称图标）6 曲线 7 安全设置 8 线路维护
    private final int sideMarginLeft = 0;// 子节点缩进
    private Context mContext;
    private SwitchBean currentSwitch;
    private CollectorBean collectorBean;// 集中器 collector
    private ISwitchCheckListen iSwitchCheckListen;
    //    private ISwitchChoseListen iSwitchChoseListen;
    private ViewHolder holder;

    public MyNodeViewHolder(Context context, CollectorBean collectorBean, ISwitchCheckListen listener, int type) {
        super(context);
        mContext = context;
        this.collectorBean = collectorBean;
        iSwitchCheckListen = listener;
        viewType = type;
    }

    public int getSideMarginLeft() {
        return sideMarginLeft;
    }

    @Override
    public View createNodeView(TreeNode treeNode, final SwitchBean switchBean) {
        currentSwitch = switchBean;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.item_tree_switch, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(this.dpToPx(this.sideMarginLeft) * (treeNode.getLevel() - 1), 0, 0, 0);
        view.setLayoutParams(layoutParams);

        holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new MyNodeViewHolder.ViewHolder();
            holder.ivSwitch = (ImageView) view.findViewById(R.id.iv_switch);
            holder.txtContent = (TextView) view.findViewById(R.id.txt_content);
            holder.imgLine = (ImageView) view.findViewById(R.id.img_line);
            holder.tvState = (TextView) view.findViewById(R.id.tv_state);
            holder.tvCode = (TextView) view.findViewById(R.id.tv_code);
            holder.imgTimeClock = (ImageView) view.findViewById(R.id.iv_time_clock);
            holder.toggleView = (ImageView) view.findViewById(R.id.toggle_icon);
            holder.ivDel = (ImageView) view.findViewById(R.id.iv_del_switch);
            holder.ivAdd = (ImageView) view.findViewById(R.id.iv_add_switch);
            holder.imgRight = (ImageView) view.findViewById(R.id.img_right);
            holder.imgNodeChose = (ImageView) view.findViewById(R.id.img_node_chose);

            view.setTag(holder);
        }
        holder.txtContent.setText(switchBean.getName());
        holder.imgLine.setImageResource(switchBean.getIcon());
        holder.tvState.setVisibility(switchBean.getSwitchState() == 0 ? View.VISIBLE : View.GONE);// 更新状态文字
        holder.tvState.setText(SwitchBean.getSwitchFaultState(view.getContext(), switchBean.fault));// 更新状态文字
        holder.tvCode.setText(switchBean.getSerialNumber());
        holder.imgTimeClock.setVisibility(switchBean.timerCount > 0 ? View.VISIBLE : View.GONE);
        initListener(switchBean);
        return view;
    }

    private int dpToPx(int dp) {
        return (int) ((float) dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public void toggle(boolean active) {
        if (currentSwitch.getChild().size() == 0) {
            holder.toggleView.setVisibility(View.INVISIBLE);
        } else {
            holder.toggleView.setImageResource(active ? R.drawable.toggle_up : R.drawable.toggle_down);
        }
    }

    /**
     * 初始化监听
     *
     * @param switchBean
     */
    private void initListener(SwitchBean switchBean) {
        if (viewType == SwitchTree.YAOKONG) {
            holder.ivSwitch.setVisibility(View.VISIBLE);
            if (collectorBean.beShared == 0 || collectorBean.enable == 1) {// 不是被分享的 或 有集中器操作权限
                holder.ivSwitch.setImageResource(NativeLine.DrawableToggle[switchBean.getSwitchState() == -1 ? 2 : switchBean.getSwitchState()]);
                holder.ivSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (iSwitchCheckListen != null) {
//                        holder.ivSwitch.setImageResource(NativeLine.DrawableToggle[2]);
                            iSwitchCheckListen.onSwitch(switchBean);
                        }
                    }
                });
            }
        } else if (viewType == SwitchTree.SHISHI) {
            holder.imgNodeChose.setVisibility(View.VISIBLE);
            holder.imgNodeChose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iSwitchCheckListen != null) {
                        iSwitchCheckListen.onResetChose();
                        iSwitchCheckListen.onChose(switchBean);
                    }
                    holder.imgNodeChose.setImageDrawable(v.getContext().getDrawable(R.drawable.dx_chose_slices));
                }
            });
//            holder.imgRight.setVisibility(View.VISIBLE);
//            holder.imgRight.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("switchBean", switchBean);
////                    intent.putExtra("collectorId", collectorBean.getCollectorID());
//                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
//                    ((Activity) mContext).finish();
//                }
//            });
//            holder.tvState.setVisibility(View.GONE);
        } else if (viewType == SwitchTree.DINGSHI) {
            holder.imgNodeChose.setVisibility(View.VISIBLE);
            holder.imgNodeChose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iSwitchCheckListen != null) {
                        iSwitchCheckListen.onResetChose();
                        iSwitchCheckListen.onChose(switchBean);
                    }
                    holder.imgNodeChose.setImageDrawable(v.getContext().getDrawable(R.drawable.dx_chose_slices));
                }
            });
//            holder.imgRight.setVisibility(View.VISIBLE);
//            holder.imgRight.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("switchBean", switchBean);
//                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
//                    ((Activity) mContext).finish();
//                }
//            });
//            holder.tvState.setVisibility(View.GONE);
        } else if (viewType == SwitchTree.JIENENG) {
            holder.imgNodeChose.setVisibility(View.VISIBLE);
            holder.imgNodeChose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iSwitchCheckListen != null) {
                        iSwitchCheckListen.onResetChose();
                        iSwitchCheckListen.onChose(switchBean);
                    }
                    holder.imgNodeChose.setImageDrawable(v.getContext().getDrawable(R.drawable.dx_chose_slices));
                }
            });
//            holder.imgRight.setVisibility(View.VISIBLE);
//            holder.imgRight.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("switchBean", switchBean);
//                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
//                    ((Activity) mContext).finish();
//                }
//            });
//            holder.tvState.setVisibility(View.GONE);
        } else if (viewType == SwitchTree.XIANLU_XIUGAI) {
            holder.imgRight.setVisibility(View.VISIBLE);
            holder.imgRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SwitchModifyActivity.class);
                    intent.putExtra("switchBean", switchBean);
                    ((Activity) mContext).startActivityForResult(intent, CommonRequestCode.REQUEST_MODIFY_LINE);
                }
            });
            holder.tvState.setVisibility(View.GONE);
        } else if (viewType == SwitchTree.QUXIAN) {
            holder.imgNodeChose.setVisibility(View.VISIBLE);
            holder.imgNodeChose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iSwitchCheckListen != null) {
                        iSwitchCheckListen.onResetChose();
                        iSwitchCheckListen.onChose(switchBean);
                    }
                    holder.imgNodeChose.setImageDrawable(v.getContext().getDrawable(R.drawable.dx_chose_slices));
                }
            });
//            holder.imgRight.setVisibility(View.VISIBLE);
//            holder.imgRight.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, SwitchModifyActivity.class);
//                    intent.putExtra("switchBean", switchBean);
//                    ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
//                    ((Activity) mContext).finish();
//                }
//            });
//            holder.tvState.setVisibility(View.GONE);
        } else if (viewType == SwitchTree.ANQUAN_SHEZHI) {
            holder.imgRight.setVisibility(View.VISIBLE);
            holder.imgRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SwitchSettingActivity.class);
                    intent.putExtra("switchBean", switchBean);
                    ((Activity) mContext).startActivityForResult(intent, CommonRequestCode.REQUEST_MODIFY_LINE);
                }
            });
            holder.tvState.setVisibility(View.GONE);
        } else if (viewType == SwitchTree.XIANLU_WEIHU) {
            holder.imgRight.setVisibility(View.GONE);
            holder.tvState.setVisibility(View.GONE);
            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iSwitchCheckListen != null) {
                        iSwitchCheckListen.onAdd(switchBean);
                    }
                }
            });
//            holder.ivDel.setVisibility(View.VISIBLE);
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iSwitchCheckListen != null) {
                        iSwitchCheckListen.onDelete(switchBean);
                    }
                }
            });
        }
    }

    class ViewHolder {
        ImageView imgLine;
        TextView txtContent;
        TextView tvState;
        TextView tvCode;
        ImageView ivSwitch;
        ImageView imgTimeClock;
        ImageView toggleView;
        ImageView ivDel;
        ImageView ivAdd;
        ImageView imgRight;
        ImageView imgNodeChose;
    }

    public interface ISwitchCheckListen {
        void onSwitch(SwitchBean cb);

        void onDelete(SwitchBean cb);

        void onAdd(SwitchBean cb);

        void onResetChose();

        void onChose(SwitchBean cb);
    }

}
