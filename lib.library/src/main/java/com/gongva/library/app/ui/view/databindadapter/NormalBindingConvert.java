package com.gongva.library.app.ui.view.databindadapter;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;

/**
 * @author
 * @data 2019/3/8
 * @email
 */
public class NormalBindingConvert {

    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }
}
