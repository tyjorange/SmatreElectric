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

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_chart, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        final ChartListItemBean bean = mListData.get(position);
        holder.getBinding().setVariable(BR.mChartListItemBean, bean);
        holder.getBinding().executePendingBindings();
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onCollectorBeanClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
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
