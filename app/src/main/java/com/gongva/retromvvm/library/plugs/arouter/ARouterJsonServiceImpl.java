package com.gongva.retromvvm.library.plugs.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;
import com.hik.core.android.api.GsonUtil;

import java.lang.reflect.Type;

/**
 * For ARouter可以传递自定义对象
 *
 * @author gongwei
 * @date 2019/3/4
 */
@Route(path = "/routerservice/json")
public class ARouterJsonServiceImpl implements SerializationService{
    @Override
    public <T> T json2Object(String input, Class<T> clazz) {
        return GsonUtil.jsonDeserializer(input, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return GsonUtil.jsonSerializer(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        Gson gson = new Gson();
        return gson.fromJson(input, clazz);
    }

    @Override
    public void init(Context context) {

    }
}