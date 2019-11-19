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
import com.rejuvee.smartelectric.family.model.bean.ReportDetailBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportAlarmAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final LayoutInflater mLayoutInflater;
    private List<ReportDetailBean.Alarm> mListData;

    public ReportAlarmAdapter(Context context) {
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
//    @Override
//    public int getCount() {
//        return mListData.size();
//    }

//    @Override
//    public ReportDetailBean.Alarm getItem(int position) {
//        return mListData.get(position);
//    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //在这里根据不同的viewType进行引入不同的布局
        ViewDataBinding binding;
        if (viewType == VIEW_TYPE_EMPTY) {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.empty_layout, parent, false);
        } else {//if (viewType == VIEW_TYPE_ITEM)
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_report_alarm, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final ReportDetailBean.Alarm bean = mListData.get(position);
            holder.getBinding().setVariable(BR.mAlarm, bean);
            holder.getBinding().executePendingBindings();
            Context context = holder.getBinding().getRoot().getContext();
            //
            TextView textView1 = holder.itemView.findViewById(R.id.tv_line_name);
            textView1.setText(String.format("%s%s", context.getString(R.string.vs4), bean.getSwitchName()));
            //
            TextView textView2 = holder.itemView.findViewById(R.id.tv_alarm_type);
            textView2.setText(SwitchBean.getSwitchFaultState(context, bean.getErrType()));
            //
            TextView textView3 = holder.itemView.findViewById(R.id.tv_alarm_count);
            textView3.setText(String.format(Locale.getDefault(), "%d%s", bean.getCount(), context.getString(R.string.vs17)));
            //
            TextView textView4 = holder.itemView.findViewById(R.id.tv_line_id);
            textView4.setText(String.format("%s%s", context.getString(R.string.vs6), bean.getSwitchCode()));
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
//            convertView = View.inflate(mContext, R.layout.item_report_alarm, null);
//            holder = new ViewHolder();
//            holder.tv_line_name = convertView.findViewById(R.id.tv_line_name);
//            holder.tv_alarm_type = convertView.findViewById(R.id.tv_alarm_type);
//            holder.tv_alarm_count = convertView.findViewById(R.id.tv_alarm_count);
//            holder.tv_line_id = convertView.findViewById(R.id.tv_line_id);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.tv_line_name.setText(String.format("%s%s", mContext.getString(R.string.vs4), getItem(position).getSwitchName()));
//        holder.tv_alarm_type.setText(SwitchBean.getSwitchFaultState(mContext, getItem(position).getErrType()));
//        holder.tv_alarm_count.setText(String.format(Locale.getDefault(), "%d%s", getItem(position).getCount(), mContext.getString(R.string.vs17)));
//        holder.tv_line_id.setText(String.format("%s%s", mContext.getString(R.string.vs6), getItem(position).getSwitchCode()));
//        return convertView;
//    }

//    class ViewHolder {
//        TextView tv_line_name;
//        TextView tv_alarm_type;
//        TextView tv_alarm_count;
//        TextView tv_line_id;
//    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<ReportDetailBean.Alarm> list) {
        mListData.clear();
        mListData.addAll(list);
        notifyDataSetChanged();
    }
}
