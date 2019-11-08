package com.rejuvee.smartelectric.family.activity.scene;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/12/15.
 */
public class ChoceSceneimgActivity extends BaseActivity implements View.OnClickListener {
    int[] imageId = new int[]{
            R.drawable.cj_qichuang_cj,
            R.drawable.cj_shuijiao_cj,
            R.drawable.cj_huijia_cj,
            R.drawable.cj_lijia_cj,
            R.drawable.cj_youxi_cj,
            R.drawable.cj_xiuxian_cj,
            R.drawable.cj_juhui_cj,
            R.drawable.cj_yongcan_cj,
            R.drawable.cj_dianying_cj,
            R.drawable.cj_kaideng_cj,
            R.drawable.cj_guandeng_cj,
            R.drawable.cj_gengduo_cj
    };

    private List<Item> listGrids = new ArrayList<>();
    private List<ImageView> chk = new ArrayList<>();
    private GridViewAdapter gridViewAdapter;
    private int currentPos = -1;

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_chocesceneimg;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }

    @Override
    protected void initView() {
//        setToolbarHide(true);
        initdata();
        findViewById(R.id.txt_cancel).setOnClickListener(this);
        findViewById(R.id.st_wancheng).setOnClickListener(this);
//      MyGridView gride_allscen = (MyGridView) findViewById(R.id.gride_allscen);
        GridView gride_allscen = findViewById(R.id.gride_allscen);
        gridViewAdapter = new GridViewAdapter(ChoceSceneimgActivity.this, listGrids);
        if (listGrids.size() > 0) {
            gride_allscen.setAdapter(gridViewAdapter);
        }
        gride_allscen.setOnItemClickListener((parent, view, position, id) -> {
            reset();
            listGrids.get(position).chose = 1;
            gridViewAdapter.notifyDataSetChanged();
            currentPos = position;
//                Intent intent = new Intent();
//                intent.putExtra("scenimg", position);
//                setResult(RESULT_OK, intent);
//                ChoceSceneimgActivity.this.finish();
        });
        findViewById(R.id.img_qichuang).setOnClickListener(this);
        findViewById(R.id.img_shuijiao).setOnClickListener(this);
        findViewById(R.id.img_huijia).setOnClickListener(this);
        findViewById(R.id.img_lijia).setOnClickListener(this);

        chk.add(findViewById(R.id.chk_qichuang));
        chk.add(findViewById(R.id.chk_shuijiao));
        chk.add(findViewById(R.id.chk_huijia));
        chk.add(findViewById(R.id.chk_lijia));

    }

    public void initdata() {
        for (int id : imageId) {
            listGrids.add(new Item(id, 0));
        }
    }

    @Override
    protected void initData() {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_qichuang:
                chocescen(0);
                break;
            case R.id.img_shuijiao:
                chocescen(1);
                break;
            case R.id.img_huijia:
                chocescen(2);
                break;
            case R.id.img_lijia:
                chocescen(3);
                break;
            case R.id.txt_cancel:
                ChoceSceneimgActivity.this.finish();
                break;
            case R.id.st_wancheng:
                if (currentPos == -1) {
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra("scenimg", currentPos);
                setResult(RESULT_OK, intent);
                ChoceSceneimgActivity.this.finish();
                break;
        }
    }

    private void chocescen(int position) {
        reset();
        chk.get(position).setImageDrawable(getDrawable(R.drawable.img_chose));
        currentPos = position;
//        Intent intent = new Intent();
//        intent.putExtra("scenimg", position);
//        setResult(RESULT_OK, intent);
//        ChoceSceneimgActivity.this.finish();
    }

    /**
     * 取消现有选择
     */
    private void reset() {
        for (ImageView iv : chk) {
            iv.setImageDrawable(getDrawable(R.drawable.img_unchose));
        }
        for (Item i : listGrids) {
            i.chose = 0;
        }
        gridViewAdapter.notifyDataSetChanged();
    }

    public class GridViewAdapter extends BaseAdapter {

        private Context context;
        private List<Item> grids;

        GridViewAdapter(Context context, List<Item> grids) {
            this.context = context;
            this.grids = grids;
        }

        @Override
        public int getCount() {
            return grids.size();
        }

        @Override
        public Object getItem(int position) {
            return grids.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.chocesceneimg_items, null);
                holder = new Holder();
                holder.img_scene = convertView.findViewById(R.id.img_scene);
                holder.img_chose = convertView.findViewById(R.id.img_chose);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.img_scene.setImageResource(grids.get(position).img);
            holder.img_chose.setImageResource(grids.get(position).chose == 1 ? R.drawable.img_chose : R.drawable.img_unchose);
            return convertView;
        }

        class Holder {
            private ImageView img_scene;
            private ImageView img_chose;
        }
    }

    class Item {
        int img;
        int chose;// 1 选中 0 未选中

        Item(int img, int chose) {
            this.img = img;
            this.chose = chose;
        }
    }
}
