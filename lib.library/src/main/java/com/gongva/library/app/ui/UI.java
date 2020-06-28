package com.gongva.library.app.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gongva.library.R;
import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.ui.view.dialog.AlertDialog;
import com.gongva.library.app.ui.view.dialog.ListDialog;

import java.util.Arrays;
import java.util.List;

/**
 * UI相关提示
 *
 * @author gongwei 2018/12/18.
 */
public abstract class UI {

    private static UI INSTANCE = null;
    protected static Toast toast = null;

    public static void setUI(UI ui) {
        INSTANCE = ui;
        toast = null;
    }

    public static void showToast(String msg) {
        //传ActivityTop的原因：在ActivitySupport中，做了系统字体大小的忽略
        showToast(TinkerApplicationCreate.getInstance().getActivityTop(), msg, null, Toast.LENGTH_SHORT);
    }

    public static void showToast(View customView) {
        //传ActivityTop的原因：在ActivitySupport中，做了系统字体大小的忽略
        showToast(TinkerApplicationCreate.getInstance().getActivityTop(), null, customView, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg, View customView, int duration) {
        if (INSTANCE != null) {
            INSTANCE.showToastImpl(context, msg, customView, duration);
            return;
        }

        if (!TextUtils.isEmpty(msg)) {
            toast = Toast.makeText(context, msg, duration);
        } else {
            toast = new Toast(context);
            toast.setView(customView);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    /**
     * Commit时的确认框，含：
     * 标题有无、一个/两个按钮、取消可否
     *
     * @param context
     * @param message
     * @param listener
     * @return
     */
    public static Dialog showLoadingDialog(Context context, String message, final DialogInterface.OnCancelListener listener) {
        if (INSTANCE != null) {
            return INSTANCE.showLoadingDialogImpl(context, message, listener);
        }

        ProgressDialog progressDialog = new ProgressDialog(context, R.style.LoadingDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(null);
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (listener != null) {
                    listener.onCancel(dialog);
                }
            }
            return true;
        });
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public static Dialog showLoadingDialog(Context context, String message) {
        return showLoadingDialog(context, message, null);
    }

    /**
     * 确认框
     *
     * @param context
     * @param message
     * @param buttonText
     * @param listener
     * @return
     */
    public static AlertDialog showConfirmDialog(Context context, String message, String buttonText, View.OnClickListener listener) {
        return showConfirmDialog(context, message, null, null, buttonText, listener);
    }

    public static AlertDialog showConfirmDialog(Context context, String message, int gravity, String buttonText, View.OnClickListener listener) {
        return showConfirmDialog(context, message, gravity, null, null, buttonText, listener);
    }

    public static AlertDialog showConfirmDialog(Context context, boolean cancelable, String message, String buttonText, View.OnClickListener listener) {
        return showConfirmDialog(context, cancelable, null, message, null, null, buttonText, listener);
    }

    public static AlertDialog showConfirmDialog(Context context, boolean cancelable, String title, String message, String buttonText, View.OnClickListener listener) {
        return showConfirmDialog(context, cancelable, title, message, null, null, buttonText, listener);
    }

    public static AlertDialog showConfirmDialog(Context context, boolean cancelable, String title, String message, int gravity, String buttonText, View.OnClickListener listener) {
        return showConfirmDialog(context, cancelable, title, message, gravity, null, null, buttonText, listener);
    }

    public static AlertDialog showConfirmDialog(Context context, String title, String message, String buttonText, View.OnClickListener listener) {
        return showConfirmDialog(context, true, title, message, null, null, buttonText, listener);
    }

    public static AlertDialog showConfirmDialog(Context context, String message, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        return showConfirmDialog(context, true, null, message, buttonLeft, listenerLeft, buttonRight, listenerRight);
    }

    public static AlertDialog showConfirmDialog(Context context, String message, int gravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        return showConfirmDialog(context, true, null, message, gravity, buttonLeft, listenerLeft, buttonRight, listenerRight);
    }

    public static AlertDialog showConfirmDialog(Context context, String title, String message, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        return showConfirmDialog(context, true, title, message, buttonLeft, listenerLeft, buttonRight, listenerRight);
    }

    public static AlertDialog showConfirmDialog(Context context, boolean cancelable, String title, String message, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        return showConfirmDialog(context, cancelable, title, message, Gravity.CENTER, buttonLeft, listenerLeft, buttonRight, listenerRight);
    }

    public static AlertDialog showConfirmDialog(Context context, boolean cancelable, String title, String message, int gravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        if (INSTANCE != null) {
            return INSTANCE.showAlertDialogImpl(context, cancelable, title, message, gravity, buttonLeft, listenerLeft, buttonRight, listenerRight);
        }
        return AlertDialog.newInstance(context, cancelable, title, message, gravity, buttonLeft, listenerLeft, buttonRight, listenerRight).show();
    }


    /**
     * 列表选择器
     *
     * @param context
     * @param bases
     * @param title
     * @param callback
     */
    public static ListDialog showListDialog(Activity context, List<String> bases, CharSequence title, ListDialog.ListDialogCallback callback) {
        if (INSTANCE != null) {
            return INSTANCE.showListDialogImpl(context, title, bases, callback).show();
        }
        return ListDialog.newInstance(context, title, bases, callback).show();
    }

    public static ListDialog showListDialog(Activity context, String[] bases, CharSequence title, ListDialog.ListDialogCallback callback) {
        return showListDialog(context, Arrays.asList(bases), title, callback);
    }

    /**
     * EditText设置默认值
     *
     * @param edt
     * @param str
     */
    public static void setEditTextDefault(EditText edt, CharSequence str) {
        if (edt != null) {
            edt.setText(str);
            if (str != null) {
                int realLength = edt.getText().toString().length();
                edt.setSelection(Math.min(realLength, str.length()));//如果edt设置了maxLength且小于str长度时，会数组下标越界，2者取小者
            }
        }
    }

    protected abstract void showToastImpl(Context context, String msg, View customView, int duration);

    protected abstract Dialog showLoadingDialogImpl(Context context, String message, DialogInterface.OnCancelListener listener);

    protected abstract AlertDialog showAlertDialogImpl(Context context, boolean cancelable, String title, String message, int gravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight);

    protected abstract ListDialog showListDialogImpl(Activity context, CharSequence title, List<String> bases, ListDialog.ListDialogCallback callback);
}
