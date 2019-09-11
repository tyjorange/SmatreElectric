package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
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
 * 线路修改
 */
public class SwitchModifyActivity extends BaseActivity {
    //    private SwitchBean switchBean;
    private CollectorBean collectorBean;
    private SwitchBean curBreaker;
    //    private float dianliang, gonglv;
    private TextView txtLineName;//线路名称
//    private ImageView imgLine;
    private EditText editLineName;
    //    private String collectId;
    private CustomLineAdapter customLineAdapter;
    //    private DialogTip dialogTip;
    private Context mContext;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_break_modify;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

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
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SuperTextView superTextView = findViewById(R.id.st_finish);
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectorBean != null) {//修改
                    modify();
                } else {//添加
                    add();
                }
            }
        });
        LinearLayout img_change = findViewById(R.id.img_change);
        if (collectorBean != null) {//修改
            img_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(mContext, SwitchTree.DINGSHI, collectorBean, new SwitchTreeDialog.ChoseCallBack() {

                        @Override
                        public void onChose(SwitchBean s) {
                            curBreaker = s;
//                        getData(switchBean);
                            txtLineName.setText("线路：" + curBreaker.getName());
                            editLineName.setText(curBreaker.getName());
                            customLineAdapter.reset();
                            customLineAdapter.setCurrentSelected(curBreaker.getIconType());
                        }
                    });
                    switchTreeDialog.show();
                }
            });
        } else {//添加
            img_change.setVisibility(View.GONE);
        }
//        txtCurDianliang = (TextView) findViewById(R.id.txt_dianliang);
//        txtCurGonglv = (TextView) findViewById(R.id.txt_gonglv);
        txtLineName = (TextView) findViewById(R.id.txt_line_name);
        editLineName = (EditText) findViewById(R.id.edit_line_name);
//        imgLine = (ImageView) findViewById(R.id.img_line);
        //    private TextView txtCurDianliang;//当前电量
        //    private TextView txtCurGonglv;//当前功率
        GridView gridView = (GridView) findViewById(R.id.grid_default_pic);
        customLineAdapter = new CustomLineAdapter(this);
        gridView.setAdapter(customLineAdapter);
//        mAdapter2 = new CustomLineAdapter(this);
//        mAdapter2.setShowName(false);
//        HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.horizontal_listview);
//        horizontalListView.setAdapter(mAdapter2);
//        horizontalListView.setDividerWidth(SizeUtils.dp2px(8));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                customLineAdapter.reset();
                customLineAdapter.setCurrentSelected(position);
            }
        });
        // 修改
        if (curBreaker != null) {
            editLineName.setText(curBreaker.getName());
            customLineAdapter.reset();
            customLineAdapter.setCurrentSelected(curBreaker.getIconType());
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
                curBreaker = data.get(0);//init bean
                txtLineName.setText("线路：" + curBreaker.getName());
                editLineName.setText(curBreaker.getName());
                customLineAdapter.reset();
                customLineAdapter.setCurrentSelected(curBreaker.getIconType());
//                getBreakSignalValue(curBreaker);
//                judgSwitchstate(curBreaker);
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                if (errorEvent == 12) {
                    CustomToast.showCustomErrorToast(SwitchModifyActivity.this, "请先添加线路");
                } else {
                    CustomToast.showCustomErrorToast(SwitchModifyActivity.this, message);
                }
                finish();
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
        curBreaker.setName(customName);
        curBreaker.setIconType(customLineAdapter.getSelectedPosition());
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
        Core.instance(this).updateBreak(curBreaker.getSwitchID(), curBreaker.getIconType(),
                curBreaker.getName(), new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Intent intent = getIntent();
                        intent.putExtra("break", curBreaker);
                        setResult(RESULT_OK, intent);
                        CustomToast.showCustomToast(mContext, "修改成功");
                        finish();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomErrorToast(SwitchModifyActivity.this, getString(R.string.operator_failure));
                    }
                });

    }

}
