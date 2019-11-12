package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rejuvee.smartelectric.family.common.utils.TreeHelper;
import com.rejuvee.smartelectric.family.model.bean.Node;

import java.util.List;

public abstract class TreeListViewAdapter<T> extends BaseAdapter {
    protected Context mContext;
    /**
     * 存储所有可见的Node
     */
    private List<Node> mNodes;
//    private LayoutInflater mInflater;
    /**
     * 存储所有的Node
     */
    private List<Node> mAllNodes;

    /**
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;

    public interface OnTreeNodeClickListener {
        void onClick(Node node, int position);
    }

    public void setOnTreeNodeClickListener(
            OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     */
    TreeListViewAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) {
        mContext = context;
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
//        mInflater = LayoutInflater.from(context);

        //设置节点点击时，可以展开以及关闭；并且将ItemClick事件继续往外公布
        mTree.setOnItemClickListener((parent, view, position, id) -> {
            expandOrCollapse(position);

            if (onTreeNodeClickListener != null) {
                onTreeNodeClickListener.onClick(mNodes.get(position), position);
            }
        });

    }

    /**
     * 更新数据
     *
     * @param datas
     */
    public void setDataAndFlush(List<T> datas) {
        mAllNodes = TreeHelper.getSortedNodes(datas, 0);
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        notifyDataSetChanged();// 刷新视图
    }
    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
    private void expandOrCollapse(int position) {
        Node n = mNodes.get(position);

        if (n != null)// 排除传入参数错误异常
        {
            if (!n.isLeaf()) {
                n.setExpand(!n.isExpand());
                mNodes = TreeHelper.filterVisibleNode(mAllNodes);
                notifyDataSetChanged();// 刷新视图
            }
        }
    }

    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        // 设置内边距
//		convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);

}
