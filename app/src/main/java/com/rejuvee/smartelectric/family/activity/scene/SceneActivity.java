package com.rejuvee.smartelectric.family.activity.scene;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.SceneBeanAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.common.widget.dialog.LoadingDlg;
import com.rejuvee.smartelectric.family.databinding.ActivitySceneBinding;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.viewmodel.SceneViewModel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */
public class SceneActivity extends BaseActivity {
    private static final String TAG = "SceneActivity";
    //    private ListView list_scene;
//    private ListSceneAdapter listSceneAdapter;
    //    private List<SceneBean> sceneBeanList = new ArrayList<>();
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
    private ActivitySceneBinding mBinding;
    private SceneBeanAdapter sceneBeanAdapter;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scene);
        SceneViewModel mViewModel = ViewModelProviders.of(this).get(SceneViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        mWaitDialog = new LoadingDlg(this, -1);
        initScene();
        getScene();
    }

    @Override
    protected void initData() {

    }

    private void initScene() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sceneBeanAdapter = new SceneBeanAdapter(this, SceneBeanAdapter.ITEM_VIEW_TYPE_VERTICAL);
        mBinding.recyclerView.setAdapter(sceneBeanAdapter);
        sceneBeanAdapter.setCallback(new SceneBeanAdapter.CallBack() {

            @Override
            public void onCollectorBeanClick(SceneBean bean) {
                Intent intent = new Intent(SceneActivity.this, CreateSceneActivity.class);
                intent.putExtra("scene", bean);
                intent.putExtra("SceneIconRes", bean.getSceneIconRes());
                Log.i(TAG, bean.getSceneIconRes() + "=sceneid=");
                startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_SCENE);
            }

            @Override
            public void onExecuteClick(SceneBean bean) {
                onExecuteItem(bean);
            }

            @Override
            public void onDelClick(SceneBean bean) {
                onDelItem(bean.getSceneId());
            }
        });

//        list_scene = findViewById(R.id.list_scene);
//        findViewById(R.id.img_add).setOnClickListener(this);
//        findViewById(R.id.img_cancel).setOnClickListener(this);
//        findViewById(R.id.img_remove).setOnClickListener(this);
//        listSceneAdapter = new ListSceneAdapter(SceneActivity.this, sceneBeanList, this);
//        list_scene.setAdapter(listSceneAdapter);
//        list_scene.setEmptyView(findViewById(R.id.empty_layout));
//        list_scene.setOnItemClickListener((parent, view, position, id) -> {
//            Intent intent = new Intent(SceneActivity.this, CreateSceneActivity.class);
//            intent.putExtra("scene", sceneBeanList.get(position));
//            intent.putExtra("SceneIconRes", sceneBeanList.get(position).getSceneIconRes());
//            Log.i(TAG, sceneBeanList.get(position).getSceneIconRes() + "=sceneid");
//            startActivityForResult(intent, CommonRequestCode.REQUEST_UPDATE_SCENE);
//        });
    }

    private void getScene() {
        mWaitDialog.show();
        Core.instance(this).findSceneByUser(new ActionCallbackListener<List<SceneBean>>() {
            @Override
            public void onSuccess(List<SceneBean> data) {
//                sceneBeanList.clear();
                sceneBeanAdapter.addAll(data);
//                listSceneAdapter.notifyDataSetChanged();
                mWaitDialog.dismiss();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {//无数据
//                    sceneBeanList.clear();
//                    listSceneAdapter.notifyDataSetChanged();
                }
                mWaitDialog.dismiss();
                CustomToast.showCustomErrorToast(SceneActivity.this, message);
            }
        });
    }

//    private void toggleDelIcon() {
//        for (SceneBean taskBean : sceneBeanList) {
//            if (taskBean.showDelIcon == View.GONE) {
//                taskBean.showDelIcon = View.VISIBLE;
//            } else {
//                taskBean.showDelIcon = View.GONE;
//            }
//        }
//        listSceneAdapter.notifyDataSetChanged();
//    }

    private void onDelItem(String sceneId) {
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

    private void deleteScene(String sceneID) {
        mWaitDialog.show();
        Core.instance(this).deleteScene(sceneID, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(SceneActivity.this, R.string.deletescene_succe, Toast.LENGTH_LONG).show();
                getScene();
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

    private void onExecuteItem(SceneBean bean) {
        dialogTip = new DialogTip(this);
        dialogTip.setTitle(getString(R.string.execute));
        dialogTip.setContent(String.format(getResources().getString(R.string.exceu_sce_issure), bean.getSceneName()));
        dialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
//                Log.i(TAG, sceneId + "=ID");
            }

            @Override
            public void onEnsure() {
                doScene(bean.getSceneId());
                dialogTip.dismiss();
            }
        });
        dialogTip.show();
    }

    private void doScene(String sceneId) {
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

    public class Presenter {
        public void onAdd(View view) {
            Intent intent = new Intent(view.getContext(), CreateSceneActivity.class);
            startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_SCENE);
        }

        public void onCancel(View view) {
            finish();
        }

        public void onRemove(View view) {
            sceneBeanAdapter.toggleDelIcon();
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_add:
//                Intent intent = new Intent(this, CreateSceneActivity.class);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_ADD_SCENE);
//                break;
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.img_remove:
//                toggleDelIcon();
//                break;
//        }
//    }

    @Override
    protected void dealloc() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CommonRequestCode.REQUEST_ADD_SCENE || requestCode == CommonRequestCode.REQUEST_UPDATE_SCENE) {
//                sceneBeanList.clear();
                getScene();
            }
        }
    }
}
