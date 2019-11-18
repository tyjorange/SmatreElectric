package com.rejuvee.smartelectric.family.api.converter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.rejuvee.smartelectric.family.common.utils.AesEncryptUtils;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private String TAG = "DecodeResponseBodyConverter";
    private TypeAdapter<T> adapter;
//    private Gson gson;

    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
        Log.d(TAG, adapter.toString());
    }

    public DecodeResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
//        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(@NonNull ResponseBody responseBody) {
        try {
            String result = responseBody.string();
            String decrypt = AesEncryptUtils.aesDecrypt(result, "abcdef0123456789");
            String res;
            if (decrypt.startsWith("\"")) {// String
                res = decrypt.replace("\\", "");// 去除转义符
                res = res.substring(1, res.length() - 1);// 去掉首位引号
            } else {// Object
                res = decrypt;
            }
            Log.d(TAG, res);
            return adapter.fromJson(res);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        } finally {
            responseBody.close();
        }
    }
}
