package com.rejuvee.smartelectric.family.activity.share;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListSetingItem;
import com.rejuvee.smartelectric.family.adapter.SettingAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.WaitDialog;
import com.rejuvee.smartelectric.family.databinding.ActivityShareListBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的分享
 */
public class ShareListActivity extends BaseActivity {
    private String TAG = "ShareListActivity";
    private List<UserMsg> listShareUsers = new ArrayList<>();// 分享的用户
    private List<ListSetingItem> mListItem = new ArrayList<>();// 转换后的items
    private UserMsg currentUser;
    private SettingAdapter mAdapter;

    private DialogTip mDialogSwitchEnable;
    private WaitDialog waitDialog;
    private CollectorBean collectorBean;

//    private SwipeRefreshLayout refreshLayout;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_share_list;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private ActivityShareListBinding mBinding;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_share_list);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);


        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        ImageView img_cancel = findViewById(R.id.img_cancel);
//        img_cancel.setOnClickListener(v -> finish());
//        findViewById(R.id.img_remove).setOnClickListener(v -> toggleDelIcon());
//        ImageView img_add = findViewById(R.id.img_add);
//        img_add.setOnClickListener(v -> {
//            Intent intent = new Intent(ShareListActivity.this, AddShareMemberActivity.class);
//            intent.putExtra("collect_id", collectorBean.getCollectorID());
//            startActivity(intent);
//        });
//        ListView listView = findViewById(R.id.list_share_users);
//        refreshLayout = findViewById(R.id.refreshlayout);
        mBinding.refreshlayout.setOnRefreshListener(this::onRefresh);
        mAdapter = new SettingAdapter(this);
        mAdapter.setSetListener(new SettingAdapter.onSettingClickListener() {
            @Override
            public void onRemove(final int position) {
                new DialogTip(ShareListActivity.this).setTitle(getString(R.string.delete))
                        .setContent(getString(R.string.cancel_share))
                        .setDialogListener(new DialogTip.onEnsureDialogListener() {

                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onEnsure() {
                                removeShare(position);
                            }
                        }).show();

            }

            @Override
            public void onSwitch(int position, int isEnable) {
                currentUser = listShareUsers.get(position);
                changeEnable(isEnable);
            }

            @Override
            public void onBeanClick(int position) {
                //nothing
            }

        });
        mBinding.listShareUsers.setLayoutManager(new LinearLayoutManager(this));
        mBinding.listShareUsers.setAdapter(mAdapter);
//        mBinding.listShareUsers.setEmptyView(mBinding.emptyLayout);
        mDialogSwitchEnable = new DialogTip(this);
        waitDialog = new WaitDialog(this);
    }

    /**
     * 改变用户控制权限
     *
     * @param isEnable
     */
    private void changeEnable(final int isEnable) {
        String title = (isEnable == 1) ? getString(R.string.vs147) : getString(R.string.vs148);
        mDialogSwitchEnable.setTitle(title);
        mDialogSwitchEnable.setContent(currentUser.getUsername());
        mDialogSwitchEnable.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onEnsure() {
                waitDialog.show();
                Core.instance(ShareListActivity.this).updateShareCollector(currentUser.getCollectorShareID(), isEnable, new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        CustomToast.showCustomToast(ShareListActivity.this, getString(R.string.operator_sucess));
                        doRequest(false);
                        waitDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomToast(ShareListActivity.this, message);
                        waitDialog.dismiss();
                    }
                });
            }
        });
        mDialogSwitchEnable.show();
    }

    /**
     * 获取分享列表
     *
     * @param showLoading
     */
    private void doRequest(boolean showLoading) {
        if (showLoading) {
            waitDialog.show();
        }
        Core.instance(this).getShareList(collectorBean.getCollectorID(), new ActionCallbackListener<List<UserMsg>>() {
            @Override
            public void onSuccess(List<UserMsg> data) {
                if (data != null && data.size() > 0) {
                    listShareUsers.clear();
                    listShareUsers.addAll(data);
                    setDataType();
                }
                waitDialog.dismiss();
                mBinding.refreshlayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
                waitDialog.dismiss();
                mBinding.refreshlayout.setRefreshing(false);
            }
        });
    }

    private void setDataType() {
        mListItem.clear();
        for (UserMsg userMsg : listShareUsers) {
            ListSetingItem setingItem = new ListSetingItem();
            setingItem.setContent(userMsg.getUsername());
            setingItem.setIsEnable(userMsg.getEnable());
            setingItem.setViewType(ListSetingItem.ITEM_VIEW_TYPE_DELETE);
            mListItem.add(setingItem);
        }
        mAdapter.addAll(mListItem);
//        mAdapter.notifyDataSetChanged();
    }

    /**
     * 取消分享
     *
     * @param position
     */
    private void removeShare(final int position) {
        UserMsg userMsg = listShareUsers.get(position);
        waitDialog.show();
        Core.instance(this).shareCollector(false, userMsg.getUsername(), collectorBean.getCollectorID(), 0, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                mListItem.remove(position);
                listShareUsers.remove(position);
                mAdapter.remove(position);
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                CustomToast.showCustomErrorToast(ShareListActivity.this, message);
            }
        });


    }

    private void onRefresh() {
        doRequest(false);
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return getString(R.string.shared_user);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onToggle(View view) {
            mAdapter.toggleDelIcon();
        }

        public void onAdd(View view) {
            Intent intent = new Intent(view.getContext(), AddShareMemberActivity.class);
            intent.putExtra("collect_id", collectorBean.getCollectorID());
            startActivity(intent);
        }

    }

    @Override
    protected void dealloc() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        doRequest(true);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_right_add, menu);
//        MenuItem menuFinishItem = menu.findItem(R.id.menu_add);
//        menuFinishItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(ShareListActivity.this, AddShareMemberActivity.class);
//                intent.putExtra("collect_id", collectorBean.getCollectorID());
//                startActivity(intent);
//                return false;
//            }
//        });
//        return true;
//    }


//    private void addShareUser(final String userName) {
//        waitDialog.show();
//        Core.instance(this).shareCollector(true, userName, collectorBean.getCollectorID(), 0, new ActionCallbackListener<Void>() {
//            @Override
//            public void onSuccess(Void data) {
//                doRequest(false);
//                CustomToast.showCustomToast(ShareListActivity.this, getString(R.string.operator_sucess));
//                waitDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(int errorEvent, String message) {
//                CustomToast.showCustomErrorToast(ShareListActivity.this, message);
//                waitDialog.dismiss();
//            }
//        });
//    }
}
