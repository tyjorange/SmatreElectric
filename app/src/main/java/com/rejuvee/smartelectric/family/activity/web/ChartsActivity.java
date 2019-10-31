package com.rejuvee.smartelectric.family.activity.web;

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

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.code.Easing;
import com.github.abel533.echarts.code.Layout;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Category;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import com.github.abel533.echarts.style.TextStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
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
//                Object[] x = new Object[]{
//                        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
//                };
//                Object[] y = new Object[]{
//                        820, 932, 901, 934, 1290, 1330, 1320
//                };
                refreshEchartsWithOption(EchartOptionUtil.getGraphOptions());
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
        System.out.println(optionString);//TODO
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

    /**
     * echarts生成类
     */
    static class EchartOptionUtil {

        static GsonOption getGraphOptions() {
            GsonOption option = new GsonOption();
            //title
            option.title("关系图test");
            //tooltip
            option.tooltip().show(false);
            //toolbox
            HashMap<String, Feature> stringFeatureHashMap = new HashMap<>();
            stringFeatureHashMap.put("restore", new Feature().show(true));
            stringFeatureHashMap.put("saveAsImage", new Feature().show(true));
            option.toolbox().show(true).setFeature(stringFeatureHashMap);
            //animationDurationUpdate
            option.animationDurationUpdate(1500);
            //animationEasingUpdate
            option.animationEasingUpdate(Easing.quinticInOut);
            //legend
            option.legend(getLegend());
            //series
            Graph graph = new Graph();
            {
                //layout
                graph.layout(Layout.force);
                //symbol
                graph.symbol("roundRect");
                //symbolSize
                graph.symbolSize(45);
                //roam
                graph.roam(true);
                //categories
                graph.categories(getCategory());
                //label
                TextStyle textStyle = new TextStyle();
                textStyle.fontSize(12);
                Normal normal = new Normal();
                normal.textStyle(textStyle);
                normal.show(true);
                ItemStyle style = new ItemStyle();
                style.normal(normal);
                graph.label(style);
                //force
                graph.force().repulsion(1000);
                //data
                graph.setData(getDatas());
                //links
                graph.links(getLinks());
            }
            option.series(graph);
            return option;
        }

        private static List<Link> getLinks() {
            List<Link> list = new ArrayList<>();
            //TODO++++++++++
            LineStyle lineStyle = new LineStyle();
            lineStyle.normal()
                    .opacity(0.9)
                    .borderWidth(1)
                    .curveness(0d);
            //TODO----------
            list.add(new Link()
                    .source(0)
                    .target(1)
                    .lineStyle(lineStyle));
            return list;
        }

        private static List<MyData> getDatas() {
            List<MyData> list = new ArrayList<>();
            //TODO++++++++++
            list.add(new MyData().category(1).name("data1").draggable(false));
            list.add(new MyData().category(2).name("data2").draggable(false));
            //TODO----------
            return list;
        }

        private static ArrayList<Category> getCategory() {
            Category category1 = new Category();
            category1.name("夫妻")
                    .itemStyle().normal().color("#009800");
            Category category2 = new Category();
            category2.name("战友")
                    .itemStyle().normal().color("#4592FF");
            Category category3 = new Category();
            category3.name("亲戚")
                    .itemStyle().normal().color("#663601");
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(category1);
            categories.add(category2);
            categories.add(category3);
            return categories;
        }

        private static Legend getLegend() {
            ArrayList<String> strings = new ArrayList<>();
            strings.add("夫妻");
            strings.add("战友");
            strings.add("亲戚");
            return new Legend().data(strings).show(true);
        }

        private static class MyData {
            private String name;
            private int category;
            private boolean draggable;

            public MyData name(String name) {
                this.name = name;
                return this;
            }

            public MyData category(int category) {
                this.category = category;
                return this;
            }

            public MyData draggable(boolean draggable) {
                this.draggable = draggable;
                return this;
            }
        }
    }
}
