package com.hik.core.android.api;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘相关Api库
 *
 * @author gongwei
 * @time 2019/10/11
 * @mail shmily__vivi@163.com
 */
public class KeyboardUtil {
    /**
     * 开启软键盘
     *
     * @param editText
     */
    public static void showSoftKeyboard(final EditText editText) {
        showSoftKeyboard(editText, false);
    }

    /**
     * 开启软键盘
     *
     * @param editText
     * @param lazy
     */
    public static void showSoftKeyboard(final EditText editText, boolean lazy) {
        if (lazy) {
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.requestFocus();
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 200);
        } else {
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 开启软键盘
     *
     * @param activity
     */
    public static void showSoftKeyboard(final Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null && activity.getCurrentFocus() instanceof EditText) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }
            }, 200);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }
}
