package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.custom.BindingViewHolder;
import com.rejuvee.smartelectric.family.databinding.EmptyLayoutBinding;
import com.rejuvee.smartelectric.family.databinding.ItemLineDeleteBinding;
import com.rejuvee.smartelectric.family.databinding.ItemLineDetail1Binding;
import com.rejuvee.smartelectric.family.databinding.ItemLineDetail2Binding;
import com.rejuvee.smartelectric.family.databinding.ItemLineNormalBinding;

import java.util.ArrayList;
import java.util.List;

import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_DELETE;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_EMPTY;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_LINEDETAIL1;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_LINEDETAIL2;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_NORMAL;

/**
 * 我的分享 分时计价
 * Created by baba on 2017/8/30.
 */
public class SettingBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    //    private Context context;
    private final LayoutInflater mLayoutInflater;
    private List<ListSetingItem> mListData;

    private onSettingClickListener mListener;

    /**
     * 回调声明
     */
    public interface onSettingClickListener {
        void onRemove(ListSetingItem item, int position);

        void onSwitch(ListSetingItem item, int isEnable);

        void onBeanClick(int position);
    }

    /**
     * 设置回调
     */
    public void setSetListener(onSettingClickListener listeter) {
        mListener = listeter;
    }

    public SettingBeanAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListData.size() != 0) {
            return mListData.get(position).getViewType();
        }
        //在这里进行判断，如果我们的集合的长度为0时，我们就使用emptyView的布局
        return ITEM_VIEW_TYPE_EMPTY;

    }

    private ItemLineDetail1Binding binding1;
    private ItemLineDetail2Binding binding2;
    private ItemLineNormalBinding binding4;
    private ItemLineDeleteBinding binding5;

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_LINEDETAIL1:  //分时计价
                binding1 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_detail1, parent, false);
                return new BindingViewHolder<>(binding1);
            case ITEM_VIEW_TYPE_LINEDETAIL2:
                binding2 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_detail2, parent, false);
                return new BindingViewHolder<>(binding2);
            case ITEM_VIEW_TYPE_EMPTY:
                EmptyLayoutBinding binding3 = DataBindingUtil.inflate(mLayoutInflater, R.layout.empty_layout, parent, false);
                return new BindingViewHolder<>(binding3);
            case ITEM_VIEW_TYPE_NORMAL:
                binding4 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_normal, parent, false);
                return new BindingViewHolder<>(binding4);
            case ITEM_VIEW_TYPE_DELETE:// 分享用户
                binding5 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_line_delete, parent, false);
                return new BindingViewHolder<>(binding5);
        }
        return new BindingViewHolder<>(DataBindingUtil.inflate(mLayoutInflater, R.layout.item_empty, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final ListSetingItem bean = mListData.get(position);
            Context context = holder.getBinding().getRoot().getContext();
            holder.getBinding().setVariable(BR.vmItem, bean);
            switch (bean.getViewType()) {
                case ITEM_VIEW_TYPE_LINEDETAIL1:  //分时计价
                    binding1.txtContent.setTextColor(context.getResources().getColor(R.color.contents_text));
//                    binding1.txtContent.setText(Html.fromHtml(bean.getContent().getValue()));
                    binding1.txtState.setTextColor(context.getResources().getColor(R.color.text_content));
//                    binding1.txtState.setText(bean.getDesc());
                    holder.itemView.setOnClickListener(v -> {
                        if (mListener != null) {
                            mListener.onBeanClick(position);
                        }
                    });
                    break;
                case ITEM_VIEW_TYPE_LINEDETAIL2:
                    binding2.txtContent.setTextColor(context.getResources().getColor(R.color.contents_text));
                    binding2.txtContent.setText(Html.fromHtml(bean.getContent().getValue()));
                    binding2.txtState.setTextColor(context.getResources().getColor(R.color.text_content));
                    binding2.txtState.setText(bean.getDesc());
                    break;
                case ITEM_VIEW_TYPE_EMPTY:
//                binding3.hashCode();
                    break;
                case ITEM_VIEW_TYPE_NORMAL:
                    binding4.txtContent.setText(bean.getContent().getValue());
                    break;
                case ITEM_VIEW_TYPE_DELETE:// 分享用户
                    binding5.txtContent.setText(bean.getContent().getValue());
//                binding5.ivDelete.setVisibility(bean.showDelIcon);
                    binding5.ivDelete.setOnClickListener(v -> {
                        if (mListener != null) {
                            mListener.onRemove(bean, position);
                        }
                    });
//                binding5.ivSwitch.setImageResource(NativeLine.DrawableToggle[listDatas.get(position).getIsEnable()]);
                    binding5.ivSwitch.setOnClickListener(v -> {
                        if (mListener != null) {
                            mListener.onSwitch(bean, (mListData.get(position).getIsEnable() == 1 ? 0 : 1));
                        }
                    });
                    break;
            }
            holder.getBinding().setVariable(BR.vmItem, bean);
            holder.getBinding().executePendingBindings();
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
    public void addAll(List<ListSetingItem> list) {
        mListData.clear();
        mListData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一项
     *
     * @param bean
     */
    public void add(ListSetingItem bean) {
        mListData.add(bean);
        notifyItemInserted(mListData.size());
        notifyDataSetChanged();
    }

    /**
     * 移除一项
     *
     * @param position
     */
    public void remove(int position) {
        if (mListData.size() == 0) {
            return;
        }
        mListData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    /**
     * 删除图标显示控制
     */
    public void toggleDelIcon() {
        for (ListSetingItem taskBean : mListData) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        notifyDataSetChanged();
    }

}
