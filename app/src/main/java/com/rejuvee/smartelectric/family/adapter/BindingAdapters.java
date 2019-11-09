package com.rejuvee.smartelectric.family.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.rejuvee.smartelectric.family.R;
import com.squareup.picasso.Picasso;

public class BindingAdapters {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.default_user_profile)
                .into(imageView);
    }
}
