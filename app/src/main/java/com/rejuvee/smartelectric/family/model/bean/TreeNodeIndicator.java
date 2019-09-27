package com.rejuvee.smartelectric.family.model.bean;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.rejuvee.smartelectric.family.R;

public class TreeNodeIndicator extends RelativeLayout {
    public static final int STATE_EXPAND = 0x01;
    public static final int STATE_COLLAPSE = 0x02;

    // 左侧线
    private View mTopLine, mBottomLine;
    private ImageView mIvMiddle;

//    private TextView tvName;
//    private TextView tvQuantity;
//    private TextView tvCharge;
//    private ImageView ivPicture;

    private int mLevel = -1, mState;
    private boolean isLast, isStart;

    private Drawable mDrawablePlusBig;
    private Drawable mDrawableMinusBig;
    private Drawable mDrawablePlusSmall;
    private Drawable mDrawableMinusSamll;
    private Drawable mDrawableLeaf;

    public TreeNodeIndicator(Context context) {
        super(context);
        inflateView(context);
    }

    public TreeNodeIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    public TreeNodeIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    private void inflateView(Context context) {
        mDrawablePlusBig = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_add_circle_outline).color(Color.BLACK).sizeDp(16);
        mDrawableMinusBig = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_remove_circle_outline).color(Color.BLACK).sizeDp(16);
        mDrawablePlusSmall = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_add_circle_outline).color(Color.BLACK).sizeDp(12);
        mDrawableMinusSamll = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_remove_circle_outline).color(Color.BLACK).sizeDp(12);
        mDrawableLeaf = new IconicsDrawable(context, GoogleMaterial.Icon.gmd_panorama_fish_eye).color(Color.BLACK).sizeDp(8);

        LayoutInflater.from(getContext()).inflate(R.layout.view_tree_node_indicator, this, true);

        mTopLine = findViewById(R.id.top_line);
        mBottomLine = findViewById(R.id.bottom_line);
        mIvMiddle = findViewById(R.id.middle_img);

//        tvName = findViewById(R.id.tv_name);
//        tvQuantity = findViewById(R.id.tv_quantity);
//        tvCharge = findViewById(R.id.tv_charge);
//        ivPicture = findViewById(R.id.iv_picture);
//
//        ivPicture.setImageResource(NativeLine.LinePictures[switchStatementBean.getIconType() % NativeLine.LinePictures.length]);
//        tvName.setText(switchStatementBean.getName());
//        tvQuantity.setText(String.valueOf(switchStatementBean.getShowValue()));
//        tvCharge.setText(String.valueOf(switchStatementBean.getShowPrice() + ""));

    }

    public void reset() {
        mLevel = -1;
        mState = 0;
        isLast = false;
        mTopLine.setVisibility(View.VISIBLE);
        mBottomLine.setVisibility(View.VISIBLE);
    }

    public void setLevel(Node node) {
        int level = node.getLevel();
        if (level == mLevel)
            return;
        mLevel = level;

        switch (mLevel) {
            case 0:
                mIvMiddle.setImageDrawable(mDrawablePlusBig);
                mTopLine.setVisibility(View.INVISIBLE);
                break;
            case 1:
                mIvMiddle.setImageDrawable(mDrawablePlusSmall);
                break;
            case 2:
                mIvMiddle.setImageDrawable(mDrawableLeaf);
                break;
            default:
                System.out.println();
                mIvMiddle.setImageDrawable(mDrawableLeaf);
        }
    }

    // 设置展开关闭状态
    public void setState(Node node) {
        int state = node.isExpand() ? TreeNodeIndicator.STATE_EXPAND : TreeNodeIndicator.STATE_COLLAPSE;
        mState = state;
        switch (mState) {
            case STATE_EXPAND:
                mBottomLine.setVisibility(View.VISIBLE);
                if (mLevel == 1) {
                    mIvMiddle.setImageDrawable(mDrawableMinusSamll);
                } else {
                    mIvMiddle.setImageDrawable(mDrawableMinusBig);
                }
                break;
            case STATE_COLLAPSE:
                if (isLast)
                    mBottomLine.setVisibility(View.INVISIBLE);
                if (mLevel == 0) {
                    mTopLine.setVisibility(View.INVISIBLE);
                    mBottomLine.setVisibility(View.INVISIBLE);
                }
                if (mLevel == 2) {
                    mIvMiddle.setImageDrawable(mDrawableLeaf);
                    return;
                }

                if (mLevel == 1) {
                    mIvMiddle.setImageDrawable(mDrawablePlusSmall);
                } else {
                    mIvMiddle.setImageDrawable(mDrawablePlusBig);
                }
                break;
        }
        if (node.isLeaf()) {
            mIvMiddle.setImageDrawable(mDrawableLeaf);
        }
    }

    public void isLast(boolean isLast) {
        this.isLast = isLast;
    }

    public void isStart(boolean isStart) {
        this.isStart = isStart;
        if (this.isStart)
            mTopLine.setVisibility(View.INVISIBLE);
    }
}
