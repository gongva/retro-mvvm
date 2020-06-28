package com.gongva.library.plugin.netbase.entity;

/**
 * 网络拦截配置接口
 *
 * @date 2020/1/14
 */
public interface INetInterceptorConfig {
    /**
     * 判断是否是拦截code
     *
     * @param interceptorCode
     * @return
     */
    boolean isInterceptorCode(int interceptorCode);

    /**
     * 请求成功状态
     *
     * @param responseResult
     * @return
     */
    boolean isRequestSuccess(ResponseResult responseResult);

    /**
     * 获取业务异常结果
     *
     * @param code
     * @param json
     * @param <T>
     * @return
     */
     <T extends ExceptionBaseBean> ResponseResult<T> getBusinessExceptionResult(int code, String json);
}
