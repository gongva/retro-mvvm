package com.gongva.library.app.ui.view.edit;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 禁止输入空格过滤器
 * ☆☆注意☆☆：
 * 代码设置InputFilter的方式加入此过滤器后，EditText在layout中的的maxLength属性会失效
 * 代码需要在设置此Filter的同时，设置LengthFilter控制maxLength
 *
 * @author gongwei
 * @date 2019/3/16
 */
public class SpaceFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceString = source.toString();
        if (sourceString.indexOf(" ") != -1) {
            return sourceString.replaceAll(" ", "");
        }
        return null;
    }
}
