package com.gongva.retromvvm.base.component;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongva.library.app.base.IInitLoadingController;
import com.gongva.library.app.base.ILoadingErrorController;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.common.view.errorpage.GeneralErrorPage;
import com.gongva.retromvvm.common.view.navigation.GeneralNavigationBar;
import com.hik.core.android.api.KeyboardUtil;
import com.hik.core.android.view.ViewUtil;
import com.noober.background.drawable.DrawableCreator;

/**
 * Activity基类
 *
 * @author gongwei
 * @created 2018/12/19.
 */
public abstract class BaseActivity<B extends ViewDataBinding> extends ActivitySupportWrapper implements IInitLoadingController, ILoadingErrorController {

    public static final String DEFAULT_BACK_TEXT = "返回";

    public B mBinding;

    protected ViewGroup mRootView;
    protected GeneralNavigationBar mNavigationBar;

    private ViewGroup contentLayout;
    private GeneralErrorPage errorPage;

    private ViewGroup loadingPanel;
    private ViewGroup loadingBackGround;
    private TextView loadingText;
    private ImageView loadingIcon;

    private View contentView;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_base);

        init();
    }

    private void init() {
        mRootView = findViewById(R.id.app_root_view);
        mNavigationBar = findViewById(R.id.app_navigation);
        contentLayout = findViewById(R.id.app_content);
        loadingPanel = findViewById(R.id.app_loading_panel);
        loadingBackGround = findViewById(R.id.app_loading_panel_bg);
        loadingText = findViewById(R.id.tv_init_loading);
        loadingIcon = findViewById(R.id.iv_init_loading);
        errorPage = findViewById(R.id.app_error_page);
        int contentResID = getContentLayoutId();
        if (contentResID > 0) {
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), contentResID, contentLayout, false);
            contentView = mBinding.getRoot();
            contentLayout.addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        initView();
        initData();
    }

    public View getRootView() {
        return mRootView;
    }

    public View getContentLayoutView() {
        return contentView;
    }

    public ViewGroup getContentLayout() {
        return contentLayout;
    }

    public GeneralNavigationBar getNavigationBar() {
        return mNavigationBar;
    }

    /**
     * 是否启用导航菜单
     *
     * @param enabled
     */
    public void setNavigationBarEnabled(boolean enabled) {
        mNavigationBar.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    public void hideNavigationBar() {
        setNavigationBarEnabled(false);
    }

    public void showNavigationBar() {
        setNavigationBarEnabled(true);
    }

    public void setTitle(CharSequence title) {
        mNavigationBar.setText(title);
    }

    //-------------------Back-------------------
    public void setBack(View.OnClickListener listener) {
        KeyboardUtil.hideSoftKeyboard(this);
        setBack(DEFAULT_BACK_TEXT, listener);
    }

    public void setBack(String title, final View.OnClickListener listener) {
        mNavigationBar.setLeftTitle(title, R.drawable.ic_back_black, listener);
    }

    public void setBack(int iconId) {
        mNavigationBar.setLeftIcon(iconId);
    }

    public void setBack(int iconId, View.OnClickListener listener) {
        mNavigationBar.setLeftIcon(iconId, listener);
    }

    public void setBack(String title, int iconId, View.OnClickListener listener) {
        mNavigationBar.setLeftTitle(title, iconId, listener);
    }

    //-------------------Menu-------------------

    /**
     * View as menu.
     */
    public View setMenu(View contentView, View.OnClickListener listener) {
        return mNavigationBar.setMenu(contentView, listener);
    }

    /**
     * 图标Menu
     */
    public void setMenu(int iconResId, View.OnClickListener listener) {
        mNavigationBar.setMenu(iconResId, listener);
    }

    /**
     * 文字Menu
     */
    public void setMenu(String text, View.OnClickListener listener) {
        mNavigationBar.setMenu(text, listener);
    }

    /**
     * 图片+文字Menu
     */
    public void setMenu(String text, int iconResId, View.OnClickListener listener) {
        mNavigationBar.setMenu(iconResId, text, listener);
    }

    /**
     * 图片+文字Menu
     */
    public void setMenuBlue(String text, View.OnClickListener listener) {
        mNavigationBar.setMenu(0, text, Color.parseColor("#4E91F9"), listener);
    }

    public void hideMenu() {
        mNavigationBar.hideMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    /**
     * 带白色背景的Init Loading
     */
    public void showInitLoadingWithBackground() {
        Drawable loadingBgDrawable = new DrawableCreator.Builder().setSolidColor(Color.parseColor("#FFFFFF")).setCornersRadius(ViewUtil.dip2px(this, 4)).build();
        loadingBackGround.setBackground(loadingBgDrawable);
        showInitLoading();
    }

    @Override
    public void showInitLoading() {
        showInitLoading("加载中...");
    }

    @Override
    public void showInitLoading(String text) {
        dismissErrorPage();
        contentLayout.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.VISIBLE);
        loadingText.setText(text);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingIcon.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void dismissInitLoading() {
        loadingPanel.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingIcon.getDrawable();
        animationDrawable.stop();
    }

    @Override
    public void showErrorPage() {
        showErrorPage(null);
    }

    @Override
    public void showErrorPage(String message) {
        showErrorPage(0, message);
    }

    @Override
    public void showErrorPage(int icResource, String message) {
        showErrorPage(icResource, message, null, null);
    }

    @Override
    public void showErrorPage(String icResource, String message) {
        showErrorPage(icResource, message, null, null);
    }

    @Override
    public void showErrorPage(String message, String action, View.OnClickListener listener) {
        showErrorPage(0, message, action, listener);
    }

    @Override
    public void showErrorPage(int icResource, String message, String action, View.OnClickListener listener) {
        dismissInitLoading();
        dismissLoadingDialog();
        contentLayout.setVisibility(View.GONE);
        errorPage.showErrorPage(icResource, message, action, listener);
    }

    @Override
    public void showErrorPage(String icResource, String message, String action, View.OnClickListener listener) {
        dismissInitLoading();
        dismissLoadingDialog();
        contentLayout.setVisibility(View.GONE);
        errorPage.showErrorPage(icResource, message, action, listener);
    }

    @Override
    public void showErrorPageForHttp(int statusCode, String responseString) {
        dismissInitLoading();
        dismissLoadingDialog();
        contentLayout.setVisibility(View.GONE);
        errorPage.showErrorPageForHttp(statusCode, responseString);
    }

    @Override
    public void dismissErrorPage() {
        errorPage.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoadingDialog() {
        super.dismissLoadingDialog();
        dismissInitLoading();
        dismissErrorPage();
        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    protected abstract int getContentLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
