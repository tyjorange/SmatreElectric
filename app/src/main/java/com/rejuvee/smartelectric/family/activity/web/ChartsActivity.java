package com.rejuvee.smartelectric.family.activity.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
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
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.AppGlobalConfig;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ChartsActivity extends BaseActivity {
    private static final String TAG = "ChartsActivity";
    private Context context;
    private WebView webView;
    private boolean isLoading;
    private LoadingDlg waitDialog;
    private CollectorBean collectorBean;
    private static List<SwitchBean> result;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_charts;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        context = this;
        findViewById(R.id.img_cancel).setOnClickListener(view -> finish());
        waitDialog = new LoadingDlg(this, -1);
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
                waitDialog.show();
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
                refreshEchartsWithOption(EchartOptionUtil.getGraphOptions());
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e(TAG, error.getDescription().toString());
                } else {
                    Log.e(TAG, error.toString());
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e(TAG, errorResponse.getStatusCode() + "");
            }
        });
        webView.loadUrl(AppGlobalConfig.HTTP_ECHARTES_TEMPLATE_URL + "#/getTemplate");
    }

    @Override
    protected void initData() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        getSwitchByCollector();
    }

    /**
     * 获取集中器下的线路
     */
    private void getSwitchByCollector() {
        Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                result = data;
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(context, getString(R.string.vs29));
                    result = new ArrayList<>();
                    waitDialog.dismiss();
                } else {
                    CustomToast.showCustomErrorToast(context, message);
                }
            }
        });
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
        Log.d(TAG, optionString);
        String call = "javascript:loadEcharts('" + optionString + "')";
        webView.loadUrl(call);
        waitDialog.dismiss();
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
        /**
         * @return GsonOption
         */
        static GsonOption getGraphOptions() {
            GsonOption option = new GsonOption();
            //title
            option.title("线路");
            //tooltip
            option.tooltip().show(false);
            //toolbox
            option.toolbox().show(true).setFeature(getFeature());
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
                ItemStyle itemStyle = new ItemStyle();
                Normal normal = new Normal();
                normal.show(true);
                itemStyle.normal(normal);
                itemStyle.normal().textStyle().fontSize(12);
                graph.label(itemStyle);
                //force
                graph.force()
                        .repulsion(3000) //节点之间的斥力因子。
//                        .initLayout("circular") //进行力引导布局前的初始化布局，初始化布局会影响到力引导的效果。
                        .gravity(1) //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。 bug: 无法设置为小数
                        .edgeLength(150); //边的两个节点之间的距离，这个距离也会受 repulsion。
                //data
                graph.setData(getDatas());
                //links
                graph.setLinks(getLinks());
            }
            option.series(graph);
            return option;
        }

        /**
         * 节点间的关系数据。示例：
         * <p>
         * links: [{
         * source: 'n1',
         * target: 'n2'
         * }, {
         * source: 'n2',
         * target: 'n3'
         * }]
         *
         * @return
         */
        private static List<Link> getLinks() {
            List<Link> list = new ArrayList<>();
            LineStyle lineStyle = new LineStyle();
            lineStyle.normal()
                    .opacity(0.9)//图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
//                    .borderWidth(1)
                    .curveness(0d);//边的曲度，支持从 0 到 1 的值，值越大曲度越大。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result.forEach(s -> {
                    if (s.getPid() != 0) {
                        list.add(new Link()
                                .source(s.getName())
                                .target(findPname(s.getPid()))
                                .lineStyle(lineStyle));
                    }
                });
            } else {
                //TODO
            }
            return list;
        }

        /**
         * 关系图的节点数据列表。
         * data: [{
         * name: '1',
         * x: 10,
         * y: 10,
         * value: 10
         * }, {
         * name: '2',
         * x: 100,
         * y: 100,
         * value: 20,
         * symbolSize: 20,
         * itemStyle: {
         * normal: {
         * color: 'red'
         * }
         * }
         * }]
         *
         * @return
         */
        private static List<MyNodeData> getDatas() {
            List<MyNodeData> list = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result.forEach(s -> {
                    list.add(new MyNodeData().category(getCategory(s)).state(s.getSwitchState()).name(s.getName()).draggable(false));
                });
            } else {
                //TODO
            }
            return list;
        }

        /**
         * 父节点名称
         */
        static AtomicReference<String> name = new AtomicReference<>();

        /**
         * 获取父节点名称
         *
         * @param pid
         * @return
         */
        private static String findPname(int pid) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result.forEach(s -> {
                    if (s.getSwitchID() == pid) {
                        name.set(s.getName());
                    }
                });
            } else {
                //TODO
            }
            return name.get();
        }

        /**
         * 数据项所在类目
         */
        static AtomicInteger res = new AtomicInteger();

        /**
         * 计算数据项所在类目
         *
         * @param s
         * @return 0 线路 1 分线 2 支线
         */
        private static int getCategory(SwitchBean s) {
            if (s.getPid() == 0) {// 父节点为0 则是线路 category=0
                res.set(0);
                return res.get();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    result.forEach(rs -> {
                        if (rs.getSwitchID() == s.getPid()) {
                            if (rs.getPid() == 0) {// 父节点的父节点为0 则是分线 category=1
                                res.set(1);
                            } else {// 剩下则是支线 category=2
                                res.set(2);
                            }
                        }
                    });
                } else {
                    //TODO
                }
            }
            return res.get();
        }

        /**
         * 各工具配置项。
         * <p>
         * 除了各个内置的工具按钮外，还可以自定义工具按钮。
         *
         * @return
         */
        private static Map<String, Feature> getFeature() {
            HashMap<String, Feature> stringFeatureHashMap = new HashMap<>();
            stringFeatureHashMap.put("restore", new Feature().show(true));
            stringFeatureHashMap.put("saveAsImage", new Feature().show(false));
            stringFeatureHashMap.put("dataView", new Feature().show(false).readOnly(true));
            return stringFeatureHashMap;
        }

        /**
         * 数据项所在类目。与图例联动
         *
         * @return
         */
        private static List<Category> getCategory() {
            Category category1 = new Category();
            category1.name("线路")
                    .itemStyle().normal().color("#009800");
            Category category2 = new Category();
            category2.name("分线")
                    .itemStyle().normal().color("#4592FF");
            Category category3 = new Category();
            category3.name("支线")
                    .itemStyle().normal().color("#663601");
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(category1);
            categories.add(category2);
            categories.add(category3);
            return categories;
        }

        /**
         * 图例组件。与类目联动
         * <p>
         * 图例组件展现了不同系列的标记(symbol)，颜色和名字。可以通过点击图例控制哪些系列不显示。
         *
         * @return
         */
        private static Legend getLegend() {
            ArrayList<String> strings = new ArrayList<>();
            strings.add("线路");
            strings.add("分线");
            strings.add("支线");
            return new Legend().data(strings).show(true);
        }

        /**
         * 节点信息
         *
         * @return
         */
        private static class MyNodeData {
            private String name;//节点名称
            private int category;//节点类目
            private boolean draggable;//是否拖拽位置
            private int state;// 1 合闸 0 拉闸 -1 错误

            MyNodeData name(String name) {
                this.name = name;
                return this;
            }

            MyNodeData category(int category) {
                this.category = category;
                return this;
            }

            MyNodeData draggable(boolean draggable) {
                this.draggable = draggable;
                return this;
            }

            MyNodeData state(int state) {
                this.state = state;
                return this;
            }

            public boolean isDraggable() {
                return draggable;
            }

            public MyNodeData setDraggable(boolean draggable) {
                this.draggable = draggable;
                return this;
            }

            public int getCategory() {
                return category;
            }

            public MyNodeData setCategory(int category) {
                this.category = category;
                return this;
            }

            public String getName() {
                return name;
            }

            public MyNodeData setName(String name) {
                this.name = name;
                return this;
            }

            public int getState() {
                return state;
            }

            public MyNodeData setState(int state) {
                this.state = state;
                return this;
            }
        }
    }
}
