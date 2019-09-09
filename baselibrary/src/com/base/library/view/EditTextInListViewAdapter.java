package com.base.library.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.base.library.utils.EditTextWatcher;


/**
 * EditText in listView cell
 * Created by shaxiaoning on 7/13/16.
 */
public abstract class EditTextInListViewAdapter extends BaseAdapter {
    private static final String TAG = EditTextInListViewAdapter.class.getName();

    /**
     * 配置item editText
     *
     * @param editText  EditText
     * @param isFocused 焦点
     * @param content   内容
     * @param listener  callback
     */
    protected void configEditText(final EditText editText, final boolean isFocused, final String content,
                                  final OnEditTextChangedListener listener) {

        if (editText.getTag() instanceof TextWatcher) {
            editText.removeTextChangedListener((TextWatcher) (editText.getTag()));
            //editText.setOnFocusChangeListener(null);
        }


        if (!TextUtils.isEmpty(content)) {
            editText.setText(content);
        } else {
            editText.setText("");

        }
        if (isFocused) {
            if (!editText.isFocused()) {
                editText.requestFocus();
            }
            CharSequence text = content;
            editText.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
        } else {
            if (editText.isFocused()) {
                editText.clearFocus();
            }
        }

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // final boolean focus = isFocused;
//                    for (ScollableBean tmpBean : listData) {
//                        tmpBean.setFocused(false);
//                    }
                    //bean.setFocused(true);
                    if (listener != null) listener.setCurItemFocusedResetOther();

                    if (!isFocused && !editText.isFocused()) {
                        editText.requestFocus();
                        editText.onWindowFocusChanged(true);


                    } else {

                    }
                }
                return false;
            }
        });

        final TextWatcher watcher = new EditTextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    //bean.setContent(null);
                    if (listener != null) listener.textChanged(null);
                } else {
                    //bean.setContent(String.valueOf(s));
                    if (listener != null) listener.textChanged(String.valueOf(s));
                }

            }
        };

        editText.addTextChangedListener(watcher);
        editText.setTag(watcher);
        //  editText.setOnFocusChangeListener(focused);


    }

    public interface OnEditTextChangedListener {
        /**
         * 设置当前item焦点,重置其它item
         */
        void setCurItemFocusedResetOther();

        /**
         * 输入后内容
         *
         * @param text
         */
        void textChanged(String text);


    }
}
