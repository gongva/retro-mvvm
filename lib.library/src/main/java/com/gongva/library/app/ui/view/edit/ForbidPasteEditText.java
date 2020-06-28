package com.gongva.library.app.ui.view.edit;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 禁用粘贴功能的输入框
 *
 * @author gongwei
 * @date 2019/5/11
 * @mail gongwei5@hikcreate.com
 */
public class ForbidPasteEditText extends ClearEditText {
    public ForbidPasteEditText(Context context) {
        super(context);
    }

    public ForbidPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForbidPasteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        //对粘贴方法不做响应
        if (id == android.R.id.paste) {
            return false;
        }
        return super.onTextContextMenuItem(id);
    }
}
