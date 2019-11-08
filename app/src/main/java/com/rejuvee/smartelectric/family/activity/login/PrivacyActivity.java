package com.rejuvee.smartelectric.family.activity.login;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;

public class PrivacyActivity extends BaseActivity {
    private WebView webView;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_privacy;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_img_cancel).setOnClickListener(v -> {
            finish();
        });
        webView = findViewById(R.id.wv_privacy);
        WebSettings webSettings = webView.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setMinimumFontSize(72);
        webSettings.setDefaultFontSize(72);
        webView.loadUrl("https://rejuvee.net/privacy.html");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dealloc() {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
        }
    }
}
