package com.gongva.library.app.ui.view.databindadapter;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

/**
 * @author gongwei
 * @data 2019/3/8
 * @email shmily__vivi@163.com
 */
public class NormalBindingConvert {

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
