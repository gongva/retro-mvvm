package com.gongva.retromvvm.repository.common.remote;

import com.gongva.library.plugin.netbase.RetrofitManager;
import com.gongva.retromvvm.common.GvContext;

/**
 * Api实例构造器
 *
 * @data 2019/3/11
 */
public class ApiGenerator {

    private volatile static IApi API;

    /**
     * api接口实列
     *
     * @return
     */
    public static IApi getApi() {
        if (API == null) {
            synchronized (ApiGenerator.class) {
                if (API == null) {
                    API = RetrofitManager.getInstance().generatorService(IApi.class, GvContext.BASE_URL_HTTP);
                }
            }
        }
        return API;
    }
}
