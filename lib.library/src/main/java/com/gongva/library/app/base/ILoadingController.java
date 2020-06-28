package com.gongva.library.app.base;

import android.content.DialogInterface;

/**
 * 普通加载进度控制器
 * <p>
 * Created by gongwei on 2018/12/18.
 */
public interface ILoadingController {

    void showLoadingDialog();

    void showLoadingDialog(CharSequence message);

    void showLoadingDialog(CharSequence message, DialogInterface.OnCancelListener listener);

    void dismissLoadingDialog();
}
