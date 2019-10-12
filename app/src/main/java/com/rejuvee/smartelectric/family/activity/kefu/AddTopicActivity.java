package com.rejuvee.smartelectric.family.activity.kefu;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;

public class AddTopicActivity extends BaseActivity {
    private Context mContext;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_topic;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.stv_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitQA();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dealloc() {

    }

    private void commitQA() {
        EditText et_new_topic = findViewById(R.id.et_new_topic);
        EditText et_new_context = findViewById(R.id.et_new_context);
        String topic = et_new_topic.getEditableText().toString();
        String context = et_new_context.getEditableText().toString();
        if (topic.isEmpty()) {
            CustomToast.showCustomErrorToast(AddTopicActivity.this, "请输入主题内容");
            return;
        }
        if (context.isEmpty()) {
            CustomToast.showCustomErrorToast(AddTopicActivity.this, "请输入问题描述");
            return;
        }
        Core.instance(mContext).addToUserChatList(topic, context, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(AddTopicActivity.this, "提交问题成功");
                setResult(RESULT_OK, getIntent());
                finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(AddTopicActivity.this, getString(R.string.operator_failure));
            }
        });
    }
}
