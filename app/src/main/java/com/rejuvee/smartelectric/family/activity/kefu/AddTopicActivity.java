package com.rejuvee.smartelectric.family.activity.kefu;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityAddTopicBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.AddTopicViewModel;

import java.util.Objects;

/**
 * 添加问题
 */
public class AddTopicActivity extends BaseActivity {
//    private Context mContext;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_add_topic;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private AddTopicViewModel mViewModel;

    @Override
    protected void initView() {
        ActivityAddTopicBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_topic);
        mViewModel = ViewModelProviders.of(this).get(AddTopicViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        mContext = this;
//        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
//        findViewById(R.id.stv_commit).setOnClickListener(v -> commitQA());
    }

    @Override
    protected void initData() {

    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onCommit(View view) {
            commitQA();
        }
    }

    @Override
    protected void dealloc() {

    }

    private void commitQA() {
//        EditText et_new_topic = findViewById(R.id.et_new_topic);
//        EditText et_new_context = findViewById(R.id.et_new_context);
//        String topic = et_new_topic.getEditableText().toString();
//        String context = et_new_context.getEditableText().toString();
        String topic = mViewModel.getTopic().getValue();
        String context = mViewModel.getContext().getValue();
        if (Objects.requireNonNull(topic).isEmpty()) {
            CustomToast.showCustomErrorToast(AddTopicActivity.this, getString(R.string.vs192));
            return;
        }
        if (Objects.requireNonNull(context).isEmpty()) {
            CustomToast.showCustomErrorToast(AddTopicActivity.this, getString(R.string.vs193));
            return;
        }
        Core.instance(this).addToUserChatList(topic, context, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddTopicActivity.this, getString(R.string.vs194));
                setResult(RESULT_OK, getIntent());
                finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(AddTopicActivity.this, message);
            }
        });
    }
}
