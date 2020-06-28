package com.gongva.library.plugin.netbase.transformer;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * 默认网络相应body转换器
 *
 * @author gongwei
 * @data 2019/5/5
 * @email shmily__vivi@163.com
 */
public class MineGsonResponseBodyConverter<T> extends AGsonResponseBodyConverter<T> {

    public static MineGsonResponseBodyConverter INSTANCE = new MineGsonResponseBodyConverter();

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = getGson().newJsonReader(value.charStream());
        try {
            return getAdapter().read(jsonReader);
        } finally {
            value.close();
        }
    }
}
