package com.rejuvee.smartelectric.family.activity.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;

public class ChartsActivity extends BaseActivity {
    private static final String TAG = "ChartsActivity";
    private WebView webView;
    private boolean isLoading;

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
        findViewById(R.id.img_cancel).setOnClickListener(view -> finish());
        webView = findViewById(R.id.wv_cha);
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //localStorage  允许存储
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);//存储的最大容量
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        // 防止系统浏览器进行网页访问
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                isLoading = true;
                Log.d(TAG, "onPageStarted " + view);
                Log.d(TAG, "onPageStarted " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isLoading) {
                    isLoading = false;
                    // onPageFinished()加载两次解决办法
                    return;
                }
                Log.d(TAG, "onPageFinished " + view);
                Log.d(TAG, "onPageFinished " + url);
                Object[] x = new Object[]{
                        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
                };
                Object[] y = new Object[]{
                        820, 932, 901, 934, 1290, 1330, 1320
                };
                refreshEchartsWithOption(EchartOptionUtil.getLineChartOptions(x, y));
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, error.getDescription().toString());
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e(TAG, errorResponse.getStatusCode() + "");
            }
        });
        webView.loadUrl("http://192.168.1.162:8088/#/cha");
    }

    @Override
    protected void initData() {

    }

    /**
     * 刷新图表
     * java调用js的loadEcharts方法刷新echart
     * 不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
     *
     * @param option
     */
    public void refreshEchartsWithOption(GsonOption option) {
        if (option == null) {
            return;
        }
        String optionString = option.toString();
        String call = "javascript:loadEcharts('" + optionString + "')";
        webView.loadUrl(call);
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

    static class EchartOptionUtil {

        static GsonOption getLineChartOptions(Object[] xAxis, Object[] yAxis) {
            GsonOption option = new GsonOption();
            option.title("折线图");
            option.legend("销量");
            option.tooltip().trigger(Trigger.axis);

            ValueAxis valueAxis = new ValueAxis();
            option.yAxis(valueAxis);

            CategoryAxis categorxAxis = new CategoryAxis();
            categorxAxis.axisLine().onZero(false);
            categorxAxis.boundaryGap(true);
            categorxAxis.data(xAxis);
            option.xAxis(categorxAxis);

            Line line = new Line();
            line.smooth(false).name("销量").data(yAxis).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
            option.series(line);
            return option;
        }
    }
}
