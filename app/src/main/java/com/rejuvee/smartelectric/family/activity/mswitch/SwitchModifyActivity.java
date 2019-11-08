package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.base.library.widget.SuperTextView;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.CustomLineAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

import java.util.List;

/**
 * 线路修改 （名称图片）
 */
public class SwitchModifyActivity extends BaseActivity {
    //    private SwitchBean switchBean;
    private CollectorBean collectorBean;
    private SwitchBean currentSwitchBean;
    //    private float dianliang, gonglv;
    private TextView txtLineName;//线路名称
//    private ImageView imgLine;
    private EditText editLineName;
    //    private String collectId;
    private CustomLineAdapter customLineAdapter;
    //    private DialogTip dialogTip;
    private Context mContext;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_break_modify;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

//    public static void startModifyBreak(Activity activity, SwitchBean breakerInfoBean) {
//        Intent intent = new Intent(activity, SwitchModifyActivity.class);
//        intent.putExtra("break", breakerInfoBean);
//        activity.startActivityForResult(intent, CommonRequestCode.REQUEST_MODIFY_LINE);
//    }

    @Override
    protected void initView() {
        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        switchBean = getIntent().getParcelableExtra("switchBean");
//        dianliang = getIntent().getFloatExtra("dianliang", 0);
//        gonglv = getIntent().getFloatExtra("gonglv", 0);
//        if (switchBean == null) {
//            return;
//        }
        ImageView img_cancel = findViewById(R.id.img_cancel);
        img_cancel.setOnClickListener(v -> finish());
        SuperTextView superTextView = findViewById(R.id.st_finish);
        superTextView.setOnClickListener(v -> {
            if (collectorBean != null) {//修改
                modify();
            } else {//添加
                add();
            }
        });
        // dialog切换线路
        LinearLayout img_change = findViewById(R.id.img_change);
        if (collectorBean != null) {//修改
            img_change.setOnClickListener(v -> {
                SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(mContext, SwitchTree.DINGSHI, collectorBean, s -> {
                    currentSwitchBean = s;
//                        getData(switchBean);
                    txtLineName.setText(String.format("%s%s", mContext.getString(R.string.vs14), currentSwitchBean.getName()));
                    editLineName.setText(currentSwitchBean.getName());
                    customLineAdapter.reset();
                    customLineAdapter.setCurrentSelected(currentSwitchBean.getIconType());
                });
                switchTreeDialog.show();
            });
            findViewById(R.id.type_rename_switch).setVisibility(View.VISIBLE);
            findViewById(R.id.type_add_switch).setVisibility(View.GONE);
        } else {//添加
            img_change.setVisibility(View.GONE);
            findViewById(R.id.type_rename_switch).setVisibility(View.GONE);
            findViewById(R.id.type_add_switch).setVisibility(View.VISIBLE);
        }
//        txtCurDianliang = (TextView) findViewById(R.id.txt_dianliang);
//        txtCurGonglv = (TextView) findViewById(R.id.txt_gonglv);
        txtLineName = findViewById(R.id.txt_line_name);
        editLineName = findViewById(R.id.edit_line_name);
//        imgLine = (ImageView) findViewById(R.id.img_line);
        //    private TextView txtCurDianliang;//当前电量
        //    private TextView txtCurGonglv;//当前功率
        GridView gridView = findViewById(R.id.grid_default_pic);
        customLineAdapter = new CustomLineAdapter(this);
        gridView.setAdapter(customLineAdapter);
//        mAdapter2 = new CustomLineAdapter(this);
//        mAdapter2.setShowName(false);
//        HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.horizontal_listview);
//        horizontalListView.setAdapter(mAdapter2);
//        horizontalListView.setDividerWidth(SizeUtils.dp2px(8));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            customLineAdapter.reset();
            CustomLineAdapter.Item item = customLineAdapter.setCurrentSelected(position);
            editLineName.setText(item.name);
        });
        // 修改
        if (currentSwitchBean != null) {
            editLineName.setText(currentSwitchBean.getName());
            customLineAdapter.reset();
            customLineAdapter.setCurrentSelected(currentSwitchBean.getIconType());
        }
