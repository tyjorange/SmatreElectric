package com.rejuvee.smartelectric.family.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * 所有活动的基类
 *
 * @author tyj
 */
public abstract class BaseActivity extends AppCompatActivity {
//    @Override
//    public void setRequestedOrientation(int requestedOrientation) {
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
//            Log.i("BaseActivity", "avoid calling setRequestedOrientation when Oreo.");
//            return;
//        }
//        super.setRequestedOrientation(requestedOrientation);
//    }

    /**
     * 如果是透明的Activity，则不能固定它的方向，因为它的方向其实是依赖其父Activity的（因为透明）。
     * <p>
     * 然而这个bug只有在8.0中有，8.1中已经修复。具体crash有两种：
     * <p>
     * 1.Activity的风格为透明，在manifest文件中指定了一个方向，则在onCreate中crash
     * <p>
     * 2.Activity的风格为透明，如果调用setRequestedOrientation方法固定方向，则crash
     * <p>
     * https://blog.csdn.net/starry_eve/article/details/82777160
     *
     * @return
     */
//    private boolean isTranslucentOrFloating() {
//        boolean isTranslucentOrFloating = false;
//        try {
//            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
//            final TypedArray ta = obtainStyledAttributes(styleableRes);
//            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
//            m.setAccessible(true);
//            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
//            m.setAccessible(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return isTranslucentOrFloating;
//    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return result;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }
        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getMyTheme() != 0) {
//            setTheme(getMyTheme());
//        }
//        setContentView(getLayoutResId());
        //每创建一个活动，就加入到活动管理器中
        ActivityFragmentManager.addActivity(this);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //每销毁一个活动，就从活动管理器中移除
        ActivityFragmentManager.removeActivity(this);
        dealloc();
    }

//    protected abstract int getLayoutResId();
//
//    protected abstract int getMyTheme();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void dealloc();


}
