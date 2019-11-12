package com.rejuvee.smartelectric.family.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;

import java.util.List;

import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_DELETE;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_EMPTY;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_LINEDETAIL1;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_LINEDETAIL2;
import static com.rejuvee.smartelectric.family.adapter.ListSetingItem.ITEM_VIEW_TYPE_NORMAL;

/**
 * Created by liuchengran on 2017/8/30.
 */
@Deprecated
public class SetingAdapter extends BaseAdapter {
    private List<ListSetingItem> listDatas;
    private Context context;
    private onSettingClickListener mListener;

    public SetingAdapter(Context context, List<ListSetingItem> listDatas) {
        this.context = context;
        this.listDatas = listDatas;
    }

    public void setSetListener(onSettingClickListener listeter) {
        mListener = listeter;
    }

    @Override
    public int getCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SetViewHolder holder = null;
        int viewType = getItemViewType(position);
        if (view == null) {
            switch (viewType) {
                case ITEM_VIEW_TYPE_LINEDETAIL1:
                    //分时计价
                    holder = new SetViewHolder();
                    view = View.inflate(context, R.layout.item_line_detail1, null);
                    holder.txtContent = view.findViewById(R.id.txt_content);
                    holder.txtDesc = view.findViewById(R.id.txt_state);
                    holder.ivDelete = view.findViewById(R.id.iv_delete);
                    view.setTag(holder);
                    break;
                case ITEM_VIEW_TYPE_LINEDETAIL2:
                    holder = new SetViewHolder();
                    view = View.inflate(context, R.layout.item_line_detail2, null);
                    holder.txtContent = view.findViewById(R.id.txt_content);
                    holder.txtDesc = view.findViewById(R.id.txt_state);
                    view.setTag(holder);
                    break;
                case ITEM_VIEW_TYPE_EMPTY:
                    view = View.inflate(context, R.layout.item_empty, null);
                    holder = new SetViewHolder();
                    view.setTag(holder);
                    break;
                case ITEM_VIEW_TYPE_NORMAL:
                    view = View.inflate(context, R.layout.item_line_normal, null);
                    holder = new SetViewHolder();
                    holder.txtContent = view.findViewById(R.id.txt_content);
                    view.setTag(holder);
                    break;
                case ITEM_VIEW_TYPE_DELETE:
                    // 分享用户
                    view = View.inflate(context, R.layout.item_line_delete, null);
                    holder = new SetViewHolder();
                    holder.txtContent = view.findViewById(R.id.txt_content);
                    holder.ivSwitch = view.findViewById(R.id.iv_switch);
                    holder.ivDelete = view.findViewById(R.id.iv_delete);
                    view.setTag(holder);
                    break;
            }
        } else {
            holder = (SetViewHolder) view.getTag();
        }
        ListSetingItem setingItem = listDatas.get(position);
        if (holder != null) {
            holder.ivDelete.setVisibility(setingItem.showDelIcon);
        }
        initData(holder, setingItem, position);
        return view;
    }

    private void initData(SetViewHolder holder, final ListSetingItem setingItem, final int position) {
        if (setingItem.getViewType() == ITEM_VIEW_TYPE_LINEDETAIL1 || setingItem.getViewType() == ITEM_VIEW_TYPE_LINEDETAIL2) {
            holder.txtContent.setTextColor(context.getResources().getColor(R.color.contents_text));
            holder.txtContent.setText(Html.fromHtml(setingItem.getContent().getValue()));
            holder.txtDesc.setTextColor(context.getResources().getColor(R.color.text_content));
            holder.txtDesc.setText(setingItem.getDesc());
        }
        if (setingItem.getViewType() == ITEM_VIEW_TYPE_NORMAL) {
            holder.txtContent.setText(setingItem.getContent().getValue());
        }
        if (setingItem.getViewType() == ITEM_VIEW_TYPE_DELETE) {
            holder.txtContent.setText(setingItem.getContent().getValue());
            holder.ivDelete.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onRemove(position);
                }
            });
            holder.ivSwitch.setImageResource(NativeLine.DrawableToggle[listDatas.get(position).getIsEnable()]);
            holder.ivSwitch.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onSwitch(position, (listDatas.get(position).getIsEnable() == 1 ? 0 : 1));
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listDatas.get(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return ListSetingItem.VIEW_TYPE_COUNT;
    }

    class SetViewHolder {
        TextView txtContent;
        TextView txtDesc;
        ImageView ivSwitch;
        ImageView ivDelete;
    }

    public interface onSettingClickListener {
        void onRemove(int position);

        void onSwitch(int position, int isEnable);
    }
}
