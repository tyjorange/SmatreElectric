package com.rejuvee.smartelectric.family.activity.kefu;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ChartListItemBeanAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivityKeFuBinding;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;

import java.util.List;

/**
 * 客服
 */
public class CustomerServiceActivity extends BaseActivity {
    //    private Context mContext;
//    private List<ChartListItemBean> mList = new ArrayList<>();
    private ChartListItemBeanAdapter adapter;
    private LoadingDlg loadingDlg;
    private String username;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_ke_fu;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityKeFuBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ke_fu);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        mContext = this;
        username = getIntent().getStringExtra("username");
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        loadingDlg = new LoadingDlg(this, -1);

//        ListView listView = mBinding.listTiwen;
        adapter = new ChartListItemBeanAdapter(this);
        mBinding.listTiwen.setAdapter(adapter);
        mBinding.listTiwen.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCallback(bean -> {
            Intent intent = new Intent(CustomerServiceActivity.this, CustomerServiceChartActivity.class);
            intent.putExtra("ChartListItemBean", bean);
            intent.putExtra("username", username);
            startActivity(intent);
        });
//        mBinding.listTiwen.setEmptyView(mBinding.emptyLayout);
//        mBinding.listTiwen.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(view.getContext(), CustomerServiceChartActivity.class);
//            intent.putExtra("ChartListItemBean", mList.get(position));
//            intent.putExtra("username", username);
//            startActivity(intent);
//        });
//        findViewById(R.id.iv_send_wenti).setOnClickListener(v -> {
//            Intent intent = new Intent(listView.getContext(), AddTopicActivity.class);
//            startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_QA);
//        });
        getData();
    }

    private void getData() {
        loadingDlg.show();
        Core.instance(this).findChatList(0, 200, new ActionCallbackListener<List<ChartListItemBean>>() {
            @Override
            public void onSuccess(List<ChartListItemBean> data) {
//                mList.clear();
//                mList.addAll(data);
                adapter.addAll(data);
                loadingDlg.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(CustomerServiceActivity.this, message);
                loadingDlg.dismiss();
            }
        });
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onSendQA(View view) {
            Intent intent = new Intent(view.getContext(), AddTopicActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_QA);
        }
    }

    @Override
    protected void dealloc() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonRequestCode.REQUEST_ADD_QA) {
            getData();
        }
    }

//    class ChartListItemBeanAdapter extends BaseAdapter {
//        private Context mContext;
//        private List<ChartListItemBean> mListData;
//
//        ChartListItemBeanAdapter(Context context, List<ChartListItemBean> listData) {
//            mContext = context;
//            mListData = listData;
//        }
//
//        @Override
//        public int getCount() {
//            return mListData.size();
//        }
//
//        @Override
//        public ChartListItemBean getItem(int position) {
//            return mListData.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = View.inflate(mContext, R.layout.item_line_chart, null);
//                holder = new ViewHolder();
//                holder.tv_topic = convertView.findViewById(R.id.tv_topic);
//                holder.tv_time = convertView.findViewById(R.id.tv_time);
//                holder.tv_updateTime = convertView.findViewById(R.id.tv_updateTime);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            holder.tv_topic.setText(String.format("%s%s", getResources().getString(R.string.vs198), getItem(position).getTopic()));
//            holder.tv_time.setText(String.format("%s%s", getString(R.string.vs200), getItem(position).getTime()));
//            holder.tv_updateTime.setText(String.format("%s%s", getString(R.string.vs201), getItem(position).getUpdateTime()));
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView tv_topic;
//            TextView tv_time;
//            TextView tv_updateTime;
//        }
//    }
}
