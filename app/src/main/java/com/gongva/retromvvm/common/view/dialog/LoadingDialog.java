package com.gongva.retromvvm.common.view.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongva.library.app.ui.view.dialog.TouchableDialog;
import com.gongva.retromvvm.R;


public class LoadingDialog extends TouchableDialog {

    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialogStyle);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        //Progress如果用帧动画，在这里start
        ImageView imageView = findViewById(R.id.iv_progress_custom);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.tv_progress_custom).setVisibility(View.VISIBLE);
            TextView txt = findViewById(R.id.tv_progress_custom);
            txt.setText(message);
            txt.invalidate();
        }
    }


    public static LoadingDialog show(Context context, CharSequence message, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        LoadingDialog dialog = getCustomLoadingProgressDialog(context, message, cancelable, cancelListener);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }

    public static LoadingDialog getCustomLoadingProgressDialog(Context context, CharSequence message, boolean cancelable, final DialogInterface.OnCancelListener cancelListener) {
        LoadingDialog dialog = new LoadingDialog(context, R.style.LoadingDialogStyle);
        dialog.setTitle("");
        dialog.setContentView(R.layout.view_progress_custom);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.tv_progress_custom).setVisibility(View.GONE);
        } else {
            TextView txt = dialog.findViewById(R.id.tv_progress_custom);
            txt.setText(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (cancelListener != null) {
                        cancelListener.onCancel(dialog);
                    }
                }
                return true;
            }
        });
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }
}