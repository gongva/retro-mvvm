package com.gongva.retromvvm.base.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.base.IInitController;
import com.gongva.library.app.base.IInitLoadingController;
import com.gongva.library.app.base.ILoadingController;
import com.gongva.library.app.base.ILoadingErrorController;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.common.view.errorpage.GeneralErrorPage;
import com.gongva.retromvvm.common.view.navigation.GeneralNavigationBar;

/**
 * Fragment基类
 *
 * @author gongwei
 * @created 2018/12/20.
 */
public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment implements IInitLoadingController, ILoadingErrorController, IInitController {

    public B mBinding;

    private ILoadingController loadingController;
    private ViewGroup mRootView;
    private GeneralNavigationBar mNavigationBar;
    private ViewGroup contentLayout;

    protected GeneralErrorPage errorPage;

    private ViewGroup loadingPanel;
    private TextView loadingText;
    private ImageView loadingIcon;
    private View contentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loadingController = (ILoadingController) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.activity_app_base, container, false);
        init();
        return mRootView;
    }

    private void init() {
        mRootView = mRootView.findViewById(R.id.app_root_view);
        mNavigationBar = mRootView.findViewById(R.id.app_navigation);
        contentLayout = mRootView.findViewById(R.id.app_content);
        loadingPanel = mRootView.findViewById(R.id.app_loading_panel);
        loadingText = mRootView.findViewById(R.id.tv_init_loading);
        loadingIcon = mRootView.findViewById(R.id.iv_init_loading);
        errorPage = mRootView.findViewById(R.id.app_error_page);
        int contentResID = getContentLayoutId();
        if (contentResID > 0) {
            contentView = View.inflate(getContext(), contentResID, null);
            contentLayout.addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public boolean isInitFinish() {
        return mBinding != null;
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
     * 判断当前activity是否在顶部
     *
     * @return
     */
    public boolean isTopActivity() {
        Activity topActivity = TinkerApplicationCreate.getInstance().getActivityTop();
        return topActivity == getActivity();
    }

    /**
     * 是否启用导航菜单
     *
     * @param enabled
     */
    public void setActionBarEnabled(boolean enabled) {
        mNavigationBar.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    public void hideActionBar() {
        setActionBarEnabled(false);
    }

    public void showActionBar() {
        setActionBarEnabled(true);
    }

    public void setTitle(CharSequence title) {
        mNavigationBar.setText(title);
    }

    //-------------------Back-------------------

    public void setHomeAsUpTitle(View.OnClickListener listener) {
        setHomeAsUpTitle(BaseActivity.DEFAULT_BACK_TEXT, listener);
    }

    public void setHomeAsUpTitle(String title, final View.OnClickListener listener) {
        mNavigationBar.setLeftTitle(title, R.drawable.ic_back_black, listener);
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
    public void showLoadingDialog() {
        loadingController.showLoadingDialog("正在加载");
    }

    @Override
    public void showLoadingDialog(CharSequence message) {
        loadingController.showLoadingDialog(message);
    }

    @Override
    public void showLoadingDialog(CharSequence message, DialogInterface.OnCancelListener listener) {
        loadingController.showLoadingDialog(message, listener);
    }

    @Override
    public void showInitLoading() {
        showInitLoading("正在加载中");
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
        loadingController.dismissLoadingDialog();
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
    public void showErrorPage(String message, String action, View.OnClickListener listener) {
        showErrorPage(0, message, action, listener);
    }

    @Override
    public void showErrorPage(int icResource, String message, String action, View.OnClickListener listener) {
        dismissInitLoading();
        dismissLoadingDialog();
        loadingPanel.setVisibility(View.GONE);
        contentLayout.setVisibility(View.GONE);
        errorPage.showErrorPage(icResource, message, action, listener);
    }

    public void showErrorPageForHttp(int statusCode, String responseString) {
        dismissInitLoading();
        dismissLoadingDialog();
        loadingPanel.setVisibility(View.GONE);
        contentLayout.setVisibility(View.GONE);
        errorPage.showErrorPageForHttp(statusCode, responseString);
    }

    @Override
    public void dismissErrorPage() {
        errorPage.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoadingDialog() {
        loadingController.dismissLoadingDialog();
        dismissInitLoading();
        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract int getContentLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
