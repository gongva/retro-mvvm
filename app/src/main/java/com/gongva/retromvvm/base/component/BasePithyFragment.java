package com.gongva.retromvvm.base.component;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongva.library.app.base.IInitController;
import com.gongva.library.app.base.ILoadingController;

/**
 * Fragment简洁精炼的基类
 *
 * @author gongwei
 * @created 2018/12/20.
 */
public abstract class BasePithyFragment<B extends ViewDataBinding> extends Fragment implements ILoadingController, IInitController {

    public B mBinding;
    protected ILoadingController mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoadingDialog = (ILoadingController) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getContentLayoutId(), container, false);
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public boolean isInitFinish() {
        return mBinding != null;
    }

    protected abstract int getContentLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.showLoadingDialog("正在加载");
    }

    @Override
    public void showLoadingDialog(CharSequence message) {
        mLoadingDialog.showLoadingDialog(message);
    }

    @Override
    public void showLoadingDialog(CharSequence message, DialogInterface.OnCancelListener listener) {
        mLoadingDialog.showLoadingDialog(message, listener);
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismissLoadingDialog();
    }
}
