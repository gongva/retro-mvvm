package com.gongva.retromvvm.common.view.dialog;

import android.app.Activity;


import com.gongva.library.app.ui.view.dialog.ListDialog;
import com.gongva.retromvvm.R;

import java.util.List;

/**
 * 列表选择器
 *
 * @author gongwei
 * @date 2019/2/12
 */
public class GvListDialog extends ListDialog {
    public GvListDialog(Activity activity, CharSequence title, List<String> data, ListDialogCallback callback) {
        super(activity, title, data, callback);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_list;
    }
}
