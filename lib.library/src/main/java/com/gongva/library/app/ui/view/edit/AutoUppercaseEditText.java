package com.gongva.library.app.ui.view.edit;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 小写字母自动转大写输入框，带清除功能
 *
 * @author gongwei
 * @date 2019/3/11
 */
public class AutoUppercaseEditText extends ClearEditText {

    public AutoUppercaseEditText(Context context) {
        this(context, null);
    }

    public AutoUppercaseEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public AutoUppercaseEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTransformationMethod(new AutoUppercase());
    }
}