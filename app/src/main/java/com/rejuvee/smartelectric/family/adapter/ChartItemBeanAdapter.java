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
import com.rejuvee.smartelectric.family.common.custom.BindingViewHolder;
import com.rejuvee.smartelectric.family.model.bean.ChartItemBean;

import java.util.ArrayList;
import java.util.List;

public class ChartItemBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM_LEFT = 1;
    private static final int VIEW_TYPE_ITEM_RIGHT = 2;
    private final LayoutInflater mLayoutInflater;
    private List<ChartItemBean> mListData;
    private String username;

    public ChartItemBeanAdapter(Context context, String username) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListData = new ArrayList<>();
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListData.size() != 0) {
            if (username.equals(mListData.get(position).getUsername())) {
                return VIEW_TYPE_ITEM_RIGHT;
            } else {
                return VIEW_TYPE_ITEM_LEFT;
            }
        }
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        return VIEW_TYPE_EMPTY;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //在这里根据不同的viewType进行引入不同的布局
        ViewDataBinding binding;
        if (viewType == VIEW_TYPE_EMPTY) {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.empty_layout, parent, false);
        } else if (viewType == VIEW_TYPE_ITEM_RIGHT) {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_chat_right, parent, false);
        } else {// VIEW_TYPE_ITEM_LEFT
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_chat_left, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final ChartItemBean bean = mListData.get(position);
            holder.getBinding().setVariable(BR.mChartItemBean, bean);
            holder.getBinding().executePendingBindings();
//            holder.itemView.setOnClickListener(v -> {
//                if (mListener != null) {
//                    mListener.onCollectorBeanClick(bean);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListData.size() != 0) {
            //同时这里也需要添加判断，如果mData.size()为0的话，只引入一个布局，就是emptyView
            return mListData.size();
        }
        // 那么，这个recyclerView的itemCount为1
        return 1;
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<ChartItemBean> list) {
        mListData.clear();
        mListData.addAll(list);
        notifyDataSetChanged();
    }
}
