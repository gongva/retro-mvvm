package com.gongva.retromvvm.base.component;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Pair;

import com.gongva.retromvvm.base.component.viewmodel.GeneralViewModel;
import com.gongva.retromvvm.base.component.viewmodel.ViewModelOperateEnum;
import com.hik.core.android.api.KeyboardUtil;

/**
 * Data Binding + ViewModel Fragment基类
 *
 * @author gongwei
 * @created 2018/12/20.
 */
public abstract class BaseVMFragment<T extends GeneralViewModel, B extends ViewDataBinding> extends BaseFragment<B> {

    public T mViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViewModelOB();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean isInitFinish() {
        return super.isInitFinish() && mViewModel != null;
    }

    private void initViewModelOB() {
        mViewModel = initViewModel();
        if (mViewModel != null) {
            mViewModel.getOperateEnumLD().observe(this, (ViewModelOperateEnum operateEnum) -> {
                switch (operateEnum) {
                    case HIDE_SOFT_KEYBOARD:
                        KeyboardUtil.hideSoftKeyboard(getActivity());
                        break;
                    case FINISH_CURRENT_ACTIVITY:
                        FragmentActivity activityHost = getActivity();
                        if (activityHost != null) {
                            activityHost.finish();
                        }
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
