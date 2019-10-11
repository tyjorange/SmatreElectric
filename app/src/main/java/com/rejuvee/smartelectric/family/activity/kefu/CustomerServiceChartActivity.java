package com.rejuvee.smartelectric.family.activity.kefu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.ChartItemBean;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceChartActivity extends BaseActivity {
    private Context mContext;
    private List<ChartItemBean> mList = new ArrayList<>();
    private ChartItemBeanAdapter adapter;
    private String username;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_customer_service_chart;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        username = getIntent().getStringExtra("username");
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView listView = (ListView) findViewById(R.id.list_chart);
        adapter = new ChartItemBeanAdapter(this, mList);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty_layout));
    }

    @Override
    protected void initData() {
        ChartListItemBean c = getIntent().getParcelableExtra("ChartListItemBean");
        Core.instance(mContext).getUserChatContent(0, 99, c.getId(), new ActionCallbackListener<List<ChartItemBean>>() {
            @Override
            public void onSuccess(List<ChartItemBean> data) {
                mList.clear();
                mList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorEvent, String message) {

            }
        });
    }

    @Override
    protected void dealloc() {

    }

    class ChartItemBeanAdapter extends BaseAdapter {
        private Context mContext;
        private List<ChartItemBean> mListData;

        ChartItemBeanAdapter(Context context, List<ChartItemBean> listData) {
            mContext = context;
            mListData = listData;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public ChartItemBean getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                if (username.equals(getItem(position).getUsername())) {
                    convertView = View.inflate(mContext, R.layout.item_chat_right, null);
                } else {
                    convertView = View.inflate(mContext, R.layout.item_chat_left, null);
                }
                holder = new ViewHolder();
                holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvContent.setText(String.format("%s", getItem(position).getContent()));
            return convertView;
        }

        class ViewHolder {
            TextView tvContent;
            ImageView ivAvatar;
        }
    }
}
