package com.gongva.library.app.ui.view.customview.navigation;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hik.core.android.api.KeyboardUtil;
import com.hik.core.android.component.listener.OnDoubleClickListener;
import com.hik.core.android.view.ViewUtil;
import com.gongva.library.R;


/**
 * 导航栏
 * 子类请重写getContentLayout定制UI
 *
 * @author gongwei 2018/12/19
 */
public class NavigationBar extends FrameLayout {

    Context mContext;
    RelativeLayout rltRoot;
    LinearLayout lltTitle;
    TextView tvTitle;
    TextView tvBack;
    LinearLayout lltMenu;
    TextView tvCustomMenu;
    ImageView ivCustomMenuIcon;
    OnDoubleClickListener doubleClickListener;
    OnClickListener doubleClick, singleTapListener;

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        doubleClickListener = new OnDoubleClickListener(context) {
            @Override
            public void onDoubleClick(View v) {
                if (doubleClick != null) {
                    doubleClick.onClick(v);
                }
            }

            @Override
            public void onClick(View v) {
                if (singleTapListener != null) {
                    singleTapListener.onClick(v);
                }
            }
        };
        View.inflate(context, getContentLayout(), this);
        rltRoot = findViewById(R.id.actionBar_nav);
        lltTitle = findViewById(R.id.llt_nav_title);
        tvTitle = findViewById(R.id.tv_nav_title);
        tvBack = findViewById(R.id.tv_nav_back);
        lltMenu = findViewById(R.id.llt_nav_menu);
        tvCustomMenu = findViewById(R.id.tv_nav_custom_menu);
        ivCustomMenuIcon = findViewById(R.id.iv_nav_menu);
        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    KeyboardUtil.hideSoftKeyboard(activity);
                    activity.finish();
                }
            }
        });
    }

    protected int getContentLayout() {
        return R.layout.view_navigation_bar;
    }

    public TextView getTvBack() {
        return tvBack;
    }

    public void setTvBack(TextView tvBack) {
        this.tvBack = tvBack;
    }

    /**
     * 设置双击事件
     *
     * @param listener
     */
    public void setOnDoubleClickListener(final OnClickListener listener) {
        this.doubleClick = listener;
    }

    /**
     * 设置标题单击事件
     *
     * @param listener
     */
    public void setOnSingleClickListener(OnClickListener listener) {
        this.singleTapListener = listener;
    }

    /**
     * 设置双击滚动到顶部
     *
     * @param scrollView
     */
    public void setScrollTop(final ScrollView scrollView) {
        setOnDoubleClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, 0);
            }
        });
    }

    /**
     * 设置双击滚动到顶部
     *
     * @param listView
     */
    public void setScrollTop(final AbsListView listView) {
        setOnDoubleClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        doubleClickListener.onTouch(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否启用导航菜单
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    public void hide() {
        setEnabled(false);
    }

    public void show() {
        setEnabled(true);
    }

    public void setBackgroundColor(int color) {
        rltRoot.setBackgroundColor(color);
    }

    public void setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
    }

    public void setText(CharSequence title) {
        tvTitle.setText(title);
    }

    public View getTvTitle() {
        return tvTitle;
    }

    /**
     * 是否启用左边返回键
     *
     * @param enabled
     */
    public void setLeftEnabled(boolean enabled) {
        tvBack.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    public void disableButtonClick() {
        tvBack.setClickable(false);
        lltMenu.setClickable(false);
    }

    public void enableButtonClick() {
        tvBack.setClickable(true);
        lltMenu.setClickable(true);
    }

    public void setLeftIcon(int iconResId) {
        setEnabled(true);
        tvBack.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
    }

    public void setLeftOnClickListener(OnClickListener listener) {
        setLeftTitle(null, R.drawable.ic_back_black, listener);
    }

    public void setLeftIcon(int iconResId, OnClickListener listener) {
        setLeftTitle(null, iconResId, listener);
    }

    public void setLeftTitle(String title, int iconResId, OnClickListener listener) {
        setEnabled(true);
        tvBack.setText(title == null ? "" : title);
        tvBack.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        tvBack.setOnClickListener(listener);
    }

    /**
     * 隐藏返回不显示
     */
    public void hideBack() {
        tvBack.setVisibility(GONE);
    }

    /**
     * 隐藏菜单不显示
     */
    public void hideMenu() {
        lltMenu.setVisibility(GONE);
    }

    /**
     * contentView当Menu
     *
     * @param contentView
     * @param listener
     * @return
     */
    public View setMenu(View contentView, OnClickListener listener) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = ViewUtil.dip2px(mContext, 15);
        lltMenu.addView(contentView, params);
        contentView.setOnClickListener(listener);
        lltMenu.setVisibility(VISIBLE);
        return lltMenu;
    }

    /**
     * 图片+文字 当Menu
     *
     * @param iconResID
     * @param text
     * @param listener
     * @return
     */
    public View setMenu(int iconResID, String text, OnClickListener listener) {
        lltMenu.setVisibility(VISIBLE);
        ivCustomMenuIcon.setVisibility(GONE);
        if (iconResID == 0 && TextUtils.isEmpty(text)) {
            tvCustomMenu.setVisibility(View.GONE);
        } else {
            tvCustomMenu.setVisibility(View.VISIBLE);
            tvCustomMenu.setText(text);
            tvCustomMenu.setCompoundDrawablesWithIntrinsicBounds(iconResID, 0, 0, 0);
            tvCustomMenu.setOnClickListener(listener);
        }
        return tvCustomMenu;
    }

    /**
     * 图片+文字 当Menu
     *
     * @param iconResID
     * @param text
     * @param textColor
     * @param listener
     * @return
     */
    public View setMenu(int iconResID, String text, int textColor, OnClickListener listener) {
        lltMenu.setVisibility(VISIBLE);
        ivCustomMenuIcon.setVisibility(GONE);
        if (iconResID == 0 && TextUtils.isEmpty(text)) {
            tvCustomMenu.setVisibility(View.GONE);
        } else {
            tvCustomMenu.setVisibility(View.VISIBLE);
            tvCustomMenu.setText(text);
            tvCustomMenu.setTextColor(textColor);
            tvCustomMenu.setCompoundDrawablesWithIntrinsicBounds(iconResID, 0, 0, 0);
            tvCustomMenu.setOnClickListener(listener);
        }
        return tvCustomMenu;
    }

    /**
     * 文字Menu
     *
     * @param text
     * @param listener
     * @return
     */
    public TextView setMenu(String text, OnClickListener listener) {
        lltMenu.setVisibility(VISIBLE);
        ivCustomMenuIcon.setVisibility(GONE);
        if (TextUtils.isEmpty(text)) {
            tvCustomMenu.setVisibility(View.GONE);
        } else {
            tvCustomMenu.setVisibility(View.VISIBLE);
            tvCustomMenu.setText(text);
            tvCustomMenu.setOnClickListener(listener);
        }
        return tvCustomMenu;
    }

    /**
     * 图标Menu
     *
     * @param iconResID
     * @param listener
     */
    public void setMenu(int iconResID, OnClickListener listener) {
        lltMenu.setVisibility(VISIBLE);
        tvCustomMenu.setVisibility(GONE);
        ivCustomMenuIcon.setVisibility(VISIBLE);
        ivCustomMenuIcon.setImageResource(iconResID);
        ivCustomMenuIcon.setOnClickListener(listener);
    }

    public TextView getCustomMenu() {
        return tvCustomMenu;
    }
}
