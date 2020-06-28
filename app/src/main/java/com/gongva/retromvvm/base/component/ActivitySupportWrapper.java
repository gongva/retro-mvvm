package com.gongva.retromvvm.base.component;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.library.app.base.ActivitySupport;
import com.gongva.library.app.base.ILoadingController;
import com.gongva.retromvvm.base.application.GvApplicationCreate;
import com.gongva.retromvvm.common.view.dialog.LoadingDialog;
import com.noober.background.BackgroundLibrary;

public abstract class ActivitySupportWrapper extends ActivitySupport implements ILoadingController {

    protected Dialog loadingDialog;
    private boolean destroyed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //BackgroundLibrary初始化
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        //ARouter注册
        ARouter.getInstance().inject(this);
    }

    @Override
    public void finish() {
        super.finish();
        GvApplicationCreate.getInstance().getAppStatus().onActivityDestroyed(this);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog("请稍候...");
    }

    @Override
    public void showLoadingDialog(CharSequence message) {
        showLoadingDialog(message, dialog -> dismissLoadingDialog());
    }

    @Override
    public void showLoadingDialog(CharSequence message, final DialogInterface.OnCancelListener listener) {
        if (loadingDialog == null) {
            loadingDialog = newLoadingDialog(message);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setOnCancelListener(null);
            loadingDialog.setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (listener != null) {
                        listener.onCancel(dialog);
                    }
                }
                return true;
            });
        }
        loadingDialog.show();
    }

    public Dialog newLoadingDialog(CharSequence message) {
        return LoadingDialog.getCustomLoadingProgressDialog(this, TextUtils.isEmpty(message) ? "请稍候..." : message, true, null);
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingDialog != null && !destroyed) {
            loadingDialog.dismiss();
        }
    }
}
