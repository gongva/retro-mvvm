package com.gongva.library.app.base;

import android.view.View;

/**
 * error加载出错控制器
 * <p>
 * Created by gongwei on 2018/12/18.
 */
public interface ILoadingErrorController {

    void showErrorPage();

    void showErrorPage(String message);

    void showErrorPage(int icResource, String message);

    void showErrorPage(String icResource, String message);

    void showErrorPage(String message, String action, View.OnClickListener listener);

    void showErrorPage(int icResource, String message, String action, View.OnClickListener listener);

    void showErrorPage(String icResource, String message, String action, View.OnClickListener listener);

    void showErrorPageForHttp(int statusCode, String responseString);

    void dismissErrorPage();
}
