package com.gongva.retromvvm.base.component.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import com.gongva.retromvvm.ui.login.LoginViewModel;


/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class GeneralViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile GeneralViewModelFactory INSTANCE;

    private final Application mApplication;

    public static GeneralViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (GeneralViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GeneralViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private GeneralViewModelFactory(Application application) {
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication);
        } // else if ...

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
