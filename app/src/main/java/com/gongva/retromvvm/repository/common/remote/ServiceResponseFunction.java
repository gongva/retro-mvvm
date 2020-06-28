package com.gongva.retromvvm.repository.common.remote;

import com.gongva.library.plugin.netbase.RetrofitManager;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.library.plugin.netbase.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 服务器异常错误拦截
 * success为ture表示请求成功（code此时默认未100000）
 *
 * @data 2019/3/12
 */
public class ServiceResponseFunction<T> implements Function<ResponseResult<T>, ObservableSource<ResponseResult<T>>> {

    private static volatile ServiceResponseFunction INSTANCE;

    public static <T> ServiceResponseFunction getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceResponseFunction.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceResponseFunction<T>();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public ObservableSource<ResponseResult<T>> apply(ResponseResult<T> tResponse) throws Exception {
        int code = tResponse.getCode();
        String message = tResponse.getMsg();
        if (RetrofitManager.getInstance().getNetInterceptorConfig().isRequestSuccess(tResponse)) {
            return Observable.just(tResponse);
        } else {
            return Observable.error(new ApiException(code, message));
        }
    }
}
