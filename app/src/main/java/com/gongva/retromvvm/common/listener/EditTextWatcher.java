package com.gongva.retromvvm.common.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * 监听EditText文字变化的Watcher
 *
 * @author gongwei
 * @date 2019.1.21
 */
public class EditTextWatcher implements TextWatcher {

    private View view;
    private WatcherCallBack callBack;

    public EditTextWatcher(View view, WatcherCallBack callBack) {
        this.view = view;
        this.callBack = callBack;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (callBack != null) {
            callBack.afterTextChanged(view.getId(), editable.toString());
        }
    }

    public interface WatcherCallBack {
        void afterTextChanged(int viewId, String text);
    }
}