package com.gongva.retromvvm.base.component;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.gongva.retromvvm.base.component.viewmodel.GeneralViewModel;
import com.gongva.retromvvm.base.component.viewmodel.ViewModelOperateEnum;
import com.hik.core.android.api.KeyboardUtil;

/**
 * ViewModule Base Activity
 *
 * @data 2019/3/8
 */
public abstract class BaseVMActivity<T extends GeneralViewModel, B extends ViewDataBinding> extends BaseActivity<B> {

    public T mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initViewModelOB();
        super.onCreate(savedInstanceState);
    }

    private void initViewModelOB() {
        mViewModel = initViewModel();
        if (mViewModel != null) {
            mViewModel.getOperateEnumLD().observe(this, (ViewModelOperateEnum operateEnum) -> {
                switch (operateEnum) {
                    case HIDE_SOFT_KEYBOARD:
                        KeyboardUtil.hideSoftKeyboard(this);
                        break;
                    case FINISH_CURRENT_ACTIVITY:
                        finish();
                        break;
                }
            });
            mViewModel.getDataLoadingLD().observe(this, (Pair<Boolean, String> pair) -> {
                if (pair.first) {
                    if (TextUtils.isEmpty(pair.second)) {
                        showLoadingDialog();
                    } else {
                        showLoadingDialog(pair.second);
                    }
                } else {
                    dismissLoadingDialog();
                }
            });
        }
    }

    protected abstract T initViewModel();
}
