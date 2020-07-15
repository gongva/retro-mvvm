package com.gongva.retromvvm.base.component.viewmodel;


import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

/**
 * viewmodel创建工具类
 *
 * @data 2019/3/8
 */
public class ViewModelUtils {

    public static ARefreshViewModel obtainViewModel(FragmentActivity activity, Class<? extends ARefreshViewModel> viewModelClass) {
        // Use a Factory to inject dependencies into the ViewModel
        GeneralViewModelFactory factory = GeneralViewModelFactory.getInstance(activity.getApplication());
        ARefreshViewModel viewModel = ViewModelProviders.of(activity, factory).get(viewModelClass);
        return viewModel;
    }
}