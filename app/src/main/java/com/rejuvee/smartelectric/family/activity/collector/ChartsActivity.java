package com.rejuvee.smartelectric.family.activity.collector;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;

public class ChartsActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_charts;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WebView webView = findViewById(R.id.wv_cha);
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.setWebViewClient(new WebViewClient());// 防止系统浏览器进行网页访问
        webView.loadUrl("http://192.168.1.162:8088/#/cha");
//        webView.loadUrl("https://www.baidu.com/");
//        String body = "示例：这里有个img标签，地址是相对路径<img src='/uploads/allimg/130923/1FP02V7-0.png' />";
//        webView.loadDataWithBaseURL("http://192.168.1.162:8088/#/login", body, "text/html", "utf-8", null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dealloc() {

    }
}
