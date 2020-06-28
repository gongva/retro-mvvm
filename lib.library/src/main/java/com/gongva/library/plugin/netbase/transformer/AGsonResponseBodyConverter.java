package com.gongva.library.plugin.netbase.transformer;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 对外通用GSON相应response body转换器
 *
 * @data 2019/5/5
 */
public abstract class AGsonResponseBodyConverter<T> extends BaseGsonBodyConverter<T> implements Converter<ResponseBody, T>, Cloneable {

    public void initParams(Gson gson, TypeAdapter<T> adapter) {
        setGson(gson);
        setAdapter(adapter);
    }

    @Override
    protected AGsonResponseBodyConverter clone() throws CloneNotSupportedException {
        return (AGsonResponseBodyConverter) super.clone();
    }
}
