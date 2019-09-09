package com.rejuvee.smartelectric.family;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.utils.PermissionUtils;
import com.base.library.utils.SizeUtils;
import com.base.library.widget.CustomToast;
import com.base.library.widget.HorizontalListView;
import com.rejuvee.smartelectric.family.activity.AddDeviceActivity;
import com.rejuvee.smartelectric.family.activity.collector.CollectorDetailActivity;
import com.rejuvee.smartelectric.family.activity.energy.CostCalculationActivity;
import com.rejuvee.smartelectric.family.activity.mine.PerInfoActivity;
import com.rejuvee.smartelectric.family.activity.mine.SettingsActivity;
import com.rejuvee.smartelectric.family.activity.mine.ThridBindActivity;
import com.rejuvee.smartelectric.family.activity.scene.SceneActivity;
import com.rejuvee.smartelectric.family.adapter.GridDeviceAdapter;
import com.rejuvee.smartelectric.family.adapter.HorizontalListSceneAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.AutoUpgrade;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.PermisionManage;
import com.rejuvee.smartelectric.family.model.bean.AutoUpgradeEventMessage;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.utils.utils;
import com.rejuvee.smartelectric.family.widget.CircleImageView;
import com.rejuvee.smartelectric.family.widget.DialogTip;
import com.rejuvee.smartelectric.family.widget.PopwindowQCode;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.dali.builder.nav.DaliBlurDrawerToggle;
import at.favre.lib.dali.builder.nav.NavigationDrawerListener;

public class MainNavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        GridDeviceAdapter.MyListener {
    private String TAG = "MainNavigationActivity";
    private DialogTip mDialogTip;
    private Context mContext;
    //场景
    private HorizontalListView listViewScene;
    private List<SceneBean> listSceneBeanData = new ArrayList<>();
    private HorizontalListSceneAdapter mSceneAdapter;
    private LinearLayout ll_add_scene;
    //集中器
    private GridView gridViewDevice;
    private List<CollectorBean> listDeviceData = new ArrayList<>();
    private GridDeviceAdapter mDeviceAdapter;
    private TextView tv_collector_count;//集中器计数
    //用户
    private CircleImageView ivHead;//头像view
    private CircleImageView ivHeadSmall;//头像view
    private TextView tvNick;//昵称view
    //    private TextView tvUsername;//用户名view
    private String nickname;
    private String username;
    private String telephone;
    private String headImgurl;
    private String wechatUnionID;
    private String qqUnionID;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    //    private Toolbar toolbar;
//    private ImageView ivUserQCode;
//    private PopwindowQCode popwindowQCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main_navigation;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mContext = this;
        navigationView = findViewById(R.id.nav_view);
        tv_collector_count = findViewById(R.id.tv_collector_count);
        tvNick = navigationView.getHeaderView(0).findViewById(R.id.user_nickname);
        ivHead = navigationView.getHeaderView(0).findViewById(R.id.user_headimg);
//        ivHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popwindowQCode.show();
//            }
//        });
        ivHeadSmall = findViewById(R.id.user_headimg_small);
        ivHeadSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
//        ivUserQCode = (ImageView) findViewById(R.id.iv_scan_code);
        navigationView.getHeaderView(0).findViewById(R.id.user_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("telephone", telephone);
                intent.putExtra("nickname", nickname);
                intent.putExtra("username", username);
                intent.putExtra("headImgurl", headImgurl);
                intent.setClass(mContext, PerInfoActivity.class);
                startActivityForResult(intent, CommonRequestCode.REQUEST_USER_INFO);
            }
        });
        ll_add_scene = findViewById(R.id.ll_add_scene);
        initScene();
        initCollector();
//        popwindowQCode = new PopwindowQCode(mContext);
    }

    @Override
    protected void initData() {
        getUserMsg();
        getScene();
        getCollector();
        initToolBar();
        AutoUpgrade.getInstacne(this).start();
        EventBus.getDefault().register(this);
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
                    ivHeadSmall.setImageResource(R.drawable.icon_user_default);
//                    toolbar.setNavigationIcon(new CircleDrawable(BitmapUtil.drawable2Bitmap(ivHead.getDrawable())));
                } else {
                    if (!headImgurl.startsWith("https://")) {
                        headImgurl = "https://" + headImgurl;
                    }
                    Picasso.with(mContext).load(headImgurl).into(ivHead);
                    Picasso.with(mContext).load(headImgurl).into(ivHeadSmall);
//                    setSmallNavigationIcon();
                }
