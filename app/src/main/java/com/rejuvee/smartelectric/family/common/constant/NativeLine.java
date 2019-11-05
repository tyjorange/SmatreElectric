package com.rejuvee.smartelectric.family.common.constant;

import android.content.Context;

import com.rejuvee.smartelectric.family.R;


/**
 * Created by liuchengran on 2017/12/19.
 */

public class NativeLine {
    public static int[] LinePictures = null;
    public static String[] LineNames = null;
    public static String[] TypeId = null;
    public static int[] imageId = null;

    public static int[] DrawableToggle = null;

    public static int[] TabTitleSignalType = null;

    public static void init(Context context) {
        LinePictures = new int[]{
                R.drawable.linepic01,
                R.drawable.linepic02,
                R.drawable.linepic03,
                R.drawable.linepic04,
                R.drawable.linepic05,
                R.drawable.linepic06,
                R.drawable.linepic07,
                R.drawable.linepic08,
                R.drawable.linepic09,
                R.drawable.linepic10,
                R.drawable.linepic11,
                R.drawable.linepic12
        };

        LineNames = new String[]{
                context.getString(R.string.line_zhaoming),
                context.getString(R.string.line_keting),
                context.getString(R.string.line_zhuwo),
                context.getString(R.string.line_woshi),
                context.getString(R.string.line_shufang),
                context.getString(R.string.line_diannao),
                context.getString(R.string.line_chufang),
                context.getString(R.string.line_weishengjian),
                context.getString(R.string.line_kongtiao),
                context.getString(R.string.line_chazuo),
                context.getString(R.string.line_reshuiqi),
                context.getString(R.string.line_other)
        };

        TypeId = new String[]{
                1 + "", 2 + "", 3 + "", 4 + "", 5 + "", 6 + "", 7 + "", 8 + "", 9 + "", 10 + ""
        };

        imageId = new int[]{
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

        DrawableToggle = new int[]{
                R.drawable.yk_kaizha,
                R.drawable.yk_hezha,
                R.drawable.yk_cuowu
        };

        TabTitleSignalType = new int[]{
                R.string.statistical_3,
                R.string.statistical_5,
                R.string.statistical_4,
                R.string.statistical_6,
                R.string.statistical_1,
                R.string.statistical_2,
                R.string.statistical_7,
                R.string.statistical_8,
                R.string.statistical_9,
                R.string.frequency,
                R.string.temperature,
                R.string.ldl
        };

    }

}
