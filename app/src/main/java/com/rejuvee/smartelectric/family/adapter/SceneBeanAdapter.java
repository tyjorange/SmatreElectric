package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BindingViewHolder;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;

import java.util.ArrayList;
import java.util.List;

public class SceneBeanAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int ITEM_VIEW_TYPE_NORMAL = 1;
    private final LayoutInflater mLayoutInflater;
    private List<SceneBean> sceneBeanList;
    private CallBack mListener;

    public interface CallBack {

        void onCollectorBeanClick(SceneBean bean);

        void onExecuteClick(SceneBean bean);

        void onDelClick(SceneBean bean);
    }

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setCallback(CallBack listener) {
        mListener = listener;
    }

    public SceneBeanAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sceneBeanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        if (viewType == ITEM_VIEW_TYPE_NORMAL) {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_scene, parent, false);
        } else {
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_scene, parent, false);
        }
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        final SceneBean bean = sceneBeanList.get(position);
        holder.getBinding().setVariable(BR.mCollectorBean, bean);
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
        holder.itemView.findViewById(R.id.img_item_remove).setVisibility(bean.showDelIcon);
        holder.itemView.findViewById(R.id.img_item_remove).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onDelClick(bean);
            }
        });
        holder.getBinding().executePendingBindings();
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
    }

    /**
     * 删除图标显示控制
     */
    public void toggleDelIcon() {
        for (SceneBean taskBean : sceneBeanList) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        notifyDataSetChanged();
    }
}
