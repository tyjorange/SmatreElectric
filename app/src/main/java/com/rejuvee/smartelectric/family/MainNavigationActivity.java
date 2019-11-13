package com.rejuvee.smartelectric.family;

import android.content.Intent;
import android.content.res.Configuration;
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

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rejuvee.smartelectric.family.activity.AddDeviceOrSwitchActivity;
import com.rejuvee.smartelectric.family.activity.collector.CollectorDetailActivity;
import com.rejuvee.smartelectric.family.activity.energy.TimePriceActivity;
import com.rejuvee.smartelectric.family.activity.kefu.CustomerServiceActivity;
import com.rejuvee.smartelectric.family.activity.mine.AboutActivity;
import com.rejuvee.smartelectric.family.activity.mine.PerInfoActivity;
import com.rejuvee.smartelectric.family.activity.mine.SettingsActivity;
import com.rejuvee.smartelectric.family.activity.mine.ThridBindActivity;
import com.rejuvee.smartelectric.family.activity.scene.SceneActivity;
import com.rejuvee.smartelectric.family.adapter.CollectorBeanAdapter;
import com.rejuvee.smartelectric.family.adapter.SceneBeanAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.ActivityFragmentManager;
import com.rejuvee.smartelectric.family.common.AutoUpgrade;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.PermissionManage;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.utils.ValidateUtils;
import com.rejuvee.smartelectric.family.common.utils.WifiUtil;
import com.rejuvee.smartelectric.family.common.widget.MyBlurDrawerToggle;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTipWithoutOkCancel;
import com.rejuvee.smartelectric.family.databinding.ActivityMainNavigationBinding;
import com.rejuvee.smartelectric.family.databinding.NavHeaderMainNavigationBinding;
import com.rejuvee.smartelectric.family.model.bean.AutoUpgradeEventMessage;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.model.bean.WxSubscribed;
import com.rejuvee.smartelectric.family.model.viewmodel.MainNavigationViewModel;

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
    //    private Context mContext;
    //场景
//    private HorizontalListView listViewScene;
//    private List<SceneBean> listSceneBeanData = new ArrayList<>();
    private SceneBeanAdapter mSceneBeanAdapter;
    //    private HorizontalListSceneAdapter mSceneAdapter;
    //    private LinearLayout ll_add_scene;
    //集中器
//    private GridView gridViewDevice;
//    private List<CollectorBean> listDeviceData = new ArrayList<>();
    private CollectorBeanAdapter mCollectorBeanAdapter;
    //    private TextView tvCollectorCount;//集中器计数
    //用户
//    private CircleImageView ivHead;//头像view
    //    private CircleImageView ivHeadSmall;//头像view
//    private TextView tvNick;//昵称view
    //    private TextView tvUsername;//用户名view

//    private String nickname;
//    private String username;
//    private String telephone;
    //    private String headImgurl;
//    private String wechatUnionID;
//    private String qqUnionID;

    //    private NavigationView navigationView;
//    private DrawerLayout drawerLayout;
    private DaliBlurDrawerToggle drawerToggle;
//    private SwipeRefreshLayout refreshLayout;

    private ActivityMainNavigationBinding mainBinding;
    private NavHeaderMainNavigationBinding navigationBinding;
    private MainNavigationViewModel mViewModel;
    //    private Toolbar toolbar;
//    private ImageView ivUserQCode;
//    private PopwindowQCode popwindowQCode;

    @Override
    protected void initView() {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation);
        mViewModel = ViewModelProviders.of(this).get(MainNavigationViewModel.class);
//        mainBinding.setVm(mViewModel);
        mainBinding.setLifecycleOwner(this);

//        tvNick = mainBinding.navView.getHeaderView(0).findViewById(R.id.user_nickname);
//        ivHead = mainBinding.navView.getHeaderView(0).findViewById(R.id.user_headimg);
        navigationBinding = DataBindingUtil.bind(mainBinding.navView.getHeaderView(0));
        if (navigationBinding != null) {
            navigationBinding.setVm(mViewModel);
            navigationBinding.setPresenter(new Presenter());
            navigationBinding.setLifecycleOwner(this);
        }
//        mainBinding.navView.inflateHeaderView(R.layout.nav_header_main_navigation);
//        mainBinding.navView
        // toolbar header img
        mainBinding.include.setVm(mViewModel);
        mainBinding.include.setPresenter(new Presenter());
        mainBinding.include.include.setPresenter(new Presenter());
