package com.rejuvee.smartelectric.family.activity.scene;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.ListViewLineSceneAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.common.CommonRequestCode;
import com.rejuvee.smartelectric.family.common.NativeLine;
import com.rejuvee.smartelectric.family.model.bean.SceneBean;
import com.rejuvee.smartelectric.family.model.bean.SceneItemBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;
import com.rejuvee.smartelectric.family.widget.dialog.DialogTip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/12/15.
 */
public class CreateSceneActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CreateSceneActivity";
    private ListView list_line;
    private EditText edit_name;
    //    private String scecount;
    private ImageView choseimg;
    private int scenimg;
    private String sceneid;
    private SceneBean mSscene;
    private ArrayList<SwitchInfoBean> listBreak = new ArrayList<>();
    private ArrayList<SwitchInfoBean> addBreakList = new ArrayList<>();
    private ListViewLineSceneAdapter listViewlineSceneAdapter;
    private TextView text_title;
    private int lineposition;
    private DialogTip dialogTip;
    //    private PopupWindow mPopWindow;
//    private String collectorID = "1";
//    private int sceneiconres;
    private ArrayList<String> Sceneswitchid = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_createscene;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
//        setToolbarHide(true);
        findViewById(R.id.add_xianlu).setOnClickListener(this);
        findViewById(R.id.re_addline).setOnClickListener(this);
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        findViewById(R.id.st_wancheng).setOnClickListener(this);

        edit_name = (EditText) findViewById(R.id.edit_name);
        list_line = (ListView) findViewById(R.id.list_line);
        choseimg = (ImageView) findViewById(R.id.choseimg);
//        text_title = (TextView) findViewById(R.id.text_title);

        Intent intent = getIntent();
        mSscene = intent.getParcelableExtra("scene");
//        sceneiconres = intent.getIntExtra("SceneIconRes", 11);
        listViewlineSceneAdapter = new ListViewLineSceneAdapter(CreateSceneActivity.this, listBreak, sceneid);
        list_line.setAdapter(listViewlineSceneAdapter);
        if (mSscene != null) {
            sceneid = mSscene.getSceneId();
            findAllBreakByscene(sceneid);
            edit_name.setText(mSscene.getSceneName());
            choseimg.setImageResource(NativeLine.imageId[mSscene.getSceneIconRes()]);
            scenimg = mSscene.getSceneIconRes();
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
                if (mSscene != null) {
                    Sceneswitchid.add(listBreak.get(lineposition).getSwitchID());
                }
                listBreak.remove(lineposition);
                listViewlineSceneAdapter.notifyDataSetChanged();
            }
        });

        list_line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lineposition = position;
                dialogTip.show();
            }
        });
//        mPopWindow = new PopupWindow();
//        list_line.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                mPopWindow.showAsDropDown(view, view.getWidth() / 2 - 30, -view.getHeight() / 4);
//                lineposition = position;
//                return true;
//            }
//        });
    }

    @Override
    protected void initData() {

    }

    /**
     * {"scneID":"";"collectorID":"";"name":"";"iconType":"int";"switchs":[{"switchID":"";"cmdData:"int"}]}
     */
    private String getJsonContent(String scneID, String name, int iconType, ArrayList<SwitchInfoBean> listBreak) {
        try {
            JSONObject scenes = new JSONObject();
//            scenes.put("userkey", utils.USER_KRY);
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

    private String createname;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_xianlu://选择场景的线路
                Intent intent = new Intent(CreateSceneActivity.this, ChoceLineActivity.class);
                intent.putExtra("sceneid", sceneid);
                intent.putParcelableArrayListExtra("breaks", listBreak);
                startActivityForResult(intent, CommonRequestCode.REQUEST_CHOSE_LINE);
                break;
            case R.id.st_wancheng:
                Editable text = edit_name.getEditableText();
                if (text.length() == 0) {
                    CustomToast.showCustomErrorToast(this, getString(R.string.sce_miaoshu));
                    break;
                }
                if (mSscene == null) {//新建场景
                    String jsonContent = getJsonContent(null, text.toString(), scenimg, listBreak);
                    Log.d(TAG, "add scene=" + jsonContent);
                    addScene1(jsonContent);
                } else {//编辑场景
                    String jsonContent = getJsonContent(sceneid, text.toString(), scenimg, listBreak);
                    addScene1(jsonContent);
                }
                break;
            case R.id.re_addline://选择场景的图标
                startActivityForResult(new Intent(this, ChoceSceneimgActivity.class), CommonRequestCode.REQUEST_SELECT_SCENE_IMG);
                break;
            case R.id.txt_cancel:
                CreateSceneActivity.this.finish();
                break;
        }
    }

    public void addScene1(String scene) {
        Core.instance(this).AddOrUpdateScene(scene, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                setResult(RESULT_OK);
                CreateSceneActivity.this.finish();
                Toast.makeText(CreateSceneActivity.this, R.string.operator_sucess, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Toast.makeText(CreateSceneActivity.this, R.string.operator_failure + message, Toast.LENGTH_LONG).show();
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
    public void findAllBreakByscene(final String sceneid) {
        Core.instance(this).findSceneSwitchByScene(sceneid, new ActionCallbackListener<List<SceneItemBean>>() {
            @Override
            public void onSuccess(List<SceneItemBean> data) {
                listBreak.clear();
                for (SceneItemBean itemBean : data) {
                    SwitchInfoBean switchInfoBean = itemBean.getSwitchs();
                    switchInfoBean.setState(itemBean.getCmdData());
                    listBreak.add(switchInfoBean);
                }
                listViewlineSceneAdapter = new ListViewLineSceneAdapter(CreateSceneActivity.this, listBreak, sceneid);
                list_line.setAdapter(listViewlineSceneAdapter);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CommonRequestCode.REQUEST_CHOSE_LINE:
                    addBreakList = data.getParcelableArrayListExtra("breaks");
//                    scecount = addBreakList.size() + "";
                    listBreak.addAll(addBreakList);  //添加已选中的线路
                    listViewlineSceneAdapter.notifyDataSetChanged();
                    break;
                case CommonRequestCode.REQUEST_SELECT_SCENE_IMG:
                    scenimg = data.getIntExtra("scenimg", 11);
                    choseimg.setImageResource(NativeLine.imageId[scenimg]);
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

    @Override
    protected void dealloc() {

    }
}
