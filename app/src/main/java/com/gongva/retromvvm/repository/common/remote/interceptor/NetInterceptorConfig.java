package com.gongva.retromvvm.repository.common.remote.interceptor;

import com.gongva.library.plugin.netbase.entity.NetInterceptorConfigAdapter;
import com.gongva.library.plugin.netbase.entity.ResponseResult;

/**
 * 网络拦截器
 *
 * @date 2020/1/13
 */
public class NetInterceptorConfig extends NetInterceptorConfigAdapter {

    private volatile static NetInterceptorConfig INSTANCE;

    public static NetInterceptorConfig getInstance() {
        if (INSTANCE == null) {
            synchronized (NetInterceptorConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetInterceptorConfig();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public boolean isInterceptorCode(int interceptorCode) {
        return false;//todo company's ErrorCode to intercept. include SSO ErrorCOde.
    }

    @Override
    public boolean isRequestSuccess(ResponseResult responseResult) {
        if (responseResult != null) {
            return responseResult.isSuccess();
        } else {
            return false;
        }
    }
}
