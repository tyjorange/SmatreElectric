package com.rejuvee.smartelectric.family.activity.share;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListSetingItem;
import com.rejuvee.smartelectric.family.adapter.SetingAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.UserMsg;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.widget.dialog.WaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的分享
 */
public class ShareListActivity extends BaseActivity {
    private List<UserMsg> listShareUsers = new ArrayList<>();
    private List<ListSetingItem> mListData = new ArrayList<>();
    private UserMsg currentUser;
    private SetingAdapter setingAdapter;

    private DialogTip mDialogSwitchEnable;
    private WaitDialog waitDialog;
    private CollectorBean collectorBean;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_share_list;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        mDialogSwitchEnable = new DialogTip(this);
        collectorBean = getIntent().getParcelableExtra("collectorBean");
        ImageView img_cancel = findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.img_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDelIcon();
            }
        });
        ImageView img_add = findViewById(R.id.img_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareListActivity.this, AddShareMemberActivity.class);
                intent.putExtra("collect_id", collectorBean.getCollectorID());
                startActivity(intent);
            }
        });
        ListView listView = (ListView) findViewById(R.id.list_share_users);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        setingAdapter = new SetingAdapter(this, mListData);
        setingAdapter.setSetListener(new SetingAdapter.onSettingClickListener() {
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

        });

        listView.setAdapter(setingAdapter);
        listView.setEmptyView(findViewById(R.id.empty_layout));

        waitDialog = new WaitDialog(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRequest(false);
            }
        });
    }

    // 改变用户控制权限
    private void changeEnable(final int isEnable) {
        String title = isEnable == 1 ? getString(R.string.vs147) : getString(R.string.vs148);
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
                refreshLayout.setRefreshing(false);
                if (data != null && data.size() > 0) {
                    listShareUsers.clear();
                    listShareUsers.addAll(data);
                    resetData();
                }
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                refreshLayout.setRefreshing(false);
                waitDialog.dismiss();
            }
        });
    }

    private void resetData() {
        mListData.clear();
        if (listShareUsers != null && listShareUsers.size() > 0) {
            for (UserMsg userMsg : listShareUsers) {
                ListSetingItem setingItem = new ListSetingItem();
                setingItem.setContent(userMsg.getUsername());
                setingItem.setIsEnable(userMsg.getEnable());
                setingItem.setViewType(ListSetingItem.ITEM_VIEW_TYPE_DELETE);
                mListData.add(setingItem);
            }
        }
        setingAdapter.notifyDataSetChanged();
    }

    private void removeShare(final int position) {
        UserMsg userMsg = listShareUsers.get(position);
        waitDialog.show();
        Core.instance(this).shareCollector(false, userMsg.getUsername(), collectorBean.getCollectorID(), 0, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                mListData.remove(position);
                listShareUsers.remove(position);
                setingAdapter.notifyDataSetChanged();
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                waitDialog.dismiss();
                CustomToast.showCustomErrorToast(ShareListActivity.this, getString(R.string.op_fail));
            }
        });


    }

    @Override
    protected void initData() {
        //doRequest(true);
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
    private void toggleDelIcon() {
        for (ListSetingItem taskBean : mListData) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        setingAdapter.notifyDataSetChanged();
    }

    private void addShareUser(final String userName) {
        waitDialog.show();
        Core.instance(this).shareCollector(true, userName, collectorBean.getCollectorID(), 0, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                doRequest(false);
                CustomToast.showCustomToast(ShareListActivity.this, getString(R.string.operator_sucess));
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomErrorToast(ShareListActivity.this, getString(R.string.op_fail));
                waitDialog.dismiss();
            }
        });
    }
}
