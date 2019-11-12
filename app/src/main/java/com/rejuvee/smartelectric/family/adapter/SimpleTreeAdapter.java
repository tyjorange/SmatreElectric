package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;
import com.rejuvee.smartelectric.family.databinding.ItemLvStatementBinding;
import com.rejuvee.smartelectric.family.model.bean.Node;

import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {
    private final LayoutInflater mLayoutInflater;

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) {
        super(mTree, context, datas, defaultExpandLevel);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ItemLvStatementBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_lv_statement, parent, false);
//        ViewHolder viewHolder = null;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.item_lv_statement, parent, false);
//            viewHolder = new ViewHolder();
//            viewHolder.tni_indicator = convertView.findViewById(R.id.tni_indicator);
//
//            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
//            viewHolder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
//            viewHolder.tvCharge = convertView.findViewById(R.id.tv_charge);
//            viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        //set indicator state
        binding.tniIndicator.reset();
        binding.tniIndicator.setLevel(node);
        binding.tniIndicator.isLast(node.isLast());
        binding.tniIndicator.setState(node);
        binding.tniIndicator.isStart(node.isStart());
        // set data
        switch (node.getLevel()) {
            case 0:
                binding.tvName.setText(String.format("%s%s", mContext.getString(R.string.vs14), node.getMoneyBean().getName()));
                break;
            case 1:
                binding.tvName.setText(String.format("%s%s", mContext.getString(R.string.vs15), node.getMoneyBean().getName()));
                break;
            case 2:
                binding.tvName.setText(String.format("%s%s", mContext.getString(R.string.vs16), node.getMoneyBean().getName()));
                break;
            default:
                binding.tvName.setText(node.getMoneyBean().getName());
        }
        binding.ivPicture.setImageResource(NativeLine.LinePictures[node.getMoneyBean().getIconType() % NativeLine.LinePictures.length]);
        binding.tvQuantity.setText(String.valueOf(node.getMoneyBean().getShowValue()));
        binding.tvCharge.setText(String.valueOf(node.getMoneyBean().getShowPrice()));

//        return convertView;
        return binding.getRoot();
    }

//    private final class ViewHolder {
//        TreeNodeIndicator tni_indicator;
//
//        TextView tvName;
//        TextView tvQuantity;
//        TextView tvCharge;
//        ImageView ivPicture;
//    }
}
