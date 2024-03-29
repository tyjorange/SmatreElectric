package com.rejuvee.smartelectric.family.activity.kefu;

import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ChartItemBeanAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityCustomerServiceChartBinding;
import com.rejuvee.smartelectric.family.model.bean.ChartItemBean;
import com.rejuvee.smartelectric.family.model.bean.ChartListItemBean;
import com.rejuvee.smartelectric.family.model.viewmodel.CustomerServiceChartViewModel;

import java.util.List;

import retrofit2.Call;

/**
 * 客服聊天
 */
public class CustomerServiceChartActivity extends BaseActivity {
    private String TAG = "CustomerServiceChartActivity";
    //    private Context mContext;
//    private List<ChartItemBean> mList = new ArrayList<>();
    private ChartItemBeanAdapter adapter;
    //    private String username;
    private ChartListItemBean chartListItemBean;
    private ActivityCustomerServiceChartBinding mBinding;
    private CustomerServiceChartViewModel mViewModel;

    @Override
    protected void initView() {
        //    @Override
        //    protected int getLayoutResId() {
        //        return R.layout.activity_customer_service_chart;
        //    }
        //
        //    @Override
        //    protected int getMyTheme() {
        //        return 0;
        //    }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_service_chart);
        mViewModel = ViewModelProviders.of(this).get(CustomerServiceChartViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        mContext = this;
        String username = getIntent().getStringExtra("username");
        chartListItemBean = getIntent().getParcelableExtra("ChartListItemBean");
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        TextView tv_topic = findViewById(R.id.tv_topic);
//        TextView tv_content = findViewById(R.id.tv_content);
//        tv_topic.setText(String.format(getString(R.string.vs203), chartListItemBean.getTopic()));
        mViewModel.setTopic(String.format(getString(R.string.vs203), chartListItemBean.getTopic()));
//        tv_content.setText(String.format(getString(R.string.vs204), chartListItemBean.getContent()));
        mViewModel.setContext(String.format(getString(R.string.vs204), chartListItemBean.getContent()));
//        ListView listView = mBinding.listChart;// findViewById(R.id.list_chart);
//        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));//List view 点击水纹效果取消
        adapter = new ChartItemBeanAdapter(this, username);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mBinding.listChart.setLayoutManager(linearLayoutManager);
        mBinding.listChart.setAdapter(adapter);
//        listView.setEmptyView(mBinding.emptyLayout);
//        findViewById(R.id.tv_commit).setOnClickListener(v -> commitQA());
        getData();
    }

    private void getData() {
        currentCall = Core.instance(this).getUserChatContent(0, 200, chartListItemBean.getId(), new ActionCallbackListener<List<ChartItemBean>>() {
            @Override
            public void onSuccess(List<ChartItemBean> data) {
//                mList.clear();
//                mList.addAll(data);
                adapter.addAll(data);
                mBinding.listChart.scrollToPosition(adapter.getItemCount() - 1);// 滑动到底部
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
            }
        });
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onCommit(View view) {
            commitQA();
        }

    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    private void commitQA() {
//        ClearEditText ce_context = findViewById(R.id.ce_context);
        String context = mViewModel.getNewContext().getValue();//  ce_context.getEditableText().toString();
        if (context == null || context.isEmpty()) {
            CustomToast.showCustomErrorToast(CustomerServiceChartActivity.this, getString(R.string.vs207));
            return;
        }
        currentCall = Core.instance(this).addToUserChatContent(chartListItemBean.getId(), context, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
//                CustomToast.showCustomToast(CustomerServiceChartActivity.this, "提交问题成功");
//                setResult(RESULT_OK, getIntent());
//                finish();
                mViewModel.setNewContext("");
                getData();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(CustomerServiceChartActivity.this, getString(R.string.vs208));
            }
        });
    }

//    class ChartItemBeanAdapter extends BaseAdapter {
//        private Context mContext;
//        private List<ChartItemBean> mListData;
//
//        ChartItemBeanAdapter(Context context, List<ChartItemBean> listData) {
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
//        public ChartItemBean getItem(int position) {
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
////            if (convertView == null) {
//            if (username.equals(getItem(position).getUsername())) {
//                convertView = View.inflate(mContext, R.layout.item_chat_right, null);
//            } else {
//                convertView = View.inflate(mContext, R.layout.item_chat_left, null);
//            }
//            holder = new ViewHolder();
//            holder.tvContent = convertView.findViewById(R.id.tvContent);
//            holder.tv_chat_time = convertView.findViewById(R.id.tv_chat_time);
//            holder.ivAvatar = convertView.findViewById(R.id.ivAvatar);
////                convertView.setTag(holder);
////            } else {
////                holder = (ViewHolder) convertView.getTag();
////            }
//            holder.tvContent.setText(String.format("%s", getItem(position).getContent()));
//            holder.tv_chat_time.setText(String.format("%s", getItem(position).getTime()));
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView tvContent;
//            TextView tv_chat_time;
//            ImageView ivAvatar;
//        }
//    }
}
