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
import com.rejuvee.smartelectric.family.model.bean.ReportBean;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final LayoutInflater mLayoutInflater;
    private List<ReportBean> mListData;

    private CallBack mListener;

    /**
     * 回调声明
     */
    public interface CallBack {

        void onClick(ReportBean bean);
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setCallback(CallBack listener) {
        mListener = listener;
    }

    public ReportAdapter(Context context) {
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
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_report, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }
//    @Override
//    public int getCount() {
//        return mListData.size();
//    }

//    @Override
//    public ReportBean getItem(int position) {
//        return mListData.get(position);
//    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final ReportBean bean = mListData.get(position);
            holder.getBinding().setVariable(BR.mReportBean, bean);
            holder.getBinding().executePendingBindings();
            Context context = holder.getBinding().getRoot().getContext();
            //
            TextView textView1 = holder.itemView.findViewById(R.id.tv_type);
            textView1.setText(bean.timeType == 1 ? context.getString(R.string.vs88) : context.getString(R.string.vs137));
            //
            TextView textView2 = holder.itemView.findViewById(R.id.tv_time);
            textView2.setText(String.format("%s - %s", bean.startDay, bean.endDay));

            holder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(bean);
                }
            });
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

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = View.inflate(mContext, R.layout.item_line_report, null);
//            holder = new ViewHolder();
//            holder.tv_type = convertView.findViewById(R.id.tv_type);
//            holder.tv_time = convertView.findViewById(R.id.tv_time);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.tv_type.setText(getItem(position).timeType == 1 ? mContext.getString(R.string.vs88) : mContext.getString(R.string.vs137));
//        holder.tv_time.setText(String.format("%s - %s", getItem(position).startDay, getItem(position).endDay));
//        return convertView;
//    }

//    class ViewHolder {
//        TextView tv_type;
//        TextView tv_time;
//    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<ReportBean> list) {
        mListData.clear();
        mListData.addAll(list);
        notifyDataSetChanged();
    }
}