//        mainBinding.include.userHeadimgSmall.setOnClickListener(v -> mainBinding.drawerLayout.openDrawer(GravityCompat.START));
//        mainBinding.navView.getHeaderView(0).findViewById(R.id.user_edit).setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.putExtra("telephone", mViewModel.getTelephone().getValue());
//            intent.putExtra("nickname", mViewModel.getNickname().getValue());
//            intent.putExtra("username", mViewModel.getUsername().getValue());
//            intent.putExtra("headImgurl", mViewModel.getHeadImgUrl().getValue());
//            intent.setClass(getBaseContext(), PerInfoActivity.class);
//            startActivityForResult(intent, CommonRequestCode.REQUEST_USER_INFO);
//        });
        mainBinding.include.include.refreshlayout.setOnRefreshListener(this::getCollector);

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
        initScene();
        initCollector();
//        getScene();
//        getCollector();
        initToolBar();
        getDensity();
        AutoUpgrade.getInstacne(this).start();
        EventBus.getDefault().register(this);
    }

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

    /**
     * 检查公众号是否关注
     */
    private void testWechatPublic() {
        Core.instance(this).validateWechatPublic(new ActionCallbackListener<WxSubscribed>() {
            @Override
            public void onSuccess(WxSubscribed data) {
                if (data.getIsSubscribed() == 0) {
                    Snackbar.make(navigationBinding.getRoot(), R.string.vs225, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.vs248, v -> {
                                Intent intent = new Intent(MainNavigationActivity.this, ThridBindActivity.class);
                                intent.putExtra("wechatUnionID", mViewModel.getWechatUnionID().getValue());
                                intent.putExtra("qqUnionID", mViewModel.getQQUnionID().getValue());
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

    /**
     * 用户信息
     */
    public void getUserMsg() {
        Core.instance(this).getUserMsg(new ActionCallbackListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg data) {
//                telephone = data.getPhone();
                mViewModel.setTelephone(data.getPhone());
//                nickname = data.getNickName();
                mViewModel.setNickname(data.getNickName());
//                username = data.getUsername();
                mViewModel.setUsername(data.getUsername());
//                headImgurl = data.getHeadImg();
                mViewModel.setHeadImgUrl(data.getHeadImg());
//                wechatUnionID = data.getWechatUnionID();
                mViewModel.setWechatUnionID(data.getWechatUnionID());
//                qqUnionID = data.getQqUnionID();
                mViewModel.setQQUnionID(data.getQqUnionID());
//                tvNick.setText(nickname);
//                tvUsername.setText(username);
//                if (headImgurl.equals("")) {
//                    headImgurl = null;
//                    ivHead.setImageResource(R.drawable.icon_user_default);
//                    mainBinding.include.userHeadimgSmall.setImageResource(R.drawable.icon_user_default);
//                } else {
//                    if (!headImgurl.startsWith("https://")) {
//                        headImgurl = "https://" + headImgurl;
//                    }
//                    RequestCreator load = Picasso.with(getBaseContext()).load(headImgurl);
//                    setHeaderIcon(load);
//                }
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                ivHead.setImageResource(R.drawable.icon_user_default);
            }
        });
    }


    /**
     * 设置头像
     */
//    private void setHeaderIcon(RequestCreator load) {
//        load.into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                ivHead.setImageBitmap(bitmap);
//                mainBinding.include.userHeadimgSmall.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                ivHead.setImageResource(R.drawable.icon_user_default);
//                mainBinding.include.userHeadimgSmall.setImageResource(R.drawable.icon_user_default);
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        });
//    }

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
        setSupportActionBar(mainBinding.include.toolbar);
        setTitle("");
        // 模糊控件Drawer
        drawerToggle = new MyBlurDrawerToggle(this,
                mainBinding.drawerLayout,
                mainBinding.include.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close,
                new NavigationDrawerListener() {
                    @Override
                    public void onDrawerClosed(View view) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                });
        drawerToggle.setDrawerIndicatorEnabled(true);
        mainBinding.drawerLayout.addDrawerListener(drawerToggle);

        mainBinding.navView.setNavigationItemSelectedListener(this);

        // 隐藏的条目
//        FloatingActionButton fab = findViewById(R.id.fab);
//        mainBinding.include.fab.setOnClickListener(view ->
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("ActionFab", null).show());
    }

    private void initScene() {
        mSceneBeanAdapter = new SceneBeanAdapter(this, SceneBeanAdapter.ITEM_VIEW_TYPE_HORIZONTAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mainBinding.include.include.recyclerView1.setLayoutManager(linearLayoutManager);
        mainBinding.include.include.recyclerView1.setAdapter(mSceneBeanAdapter);
        mSceneBeanAdapter.setCallback(new SceneBeanAdapter.CallBack() {

            @Override
            public void onCollectorBeanClick(SceneBean bean) {
            }

            @Override
            public void onExecuteClick(SceneBean bean) {
                doDialog(bean);
            }

            @Override
            public void onRemove(SceneBean bean, int pos) {
            }
        });
    }

    private void getScene() {
        Core.instance(this).findSceneByUser(new ActionCallbackListener<List<SceneBean>>() {
            @Override
            public void onSuccess(List<SceneBean> data) {
//                listSceneBeanData.clear();
//                listSceneBeanData.addAll(data);
//                mSceneAdapter.notifyDataSetChanged();
                mSceneBeanAdapter.addAll(data);
//                updateRefreshState();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {//无数据
//                    listSceneBeanData.clear();
//                    mSceneAdapter.notifyDataSetChanged();
                }
//                updateRefreshState();
            }
        });
    }

    private void initCollector() {
        mCollectorBeanAdapter = new CollectorBeanAdapter(this);
        mainBinding.include.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.include.include.recyclerView.setAdapter(mCollectorBeanAdapter);
        mCollectorBeanAdapter.setCallback(bean -> {
            if (bean.getDeviceName() == null) {// 点击的是空项的添加按钮
                startActivityForResult(new Intent(MainNavigationActivity.this, AddDeviceOrSwitchActivity.class), CommonRequestCode.REQUEST_ADD_COLLECTOR);
            } else {
                Intent intent = new Intent(MainNavigationActivity.this, CollectorDetailActivity.class);
                intent.putExtra("collectorBean", bean);
                startActivity(intent);
            }
        });
    }

    private void getCollector() {
        Core.instance(this).getCollector(new ActionCallbackListener<List<CollectorBean>>() {
            @Override
            public void onSuccess(List<CollectorBean> data) {
                mainBinding.include.include.tvCollectorCount.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.vs13), data.size()));
//                listDeviceData.clear();
//                listDeviceData.addAll(data);
                data.add(new CollectorBean(Parcel.obtain()));// 加一个空项作为添加按钮
                mCollectorBeanAdapter.addAll(data);
//                mDeviceAdapter.notifyDataSetChanged();
//                updateRefreshState();
                mainBinding.include.include.refreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    mainBinding.include.include.tvCollectorCount.setText(String.format(Locale.getDefault(), "%s%d", getString(R.string.vs13), 0));
                    List<CollectorBean> list = new ArrayList<>();
                    list.add(new CollectorBean(Parcel.obtain()));// 加一个空项作为添加按钮
                    mCollectorBeanAdapter.addAll(list);
                }
//                updateRefreshState();
                mainBinding.include.include.refreshlayout.setRefreshing(false);
            }
        });
    }

    private void doDialog(SceneBean bean) {
        mDialogTip = new DialogTip(this);
        mDialogTip.setTitle(getString(R.string.exceu_sce));
        mDialogTip.setContent(String.format(getString(R.string.exceu_sce_issure), bean.getSceneName()));
        mDialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                mDialogTip.dismiss();
            }

            @Override
            public void onEnsure() {
                mDialogTip.dismiss();
                doExecute(bean.getSceneId());
            }
        });
        mDialogTip.show();
    }

    /**
     * 执行场景
     *
     * @param sceneId
     */
    private void doExecute(final String sceneId) {
        Core.instance(this).doExcuteScene(sceneId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(MainNavigationActivity.this, getString(R.string.scene_excute_sucess));
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(MainNavigationActivity.this, getString(R.string.zhixing_fail));
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDialogTip = new DialogTip(this);
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

    /**
     * 侧拉菜单
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.me_disanfang) {
            Intent intent = new Intent(this, ThridBindActivity.class);
            intent.putExtra("wechatUnionID", mViewModel.getWechatUnionID().getValue());
            intent.putExtra("qqUnionID", mViewModel.getQQUnionID().getValue());
            startActivityForResult(intent, CommonRequestCode.REQUEST_THIRD_BIND);
        } else if (id == R.id.me_jisuanqi) {
            startActivity(new Intent(this, TimePriceActivity.class));
        } else if (id == R.id.me_kefu) {
            Intent intent = new Intent(this, CustomerServiceActivity.class);
            intent.putExtra("username", mViewModel.getUsername().getValue());
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

    public class Presenter {
        public void onClickAddScene(View view) {
            startActivity(new Intent(view.getContext(), SceneActivity.class));
        }

        public void onClickHeadImg(View view) {
            mainBinding.drawerLayout.openDrawer(GravityCompat.START);
        }

        public void onEdit(View view) {
            Intent intent = new Intent();
            intent.putExtra("telephone", mViewModel.getTelephone().getValue());
            intent.putExtra("nickname", mViewModel.getNickname().getValue());
            intent.putExtra("username", mViewModel.getUsername().getValue());
            intent.putExtra("headImgurl", mViewModel.getHeadImgUrl().getValue());
            intent.setClass(MainNavigationActivity.this, PerInfoActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_USER_INFO);
        }

        public void onFab(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("ActionFab", null).show();
        }
    }

    @Override
    protected void dealloc() {
        AutoUpgrade.getInstacne(this).destroyInstance();
        EventBus.getDefault().unregister(this);
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
