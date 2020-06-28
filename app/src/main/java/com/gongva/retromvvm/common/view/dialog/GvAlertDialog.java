package com.gongva.retromvvm.common.view.dialog;

import android.content.Context;
import android.view.View;

import com.gongva.library.app.ui.view.dialog.AlertDialog;
import com.gongva.retromvvm.R;


/**
 * 确认弹窗
 *
 * @author gongwei
 * @time 2019/11/06
 * @mail shmily__vivi@163.com
 */
public class GvAlertDialog extends AlertDialog {

    public GvAlertDialog(Context context, boolean cancelAble, String title, String message, int messageGravity, String buttonLeft, View.OnClickListener listenerLeft, String buttonRight, View.OnClickListener listenerRight) {
        super(context, cancelAble, title, message, messageGravity, buttonLeft, listenerLeft, buttonRight, listenerRight);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_confirm_dialog;
    }

    @Override
    public AlertDialog addView(View view) {
        return super.addView(view);
    }
}
