package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;
import com.rejuvee.smartelectric.family.common.custom.BindingViewHolder;
import com.rejuvee.smartelectric.family.databinding.EmptyLayoutBinding;
import com.rejuvee.smartelectric.family.databinding.ItemSceneSwitchBinding;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;
import com.rejuvee.smartelectric.family.model.viewmodel.SwitchInfoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */
public class ListViewLineSceneAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final LayoutInflater mLayoutInflater;
    private List<SwitchInfoBean> mListData;
    private Context context;
    private FragmentActivity fragmentActivity;
//    private int count;
//    private String sceneid;


    public ListViewLineSceneAdapter(Context context) {
        this.context = context;
        fragmentActivity = (FragmentActivity) context;
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
//        return result.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return result.get(position);
//    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //在这里根据不同的viewType进行引入不同的布局
//        ViewDataBinding binding;
        if (viewType == VIEW_TYPE_EMPTY) {
            EmptyLayoutBinding emp = DataBindingUtil.inflate(mLayoutInflater, R.layout.empty_layout, parent, false);
            return new BindingViewHolder<>(emp);
        } else {//if (viewType == VIEW_TYPE_ITEM)
            ItemSceneSwitchBinding b = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_scene_switch, parent, false);
            return new BindingViewHolder<>(b);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        if (mListData.size() != 0) {
            final SwitchInfoBean bean = mListData.get(position);
            SwitchInfoViewModel vm = ViewModelProviders.of(fragmentActivity).get(SwitchInfoViewModel.class);
            vm.setImgRes(NativeLine.LinePictures[bean.getIconType() % NativeLine.LinePictures.length]);
            vm.setCollectorName(String.format("%s%s", context.getString(R.string.vs5), bean.getCollector().getDeviceName()));
            vm.setSwitchName(String.format("%s%s", context.getString(R.string.vs4), bean.getName()));
            if (bean.getState() == 1) {
                vm.setCmdState(R.drawable.yk_hezha);
            } else if (bean.getState() == 0) {
                vm.setCmdState(R.drawable.yk_kaizha);
            } else {// if (State == -1)
                vm.setCmdState(R.drawable.yk_kaizha);
            }
            bean.setState(0);// 添加的时候 默认开闸
            holder.itemView.findViewById(R.id.img_kaiguang).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ItemSceneSwitchBinding f = (ItemSceneSwitchBinding) holder.getBinding();
            f.setPresenter(new Presenter());
            f.setVm(vm);
//            f.executePendingBindings();
//            holder.getBinding().setVariable(BR.vm, vm);
//            holder.getBinding().setVariable(BR.presenter, new Presenter());
//            holder.getBinding().executePendingBindings();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
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
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        final Holder holder;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_scene_switch, null);
//            holder = new Holder();
//            holder.img_device = convertView.findViewById(R.id.img_device);
//            holder.txt_device = convertView.findViewById(R.id.txt_device);
//            holder.txt_room = convertView.findViewById(R.id.txt_room);
//            holder.img_kaiguang = convertView.findViewById(R.id.img_kaiguang);
//            convertView.setTag(holder);
//        } else {
//            holder = (Holder) convertView.getTag();
//        }
//        holder.img_device.setImageResource(NativeLine.LinePictures[result.get(position).getIconType() % NativeLine.LinePictures.length]);
//        holder.txt_device.setText(String.format("%s%s", context.getString(R.string.vs5), result.get(position).getCollector().getDeviceName()));
//        holder.txt_room.setText(String.format("%s%s", context.getString(R.string.vs4), result.get(position).getName()));
//        if (result.get(position).getState() == 1) {
//            holder.img_kaiguang.setImageResource(R.drawable.yk_hezha);
//        } else if (result.get(position).getState() == 0) {
//            holder.img_kaiguang.setImageResource(R.drawable.yk_kaizha);
//        } else {// if (State == -1)
//            holder.img_kaiguang.setImageResource(R.drawable.yk_kaizha);
//        }
//        result.get(position).setState(0);// 添加的时候 默认开闸
//
//        holder.img_kaiguang.setOnClickListener(v -> {
//            int state = result.get(position).getState();
//            result.get(position).setState(state == 0 ? 1 : 0);
//            System.out.println("cmdData:" + result.get(position).getState());
//            ListViewLineSceneAdapter.this.notifyDataSetChanged();
//        });
//        return convertView;
//    }
    public class Presenter {
        public void onChange(String viewModel) {
            System.out.println("onChange " + viewModel);
        }

        public void onDel(String viewModel) {
            System.out.println("onDel " + viewModel);
        }
    }

    /**
     * 刷新整个列表
     *
     * @param list
     */
    public void addAll(List<SwitchInfoBean> list) {
        mListData.clear();
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

    public List<SwitchInfoBean> getResult() {
        return mListData;
    }
}
