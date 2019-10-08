package com.rejuvee.smartelectric.family.utils;

import android.os.Message;
import android.view.View;
import android.widget.SeekBar;

import com.rejuvee.smartelectric.family.custom.LastMsgHandler;
import com.rejuvee.smartelectric.family.widget.ObservableScrollView;

/**
 * 滚动界面滚动条双向绑定
 */
public class ScrollBindHelper implements SeekBar.OnSeekBarChangeListener, ObservableScrollView.ScrollViewListener {

    private final SeekBar seekBar;
    private final ObservableScrollView scrollView;
    private final View scrollContent;
    private static ScrollBindHelper helper;
    //    private static boolean ifii;
    private static VisibleHandler handler = new VisibleHandler();

    /**
     * 使用静态方法来绑定逻辑，代码可读性更高。
     */
    public static void bind(SeekBar seekBar, ObservableScrollView scrollView) {
        //初始化工具类
        //封装好的获取屏幕工具类  进行初始化
        ViewUtil.init(seekBar.getContext().getApplicationContext());

//        if (helper == null) {
            helper = new ScrollBindHelper(seekBar, scrollView);
            seekBar.setOnSeekBarChangeListener(helper);
            scrollView.setScrollViewListener(helper);
//        }
        resetThumb();
    }

    private static void resetThumb() {
        handler.reset();
    }

    private static void flushThumb() {
//        System.out.println(helper.getScrollRange());
        if (helper.getScrollRange() < 0) {// 内容高度小于屏幕高度
            helper.hideScroll();
        } else {
            helper.showScroll();
        }
    }

    //设置全局属性
    private ScrollBindHelper(SeekBar seekBar, ObservableScrollView scrollView) {
        this.seekBar = seekBar;
        this.scrollView = scrollView;
        //获取scrollview的第一个孩子的高度，在这里第一个孩子就是就是LinearLayout
        this.scrollContent = scrollView.getChildAt(0);
    }

    //用户是否正在拖动SeekBar的标志
    private boolean isUserSeeking;

    //获取TextView的高度
    private int getContentRange() {
        return scrollContent.getHeight();
    }

    //获取滚动范围
    private int getScrollRange() {
        //换句话说就是View的高度 -  Scrollview的高度
        return getContentRange() - scrollView.getHeight();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        showScroll();
        if (!isUserSeeking) {
            handler.reset();
        }

        //不是用户操作的时候不触发
//        if (!fromUser) {
//            return;
//        }

        scrollView.scrollTo(0, progress * getScrollRange() / 100);
    }

    //SeekBar的拖动事件
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isUserSeeking = true;
//        ifii = false;
        handler.clearAll();
    }

    //SeekBar的停止拖动事件
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isUserSeeking = false;
        handler.reset();
    }

    // 延迟刷新Thumb 否则会计算view高度错误
    private static final long DEFAULT_TIME_OUT = 500L;

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        //用户触控时不触发
        int range = getScrollRange();
        if (isUserSeeking) {
            return;
        } else if (range < ViewUtil.getScreenHeightPx() * 3) {//宽度小于三个屏幕不做处理
            return;
        }

        seekBar.setProgress(range != 0 ? (y * 100 / range) : 0);
    }

    private static class VisibleHandler extends LastMsgHandler {

//        private ScrollBindHelper helper;

//        VisibleHandler(ScrollBindHelper helper) {
//            this.helper = helper;
//        }

        void reset() {
            sendMsgDelayed(DEFAULT_TIME_OUT);
        }

        @Override
        protected void handleLastMessage(Message msg) {
//            helper.hideScroll();
            flushThumb();
        }
    }

    //隐藏SeekBar
    private void hideScroll() {
        seekBar.setVisibility(View.INVISIBLE);
    }

    //显示SeekBar
    private void showScroll() {
        seekBar.setVisibility(View.VISIBLE);
    }
}
