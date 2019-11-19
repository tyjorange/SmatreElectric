package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.custom.BindingViewHolder;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.bean.WarnBean;

import java.util.ArrayList;
import java.util.List;

public class LineWarnBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final LayoutInflater mLayoutInflater;
    private List<WarnBean> mListData;

    public LineWarnBeanAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListData.size() != 0) {
            return VIEW_TYPE_ITEM;
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
        } else {//if (viewType == VIEW_TYPE_ITEM)
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_warn, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final WarnBean bean = mListData.get(position);
            holder.getBinding().setVariable(BR.mWarnBean, bean);
            holder.getBinding().executePendingBindings();
            Context context = holder.getBinding().getRoot().getContext();
            //
            TextView textView1 = holder.itemView.findViewById(R.id.tv_type);
            textView1.setText(SwitchBean.getSwitchFaultState(context, bean.getParamID()));
            //
            TextView textView2 = holder.itemView.findViewById(R.id.tv_state1);
            textView2.setText(String.format("%s%s", context.getString(R.string.vs9), bean.getParamValue()));
            //
            TextView textView3 = holder.itemView.findViewById(R.id.tv_state2);
            textView3.setText(String.format("%s%s", context.getString(R.string.vs10), bean.getWarningValue()));
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
    public void addAll(List<WarnBean> list) {
//        mListData.clear();
        mListData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 清除整个列表
     */
    public void clearAll() {
        mListData.clear();
        notifyDataSetChanged();
    }
}
