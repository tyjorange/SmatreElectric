package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.collector.CollectorAttrSetActivity;
import com.rejuvee.smartelectric.family.common.custom.BindingViewHolder;
import com.rejuvee.smartelectric.family.databinding.SetcountsItemsBinding;

import java.util.ArrayList;
import java.util.List;

public class AttrSetBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private List<CollectorAttrSetActivity.AttrItem> counts;
    private final LayoutInflater mLayoutInflater;
    private int currentPosition;
    private CallBack mListener;

    /**
     * 回调声明
     */
    public interface CallBack {

        void onCollectorBeanClick(int pos);
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setCallback(CallBack listener) {
        mListener = listener;
    }

    public AttrSetBeanAdapter(Context context, int currentPosition) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentPosition = currentPosition;
        counts = new ArrayList<>();
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SetcountsItemsBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.setcounts_items, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        CollectorAttrSetActivity.AttrItem bean = counts.get(position);
        bean.setSelected(currentPosition != position);
        holder.getBinding().setVariable(BR.attrItem, bean);
        holder.getBinding().executePendingBindings();
//        holder.imgSlecte.setImageDrawable(curposition != position ? getDrawable(R.drawable.dx_unchose_slices) : getDrawable(R.drawable.dx_chose_slices));
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onCollectorBeanClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return counts.size();
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<CollectorAttrSetActivity.AttrItem> list) {
        counts.clear();
        counts.addAll(list);
        notifyDataSetChanged();
    }
}
