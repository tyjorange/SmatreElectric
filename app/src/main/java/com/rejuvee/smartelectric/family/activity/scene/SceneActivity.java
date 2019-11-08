package com.rejuvee.smartelectric.family.activity.scene;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListSceneAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */
public class SceneActivity extends BaseActivity implements View.OnClickListener, ListSceneAdapter.MyListener {
    private static final String TAG = "SceneActivity";
    private ListView list_scene;
    private ListSceneAdapter listSceneAdapter;
    private List<SceneBean> sceneBeanList = new ArrayList<>();
    private DialogTip dialogTip;
    private LoadingDlg mWaitDialog;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_scene;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        list_scene = findViewById(R.id.list_scene);
        findViewById(R.id.img_add).setOnClickListener(this);
        findViewById(R.id.img_cancel).setOnClickListener(this);
        findViewById(R.id.img_remove).setOnClickListener(this);
        listSceneAdapter = new ListSceneAdapter(SceneActivity.this, sceneBeanList, this);
        list_scene.setAdapter(listSceneAdapter);
        list_scene.setEmptyView(findViewById(R.id.empty_layout));
        list_scene.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(SceneActivity.this, CreateSceneActivity.class);
            intent.putExtra("scene", sceneBeanList.get(position));
            intent.putExtra("SceneIconRes", sceneBeanList.get(position).getSceneIconRes());
            Log.i(TAG, sceneBeanList.get(position).getSceneIconRes() + "=sceneid");
            startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_SCENE);
        });
        mWaitDialog = new LoadingDlg(this, -1);
    }

    @Override
    protected void initData() {
        NativeLine.init(this);
        getData();
    }

    public void getData() {
        mWaitDialog.show();
        Core.instance(this).findSceneByUser(new ActionCallbackListener<List<SceneBean>>() {
            @Override
            public void onSuccess(List<SceneBean> data) {
                sceneBeanList.clear();
                sceneBeanList.addAll(data);
                listSceneAdapter.notifyDataSetChanged();
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {//无数据
                    sceneBeanList.clear();
                    listSceneAdapter.notifyDataSetChanged();
                }
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(SceneActivity.this, message);
            }
        });
    }

    public void deleteScene(String sceneID) {
        mWaitDialog.show();
        Core.instance(this).deleteScene(sceneID, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(SceneActivity.this, R.string.deletescene_succe, Toast.LENGTH_LONG).show();
                getData();
//                MainPageNewFragment.NEED_REFRESH = true;
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Toast.makeText(SceneActivity.this, message, Toast.LENGTH_LONG).show();
                mWaitDialog.dismiss();
            }
        });
    }

    public void doScene(String sceneId) {
        mWaitDialog.show();
        Core.instance(this).doExcuteScene(sceneId, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                CustomToast.showCustomToast(SceneActivity.this, getString(R.string.zhixing_sucess));
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                CustomToast.showCustomToast(SceneActivity.this, getString(R.string.zhixing_fail));
                mWaitDialog.dismiss();
            }
        });
    }


    private void toggleDelIcon() {
        for (SceneBean taskBean : sceneBeanList) {
            if (taskBean.showDelIcon == View.GONE) {
                taskBean.showDelIcon = View.VISIBLE;
            } else {
                taskBean.showDelIcon = View.GONE;
            }
        }
        listSceneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelItem(String sceneId) {
        dialogTip = new DialogTip(this);
        dialogTip.setTitle(getString(R.string.deletescen));
        dialogTip.setContent(getResources().getString(R.string.sce_issure));
        dialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
                Log.i(TAG, sceneId + "=ID");
            }

            @Override
            public void onEnsure() {
                deleteScene(sceneId);
                dialogTip.dismiss();
            }
        });
        dialogTip.show();
    }

    @Override
    public void onExcuteItem(String sceneId, int position) {
        dialogTip = new DialogTip(this);
        dialogTip.setTitle(getString(R.string.execute));
        dialogTip.setContent(String.format(getResources().getString(R.string.exceu_sce_issure), sceneBeanList.get(position).getSceneName()));
        dialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
                Log.i(TAG, sceneId + "=ID");
            }

            @Override
            public void onEnsure() {
                doScene(sceneId);
                dialogTip.dismiss();
            }
        });
        dialogTip.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add:
                Intent intent = new Intent(this, CreateSceneActivity.class);
                startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_SCENE);
                break;
            case R.id.img_cancel:
                finish();
                break;
            case R.id.img_remove:
                toggleDelIcon();
                break;
        }
    }

    @Override
    protected void dealloc() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_ADD_SCENE || requestCode == CommonRequestCode.REQUEST_UPDATE_SCENE) {
                sceneBeanList.clear();
                getData();
            }
        }
    }
}
