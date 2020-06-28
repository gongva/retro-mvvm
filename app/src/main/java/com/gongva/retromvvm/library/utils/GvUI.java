package com.gongva.retromvvm.library.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.ui.UI;
import com.gongva.library.app.ui.view.dialog.AlertDialog;
import com.gongva.library.app.ui.view.dialog.ListDialog;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.common.view.dialog.GvAlertDialog;
import com.gongva.retromvvm.common.view.dialog.GvListDialog;
import com.gongva.retromvvm.common.view.dialog.LoadingDialog;

import java.util.List;

/**
 * UI相关提示
 *
 * @author gongwei 2018/12/18.
 */
public class GvUI extends UI {

    @Override
    protected void showToastImpl(Context context, String msg, View customView, int duration) {
        if (context == null) {
            context = TinkerApplicationCreate.getApplication();
        }
        if (!TextUtils.isEmpty(msg) || customView != null) {
            if (toast != null) {
                toast.cancel();
            }
            toast = new Toast(context);
            if (customView != null) {
                //set custom view for toast
                toast.setView(customView);
                toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                //get toast layout view
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dialog_toast, null);
                TextView tvToastContent = view.findViewById(R.id.tv_toast);
                tvToastContent.setText(msg);
                toast.setView(view);
            }
            toast.setDuration(duration);
            toast.show();
        }
    }

    @Override
    protected Dialog showLoadingDialogImpl(Context context, String message, DialogInterface.OnCancelListener listener) {
        LoadingDialog loadingDialog = LoadingDialog.getCustomLoadingProgressDialog(context, message, true, listener);
        loadingDialog.show();
        return loadingDialog;
    }

    @Override
    protected AlertDialog showAlertDialogImpl(Context context, boolean cancelable, String title, String message, int gravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        return GvAlertDialog.newInstance(context, cancelable, title, message, gravity, buttonLeft, listenerLeft, buttonRight, listenerRight).show();
    }

    @Override
    protected ListDialog showListDialogImpl(Activity context, CharSequence title, List<String> bases, ListDialog.ListDialogCallback callback) {
        return GvListDialog.newInstance(context, title, bases, callback).show();
    }
}