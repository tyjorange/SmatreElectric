package com.rejuvee.smartelectric.family;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.SizeUtils;
import com.base.library.widget.CustomToast;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rejuvee.smartelectric.family.activity.energy.TimePriceActivity;
import com.rejuvee.smartelectric.family.activity.kefu.CustomerServiceActivity;
import com.rejuvee.smartelectric.family.activity.mine.AboutActivity;
import com.rejuvee.smartelectric.family.activity.mine.PerInfoActivity;
import com.rejuvee.smartelectric.family.activity.mine.SettingsActivity;
import com.rejuvee.smartelectric.family.activity.mine.ThridBindActivity;
import com.rejuvee.smartelectric.family.activity.scene.SceneActivity;
import com.rejuvee.smartelectric.family.adapter.CollectorBeanAdapter;
import com.rejuvee.smartelectric.family.adapter.HorizontalListSceneAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.ActivityFragmentManager;
import com.rejuvee.smartelectric.family.common.AutoUpgrade;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.PermissionManage;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.common.utils.WifiUtil;
import com.rejuvee.smartelectric.family.common.widget.CircleImageView;
import com.rejuvee.smartelectric.family.common.widget.MyBlurDrawerToggle;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.databinding.ActivityMainNavigationBinding;
import com.rejuvee.smartelectric.family.model.bean.AutoUpgradeEventMessage;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.model.bean.WxSubscribed;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.favre.lib.dali.builder.nav.DaliBlurDrawerToggle;
import at.favre.lib.dali.builder.nav.NavigationDrawerListener;

public class MainNavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = "MainNavigationActivity";
    private DialogTip mDialogTip;
    private Context mContext;
    //场景
//    private HorizontalListView listViewScene;
    private List<SceneBean> listSceneBeanData = new ArrayList<>();
    private HorizontalListSceneAdapter mSceneAdapter;
    //    private LinearLayout ll_add_scene;
    //集中器
//    private GridView gridViewDevice;
//    private List<CollectorBean> listDeviceData = new ArrayList<>();
    private CollectorBeanAdapter mCollectorBeanAdapter;
    //    private TextView tvCollectorCount;//集中器计数
    //用户
    private CircleImageView ivHead;//头像view
    //    private CircleImageView ivHeadSmall;//头像view
    private TextView tvNick;//昵称view
    //    private TextView tvUsername;//用户名view

    private String nickname;
    private String username;
    private String telephone;
    private String headImgurl;
    private String wechatUnionID;
    private String qqUnionID;

    //    private NavigationView navigationView;
//    private DrawerLayout drawerLayout;
    private DaliBlurDrawerToggle drawerToggle;
//    private SwipeRefreshLayout refreshLayout;

    private ActivityMainNavigationBinding mBinding;
    //    private Toolbar toolbar;
//    private ImageView ivUserQCode;
//    private PopwindowQCode popwindowQCode;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_main_navigation;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    /**
     * 获取屏幕参数
     */
    private void getDensity() {
        Log.w(TAG, ValidateUtils.getSystemLanguage());
        Log.w(TAG, ValidateUtils.getSystemVersion());
        Log.w(TAG, ValidateUtils.getDeviceBrand());
        Log.w(TAG, ValidateUtils.getSystemModel());
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        float densityDpi = outMetrics.densityDpi;
        Log.w(TAG, outMetrics.toString());
        Log.w(TAG, "widthPixel = " + widthPixel + ",heightPixel = " + heightPixel);
        Log.w(TAG, "densityDpi = " + densityDpi);
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation);
        MainNavigationViewModel mViewModel = ViewModelProviders.of(this).get(MainNavigationViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setLifecycleOwner(this);

        mSceneAdapter = new HorizontalListSceneAdapter(this, listSceneBeanData);
        mBinding.include.include.setHorizontalListSceneAdapter(mSceneAdapter);

        getDensity();
        mContext = this;
//        navigationView = findViewById(R.id.nav_view);
//        tvCollectorCount = findViewById(R.id.tv_collector_count);
        tvNick = mBinding.navView.getHeaderView(0).findViewById(R.id.user_nickname);
        ivHead = mBinding.navView.getHeaderView(0).findViewById(R.id.user_headimg);
//        ivHeadSmall = findViewById(R.id.user_headimg_small);
        mBinding.include.userHeadimgSmall.setOnClickListener(v -> mBinding.drawerLayout.openDrawer(GravityCompat.START));
        mBinding.navView.getHeaderView(0).findViewById(R.id.user_edit).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("telephone", telephone);
            intent.putExtra("nickname", nickname);
            intent.putExtra("username", username);
            intent.putExtra("headImgurl", headImgurl);
            intent.setClass(mContext, PerInfoActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_USER_INFO);
        });
