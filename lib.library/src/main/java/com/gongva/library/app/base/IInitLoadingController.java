package com.gongva.library.app.base;

/**
 * init加载进度控制器
 * <p>
 * Created by gongwei on 2018/12/18.
 */
public interface IInitLoadingController extends ILoadingController {

    void showInitLoading();

    void showInitLoading(String text);

    void dismissInitLoading();
}
