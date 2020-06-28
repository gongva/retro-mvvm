package com.gongva.retromvvm.base.component;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gongva.library.app.TinkerApplicationCreate;
import com.hik.core.android.api.KeyboardUtil;

/**
 * Activity简洁精炼的基类
 *
 * @data 2019/3/8
 */
public abstract class BasePithyActivity<B extends ViewDataBinding> extends ActivitySupportWrapper {

    public B mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getContentLayoutId());
        initView();
        initData();
    }

    protected abstract int getContentLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeyboardUtil.hideSoftKeyboard(this);
    }

    @Override
    public void finish() {
        super.finish();
        TinkerApplicationCreate.getInstance().getAppStatus().onActivityDestroyed(this);
    }
}