//        ll_add_scene = findViewById(R.id.ll_add_scene);
        initScene();
        initCollector();
//        refreshLayout = findViewById(R.id.refreshlayout);
        mBinding.include.include.refreshlayout.setOnRefreshListener(this::getCollector);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            //问题出在6.0 && 6.0.1版本中，这个权限默认是被拒绝，无法获取这个权限。所以，在需要个权限的时候会出现权限问题导致应用因为权限问题崩溃
            if (!Settings.System.canWrite(this)) {
                Intent goToSettings = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                goToSettings.setData(Uri.parse("package:" + getPackageName()));
                startActivity(goToSettings);
            }
        }
        PermissionManage.getInstance().setCallBack(new PermissionManage.PermissionCallBack() {

            @Override
            public void onGranted() {
                WifiUtil.getInstance(getApplicationContext());
            }

            @Override
            public void onDenied() {

            }
        }).hasLocationStorage(this);
        getUserMsg();
        testWechatPublic();
//        getScene();
//        getCollector();
        initToolBar();
        AutoUpgrade.getInstacne(this).start();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * 检查公众号是否关注
     */
    private void testWechatPublic() {
        Core.instance(this).validateWechatPublic(new ActionCallbackListener<WxSubscribed>() {
            @Override
            public void onSuccess(WxSubscribed data) {
                if (data.getIsSubscribed() == 0) {
                    Snackbar.make(tvNick, R.string.vs225, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.vs248, v -> {
                                Intent intent = new Intent(mContext, ThridBindActivity.class);
                                intent.putExtra("wechatUnionID", wechatUnionID);
                                intent.putExtra("qqUnionID", qqUnionID);
                                startActivityForResult(intent, CommonRequestCode.REQUEST_THIRD_BIND);
                            }).setActionTextColor(getResources().getColor(R.color.blue_light))
                            .setDuration(10000).show();
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.i(TAG, message);
            }
        });
    }

    @Override
    protected void dealloc() {
        AutoUpgrade.getInstacne(this).destroyInstance();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 用户信息
     */
    public void getUserMsg() {
        Core.instance(mContext).getUserMsg(new ActionCallbackListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg data) {
                telephone = data.getPhone();
                nickname = data.getNickName();
                username = data.getUsername();
                headImgurl = data.getHeadImg();
                wechatUnionID = data.getWechatUnionID();
                qqUnionID = data.getQqUnionID();
                tvNick.setText(nickname);
//                tvUsername.setText(username);
                if (headImgurl.equals("")) {
                    headImgurl = null;
                    ivHead.setImageResource(R.drawable.icon_user_default);
                    mBinding.include.userHeadimgSmall.setImageResource(R.drawable.icon_user_default);
                } else {
                    if (!headImgurl.startsWith("https://")) {
                        headImgurl = "https://" + headImgurl;
                    }
                    RequestCreator load = Picasso.with(mContext).load(headImgurl);
                    setHeaderIcon(load);
                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                ivHead.setImageResource(R.drawable.icon_user_default);
            }
        });
    }


    /**
     * 设置头像
     */
    private void setHeaderIcon(RequestCreator load) {
        load.into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ivHead.setImageBitmap(bitmap);
                mBinding.include.userHeadimgSmall.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                ivHead.setImageResource(R.drawable.icon_user_default);
                mBinding.include.userHeadimgSmall.setImageResource(R.drawable.icon_user_default);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    /**
     * 生成用户名二维码
     */
//    private void setQCodeUserName() {
//        Bitmap bmpUser = ValidateUtils.createQRcodeImage(username, SizeUtils.dp2px(100), SizeUtils.dp2px(100));
//        if (bmpUser != null) {
//            ivUserQCode.setImageBitmap(bmpUser);
//            popwindowQCode.setQCodeImageBitmap(bmpUser);
//        }
//    }
    private void initToolBar() {
//        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mBinding.include.toolbar);
        setTitle("");

//        drawerLayout = findViewById(R.id.drawer_layout);
//        CircleImageView civ = (CircleImageView) findViewById(R.id.user_headimg_s);
//        toolbar.setNavigationIcon(new CircleDrawable(BitmapUtil.drawable2Bitmap(ivHead.getDrawable())));
        // 模糊控件Drawer
        drawerToggle = new MyBlurDrawerToggle(this, mBinding.drawerLayout, mBinding.include.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close, new NavigationDrawerListener() {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        });
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerToggle.syncState();
//        drawerToggle.setConfig(1, 2, DaliBlurDrawerToggle.CacheMode.AUTO);
        drawerToggle.setDrawerIndicatorEnabled(true);
        mBinding.drawerLayout.addDrawerListener(drawerToggle);

//        NavigationView navigationView = findViewById(R.id.nav_view);
        mBinding.navView.setNavigationItemSelectedListener(this);

        // 隐藏的条目
//        FloatingActionButton fab = findViewById(R.id.fab);
        mBinding.include.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("ActionFab", null).show());
    }

    private void initScene() {
//        listViewScene = findViewById(R.id.main_scene);
        mBinding.include.include.mainScene.setDividerWidth(SizeUtils.dp2px(35));//图标间隔
//        mSceneAdapter = new HorizontalListSceneAdapter(this, listSceneBeanData);
//        mBinding.include.include.mainScene.setAdapter(mSceneAdapter);
        mBinding.include.include.mainScene.setOnItemClickListener((parent, view, position, id) -> {
            mDialogTip = new DialogTip(mContext);
            mDialogTip.setTitle(getString(R.string.exceu_sce));
            mDialogTip.setContent(String.format(getString(R.string.exceu_sce_issure), listSceneBeanData.get(position).getSceneName()));
            mDialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
                @Override
                public void onCancel() {
                    mDialogTip.dismiss();
                }

                @Override
                public void onEnsure() {
                    mDialogTip.dismiss();
                    doExecute(listSceneBeanData.get(position).getSceneId());
                }
            });
            mDialogTip.show();
        });

        mBinding.include.include.llAddScene.setOnClickListener(v -> startActivity(new Intent(mContext, SceneActivity.class)));
    }

    private void getScene() {
        Core.instance(this).findSceneByUser(new ActionCallbackListener<List<SceneBean>>() {
            @Override
            public void onSuccess(List<SceneBean> data) {
                listSceneBeanData.clear();
                listSceneBeanData.addAll(data);
                mSceneAdapter.notifyDataSetChanged();
//                updateRefreshState();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {//无数据
                    listSceneBeanData.clear();
                    mSceneAdapter.notifyDataSetChanged();
                }
//                updateRefreshState();
            }
        });
    }

    public class Presenter {
        public void onClickAddItem(View view) {
//            mCollectorBeanAdapter.add(new Employee("haha", "1", false));
        }

        public void onClickRemoveItem(View view) {
//            mCollectorBeanAdapter.remove();
        }
    }

    private void initCollector() {
//        gridViewDevice = findViewById(R.id.grid_device);
        mCollectorBeanAdapter = new CollectorBeanAdapter(this);
        mBinding.include.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.include.include.recyclerView.setAdapter(mCollectorBeanAdapter);
        mCollectorBeanAdapter.setCallback(new CollectorBeanAdapter.CallBack() {

            @Override
            public void onCollectorBeanClick(CollectorBean bean) {
                //TODO sout
                System.out.println(bean);
            }
        });
//        gridViewDevice.setEmptyView(findViewById(R.id.empty_layout));
//        mBinding.include.include.gridDevice.setOnItemClickListener((parent, view, position, id) -> {
////            if (mDeviceAdapter.isEditMode()) {
////                return;
////            }
//            if (position == listDeviceData.size()) {
//                startActivityForResult(new Intent(mContext, AddDeviceOrSwitchActivity.class), CommonRequestCode.REQUEST_ADD_COLLECTOR);
//            } else {
//                CollectorBean collectorBean = listDeviceData.get(position);
//                Intent intent = new Intent(MainNavigationActivity.this, CollectorDetailActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//            }
//        });
        //长按删除
        // Deprecated
//        mBinding.include.include.gridDevice.setOnItemLongClickListener((parent, view, position, id) -> {
//                if (position == listDeviceData.size()) {
//                    return true;
//                }
//                if (mDeviceAdapter.isEditMode()) {
//                    mDeviceAdapter.setEditMode(false);
//                } else {
//                    mDeviceAdapter.setEditMode(true);
//                }
//                Vibrator vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
//                vibrator.vibrate(10000);
//            return true;
//        });
    }

    private void getCollector() {
        Core.instance(this).getCollector(new ActionCallbackListener<List<CollectorBean>>() {
            @Override
            public void onSuccess(List<CollectorBean> data) {
                mBinding.include.include.tvCollectorCount.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.vs13), data.size()));
//                listDeviceData.clear();
//                listDeviceData.addAll(data);
                data.add(new CollectorBean(Parcel.obtain()));// 加一个空项作为添加按钮事件
                mCollectorBeanAdapter.addAll(data);
//                mDeviceAdapter.notifyDataSetChanged();
//                updateRefreshState();
                mBinding.include.include.refreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                updateRefreshState();
                mBinding.include.include.refreshlayout.setRefreshing(false);
            }
        });
    }

    /**
     * 执行场景
     *
     * @param sceneId
     */
    public void doExecute(final String sceneId) {
        Core.instance(this).doExcuteScene(sceneId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(mContext, getString(R.string.scene_excute_sucess));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(mContext, getString(R.string.zhixing_fail));
            }
        });
    }

    /**
     * 长按删除
     */
