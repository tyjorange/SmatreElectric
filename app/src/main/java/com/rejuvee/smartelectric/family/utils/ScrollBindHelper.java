package com.rejuvee.smartelectric.family.utils;

import android.os.Message;
import android.view.View;
import android.widget.SeekBar;

import com.rejuvee.smartelectric.family.custom.LastMsgHandler;
import com.rejuvee.smartelectric.family.widget.ObservableScrollView;

public class ScrollBindHelper implements SeekBar.OnSeekBarChangeListener, ObservableScrollView.ScrollViewListener {

    private final SeekBar seekBar;
    private final ObservableScrollView scrollView;
    private final View scrollContent;
    private static ScrollBindHelper helper;
    private static boolean ifii;

    /**
     * 使用静态方法来绑定逻辑，代码可读性更高。
     */
    public static ScrollBindHelper bind(SeekBar seekBar, ObservableScrollView scrollView) {
        //初始化工具类
        //封装好的获取屏幕工具类  进行初始化
        ViewUtil.init(seekBar.getContext().getApplicationContext());

        helper = new ScrollBindHelper(seekBar, scrollView);
        seekBar.setOnSeekBarChangeListener(helper);
        scrollView.setScrollViewListener(helper);
        return helper;
    }

    //设置全局属性
    private ScrollBindHelper(SeekBar seekBar, ObservableScrollView scrollView) {
        this.seekBar = seekBar;
        this.scrollView = scrollView;
        //获取scrollview的第一个孩子的高度，在这里第一个孩子就是就是TextView
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
        //换句话说就是TextView的高度 -  Scrollview的高度
        return scrollContent.getHeight() - scrollView.getHeight();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        showScroll();
        System.out.println(progress);
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
        ifii = false;
        handler.clearAll();
    }

    //SeekBar的停止拖动事件
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isUserSeeking = false;
        handler.reset();
    }

    /*动画*/
    public static final long DEFAULT_TIME_OUT = 1000L;

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        //用户触控时不触发
        if (isUserSeeking) {
            return;
        } else if (getContentRange() < ViewUtil.getScreenHeightPx() * 3) {//宽度小于三个屏幕不做处理
            return;
        }

        int range = getScrollRange();
        seekBar.setProgress(range != 0 ? (y * 100 / range) : 0);
    }

    private static class VisibleHandler extends LastMsgHandler {

        private ScrollBindHelper helper;

        public VisibleHandler(ScrollBindHelper helper) {
            this.helper = helper;
        }

        public void reset() {
            sendMsgDelayed(DEFAULT_TIME_OUT);
        }

        @Override
        protected void handleLastMessage(Message msg) {
//            helper.hideScroll();
        }
    }

    private VisibleHandler handler = new VisibleHandler(this);

    //隐藏SeekBar
    private void hideScroll() {
        seekBar.setVisibility(View.GONE);
    }

    //显示SeekBar
    private void showScroll() {
        seekBar.setVisibility(View.VISIBLE);
    }
}
