package com.gongva.library.plugin.netbase;

import com.gongva.library.plugin.netbase.exception.ApiException;

/**
 * 用于统一控制Rx网络请求回调适配器
 *
 * @author gongwei
 * @data 2019/3/8
 * @email shmily__vivi@163.com
 */

public class RequestObserverCallbackAdapter<T> extends RequestObserverCallback<T> {

    public RequestObserverCallbackAdapter(){
        super();
    }

    public RequestObserverCallbackAdapter(int tag){
        super(tag);
    }

    @Override
    public void loadStart() {
    }

    @Override
    public void loadFinish() {

    }

    @Override
    public void loadData(T t) {

    }

    @Override
    public void loadError(ApiException e) {

    }

}
