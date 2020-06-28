package com.gongva.retromvvm.repository.common.remote;

import com.gongva.library.plugin.netbase.RetrofitManager;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.library.plugin.netbase.exception.ApiException;
import com.gongva.library.plugin.netbase.exception.CustomerError;
import com.gongva.library.plugin.netbase.transformer.AGsonResponseBodyConverter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * 接口返回结果解析器
 *
 * @data 2019/5/5
 */
public class CustomerResponseBodyConvert<T> extends AGsonResponseBodyConverter<T> {

    public static CustomerResponseBodyConvert INSTANCE = new CustomerResponseBodyConvert();

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        ResponseResult result = getGson().fromJson(response, ResponseResult.class);
        if (result == null || !result.isSuccess()) {
            value.close();
            if (result != null) {
                if (RetrofitManager.getInstance().getNetInterceptorConfig().isInterceptorCode(result.getCode())) {
                    throw new ApiException(result.getCode(), "");
                } else {
                    throw new ApiException(response, result.getCode(), result.getMsg());
                }
            } else {
                throw new ApiException(CustomerError.BUSINESS_ERROR, "数据返回异常");
            }
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = getGson().newJsonReader(reader);

        try {
            return getAdapter().read(jsonReader);
        } finally {
            value.close();
        }
    }
}
