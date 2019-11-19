package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.custom.BindingViewHolder;
import com.rejuvee.smartelectric.family.databinding.ItemEmptyBinding;
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
public class SettingAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private Context context;
    private final LayoutInflater mLayoutInflater;
    private List<ListSetingItem> listDatas;

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

    public SettingAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.listDatas = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return listDatas.get(position).getViewType();
    }

    private ItemLineDetail1Binding binding1;
    private ItemLineDetail2Binding binding2;
    private ItemEmptyBinding binding3;
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
                binding3 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_empty, parent, false);
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
        final ListSetingItem bean = listDatas.get(position);
        switch (bean.getViewType()) {
            case ITEM_VIEW_TYPE_LINEDETAIL1:  //分时计价
                binding1.txtContent.setTextColor(context.getResources().getColor(R.color.contents_text));
//                binding1.txtContent.setText(Html.fromHtml(bean.getContent()));
                binding1.txtState.setTextColor(context.getResources().getColor(R.color.text_content));
                binding1.txtState.setText(bean.getDesc());
                holder.itemView.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onBeanClick(position);
                    }
                });
                break;
            case ITEM_VIEW_TYPE_LINEDETAIL2:
                binding2.txtContent.setTextColor(context.getResources().getColor(R.color.contents_text));
//                binding2.txtContent.setText(Html.fromHtml(bean.getContent()));
                binding2.txtState.setTextColor(context.getResources().getColor(R.color.text_content));
                binding2.txtState.setText(bean.getDesc());
                break;
            case ITEM_VIEW_TYPE_EMPTY:
                break;
            case ITEM_VIEW_TYPE_NORMAL:
//                binding4.txtContent.setText(bean.getContent());
                break;
            case ITEM_VIEW_TYPE_DELETE:// 分享用户
//                binding5.txtContent.setText(bean.getContent());
//                binding5.ivDelete.setVisibility(bean.showDelIcon);
                binding5.ivDelete.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onRemove(bean, position);
                    }
                });
//                binding5.ivSwitch.setImageResource(NativeLine.DrawableToggle[listDatas.get(position).getIsEnable()]);
                binding5.ivSwitch.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onSwitch(bean, (listDatas.get(position).getIsEnable() == 1 ? 0 : 1));
                    }
                });
                break;
        }
        holder.getBinding().setVariable(BR.vmItem, bean);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<ListSetingItem> list) {
        listDatas.clear();
        listDatas.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一项
     *
     * @param bean
     */
    public void add(ListSetingItem bean) {
        listDatas.add(bean);
        notifyItemInserted(listDatas.size());
        notifyDataSetChanged();
    }

    /**
     * 移除一项
     *
     * @param position
     */
    public void remove(int position) {
        if (listDatas.size() == 0) {
            return;
        }
        listDatas.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    /**
     * 删除图标显示控制
     */
    public void toggleDelIcon() {
        for (ListSetingItem taskBean : listDatas) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        notifyDataSetChanged();
    }

}
