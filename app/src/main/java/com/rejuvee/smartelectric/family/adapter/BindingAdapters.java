package com.rejuvee.smartelectric.family.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }
}
