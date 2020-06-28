package com.gongva.library.plugin.netbase.entity;

/**
 * 网络拦截适配器
 *
 * @date 2020/1/14
 */
public class NetInterceptorConfigAdapter implements INetInterceptorConfig {

    public static final NetInterceptorConfigAdapter INSTANCE = new NetInterceptorConfigAdapter();

    @Override
    public boolean isInterceptorCode(int interceptorCode) {
        return false;
    }

    @Override
    public boolean isRequestSuccess(ResponseResult responseResult) {
        return true;
    }

    @Override
    public synchronized <T extends ExceptionBaseBean> ResponseResult<T> getBusinessExceptionResult(int code, String json) {
        return null;
    }
}
