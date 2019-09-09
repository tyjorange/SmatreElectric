package com.base.library.utils;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by liuchengran on 2017/7/4.
 */
public class OkHttpClientUtils {

    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (OkHttpClientUtils.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            //设置缓存
                            .cache(new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "slst"), 1024*1024))
                    .build();
                }
            }
        }
        return client;
    }
}
