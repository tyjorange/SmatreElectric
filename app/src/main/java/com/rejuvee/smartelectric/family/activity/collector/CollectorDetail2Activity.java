package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.curve.CurveActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchSettingActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.YaoKongActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivityCollectorDetail2Binding;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

/**
 * 专业功能
 */
public class CollectorDetail2Activity extends BaseActivity {
    private CollectorBean collectorBean;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_collector_detail2;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
        ActivityCollectorDetail2Binding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collector_detail2);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);

        collectorBean = getIntent().getParcelableExtra("collectorBean");
//        ImageView backBtn = findViewById(R.id.img_cancel);
//        TextView txt_quxian = findViewById(R.id.txt_quxian);
//        TextView txt_anquan_sz = findViewById(R.id.txt_anquan_sz);
//        TextView txt_dianxiang_sz = findViewById(R.id.txt_dianxiang_sz);
//        TextView txt_xianlu_wh = findViewById(R.id.txt_xianlu_wh);

//        backBtn.setOnClickListener(this);
//        txt_quxian.setOnClickListener(this);
//        txt_anquan_sz.setOnClickListener(this);
//        txt_dianxiang_sz.setOnClickListener(this);
//        txt_xianlu_wh.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onQuxian(View view) {
            Intent intent = new Intent(view.getContext(), CurveActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            startActivity(intent);
        }

        public void onAnquan(View view) {
            Intent intent = new Intent(view.getContext(), SwitchSettingActivity.class);
            intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", YaoKongActivity.ANQUAN_SHEZHI);
            startActivity(intent);
        }

        public void onDianxiang(View view) {
            Intent intent = new Intent(view.getContext(), CollectorAttrActivity.class);
            intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", 7);
            startActivity(intent);
        }

        public void onXianlu(View view) {
            //  intent = new Intent(this, SwitchTreeActivity.class);
            Intent intent = new Intent(view.getContext(), YaoKongActivity.class);
            intent.putExtra("collectorBean", collectorBean);
            intent.putExtra("viewType", YaoKongActivity.XIANLU_WEIHU);
            startActivity(intent);
        }
    }

    @Override
    protected void dealloc() {

    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.img_cancel://返回
//                finish();
//                break;
//            case R.id.txt_quxian:
//                Intent intent = new Intent(this, CurveActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                startActivity(intent);
//                break;
//            case R.id.txt_anquan_sz:
//                intent = new Intent(this, SwitchSettingActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
////                intent.putExtra("viewType", YaoKongActivity.ANQUAN_SHEZHI);
//                startActivity(intent);
//                break;
//            case R.id.txt_dianxiang_sz:
//                intent = new Intent(this, CollectorAttrActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
////                intent.putExtra("viewType", 7);
//                startActivity(intent);
//                break;
//            case R.id.txt_xianlu_wh:
////                intent = new Intent(this, SwitchTreeActivity.class);
//                intent = new Intent(this, YaoKongActivity.class);
//                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", YaoKongActivity.XIANLU_WEIHU);
//                startActivity(intent);
//                break;
//        }
//    }
}
