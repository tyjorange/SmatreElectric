package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.base.frame.net.ActionCallbackListener;
import com.base.library.widget.CustomToast;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.CustomLineAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityBreakModifyBinding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;
import com.rejuvee.smartelectric.family.model.viewmodel.SwitchModifyViewModel;

import java.util.List;

/**
 * 线路修改 （名称图片）
 */
public class SwitchModifyActivity extends BaseActivity {
    //    private SwitchBean switchBean;
    private CollectorBean collectorBean;
    private SwitchBean currentSwitchBean;
    //    private float dianliang, gonglv;
//    private TextView txtLineName;//线路名称
//    private ImageView imgLine;
//    private EditText editLineName;
    //    private String collectId;
    private CustomLineAdapter customLineAdapter;
    //    private DialogTip dialogTip;
//    private Context mContext;

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
    private ActivityBreakModifyBinding mBinding;
    private SwitchModifyViewModel mViewModel;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_break_modify);
        mViewModel = ViewModelProviders.of(this).get(SwitchModifyViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        mContext = this;
        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        switchBean = getIntent().getParcelableExtra("switchBean");
//        dianliang = getIntent().getFloatExtra("dianliang", 0);
//        gonglv = getIntent().getFloatExtra("gonglv", 0);
//        if (switchBean == null) {
//            return;
//        }
//        ImageView img_cancel = findViewById(R.id.img_cancel);
//        img_cancel.setOnClickListener(v -> finish());
//        SuperTextView superTextView = findViewById(R.id.st_finish);
//        superTextView.setOnClickListener(v -> {
//            if (collectorBean != null) {//修改
//                modify();
//            } else {//添加
//                add();
//            }
//        });
        // dialog切换线路
//        LinearLayout img_change = findViewById(R.id.img_change);
        if (collectorBean != null) {//修改
            mBinding.typeRenameSwitch.setVisibility(View.VISIBLE);
            mBinding.typeAddSwitch.setVisibility(View.GONE);
        } else {//添加
            mBinding.imgChange.setVisibility(View.GONE);
            mBinding.typeRenameSwitch.setVisibility(View.GONE);
            mBinding.typeAddSwitch.setVisibility(View.VISIBLE);
        }
//        txtCurDianliang = (TextView) findViewById(R.id.txt_dianliang);
//        txtCurGonglv = (TextView) findViewById(R.id.txt_gonglv);
//        txtLineName = findViewById(R.id.txt_line_name);
//        editLineName = findViewById(R.id.edit_line_name);
//        imgLine = (ImageView) findViewById(R.id.img_line);
        //    private TextView txtCurDianliang;//当前电量
        //    private TextView txtCurGonglv;//当前功率
//        GridView gridView = mBinding.gridDefaultPic;//findViewById(R.id.grid_default_pic);
        customLineAdapter = new CustomLineAdapter(this);
        mBinding.gridDefaultPic.setAdapter(customLineAdapter);
//        mAdapter2 = new CustomLineAdapter(this);
//        mAdapter2.setShowName(false);
//        HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.horizontal_listview);
//        horizontalListView.setAdapter(mAdapter2);
//        horizontalListView.setDividerWidth(SizeUtils.dp2px(8));

        mBinding.gridDefaultPic.setOnItemClickListener((parent, view, position, id) -> {
            customLineAdapter.reset();
            CustomLineAdapter.Item item = customLineAdapter.setCurrentSelected(position);
//            editLineName.setText(item.name);
            mViewModel.setEditLineName(item.name);
        });
        // 修改
        if (currentSwitchBean != null) {
//            editLineName.setText(currentSwitchBean.getName());
            mViewModel.setEditLineName(currentSwitchBean.getName());
            customLineAdapter.reset();
            customLineAdapter.setCurrentSelected(currentSwitchBean.getIconType());
        }
//        horizontalListView.setOnItemClickListener(new AdapterView.CallBack() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mAdapter2.setCurrentSelected(position);
//            }
//        });
        if (collectorBean != null) {
            getSwitchByCollector();
        }
    }

    /**
     * 获取集中器下的线路 第一个作为默认显示
     */
    private void getSwitchByCollector() {
        Core.instance(this).getSwitchByCollector(collectorBean.getCode(), "nohierarchy", new ActionCallbackListener<List<SwitchBean>>() {
            @Override
            public void onSuccess(List<SwitchBean> data) {
                currentSwitchBean = data.get(0);//init bean
//                txtLineName.setText(String.format("%s%s", getBaseContext().getString(R.string.vs14), currentSwitchBean.getName()));
                mViewModel.setTxtLineName(String.format("%s%s", SwitchModifyActivity.this.getString(R.string.vs14), currentSwitchBean.getName()));
//                editLineName.setText(currentSwitchBean.getName());
                mViewModel.setEditLineName(currentSwitchBean.getName());
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
                mBinding.csv.setVisibility(View.INVISIBLE);
                mBinding.stFinish.setVisibility(View.INVISIBLE);
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
    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onImgChange(View view) {
            SwitchTreeDialog switchTreeDialog = new SwitchTreeDialog(view.getContext(), SwitchTree.DINGSHI, collectorBean, s -> {
                currentSwitchBean = s;
//                        getData(switchBean);
//                txtLineName.setText(String.format("%s%s", view.getContext().getString(R.string.vs14), currentSwitchBean.getName()));
                mViewModel.setTxtLineName(String.format("%s%s", view.getContext().getString(R.string.vs14), currentSwitchBean.getName()));
//                editLineName.setText(currentSwitchBean.getName());
                mViewModel.setEditLineName(currentSwitchBean.getName());
                customLineAdapter.reset();
                customLineAdapter.setCurrentSelected(currentSwitchBean.getIconType());
            });
            switchTreeDialog.show();
        }

        public void onCommit(View view) {
            if (collectorBean != null) {//修改
                modify();
            } else {//添加
                add();
            }
        }
    }

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
        String customName = mViewModel.getEditLineName().getValue();//editLineName.getEditableText().toString();
        intent.putExtra("name", customName);
        intent.putExtra("icontype", customLineAdapter.getSelectedPosition());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void modify() {
        String customName = mViewModel.getEditLineName().getValue();//editLineName.getEditableText().toString();
//        CustomLineAdapter.Line selected = mAdapter2.getSelected();
        if (customName == null || customName.isEmpty()) {
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
        Core.instance(this).updateBreak(
                currentSwitchBean.getSwitchID(),
                currentSwitchBean.getIconType(),
                currentSwitchBean.getName(), new ActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Intent intent = getIntent();
                        intent.putExtra("break", currentSwitchBean);
                        setResult(RESULT_OK, intent);
                        CustomToast.showCustomToast(SwitchModifyActivity.this, getString(R.string.vs139));
                        finish();
                    }

                    @Override
                    public void onFailure(int errorEvent, String message) {
                        CustomToast.showCustomErrorToast(SwitchModifyActivity.this, message);
                    }
                });

    }

}