//        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mAdapter2.setCurrentSelected(position);
//            }
//        });
    }

    @Override
    protected void initData() {
        if (collectorBean != null) {
            getSwitchByCollector();
        }
//        if (switchBean == null) {
//            return;
//        }
//        txtLineName.setText("线路：" + curBreaker.getName());
//        imgLine.setImageResource(switchBean.getIcon());

//        String strDianliang = String.format(getResources().getString(R.string.cur_dianliang),
//                "<font color='#0d77ff'>",
//                dianliang,
//                "</font>");
//        txtCurDianliang.setText(Html.fromHtml(strDianliang));

//        String strGonglv = String.format(getResources().getString(R.string.cur_gonglv),
//                "<font color='#0d77ff'>",
//                gonglv,
//                "</font>");
//        txtCurGonglv.setText(Html.fromHtml(strGonglv));

//        collectId = getIntent().getStringExtra("collect_id");
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                currentSwitchBean = data.get(0);//init bean
                txtLineName.setText(String.format("%s%s", mContext.getString(R.string.vs14), currentSwitchBean.getName()));
                editLineName.setText(currentSwitchBean.getName());
                customLineAdapter.reset();
                customLineAdapter.setCurrentSelected(currentSwitchBean.getIconType());
//                getBreakSignalValue(curBreaker);
//                judgSwitchstate(curBreaker);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(SwitchModifyActivity.this, getString(R.string.vs29));
                } else {
                    CustomToast.showCustomErrorToast(SwitchModifyActivity.this, message);
                }
//                finish();
                findViewById(R.id.csv).setVisibility(View.INVISIBLE);
                findViewById(R.id.st_finish).setVisibility(View.INVISIBLE);
            }
        });
    }
//    @Override
//    protected String getToolbarTitle() {
//        return getResources().getString(R.string.line_modify);
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return true;
//    }

    @Override
    protected void dealloc() {

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_finish, menu);
//        MenuItem menuFinishItem = menu.findItem(R.id.menu_item_finish);
//        menuFinishItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                modify();
//                return false;
//            }
//        });
//        return true;
//    }
    private void add() {
        Intent intent = getIntent();
        String customName = editLineName.getEditableText().toString();
        intent.putExtra("name", customName);
        intent.putExtra("icontype", customLineAdapter.getSelectedPosition());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void modify() {
        String customName = editLineName.getEditableText().toString();
//        CustomLineAdapter.Line selected = mAdapter2.getSelected();
        if (customName.isEmpty()) {
            CustomToast.showCustomErrorToast(this, getResources().getString(R.string.input_custom_name));
            return;
        }
//        if (selected == null) {
//            CustomToast.showCustomErrorToast(this, getResources().getString(R.string.select_custom_pic));
//            return;
//        }
        currentSwitchBean.setName(customName);
        currentSwitchBean.setIconType(customLineAdapter.getSelectedPosition());
        finishModify();
    }

    private void finishModify() {
       /* List<SwitchBean> listBreaks = AppGlobalConfig.instance().getBreaks(collectId);
        if (listBreaks != null) {
            for (SwitchBean breaker : listBreaks) {
                if (breaker.getAlias().compareTo(switchBean.getAlias()) == 0) {
                    CustomToast.showCustomErrorToast(this, getResources().getString(R.string.duplicated_line_name));
                    return;
                }
            }
        }*/
        Core.instance(this).updateBreak(currentSwitchBean.getSwitchID(), currentSwitchBean.getIconType(),
                currentSwitchBean.getName(), new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Intent intent = getIntent();
                        intent.putExtra("break", currentSwitchBean);
                        setResult(RESULT_OK, intent);
                        CustomToast.showCustomToast(mContext, getString(R.string.vs139));
                        finish();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomErrorToast(SwitchModifyActivity.this, message);
                    }
                });

    }

}
