package com.base.library.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * json string to pojo
 * Created by shaxiaoning on 1/5/17.
 */

public class Json2Pojo<JSON_TYPE> {

    private final Class<JSON_TYPE> clazz;
    private OnJson2PojoCallback<JSON_TYPE> callback;

    public Json2Pojo(OnJson2PojoCallback<JSON_TYPE> callback, String json,
                     Class<JSON_TYPE> parClazz) {
        this.clazz = parClazz;
        this.callback = callback;
        try {
            json2pojo(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss");
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(
            Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json,
                                        java.lang.reflect.Type typeOfT,
                                        JsonDeserializationContext context)
                        throws JsonParseException {
                    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateString = json.getAsString()
                            .replace("Z", "+0000");
                    return DATE_FORMAT.parse(dateString, new ParsePosition(0));
                }
            }).create();



    public void json2pojo(String json) throws Exception {
        JSON_TYPE POJO = GSON.fromJson(json, clazz);
        if (callback != null) callback.onJson2Pojo(POJO);
    }


    public interface OnJson2PojoCallback<JSON_TYPE> {
        void onJson2Pojo(JSON_TYPE POJO);
    }
}
