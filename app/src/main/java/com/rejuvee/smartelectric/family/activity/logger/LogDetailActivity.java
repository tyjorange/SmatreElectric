package com.rejuvee.smartelectric.family.activity.logger;

import android.view.View;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.LineOperateRecordAdapter;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityLogDetailBinding;
import com.rejuvee.smartelectric.family.model.bean.RecordBean;

import java.util.List;

/**
 * 日志详情
 */
public class LogDetailActivity extends BaseActivity {
    //    private List<RecordBean> mListData;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_log_detail;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityLogDetailBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_log_detail);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
        ListView listView = mBinding.listLogs;//findViewById(R.id.list_logs);
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());

        List<RecordBean> listData = getIntent().getParcelableArrayListExtra("records");
        LineOperateRecordAdapter mAdapter = new LineOperateRecordAdapter(this, listData);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    //    @Override
//    protected String getToolbarTitle() {
//        return getIntent().getStringExtra("title");
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

    }

    @Override
    protected void dealloc() {

    }
}
