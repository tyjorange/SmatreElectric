package com.rejuvee.smartelectric.family.activity.kefu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.ChartItemBean;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;
import com.rejuvee.smartelectric.family.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 客服聊天
 */
public class CustomerServiceChartActivity extends BaseActivity {
    private Context mContext;
    private List<ChartItemBean> mList = new ArrayList<>();
    private ChartItemBeanAdapter adapter;
    private String username;
    private ChartListItemBean chartListItemBean;

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
        chartListItemBean = getIntent().getParcelableExtra("ChartListItemBean");
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_topic = findViewById(R.id.tv_topic);
        TextView tv_content = findViewById(R.id.tv_content);
        tv_topic.setText("聊天主题：" + chartListItemBean.getTopic());
        tv_content.setText("问题：" + chartListItemBean.getContent());
        ListView listView = (ListView) findViewById(R.id.list_chart);
        adapter = new ChartItemBeanAdapter(this, mList);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty_layout));

        findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitQA();
            }
        });
    }

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        Core.instance(mContext).getUserChatContent(0, 99, chartListItemBean.getId(), new ActionCallbackListener<List<ChartItemBean>>() {
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

    private void commitQA() {
        ClearEditText ce_context = findViewById(R.id.ce_context);
        String context = ce_context.getEditableText().toString();
        if (context.isEmpty()) {
            CustomToast.showCustomErrorToast(CustomerServiceChartActivity.this, "不能发送空消息");
            return;
        }
        Core.instance(mContext).addToUserChatContent(chartListItemBean.getId(), context, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
//                CustomToast.showCustomToast(CustomerServiceChartActivity.this, "提交问题成功");
//                setResult(RESULT_OK, getIntent());
//                finish();
                ce_context.setText("");
                getData();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(CustomerServiceChartActivity.this, "发送失败");
            }
        });
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
//            if (convertView == null) {
            if (username.equals(getItem(position).getUsername())) {
                convertView = View.inflate(mContext, R.layout.item_chat_right, null);
            } else {
                convertView = View.inflate(mContext, R.layout.item_chat_left, null);
            }
            holder = new ViewHolder();
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tv_chat_time = (TextView) convertView.findViewById(R.id.tv_chat_time);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.ivAvatar);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
            holder.tvContent.setText(String.format("%s", getItem(position).getContent()));
            holder.tv_chat_time.setText(String.format("%s", getItem(position).getTime()));
            return convertView;
        }

        class ViewHolder {
            TextView tvContent;
            TextView tv_chat_time;
            ImageView ivAvatar;
        }
    }
}
