package com.rejuvee.smartelectric.family.common.custom;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * 限制输入格式为 ######.## 不足位数补0
 */
public class MyTextWatcher implements TextWatcher, View.OnFocusChangeListener {
    private DecimalFormat decimalFormat;
    private boolean deleteLastChar;// 是否需要删除末尾
    private boolean deleteStartChar;// 是否需要删除其起始
    private EditText editText;

    private int a;// 小数点前的位数
    private int b;// 小数点后的位数

    public MyTextWatcher(EditText editText, String patten) {
        this.editText = editText;
        this.decimalFormat = new DecimalFormat(patten);
        String[] split = patten.split("\\.");
        a = split[0].length();
        b = split[1].length();
    }

//    public DecimalFormat getDecimalFormat() {
//        return decimalFormat;
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (editText.getText().toString().contains(".")) {
            // 已经输入 . 不能重复输入
            if (editText.getText().toString().indexOf(".", editText.getText().toString().indexOf(".") + 1) > 0) {
                editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                editText.setSelection(editText.getText().toString().length());
            }
        }
        if (charSequence.toString().contains(".")) {
            // 如果点后面有超过三位数值,则删掉最后一位
            int length = charSequence.length() - charSequence.toString().lastIndexOf(".");
            // 说明后面有3位数值
//            deleteLastChar = length >= 4;
            deleteLastChar = (length >= (b + 2));
        } else {
            // 说明前面有6位数值
//            deleteStartChar = charSequence.length() == 7;
            deleteStartChar = (charSequence.length() == (a + 1));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == null) {
            return;
        }
        if (deleteStartChar || deleteLastChar) {
            // 设置新的截取的字符串
            editText.setText(editable.toString().substring(0, editable.toString().length() - 1));
            // 光标强制到末尾
            editText.setSelection(editText.getText().length());
        }
        if (editable.toString().startsWith(".")) {// 以小数点开头，前面自动加上 "0"
            editText.setText(String.format("0%s", editable));
            editText.setSelection(editText.getText().length());
        } else if (editable.toString().endsWith(".")) {// 以小数点结尾，后面自动加上 "0"
//            editText.setText(editable + "0");
//            editText.setSelection(editText.getText().length());
            System.out.println();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            String s = editText.getEditableText().toString();
            String formatted = decimalFormat.format(Double.valueOf(s.isEmpty() ? "0" : s));
            editText.setText(formatted);
        }
    }
}
