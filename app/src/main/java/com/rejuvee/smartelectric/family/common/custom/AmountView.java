package com.rejuvee.smartelectric.family.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.rejuvee.smartelectric.family.R;

/**
 * 自定义组件：购买数量，带减少增加按钮
 */
public class AmountView extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = "AmountView";
    private float amount = 1; //当前值
    private float val_max = 1; //最大值
    private float val_min = 1; //最小值

    private OnAmountChangeListener mListener;

    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;

    public AmountView(Context context) {
        this(context, null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = findViewById(R.id.etAmount);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.setOnFocusChangeListener(this);
//        etAmount.addTextChangedListener(this);

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setVal_max(float val_max) {
        this.val_max = val_max;
    }

    public void setVal_min(float val_min) {
        this.val_min = val_min;
    }

    public void setAmount(float amount) {
        this.amount = amount;
        etAmount.setText(String.format("%s", amount));
    }

    public float getAmount() {
        validValue();
        return amount;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > val_min) {
                amount--;
                etAmount.setText(String.format("%s", amount));
            }
        } else if (i == R.id.btnIncrease) {
            if (amount < val_max) {
                amount++;
                etAmount.setText(String.format("%s", amount));
            }
        }
        v.setFocusable(true);
        etAmount.clearFocus();
        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            return;
        }
        validValue();
    }

    /**
     * 纠正错误值 防止超过最大最小值
     */
    private void validValue() {
        String s = etAmount.getEditableText().toString();
        if (s.trim().isEmpty() || s.equals(".")) {
            etAmount.setText(String.format("%s", amount));
            return;
        }
        amount = Float.valueOf(s);
        if (amount > val_max) {
            amount = val_max;
            etAmount.setText(String.format("%s", val_max));
        }
        if (amount < val_min) {
            amount = val_min;
            etAmount.setText(String.format("%s", val_min));
        }
        etAmount.clearFocus();
        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    public interface OnAmountChangeListener {
        void onAmountChange(View view, float amount);
    }

}
