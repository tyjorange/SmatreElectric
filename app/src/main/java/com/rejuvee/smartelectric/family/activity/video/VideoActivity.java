package com.rejuvee.smartelectric.family.activity.video;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityVideoBinding;

public class VideoActivity extends BaseActivity {


    @Override
    protected void initView() {
        ActivityVideoBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        String s = getIntent().getStringExtra("videoUrl");
        String url = "http://" + s;
        // 设置播放的资源路径，使用 setVideoURI(Uri uri)  或者 setVideoPath(String path)(底层也是调用的  serVideoURL(Uri uri))
        mBinding.video.setVideoPath(url);

        //为控件设置焦点
        mBinding.video.requestFocus();

        // 为 VideoView 视图设置媒体控制器，设置了之后就会自动由进度条、前进、后退等操作
        mBinding.video.setMediaController(new MediaController(this));

        // 视频准备完成时回调
        mBinding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("tag", "--------------视频准备完毕,可以进行播放.......");
            }
        });
        // 视频播放发送错误时回调
        mBinding.video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("tag", "---------------------视频播放失败...........");
                return false;
            }
        });

        // 开始播放视频
        mBinding.video.start();

    }

    @Override
    protected void dealloc() {

    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }
    }
}
