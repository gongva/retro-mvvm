package com.gongva.retromvvm.base.component.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.gongva.library.app.ui.view.databindadapter.IRefreshViewModel;

/**
 * view model 刷新服务
 *
 * @data 2019/3/8
 */
public abstract class ARefreshViewModel extends AndroidViewModel implements IRefreshViewModel {

    private final MutableLiveData<Pair<Boolean, String>> dataLoadingLD = new MutableLiveData();
    private final MutableLiveData<Boolean> dataMoreLoadingLD = new MutableLiveData();
    private ARefreshViewModelCallback aRefreshViewModelCallback;
    private ALoadMoreViewModelCallback aLoadMoreViewModelCallback;

    public ARefreshViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Pair<Boolean, String>> getDataLoadingLD() {
        return dataLoadingLD;
    }

    public MutableLiveData<Boolean> getDataMoreLoadingLD() {
        return dataMoreLoadingLD;
    }

    public void startLoading() {
        dataLoadingLD.setValue(new Pair<>(true, null));
    }

    public void startLoading(String msg) {
        dataLoadingLD.setValue(new Pair<>(true, msg));
    }

    public void stopLoading() {
        dataLoadingLD.setValue(new Pair<>(false, null));
    }

    public void startLoadingMore() {
        dataMoreLoadingLD.setValue(true);
    }

    public void stopLoadingMore() {
        dataMoreLoadingLD.setValue(false);
    }

    public ARefreshViewModelCallback getaRefreshViewModelCallback() {
        return aRefreshViewModelCallback;
    }

    public void setaRefreshViewModelCallback(ARefreshViewModelCallback aRefreshViewModelCallback) {
        this.aRefreshViewModelCallback = aRefreshViewModelCallback;
    }

    public ALoadMoreViewModelCallback getaLoadMoreViewModelCallback() {
        return aLoadMoreViewModelCallback;
    }

    public void setaLoadMoreViewModelCallback(ALoadMoreViewModelCallback aLoadMoreViewModelCallback) {
        this.aLoadMoreViewModelCallback = aLoadMoreViewModelCallback;
    }

    public interface ARefreshViewModelCallback {
        void onRefreshCallback();
    }

    public interface ALoadMoreViewModelCallback {
        void onLoadMoreCallback();
    }
}
