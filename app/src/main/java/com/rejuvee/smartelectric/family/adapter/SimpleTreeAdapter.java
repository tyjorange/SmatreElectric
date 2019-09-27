package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.Node;
import com.rejuvee.smartelectric.family.model.bean.TreeNodeIndicator;

import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) {
        super(mTree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_lv_statement, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tni_indicator = (TreeNodeIndicator) convertView.findViewById(R.id.tni_indicator);

            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.tvQuantity = convertView.findViewById(R.id.tv_quantity);
            viewHolder.tvCharge = convertView.findViewById(R.id.tv_charge);
            viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //set indicator state
        viewHolder.tni_indicator.reset();
        viewHolder.tni_indicator.setLevel(node);
        viewHolder.tni_indicator.isLast(node.isLast());
        viewHolder.tni_indicator.setState(node);
        viewHolder.tni_indicator.isStart(node.isStart());
        // set data
        switch (node.getLevel()) {
            case 0:
                viewHolder.tvName.setText("线路：" + node.getMoneyBean().getName());
                break;
            case 1:
                viewHolder.tvName.setText("分线：" + node.getMoneyBean().getName());
                break;
            case 2:
                viewHolder.tvName.setText("支线：" + node.getMoneyBean().getName());
                break;
            default:
                viewHolder.tvName.setText(node.getMoneyBean().getName());
        }
        viewHolder.ivPicture.setImageResource(NativeLine.LinePictures[node.getMoneyBean().getIconType() % NativeLine.LinePictures.length]);
        viewHolder.tvQuantity.setText(String.valueOf(node.getMoneyBean().getShowValue()));
        viewHolder.tvCharge.setText(String.valueOf(node.getMoneyBean().getShowPrice()));

        return convertView;
    }

    private final class ViewHolder {
        TreeNodeIndicator tni_indicator;

        TextView tvName;
        TextView tvQuantity;
        TextView tvCharge;
        ImageView ivPicture;
    }
}
