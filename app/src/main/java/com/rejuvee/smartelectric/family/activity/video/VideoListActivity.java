package com.rejuvee.smartelectric.family.activity.video;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.VideoListBeanAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityVideoListBinding;
import com.rejuvee.smartelectric.family.model.bean.VideoInfo;

import java.util.List;

import retrofit2.Call;

/**
 * 常见问题列表
 */
public class VideoListActivity extends BaseActivity {

    private VideoListBeanAdapter adapter;

    @Override
    protected void initView() {
        ActivityVideoListBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_list);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        adapter = new VideoListBeanAdapter(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
        adapter.setCallback(bean -> {
            Intent intent = new Intent(VideoListActivity.this, VideoActivity.class);
            intent.putExtra("videoUrl", bean.getVideoURL());
            startActivity(intent);
        });

        getData();
    }

    private Call<?> currentCall;

    @Override
    protected void dealloc() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    private void getData() {
        currentCall = Core.instance(this).getAllVideo(new ActionCallbackListener<List<VideoInfo>>() {
            @Override
            public void onSuccess(List<VideoInfo> data) {
                adapter.addAll(data);
            }

            @Override
            public void onFailure(int errorEvent, String message) {

            }
        });
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }
    }
}
