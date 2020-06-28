package com.gongva.library.app.ui.view.edit;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 限制输入
 * 仅支持中文 数字 英文
 *
 * @author gongwei
 * @time 2019/11/22
 */
public class LimitInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.replaceAll("").trim();
    }
}
