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
import com.rejuvee.smartelectric.family.databinding.ItemEasySceneBinding;
import com.rejuvee.smartelectric.family.databinding.ItemSceneBinding;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;

import java.util.ArrayList;
import java.util.List;

public class SceneBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    public static final int ITEM_VIEW_TYPE_HORIZONTAL = 1;// 横向
    public static final int ITEM_VIEW_TYPE_VERTICAL = 2;// 竖向
    private final LayoutInflater mLayoutInflater;
    private List<SceneBean> sceneBeanList;
    private CallBack mListener;
    private int currentItemType;

    public interface CallBack {

        void onCollectorBeanClick(SceneBean bean);//点击

        void onExecuteClick(SceneBean bean);//执行场景

        void onRemove(SceneBean bean, int position);//移除场景
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setCallback(CallBack listener) {
        mListener = listener;
    }

    public SceneBeanAdapter(Context context, int type) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sceneBeanList = new ArrayList<>();
        currentItemType = type;
    }

    private ItemEasySceneBinding binding1;
    private ItemSceneBinding binding2;

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ViewDataBinding binding;
        if (currentItemType == ITEM_VIEW_TYPE_HORIZONTAL) {
            binding1 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_easy_scene, parent, false);
            return new BindingViewHolder<>(binding1);
        } else {// if(currentItemType == ITEM_VIEW_TYPE_VERTICAL)
            binding2 = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_scene, parent, false);
            return new BindingViewHolder<>(binding2);
        }
//        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        final SceneBean bean = sceneBeanList.get(position);
        holder.getBinding().setVariable(BR.mSceneBean, bean);
        holder.getBinding().executePendingBindings();
        if (currentItemType == ITEM_VIEW_TYPE_HORIZONTAL) {
            holder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onExecuteClick(bean);
                }
            });
        } else if (currentItemType == ITEM_VIEW_TYPE_VERTICAL) {
            holder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onCollectorBeanClick(bean);
                }
            });
            holder.itemView.findViewById(R.id.img_zhixing).setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onExecuteClick(bean);
                }
            });
//            binding2.imgItemRemove.setVisibility(bean.showDelIcon);
            binding2.imgItemRemove.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onRemove(bean, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return sceneBeanList.size();
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<SceneBean> list) {
        sceneBeanList.clear();
        sceneBeanList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一项
     *
     * @param bean
     */
    public void add(SceneBean bean) {
        sceneBeanList.add(bean);
        notifyItemInserted(sceneBeanList.size());
        notifyDataSetChanged();
    }

    /**
     * 移除一项
     *
     * @param position
     */
    public void remove(int position) {
        if (sceneBeanList.size() == 0) {
            return;
        }
        sceneBeanList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    /**
     * 删除图标显示控制
     */
    public void toggleDelIcon() {
        for (SceneBean bean : sceneBeanList) {
            if (bean.showDelIcon == View.GONE) {
                bean.showDelIcon = View.VISIBLE;
            } else {
                bean.showDelIcon = View.GONE;
            }
        }
        notifyDataSetChanged();
    }
}