//                setQCodeUserName();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                ivHead.setImageResource(R.drawable.icon_user_default);
            }
        });
    }


    /**
     * 小头像
     */
    @Deprecated
    private void setSmallNavigationIcon() {
        RequestCreator load = Picasso.with(mContext).load(headImgurl);
        load.into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                toolbar.setNavigationIcon(new CircleDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                ivHead.setImageResource(R.drawable.icon_user_default);
//                toolbar.setNavigationIcon(new CircleDrawable(BitmapUtil.drawable2Bitmap(ivHead.getDrawable())));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                toolbar.setNavigationIcon(new CircleDrawable(BitmapUtil.drawable2Bitmap(placeHolderDrawable)));
            }
        });
    }

    /**
     * 生成用户名二维码
     */
    private void setQCodeUserName() {
        Bitmap bmpUser = utils.createQRcodeImage(username, SizeUtils.dp2px(100), SizeUtils.dp2px(100));
        if (bmpUser != null) {
//            ivUserQCode.setImageBitmap(bmpUser);
//            popwindowQCode.setQCodeImageBitmap(bmpUser);
        }
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        CircleImageView civ = (CircleImageView) findViewById(R.id.user_headimg_s);
//        toolbar.setNavigationIcon(new CircleDrawable(BitmapUtil.drawable2Bitmap(ivHead.getDrawable())));
        DaliBlurDrawerToggle drawerToggle = new DaliBlurDrawerToggle(this, drawer, toolbar,
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
        drawer.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();
        drawerToggle.setDrawerIndicatorEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initScene() {
        listViewScene = findViewById(R.id.main_scene);
        listViewScene.setDividerWidth(SizeUtils.dp2px(45));//图标间隔
        mSceneAdapter = new HorizontalListSceneAdapter(this, listSceneBeanData);
        listViewScene.setAdapter(mSceneAdapter);
        listViewScene.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
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
            }
        });

        ll_add_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SceneActivity.class));
            }
        });
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

    private void initCollector() {
        gridViewDevice = findViewById(R.id.grid_device);
        mDeviceAdapter = new GridDeviceAdapter(listDeviceData, mContext, this);
        gridViewDevice.setAdapter(mDeviceAdapter);
//        gridViewDevice.setEmptyView(findViewById(R.id.empty_layout));
        gridViewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mDeviceAdapter.isEditMode()) {
                    return;
                }
                if (position == listDeviceData.size()) {
                    startActivityForResult(new Intent(mContext, AddDeviceActivity.class), CommonRequestCode.REQUEST_ADD_COLLECTOR);
                } else {
                    CollectorBean collectorBean = listDeviceData.get(position);
                    Intent intent = new Intent(MainNavigationActivity.this, CollectorDetailActivity.class);
                    intent.putExtra("collectorBean", collectorBean);
                    startActivity(intent);
                }
            }
        });
        //长按删除
        gridViewDevice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == listDeviceData.size()) {
                    return true;
                }
                if (mDeviceAdapter.isEditMode()) {
                    mDeviceAdapter.setEditMode(false);
                } else {
                    mDeviceAdapter.setEditMode(true);
                }
                Vibrator vibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
                return true;
            }
        });
//        findViewById(R.id.txt_add_device).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(mContext, AddDeviceActivity.class), requestCode_add_device);
//            }
//        });
    }

    private void getCollector() {
        Core.instance(this).getCollector(new ActionCallbackListener<List<CollectorBean>>() {
            @Override
            public void onSuccess(List<CollectorBean> data) {
                tv_collector_count.setText("总电箱数量：" + data.size());
                listDeviceData.clear();
                listDeviceData.addAll(data);
                mDeviceAdapter.notifyDataSetChanged();
//                updateRefreshState();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
//                updateRefreshState();
            }
        });
    }

    /**
     * 执行场景
     *
     * @param sceneid
     */
    public void doExecute(final String sceneid) {
        Core.instance(this).doExcuteScene(sceneid, new ActionCallbackListener<Void>() {
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

    @Override
    public void onDel(int position) {
        DialogTip dialogTip = new DialogTip(mContext);
        dialogTip.setTitle(mContext.getResources().getString(R.string.delete))
                .setContent(mContext.getResources().getString(R.string.delete_device))
                .setDialogListener(new DialogTip.onEnsureDialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onEnsure() {
                        CollectorBean collectorBean = listDeviceData.get(position);
                        Core.instance(mContext).unbindDevice(collectorBean.getCode(), new ActionCallbackListener<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                listDeviceData.remove(position);
                                mDeviceAdapter.notifyDataSetChanged();
                                mDeviceAdapter.setEditMode(false);
                                tv_collector_count.setText("总电箱数量：" + listDeviceData.size());
                                CustomToast.showCustomToast(mContext, "解除绑定成功");
                            }

                            @Override
                            public void onFailure(int errorEvent, String message) {
                                CustomToast.showCustomErrorToast(mContext, message);
                            }
                        });

                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mDeviceAdapter.isEditMode()) {
            mDeviceAdapter.setEditMode(false);
        } else {
            mDialogTip = new DialogTip(mContext);
            mDialogTip.setTitle("退出");
            mDialogTip.setContent("是否退出电母?");
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
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            startActivity(new Intent(this, CostCalculationActivity.class));
        } else if (id == R.id.me_kefu) {

        } else if (id == R.id.me_wenti) {

        } else if (id == R.id.me_guanyu) {

        } else if (id == R.id.me_shezhi) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        //收起侧边栏
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void autoUpgrade(final AutoUpgradeEventMessage autoUpgrade) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermisionManage.getInstance().startRequest(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, new PermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    AutoUpgrade.getInstacne(MainNavigationActivity.this).installApkNew(autoUpgrade.upGradeUri);
                }

                @Override
                public void onPermissionDenied(String[] deniedPermissions) {
                    Log.e(TAG, "onPermissionDenied");
                }
            });
        } else {
            AutoUpgrade.getInstacne(MainNavigationActivity.this).installApkNew(autoUpgrade.upGradeUri);
        }
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
