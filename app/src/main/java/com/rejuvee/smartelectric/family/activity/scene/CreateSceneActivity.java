package com.rejuvee.smartelectric.family.activity.scene;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListViewLineSceneAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.constant.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.constant.NativeLine;
import com.rejuvee.smartelectric.family.common.widget.dialog.DialogTip;
import com.rejuvee.smartelectric.family.databinding.ActivityCreatesceneBinding;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.bean.SceneItemBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;
import com.rejuvee.smartelectric.family.model.viewmodel.CreateSceneViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Administrator on 2017/12/15.
 */
public class CreateSceneActivity extends BaseActivity {
    private static final String TAG = "CreateSceneActivity";
    //    private ListView list_line;
//    private EditText edit_name;
    //    private String scecount;
//    private ImageView choseimg;
    //    private String sceneid;
    private int scenimg;// 返回的iconType
    private SceneBean sceneBean;
    private ArrayList<SwitchInfoBean> listBreak = new ArrayList<>();
    private ArrayList<SwitchInfoBean> addBreakList = new ArrayList<>();
    private ArrayList<String> sceneswitchid = new ArrayList<>();
    private ListViewLineSceneAdapter listViewlineSceneAdapter;
    //    private TextView text_title;
    private int lineposition;
    private DialogTip dialogTip;
    //    private PopupWindow mPopWindow;
//    private String collectorID = "1";
//    private int sceneiconres;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_createscene;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    private ActivityCreatesceneBinding mBinding;
    private CreateSceneViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_createscene);
        mViewModel = ViewModelProviders.of(this).get(CreateSceneViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

//        setToolbarHide(true);
//        findViewById(R.id.add_xianlu).setOnClickListener(this);
//        findViewById(R.id.ll_choseImg).setOnClickListener(this);
//        findViewById(R.id.txt_cancel).setOnClickListener(this);
//        findViewById(R.id.st_wancheng).setOnClickListener(this);

//        edit_name = findViewById(R.id.edit_name);
//        list_line = findViewById(R.id.list_line);
//        choseimg = findViewById(R.id.choseimg);
//        text_title = (TextView) findViewById(R.id.text_title);

//        Intent intent = getIntent();
        sceneBean = getIntent().getParcelableExtra("scene");
//        sceneiconres = intent.getIntExtra("SceneIconRes", 11);
        listViewlineSceneAdapter = new ListViewLineSceneAdapter(CreateSceneActivity.this, listBreak);
        mBinding.listLine.setAdapter(listViewlineSceneAdapter);
        if (sceneBean != null) {
//            sceneid = sceneBean.getSceneId();
            findAllBreakByscene(sceneBean.getSceneId());
//            edit_name.setText(sceneBean.getSceneName());
            mViewModel.setSceneName(sceneBean.getSceneName());
            mBinding.choseimg.setImageResource(sceneBean.getSceneIconRes());
//            scenimg = sceneBean.getSceneIconRes();
//            text_title.setText(R.string.edit_scene);
        }

        dialogTip = new DialogTip(this);
        dialogTip.setTitle(getString(R.string.vs149));
        dialogTip.setContent(getString(R.string.vs150));
        dialogTip.setDialogListener(new DialogTip.onEnsureDialogListener() {
            @Override
            public void onCancel() {
                dialogTip.dismiss();
            }

            @Override
            public void onEnsure() {
                dialogTip.dismiss();
                if (sceneBean != null) {
                    sceneswitchid.add(listBreak.get(lineposition).getSwitchID());
                }
                listBreak.remove(lineposition);
                listViewlineSceneAdapter.notifyDataSetChanged();
            }
        });

        mBinding.listLine.setOnItemClickListener((parent, view, position, id) -> {
            lineposition = position;
            dialogTip.show();
        });
//        mPopWindow = new PopupWindow();
//         mBinding.listLine.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                mPopWindow.showAsDropDown(view, view.getWidth() / 2 - 30, -view.getHeight() / 4);
//                lineposition = position;
//                return true;
//            }
//        });
    }

    /**
     * {"scneID":"";"collectorID":"";"name":"";"iconType":"int";"switchs":[{"switchID":"";"cmdData:"int"}]}
     */
    private String getJsonContent(String scneID, String name, int iconType, ArrayList<SwitchInfoBean> listBreak) {
        try {
            JSONObject scenes = new JSONObject();
//            scenes.put("userkey", ValidateUtils.USER_KRY);
            scenes.put("sceneID", scneID == null ? "" : scneID);
            scenes.put("name", name);
            scenes.put("iconType", iconType);
            JSONArray switchs = new JSONArray();
            for (int i = 0; i < listBreak.size(); i++) {
                JSONObject jo = new JSONObject();
                jo.put("switchID", listBreak.get(i).getSwitchID());
                jo.put("cmdData", listBreak.get(i).getState());
                switchs.put(jo);
            }
            scenes.put("switchs", switchs);
            return scenes.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private String createname;

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add_xianlu://选择场景的线路
//                Intent intent = new Intent(CreateSceneActivity.this, ChoceLineActivity.class);
//                intent.putExtra("sceneid", sceneBean.getSceneId());
//                intent.putParcelableArrayListExtra("breaks", listBreak);
//                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
//                break;
//            case R.id.st_wancheng:
//                String _sceneName = mViewModel.getSceneName().getValue();//edit_name.getEditableText();
//                if (Objects.requireNonNull(_sceneName).length() == 0) {
//                    CustomToast.showCustomErrorToast(this, getString(R.string.sce_miaoshu));
//                    break;
//                }
//                String jsonContent;
//                if (sceneBean == null) {//新建场景
//                    jsonContent = getJsonContent(null, _sceneName, scenimg, listBreak);
//                    Log.d(TAG, "add scene=" + jsonContent);
//                    addScene1(jsonContent);
//                } else {//编辑场景
//                    jsonContent = getJsonContent(sceneBean.getSceneId(), _sceneName, scenimg, listBreak);
//                    Log.d(TAG, "edit scene=" + jsonContent);
//                    addScene1(jsonContent);
//                }
//                break;
//            case R.id.ll_choseImg://选择场景的图标
//                startActivityForResult(new Intent(this, ChoceSceneimgActivity.class), CommonRequestCode.REQUEST_SELECT_SCENE_IMG);
//                break;
//            case R.id.txt_cancel:
//                CreateSceneActivity.this.finish();
//                break;
//        }
//    }

    private void addScene1(String scene) {
        Core.instance(this).AddOrUpdateScene(scene, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                setResult(RESULT_OK);
                CreateSceneActivity.this.finish();
                Toast.makeText(CreateSceneActivity.this, R.string.operator_sucess, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Toast.makeText(CreateSceneActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /* public void removeCircuitBreak(String sceneSwitchID) {
         Core.instance(this).deleteSceneSwitch(sceneSwitchID, new ActionCallbackListener<Void>() {
             @Override
             public void onSuccess(Void data) {

                 findAllBreakByscene(sceneid);
                 Toast.makeText(CreateSceneActivity.this, "删除线路成功" , Toast.LENGTH_LONG).show();
             }

             @Override
             public void onFailure(int errorEvent, String message) {
                 Toast.makeText(CreateSceneActivity.this, "删除线路失败"+message , Toast.LENGTH_LONG).show();
             }
         });
     }*/
    private void findAllBreakByscene(final String sceneid) {
        Core.instance(this).findSceneSwitchByScene(sceneid, new ActionCallbackListener<List<SceneItemBean>>() {
            @Override
            public void onSuccess(List<SceneItemBean> data) {
                listBreak.clear();
                for (SceneItemBean itemBean : data) {
                    SwitchInfoBean switchInfoBean = itemBean.getSwitchs();
                    switchInfoBean.setState(itemBean.getCmdData());
                    listBreak.add(switchInfoBean);
                }
                listViewlineSceneAdapter = new ListViewLineSceneAdapter(CreateSceneActivity.this, listBreak);
                mBinding.listLine.setAdapter(listViewlineSceneAdapter);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CommonRequestCode.REQUEST_CHOSE_LINE:
                    addBreakList = data.getParcelableArrayListExtra("breaks");
//                    scecount = addBreakList.size() + "";
                    listBreak.addAll(Objects.requireNonNull(addBreakList));  //添加已选中的线路
                    listViewlineSceneAdapter.notifyDataSetChanged();
                    break;
                case CommonRequestCode.REQUEST_SELECT_SCENE_IMG:
                    scenimg = data.getIntExtra("scenimg", 0);
                    mBinding.choseimg.setImageResource(NativeLine.imageId[scenimg]);
                    break;
            }
        }
    }

    //    @Override
//    protected String getToolbarTitle() {
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return false;
//    }
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onAdd(View view) {
            Intent intent = new Intent(view.getContext(), ChoceLineActivity.class);
            intent.putExtra("sceneid", sceneBean.getSceneId());
            intent.putParcelableArrayListExtra("breaks", listBreak);
            startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
        }

        public void onChose(View view) {
            startActivityForResult(new Intent(view.getContext(), ChoceSceneimgActivity.class), CommonRequestCode.REQUEST_SELECT_SCENE_IMG);
        }

        public void onCommit(View view) {
            String _sceneName = mViewModel.getSceneName().getValue();//edit_name.getEditableText();
            if (_sceneName == null || _sceneName.length() == 0) {
                CustomToast.showCustomErrorToast(view.getContext(), getString(R.string.sce_miaoshu));
                return;
            }
            String jsonContent;
            if (sceneBean == null) {//新建场景
                jsonContent = getJsonContent(null, _sceneName, scenimg, listBreak);
                Log.d(TAG, "add scene=" + jsonContent);
                addScene1(jsonContent);
            } else {//编辑场景
                jsonContent = getJsonContent(sceneBean.getSceneId(), _sceneName, scenimg, listBreak);
                Log.d(TAG, "edit scene=" + jsonContent);
                addScene1(jsonContent);
            }
        }

    }

    @Override
    protected void dealloc() {

    }
}
