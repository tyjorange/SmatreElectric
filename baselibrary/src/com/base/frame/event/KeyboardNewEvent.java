package com.base.frame.event;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.base.frame.log.LogUtil;


/**
 * 自定义键盘事件
 *
 * @author admin
 */
public class KeyboardNewEvent {
    private static final String LOGTAG = LogUtil.makeLogTag(KeyboardNewEvent.class);
    private int screenHeight = 0;

    private KeyboardNewEvent() {

    }

    private Context mContext;

    public KeyboardNewEvent(Context mContext) {
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


    private boolean keyboardListenersAttached = false;
    private ViewGroup contentView;
    private static KeyboardNewEvent.KeyboardStatusListener keyboardStatusListener;

    public void attachKeyboardListeners(ViewGroup rootLayout,
                                        boolean isTranslucentStatusBar,
                                        KeyboardEventListener keyboardEventListener) {
        if (keyboardListenersAttached) {
            return;
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            isTranslucentStatusBar = false;
        }
        this.contentView = rootLayout;
        attach((Activity) mContext, isTranslucentStatusBar, keyboardEventListener);
        this.screenHeight = rootLayout.getResources().getDisplayMetrics().heightPixels;
        keyboardListenersAttached = true;
    }

    /**
     * Recommend invoked by {@link Activity#onCreate(Bundle)}
     * For align the height of the keyboard to {@code target} as much as possible.
     * For save the refresh the keyboard height to shared-preferences.
     *
     * @param activity contain the view
     */
    public static void attach(final Activity activity, boolean isTranslucentStatusBar,
                              KeyboardEventListener keyboardEventListener) {
        final ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        keyboardStatusListener = new KeyboardNewEvent.KeyboardStatusListener(isTranslucentStatusBar, contentView,
                keyboardEventListener);
        contentView.getViewTreeObserver().
                addOnGlobalLayoutListener(keyboardStatusListener);
    }


    public void onDestroy() {

        if (keyboardListenersAttached) {
//
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                contentView.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardStatusListener);
            }
        }
    }

    private KeyboardEventListener keyboardEventListener;

    public interface KeyboardEventListener {
        void onShowKeyboard();

        void onHideKeyboard(int size);
    }

    private static class KeyboardStatusListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static String TAG = "KeyboardStatusListener";

        private int previousDisplayHeight = 0;
        private final ViewGroup contentView;
        private final KeyboardEventListener keyboardEventListener;
        private final boolean isFullScreen;
        private final int statusBarHeight;
        private boolean lastKeyboardShowing;

        KeyboardStatusListener(boolean isFullScreen, ViewGroup contentView,
                               KeyboardEventListener keyboardEventListener) {
            this.contentView = contentView;
            this.keyboardEventListener = keyboardEventListener;
            this.isFullScreen = isFullScreen;
            this.statusBarHeight = StatusBarHeightUtil.getStatusBarHeight(contentView.getContext());
        }

        @Override
        public void onGlobalLayout() {
            final View userRootView = contentView.getChildAt(0);

            // Step 1. calculate the current display frame's height.
            Rect r = new Rect();
            userRootView.getWindowVisibleDisplayFrame(r);
            final int displayHeight = (r.bottom - r.top);

//            calculateKeyboardHeight(displayHeight);
            calculateKeyboardShowing(displayHeight);

            previousDisplayHeight = displayHeight;
        }

        private void calculateKeyboardHeight(final int displayHeight) {
            // first result.
            if (previousDisplayHeight == 0) {
                previousDisplayHeight = displayHeight;

                // init the panel height for target.
//               KeyboardUtil.getValidPanelHeight(getContext());
                return;
            }

            int keyboardHeight;
            if (isFullScreen) {
                // the height of content parent = contentView.height + actionBar.height
                final View actionBarOverlayLayout = (View) contentView.getParent();

                keyboardHeight = actionBarOverlayLayout.getHeight() - displayHeight;

                Log.d(TAG, String.format("action bar over layout %d display height: %d",
                        ((View) contentView.getParent()).getHeight(), displayHeight));

            } else {
                keyboardHeight = Math.abs(displayHeight - previousDisplayHeight);
            }
            // no change.
            if (keyboardHeight <= 0) {
                return;
            }

            Log.d(TAG, String.format("pre display height: %d display height: %d keyboard: %d ",
                    previousDisplayHeight, displayHeight, keyboardHeight));

            // influence from the layout of the Status-bar.
            if (keyboardHeight == this.statusBarHeight) {
                Log.w(TAG, String.format("On global layout change get keyboard height just equal" +
                        " statusBar height %d", keyboardHeight));
                return;
            }

            // save the keyboardHeight
//            boolean changed = KeyboardUtil.saveKeyboardHeight(getContext(), keyboardHeight);
//            if (changed) {
//                final int validPanelHeight = KeyboardUtil.getValidPanelHeight(getContext());
//                if (this.panelHeightTarget.getHeight() != validPanelHeight) {
//                    // Step3. refresh the panel's height with valid-panel-height which refer to
//                    // the last keyboard height
//                    this.panelHeightTarget.refreshHeight(validPanelHeight);
//                }
//            }
        }

        private int maxOverlayLayoutHeight;

        private void calculateKeyboardShowing(final int displayHeight) {

            boolean isKeyboardShowing;

            // the height of content parent = contentView.height + actionBar.height
            final View actionBarOverlayLayout = (View) contentView.getParent();
            final int actionBarOverlayLayoutHeight = actionBarOverlayLayout.getHeight();

            if (isFullScreen) {
                if (actionBarOverlayLayoutHeight - displayHeight == this.statusBarHeight) {
                    // handle the case of status bar layout, not keyboard active.
                    isKeyboardShowing = lastKeyboardShowing;
                } else {
                    isKeyboardShowing = actionBarOverlayLayoutHeight > displayHeight;
                }

            } else {

                final int phoneDisplayHeight = contentView.getResources().getDisplayMetrics().heightPixels;
                if (phoneDisplayHeight == actionBarOverlayLayoutHeight &&
                        actionBarOverlayLayout.getPaddingTop() == 0) {
                    // no space to settle down the status bar, switch to fullscreen,
                    // only in the case of paused and opened the fullscreen page.
                    Log.w(TAG, String.format("skip the keyboard status calculate, the current" +
                                    " activity is paused. and phone-display-height %d," +
                                    " root-height+actionbar-height %d", phoneDisplayHeight,
                            actionBarOverlayLayoutHeight));
                    return;

                }
                if (maxOverlayLayoutHeight == 0) {
                    // non-used.
                    isKeyboardShowing = lastKeyboardShowing;
                } else if (displayHeight > maxOverlayLayoutHeight) {
                    isKeyboardShowing = false;
                } else {
                    isKeyboardShowing = true;
                }

                maxOverlayLayoutHeight = Math.max(maxOverlayLayoutHeight, actionBarOverlayLayoutHeight);
            }

//            if (lastKeyboardShowing != isKeyboardShowing) {
//                Log.d(TAG, String.format("displayHeight %d actionBarOverlayLayoutHeight %d " +
//                                "keyboard status change: %B",
//                        displayHeight, actionBarOverlayLayoutHeight, isKeyboardShowing));
////                this.panelHeightTarget.onKeyboardShowing(isKeyboardShowing);
            if (!isKeyboardShowing) {
                if (keyboardEventListener != null)
                    keyboardEventListener.onShowKeyboard();
            } else {
                View layout = (View) contentView.getParent();
                int height = layout.getHeight() - displayHeight;
                if (keyboardEventListener != null)
                    keyboardEventListener.onHideKeyboard(height);
            }
//            }

//            lastKeyboardShowing = isKeyboardShowing;

        }

        private Context getContext() {
            return contentView.getContext();
        }
    }
}
