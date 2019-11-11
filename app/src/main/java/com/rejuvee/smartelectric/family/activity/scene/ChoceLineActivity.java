package com.rejuvee.smartelectric.family.activity.scene;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.base.frame.net.ActionCallbackListener;
import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.adapter.GridViewAdapter;
import com.rejuvee.smartelectric.family.api.Core;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityChoceLineBinding;
import com.rejuvee.smartelectric.family.model.bean.SwitchInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择场景下的线路
 * Created by Administrator on 2017/12/15.
 */
public class ChoceLineActivity extends BaseActivity {
    private static final String TAG = "ChoceLineActivity";
    //    private GridView grid_device;
    private GridViewAdapter gridViewAdapter;
    //    private TextView text_choceall;
    private List<SwitchInfoBean> mListAllBreak = new ArrayList<>();//所有线路
    //    private SceneBean scene1;
//    private Intent intent;
//    private String sceneid;


//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_choce_line;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    private ActivityChoceLineBinding mBinding;
    @Override
    protected void initView() {
//        setToolbarHide(true);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_choce_line);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
        initAllLine();

//        grid_device = findViewById(R.id.grid_device);
        gridViewAdapter = new GridViewAdapter(ChoceLineActivity.this, mListAllBreak);
        mBinding.gridDevice.setAdapter(gridViewAdapter);

//        text_choceall = findViewById(R.id.text_choceall);
//        findViewById(R.id.text_choceall).setOnClickListener(this);
//        findViewById(R.id.bu_wancheng).setOnClickListener(this);
//        findViewById(R.id.img_cancel).setOnClickListener(this);

        mBinding.gridDevice.setOnItemClickListener((parent, view, position, id) -> {
            boolean flag = mListAllBreak.get(position).isFlag();
            mListAllBreak.get(position).setFlag(!flag);
            gridViewAdapter.notifyDataSetChanged();
            updateRightMenu(isAllSelect());
        });
    }

    //是否已经全选
    private boolean isAllSelect() {
        boolean isAllSelect = true;
        for (SwitchInfoBean breaker : mListAllBreak) {
            if (!breaker.isFlag()) {
                isAllSelect = false;
                break;
            }
        }
        return isAllSelect;
    }

    //更新右上角菜单项文字
    private void updateRightMenu(boolean isSelectAll) {
        if (isSelectAll) {
            mBinding.textChoceall.setText(R.string.cancel);
            mBinding.textChoceall.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.img_chose), null, null, null);
        } else {
            mBinding.textChoceall.setText(R.string.sce_choceall);
            mBinding.textChoceall.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawable(R.drawable.img_unchose), null, null, null);
        }
    }

    private void updateBreakSelect(boolean select) {
        for (SwitchInfoBean breaker : mListAllBreak) {
            breaker.setFlag(select);
        }
        gridViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<SwitchInfoBean> getSelected() {
        ArrayList<SwitchInfoBean> listSelected = new ArrayList<>();
        for (SwitchInfoBean breaker : mListAllBreak) {
            if (breaker.isFlag()) {
                listSelected.add(breaker);
            }
        }
        return listSelected;
    }

    @Override
    protected void initData() {

    }

    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.text_choceall:
//                boolean isAllSelect = isAllSelect();
//                updateBreakSelect(!isAllSelect());
//                updateRightMenu(!isAllSelect);
//                break;
//            case R.id.bu_wancheng:
//                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("breaks", getSelected());
//                setResult(RESULT_OK, intent);
//                ChoceLineActivity.this.finish();
//                break;
//            case R.id.img_cancel:
//                ChoceLineActivity.this.finish();
//                break;
//        }
//    }

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

        public void onCommit(View view) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("breaks", getSelected());
            setResult(RESULT_OK, intent);
            ChoceLineActivity.this.finish();
        }

        public void onChoseAll(View view) {
//            boolean isAllSelect = isAllSelect();
            updateBreakSelect(!isAllSelect());
            updateRightMenu(!isAllSelect());
        }

    }

    @Override
    protected void dealloc() {

    }

    private void initAllLine() {
        Core.instance(this).getAllBreakersByUser(new ActionCallbackListener<List<SwitchInfoBean>>() {
            @Override
            public void onSuccess(List<SwitchInfoBean> data) {
                mListAllBreak.clear();
                mListAllBreak.addAll(data);
                Log.i(TAG, mListAllBreak.size() + "==size");
                intidatasours();
                gridViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                Log.e(TAG, message);
            }
        });
    }

    public void intidatasours() {
        List<SwitchInfoBean> listSceneBreak = getIntent().getParcelableArrayListExtra("breaks");

        if (listSceneBreak != null && listSceneBreak.size() > 0) {

            for (int i = 0; i < listSceneBreak.size(); i++) {
                for (int j = 0; j < mListAllBreak.size(); j++) {
                    if (mListAllBreak.get(j).getSwitchID().equals(listSceneBreak.get(i).getSwitchID())) {
                        mListAllBreak.remove(mListAllBreak.get(j));
                    }
                }
            }
        }
    }
}
