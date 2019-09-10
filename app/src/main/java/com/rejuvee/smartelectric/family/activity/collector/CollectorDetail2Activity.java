package com.rejuvee.smartelectric.family.activity.collector;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.activity.mswitch.SwitchTreeActivity;
import com.rejuvee.smartelectric.family.activity.mswitch.YaoKongActivity;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.model.bean.CollectorBean;

public class CollectorDetail2Activity extends BaseActivity implements View.OnClickListener {
    private CollectorBean collectorBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_collector_detail2;
    }

    @Override
    protected int getMyTheme() {
        return 0;
    }

    @Override
    protected void initView() {
        ImageView backBtn = findViewById(R.id.img_cancel);
        TextView txt_quxian = findViewById(R.id.txt_quxian);
        TextView txt_anquan_sz = findViewById(R.id.txt_anquan_sz);
        TextView txt_dianxiang_sz = findViewById(R.id.txt_dianxiang_sz);
        TextView txt_xianlu_wh = findViewById(R.id.txt_xianlu_wh);

        backBtn.setOnClickListener(this);
        txt_quxian.setOnClickListener(this);
        txt_anquan_sz.setOnClickListener(this);
        txt_dianxiang_sz.setOnClickListener(this);
        txt_xianlu_wh.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        collectorBean = getIntent().getParcelableExtra("collectorBean");
    }

    @Override
    protected void dealloc() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_cancel://返回
                finish();
                break;
            case R.id.txt_quxian:
                Intent intent = new Intent(this, CurveActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                startActivity(intent);
                break;
            case R.id.txt_anquan_sz:
                intent = new Intent(this, SwitchTreeActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                intent.putExtra("viewType", SwitchTreeActivity.ANQUAN_SHEZHI);
                startActivity(intent);
                break;
            case R.id.txt_dianxiang_sz:
                intent = new Intent(this, CollectorAttrActivity.class);
                intent.putExtra("collectorBean", collectorBean);
//                intent.putExtra("viewType", 7);
                startActivity(intent);
                break;
            case R.id.txt_xianlu_wh:
//                intent = new Intent(this, SwitchTreeActivity.class);
                intent = new Intent(this, YaoKongActivity.class);
                intent.putExtra("collectorBean", collectorBean);
                intent.putExtra("viewType", YaoKongActivity.XIANLU_WEIHU);
                startActivity(intent);
                break;
        }
    }
}