//    @Override
//    @Deprecated
//    public void onDel(int position) {
//        DialogTip dialogTip = new DialogTip(mContext);
//        dialogTip.setTitle(mContext.getResources().getString(R.string.delete)).setRedBtn()
//                .setContent(mContext.getResources().getString(R.string.delete_device))
//                .setDialogListener(new DialogTip.onEnsureDialogListener() {
//                    @Override
//                    public void onCancel() {
//
//                    }
//
//                    @Override
//                    public void onEnsure() {
//                        CollectorBean collectorBean = listDeviceData.get(position);
//                        Core.instance(mContext).unbindDevice(collectorBean.getCode(), new ActionCallbackListener<Void>() {
//                            @Override
//                            public void onSuccess(Void data) {
//                                listDeviceData.remove(position);
//                                mDeviceAdapter.notifyDataSetChanged();
//                                mDeviceAdapter.setEditMode(false);
//                                tvCollectorCount.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.vs13), listDeviceData.size()));
//                                CustomToast.showCustomToast(mContext, getString(R.string.vs66));
//                            }
//
//                            @Override
//                            public void onFailure(int errorEvent, String message) {
//                                CustomToast.showCustomErrorToast(mContext, message);
//                            }
//                        });
//
//                    }
//                }).show();
//    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        boolean drawerOpen = drawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
//        drawerLayout = findViewById(R.id.drawer_layout);
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//        } else if (mDeviceAdapter.isEditMode()) {
//            mDeviceAdapter.setEditMode(false);
        } else {
            mDialogTip = new DialogTip(mContext);
            mDialogTip.setTitle(getString(R.string.vs67));
            mDialogTip.setContent(getString(R.string.vs68));
            mDialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
                @Override
                public void onCancel() {
                    mDialogTip.dismiss();
                }

                @Override
                public void onEnsure() {
                    quit();
                }
            });
            mDialogTip.show();
        }
    }

    private void quit() {
        ActivityFragmentManager.removeAll();
        ActivityFragmentManager.finishAll();
        super.onBackPressed();
    }

    /**
     * 右上角按钮
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    /**
     * 右上角按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            WXHelper.toMiniProgram(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.me_disanfang) {
            Intent intent = new Intent(this, ThridBindActivity.class);
            intent.putExtra("wechatUnionID", wechatUnionID);
            intent.putExtra("qqUnionID", qqUnionID);
            startActivityForResult(intent, CommonRequestCode.REQUEST_THIRD_BIND);
        } else if (id == R.id.me_jisuanqi) {
            startActivity(new Intent(this, TimePriceActivity.class));
        } else if (id == R.id.me_kefu) {
            Intent intent = new Intent(this, CustomerServiceActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else if (id == R.id.me_wenti) {
            DialogTipWithoutOkCancel d = new DialogTipWithoutOkCancel(this);
            d.hiddenTitle();
            d.showImg();
            d.setContent(getString(R.string.vs30));
            d.show();
        } else if (id == R.id.me_guanyu) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.me_shezhi) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        //收起侧边栏
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void autoUpgrade(final AutoUpgradeEventMessage autoUpgrade) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (PermissionManage.getInstance().isPermissionGranted(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
//                AutoUpgrade.getInstacne(MainNavigationActivity.this).installApkNew(autoUpgrade.upGradeUri);
//            } else {
//                PermissionManage.getInstance().startRequest(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, new PermissionUtils.OnPermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        AutoUpgrade.getInstacne(MainNavigationActivity.this).installApkNew(autoUpgrade.upGradeUri);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(String[] deniedPermissions) {
//                        Log.e(TAG, "onPermissionDenied");
//                    }
//                });
//            }
//        } else {
//            AutoUpgrade.getInstacne(MainNavigationActivity.this).installApkNew(autoUpgrade.upGradeUri);
//        }
        PermissionManage.getInstance().setCallBack(new PermissionManage.PermissionCallBack() {

            @Override
            public void onGranted() {
                AutoUpgrade.getInstacne(MainNavigationActivity.this).installApkNew(autoUpgrade.upGradeUri);
            }

            @Override
            public void onDenied() {
//                CustomToast.showCustomErrorToast(mContext, getString(R.string.vs46));
                Log.e(TAG, "onPermissionDenied");
            }
        }).hasInstall(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getScene();
        getCollector();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonRequestCode.REQUEST_THIRD_BIND) {
            Log.i(TAG, "REQUEST_THIRD_BIND 返回");
            getUserMsg();
        } else if (requestCode == CommonRequestCode.REQUEST_USER_INFO) {
            Log.i(TAG, "REQUEST_USER_INFO 返回");
            getUserMsg();
        } else if (requestCode == CommonRequestCode.REQUEST_ADD_COLLECTOR) {
            getCollector();
        }
    }
}
