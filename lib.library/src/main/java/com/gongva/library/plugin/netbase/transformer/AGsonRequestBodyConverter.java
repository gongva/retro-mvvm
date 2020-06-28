package com.gongva.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 对外通用GSON相应request body转换器
 *
 * @author
 * @data 2019/5/5
 * @email
 */
public abstract class AGsonRequestBodyConverter<T> extends BaseGsonBodyConverter<T> implements Converter<T, RequestBody>, Cloneable {
    public void initParams(Gson gson, TypeAdapter<T> adapter) {
        setGson(gson);
        setAdapter(adapter);
    }

    @Override
    protected AGsonRequestBodyConverter clone() throws CloneNotSupportedException {
        return (AGsonRequestBodyConverter) super.clone();
    }
}
