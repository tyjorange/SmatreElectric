package com.rejuvee.smartelectric.family.api.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.rejuvee.smartelectric.family.model.bean.WXAccessTokenRet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class ConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static ConverterFactory create() {
        return create(new Gson());
    }

    private static ConverterFactory create(Gson gson) {
        return new ConverterFactory(gson);
    }

    private ConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson is null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == WXAccessTokenRet.class) {
            return null;//不拦截则返回null
        }
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecodeResponseBodyConverter<>(adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new EncodeRequestBodyConverter(gson);
    }


}
