package com.gongva.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.nio.charset.Charset;

import okhttp3.MediaType;

/**
 * 通用GSON base body转换器
 *
 * @data 2019/5/5
 */
public abstract class BaseGsonBodyConverter<T> {

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private Gson gson;
    private TypeAdapter<T> adapter;

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public TypeAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }
}
