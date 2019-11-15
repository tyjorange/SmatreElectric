package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BindingViewHolder;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;

import java.util.ArrayList;
import java.util.List;

public class ChartListItemBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_EMPTY = 0;
    private final LayoutInflater mLayoutInflater;
    private List<ChartListItemBean> mListData;

    private CallBack mListener;

    /**
     * 回调声明
     */
    public interface CallBack {

        void onCollectorBeanClick(ChartListItemBean bean);
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setCallback(CallBack listener) {
        mListener = listener;
    }

    public ChartListItemBeanAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        if (mListData.size() != 0) {
            return VIEW_TYPE_ITEM;
        }
        return VIEW_TYPE_EMPTY;
        //如果有数据，则使用ITEM的布局
    }
    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //在这里根据不同的viewType进行引入不同的布局
        ViewDataBinding binding;
        if (viewType == VIEW_TYPE_EMPTY) {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.empty_layout, parent, false);
        } else {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_chart, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final ChartListItemBean bean = mListData.get(position);
            holder.getBinding().setVariable(BR.mChartListItemBean, bean);
            holder.getBinding().executePendingBindings();
            holder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onCollectorBeanClick(bean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //同时这里也需要添加判断，如果mData.size()为0的话，只引入一个布局，就是emptyView
        // 那么，这个recyclerView的itemCount为1
        if (mListData.size() != 0) {
            return mListData.size();
        }
        return 1;
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<ChartListItemBean> list) {
        mListData.clear();
        mListData.addAll(list);
        notifyDataSetChanged();
    }
}
