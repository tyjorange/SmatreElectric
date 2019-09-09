package com.base.frame.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.base.frame.log.DebugLog;
import com.base.frame.log.LogUtil;


/**
 * 自定义键盘事件
 *
 * @author admin
 */
public class KeyboardEvent {
    private static final String LOGTAG = LogUtil.makeLogTag(KeyboardEvent.class);
    private int screenHeight = 0;

    private KeyboardEvent() {

    }

    private Context mContext;

    public KeyboardEvent(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 隐藏键盘
     *
     * @param mContext
     */
    public static boolean hideSoftInput(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return false;

    }

    /**
     * 显示键盘
     *
     * @param mContext
     */
    public static boolean showSoftInput(Context mContext, View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInputFromInputMethod(((Activity)
        // mContext).getCurrentFocus()
        // .getWindowToken(), 0);
        return imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean softInputIsActivite(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int heightDiff = contentView.getRootView().getHeight() - contentView.getHeight();
                    DebugLog.i(LOGTAG, contentView.getRootView().getHeight() + "===" + contentView.getHeight());
                    int contentViewTop = ((Activity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
                    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
                    if (screenHeight > 800) {
                        if (heightDiff > 450) {
                            onHideKeyboard();

                            Intent intent = new Intent("KeyboardWillHide");
                            broadcastManager.sendBroadcast(intent);

                        } else {
                            int keyboardHeight = heightDiff - contentViewTop;
                            onShowKeyboard(keyboardHeight);

                            Intent intent = new Intent("KeyboardWillShow");
                            intent.putExtra("KeyboardHeight", keyboardHeight);
                            broadcastManager.sendBroadcast(intent);

                        }
                    } else {//screenHeight=480*800
                        if (heightDiff > 300) {
                            onHideKeyboard();

                            Intent intent = new Intent("KeyboardWillHide");
                            broadcastManager.sendBroadcast(intent);

                        } else {
                            int keyboardHeight = heightDiff - contentViewTop;
                            onShowKeyboard(keyboardHeight);

                            Intent intent = new Intent("KeyboardWillShow");
                            intent.putExtra("KeyboardHeight", keyboardHeight);
                            broadcastManager.sendBroadcast(intent);

                        }
                    }

                }
            };

    private boolean keyboardListenersAttached = false;
    private int previousDisplayHeight = 0;
    private ViewGroup contentView;
    private boolean isFullScreen;
    private int statusBarHeight;
    private boolean lastKeyboardShowing;

    private void onShowKeyboard(int keyboardHeight) {
//        toggleCloseMenu();
        if (keyboardEventListener != null)
            keyboardEventListener.onShowKeyboard();

    }

    private void onHideKeyboard() {
        if (keyboardEventListener != null)
            keyboardEventListener.onHideKeyboard();
//        toggleOpenMenu();
    }

    public void attachKeyboardListeners(ViewGroup rootLayout, KeyboardEventListener keyboardEventListener) {
        if (keyboardListenersAttached) {
            return;
        }
        this.keyboardEventListener = keyboardEventListener;
        this.contentView = rootLayout;
        this.contentView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        this.screenHeight = rootLayout.getResources().getDisplayMetrics().heightPixels;
        keyboardListenersAttached = true;
    }


    public void onDestroy() {

        if (keyboardListenersAttached) {
//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                contentView.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardLayoutListener);
            } else {
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
            }
        }
    }

    private KeyboardEventListener keyboardEventListener;

    public interface KeyboardEventListener {
        void onShowKeyboard();

        void onHideKeyboard();
    }


}
