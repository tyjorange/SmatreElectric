package com.rejuvee.smartelectric.family.api.converter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.rejuvee.smartelectric.family.utils.AesEncryptUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class EncodeRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private String TAG = "EncodeRequestBodyConverter";
    private Gson gson;

    public EncodeRequestBodyConverter(T p0, TypeAdapter<T> adapter) {
//        this.adapter = adapter;
    }

    EncodeRequestBodyConverter(Gson gson) {
        this.gson = gson;
//        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(@NonNull T o) {
        try {
            String content = gson.toJson(o);
//            String content = adapter.toJson(o);
            String encrypt = AesEncryptUtils.aesEncrypt(content, "abcdef0123456789");
            Log.d(TAG, content);
            Log.d(TAG, encrypt);
            return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
