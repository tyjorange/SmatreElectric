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
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

import java.util.ArrayList;
import java.util.List;

public class CollectorBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int ITEM_VIEW_TYPE_NORMAL = 1;
    private static final int ITEM_VIEW_TYPE_ADD = 2;
    private final LayoutInflater mLayoutInflater;
    private List<CollectorBean> collectorBeanList;

    private CallBack mListener;

    public interface CallBack {

        void onCollectorBeanClick(CollectorBean bean);
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setCallback(CallBack listener) {
        mListener = listener;
    }

    public CollectorBeanAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        collectorBeanList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == collectorBeanList.size() - 1) {//最后一个
            return ITEM_VIEW_TYPE_ADD;
        }
        return ITEM_VIEW_TYPE_NORMAL;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        if (viewType == ITEM_VIEW_TYPE_NORMAL) {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_easy_device, parent, false);
        } else { //if (viewType == ITEM_VIEW_TYPE_ADD)
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_easy_device_add, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        final CollectorBean bean = collectorBeanList.get(position);
        holder.getBinding().setVariable(BR.mCollectorBean, bean);
        holder.getBinding().executePendingBindings();
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onCollectorBeanClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectorBeanList.size();
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<CollectorBean> list) {
        collectorBeanList.clear();
        collectorBeanList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一项
     *
     * @param bean
     */
    public void add(CollectorBean bean) {
        collectorBeanList.add(bean);
        notifyItemInserted(collectorBeanList.size());
    }

    /**
     * 移除一项
     *
     * @param position
     */
    public void remove(int position) {
        if (collectorBeanList.size() == 0) {
            return;
        }
        collectorBeanList.remove(position);
        notifyItemRemoved(position);
    }
}
