package com.rejuvee.smartelectric.family.activity.mswitch;

import android.content.Intent;
import android.widget.GridView;

import com.base.library.utils.SizeUtils;
import com.base.library.widget.HorizontalListView;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.CustomLineAdapter;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.SwitchBean;

/**
 * 扫描后设置线路名称
 */
@Deprecated
public class SetSwitchNameActivity extends BaseActivity {
    private SwitchBean switchBean = new SwitchBean();
    private GridView gridView;
    private HorizontalListView horizontalListView;
//    private EditText editLineName;
    private CustomLineAdapter mAdapter1, mAdapter2;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_break_name;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        findViewById(R.id.img_cancel).setOnClickListener(v -> finish());
        findViewById(R.id.st_finish).setOnClickListener(v -> modify());
//        editLineName = (EditText) findViewById(R.id.edit_line_name);
        gridView = findViewById(R.id.grid_default_pic);
        horizontalListView = findViewById(R.id.horizontal_listview);

        mAdapter1 = new CustomLineAdapter(this);
        mAdapter2 = new CustomLineAdapter(this);
//        mAdapter2.setShowName(false);

        gridView.setAdapter(mAdapter1);
        horizontalListView.setAdapter(mAdapter2);
        horizontalListView.setDividerWidth(SizeUtils.dp2px(8));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
//                mAdapter1.setCurrentSelected(position);
//                CustomLineAdapter.Line line = (CustomLineAdapter.Line) mAdapter1.getItem(position);
//                switchBean.setName(line.name);
//                switchBean.setIcon(line.picRes);
            switchBean.setIconType(position);

//                mAdapter2.setCurrentSelected(-1);
        });


        horizontalListView.setOnItemClickListener((parent, view, position, id) -> {
//                mAdapter2.setCurrentSelected(position);
//                mAdapter1.setCurrentSelected(-1);
        });
    }

    @Override
    protected void initData() {
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


    private void modify() {
//        CustomLineAdapter.Line selected = null;
        int selectedPosition = -1;
//        if (mAdapter1.getSelected() != null) {
//            selected = mAdapter1.getSelected();
//            selectedPosition = mAdapter1.getSelectedPosition();
//        }
//        if (mAdapter2.getSelected() != null) {
//            selected = mAdapter2.getSelected();
//            selected.name = editLineName.getEditableText().toString();
//            selectedPosition = mAdapter2.getSelectedPosition();
//        }

//        if (selected == null || selected.name == null || selected.name.isEmpty()) {
//            CustomToast.showCustomErrorToast(this, getString(R.string.ple_set_line_name));
//            return;
//        }

        Intent intent = getIntent();
//        intent.putExtra("name", selected.name);
        intent.putExtra("icontype", selectedPosition);
        setResult(RESULT_OK, intent);
        finish();
    }


}
