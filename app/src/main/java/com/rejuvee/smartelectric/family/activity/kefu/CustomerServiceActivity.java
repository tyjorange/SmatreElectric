package com.rejuvee.smartelectric.family.activity.kefu;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;
import com.rejuvee.smartelectric.family.widget.dialog.LoadingDlg;

import java.util.ArrayList;
import java.util.List;

public class CustomerServiceActivity extends BaseActivity {
    private Context mContext;
    private List<ChartListItemBean> mList = new ArrayList<>();
    private ChartListItemBeanAdapter adapter;
    private LoadingDlg loadingDlg;
    private String username;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ke_fu;
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
        loadingDlg = new LoadingDlg(this, -1);

        ListView listView = (ListView) findViewById(R.id.list_tiwen);
        adapter = new ChartListItemBeanAdapter(this, mList);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty_layout));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, CustomerServiceChartActivity.class);
                intent.putExtra("ChartListItemBean", mList.get(position));
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        loadingDlg.show();
        Core.instance(mContext).findChatList(0, 99, new ActionCallbackListener<List<ChartListItemBean>>() {
            @Override
            public void onSuccess(List<ChartListItemBean> data) {
                mList.clear();
                mList.addAll(data);
                adapter.notifyDataSetChanged();
                loadingDlg.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(CustomerServiceActivity.this, getString(R.string.operator_failure));
                loadingDlg.dismiss();
            }
        });
    }

    @Override
    protected void dealloc() {

    }

    class ChartListItemBeanAdapter extends BaseAdapter {
        private Context mContext;
        private List<ChartListItemBean> mListData;

        ChartListItemBeanAdapter(Context context, List<ChartListItemBean> listData) {
            mContext = context;
            mListData = listData;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public ChartListItemBean getItem(int position) {
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
                convertView = View.inflate(mContext, R.layout.item_line_chart, null);
                holder = new ViewHolder();
                holder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_updateTime = (TextView) convertView.findViewById(R.id.tv_updateTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_topic.setText(String.format("%s%s", "主题：", getItem(position).getTopic()));
            holder.tv_time.setText(String.format("%s%s", "发起时间：", getItem(position).getTime()));
            holder.tv_updateTime.setText(String.format("%s%s", "最后更新时间：", getItem(position).getUpdateTime()));
            return convertView;
        }

        class ViewHolder {
            TextView tv_topic;
            TextView tv_time;
            TextView tv_updateTime;
        }
    }
}
