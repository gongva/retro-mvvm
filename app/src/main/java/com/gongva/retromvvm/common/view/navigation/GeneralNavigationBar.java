package com.gongva.retromvvm.common.view.navigation;

import android.content.Context;
import android.util.AttributeSet;

import com.gongva.library.app.ui.view.customview.navigation.NavigationBar;
import com.gongva.retromvvm.R;

/**
 * 导航栏
 *
 * @author gongwei 2018/12/19
 */
public class GeneralNavigationBar extends NavigationBar {

    public GeneralNavigationBar(Context context) {
        super(context);
    }

    public GeneralNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.view_general_navigation_bar;
    }
}
