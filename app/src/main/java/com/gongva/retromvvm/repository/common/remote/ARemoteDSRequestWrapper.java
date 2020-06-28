package com.gongva.retromvvm.repository.common.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.gongva.library.plugin.netbase.ARemoteDSRequestManager;
import com.gongva.library.plugin.netbase.NetConstant;
import com.gongva.library.plugin.netbase.RequestObserverCallback;
import com.gongva.library.plugin.netbase.RequestObserverCallbackAdapter;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.library.plugin.netbase.exception.ApiException;
import com.gongva.library.plugin.netbase.linstener.IDataSourceDataClear;
import com.gongva.library.plugin.netbase.linstener.IRemoteRequestDisposable;
import com.gongva.library.plugin.netbase.linstener.ImageRequestCallbackAdapter;
import com.gongva.library.plugin.netbase.scheduler.SchedulerProvider;
import com.gongva.library.plugin.netbase.transformer.ResponseTransformer;
import com.hik.core.android.api.GsonUtil;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

/**
 * 远端数据请求包装器
 * 包含：
 * 1：订阅清理
 * 2：请求包装
 * <p>
 * 用于取消RxJava2的订阅
 * https://blog.csdn.net/j550341130/article/details/80540759
 *
 * @author gongwei
 * @data 2019/3/18
 * @email shmily__vivi@163.com
 */
public class ARemoteDSRequestWrapper implements IDataSourceDataClear, IRemoteRequestDisposable {

    private ARemoteDSRequestManager mDSRequestManager;

    public ARemoteDSRequestWrapper() {
        mDSRequestManager = ARemoteDSRequestManager.newInstance(getClass().getSimpleName());
    }

    public synchronized int getCacheDisposableCount() {
        return mDSRequestManager.getCacheDisposableCount();
    }

    public synchronized void setCacheDisposableCount(int cacheDisposableCount) {
        mDSRequestManager.setCacheDisposableCount(cacheDisposableCount);
    }

    @Override
    public void clear(int tag) {
        mDSRequestManager.clear(tag);
    }

    @Override
    public void clearAll() {
        mDSRequestManager.clearAll();
    }

    @Override
    public boolean isAllDisposed() {
        return mDSRequestManager.isAllDisposed();
    }

    @Override
    public boolean isTagDisposed(int tag) {
        return mDSRequestManager.isTagDisposed(tag);
    }

    @Override
    public <D> DisposableObserver<D> addDisposable(@NonNull RequestObserverCallback<D> d) {
        return mDSRequestManager.addDisposable(d);
    }

    @Override
    public boolean removeDisposable(Disposable d) {
        return mDSRequestManager.removeDisposable(d);
    }

    @Override
    public boolean deleteDisposable(Disposable d) {
        return mDSRequestManager.deleteDisposable(d);
    }

    /**
     * 包装Extra网络请求,请求在io线程直线，在main线程回调
     * 默认请求observable放到ARemoteDSClearer管理
     *
     * @param observable 请求可观察者
     * @param callback   请求回调适配器
     * @param <D>        请求回调类型
     */
    public <D> void wrapperExtraRequestObservable(@NonNull Observable<D> observable,
                                                  @NonNull RequestObserverCallbackAdapter<D> callback) {
        wrapperExtraRequestObservable(observable, callback, true);
    }

    /**
     * 包Extra装网络请求,请求在io线程直线，在main线程回调
     *
     * @param observable    请求可观察者
     * @param callback      请求回调适配器
     * @param addDisposable 是否添加Observable释放管理
     * @param <D>           请求回调类型
     */
    public <D> void wrapperExtraRequestObservable(@NonNull Observable<D> observable,
                                                  @NonNull RequestObserverCallbackAdapter<D> callback, boolean addDisposable) {
        if (addDisposable) {
            observable.compose(SchedulerProvider.applySchedulers())
                    .subscribe(addDisposable(callback));
        } else {
            observable.compose(SchedulerProvider.applySchedulers())
                    .subscribe(callback);
        }
    }

    /**
     * 包装网络请求,请求在io线程直线，在main线程回调
     * 默认请求observable放到ARemoteDSClearer管理
     *
     * @param observable 请求可观察者
     * @param callback   请求回调适配器
     * @param <D>        请求回调类型
     */
    public <D> void wrapperRequestObservable(@NonNull Observable<ResponseResult<D>> observable,
                                             @NonNull RequestObserverCallbackAdapter<ResponseResult<D>> callback) {
        wrapperRequestObservable(observable, callback, true);
    }

    /**
     * 包装网络请求,请求在io线程直线，在main线程回调
     *
     * @param observable    请求可观察者
     * @param callback      请求回调适配器
     * @param addDisposable 是否添加Observable释放管理
     * @param <D>           请求回调类型
     */
    public <D> void wrapperRequestObservable(@NonNull Observable<ResponseResult<D>> observable,
                                             @NonNull RequestObserverCallbackAdapter<ResponseResult<D>> callback, boolean addDisposable) {
        if (addDisposable) {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applySchedulers())
                    .subscribe(addDisposable(callback));
        } else {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applySchedulers())
                    .subscribe(callback);
        }
    }

    /**
     * 包装网络请求,请求在io线程直线，在main线程回调
     * 默认请求observable放到ARemoteDSClearer管理
     *
     * @param observable 请求可观察者
     * @param callback   请求回调适配器
     * @param <D>        请求回调类型
     */
    public <D> void wrapperRequestObservable(@NonNull Observable<ResponseResult<D>> observable,
                                             @NonNull RequestObserverCallback<ResponseResult<D>> callback) {
        wrapperRequestObservable(observable, callback, true);
    }

    /**
     * 包装网络请求,请求在io线程直线，在main线程回调
     *
     * @param observable    请求可观察者
     * @param callback      请求回调适配器
     * @param addDisposable 是否添加Observable释放管理
     * @param <D>           请求回调类型
     */
    public <D> void wrapperRequestObservable(@NonNull Observable<ResponseResult<D>> observable,
                                             @NonNull RequestObserverCallback<ResponseResult<D>> callback, boolean addDisposable) {
        if (addDisposable) {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applySchedulers())
                    .subscribe(addDisposable(callback));
        } else {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applySchedulers())
                    .subscribe(callback);
        }
    }

    /**
     * 包装网络请求,请求在io线程直线，在新线程回调
     * 需要调用ThreadConvertUtils切换线程
     * 默认请求observable放到ARemoteDSClearer管理
     *
     * @param observable 请求可观察者
     * @param callback   请求回调适接口
     * @param <D>        请求回调类型
     */
    public <D> void wrapperRequestObservableOnNewThread(@NonNull Observable<ResponseResult<D>> observable,
                                                        @NonNull RequestObserverCallbackAdapter<ResponseResult<D>> callback) {
        wrapperRequestObservableOnNewThread(observable, callback, true);
    }

    /**
     * 包装网络请求,请求在io线程直线，在新线程回调
     * 需要调用ThreadConvertUtils切换线程
     *
     * @param observable    请求可观察者
     * @param callback      请求回调适接口
     * @param addDisposable 是否添加Observable释放管理
     * @param <D>           请求回调类型
     */
    public <D> void wrapperRequestObservableOnNewThread(@NonNull Observable<ResponseResult<D>> observable,
                                                        @NonNull RequestObserverCallbackAdapter<ResponseResult<D>> callback, boolean addDisposable) {
        if (addDisposable) {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applyNewThreadSchedulers())
                    .subscribe(addDisposable(callback));
        } else {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applyNewThreadSchedulers())
                    .subscribe(callback);
        }
    }

    /**
     * 包装网络请求,请求在io线程直线，在新线线程回调
     * 需要调用ThreadConvertUtils切换线程
     * 默认请求observable放到ARemoteDSClearer管理
     *
     * @param observable 请求可观察者
     * @param callback   请求回调适接口
     * @param <D>        请求回调类型
     */
    public <D> void wrapperRequestObservableOnNewThread(@NonNull Observable<ResponseResult<D>> observable,
                                                        @NonNull RequestObserverCallback<ResponseResult<D>> callback) {
        wrapperRequestObservableOnNewThread(observable, callback, true);
    }

    /**
     * 包装网络请求,请求在io线程直线，在新线线程回调
     * 需要调用ThreadConvertUtils切换线程
     *
     * @param observable    请求可观察者
     * @param callback      请求回调适接口
     * @param addDisposable 是否添加Observable释放管理
     * @param <D>           请求回调类型
     */
    public <D> void wrapperRequestObservableOnNewThread(@NonNull Observable<ResponseResult<D>> observable,
                                                        @NonNull RequestObserverCallback<ResponseResult<D>> callback, boolean addDisposable) {
        if (addDisposable) {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applyNewThreadSchedulers())
                    .subscribe(addDisposable(callback));
        } else {
            observable.compose(ResponseTransformer.handleResult(new ServiceResponseFunction<>()))
                    .compose(SchedulerProvider.applyNewThreadSchedulers())
                    .subscribe(callback);
        }
    }

    /**
     * 包装Image网络请求库，请求在io线程直线，在main线程回调
     *
     * @param observable 请求可观察者
     * @param callback   请求回调接口
     */
    public void wrapperImageRequestObservable(@NonNull Observable<ResponseBody> observable,
                                              @NonNull ImageRequestCallbackAdapter callback) {
        wrapperImageRequestObservable(observable, callback, true);
    }

    /**
     * 包装Image网络请求库，请求在io线程直线，在main线程回调
     *
     * @param observable    请求可观察者
     * @param callback      请求回调接口
     * @param addDisposable 是否添加Observable释放管理
     */
    public void wrapperImageRequestObservable(@NonNull Observable<ResponseBody> observable,
                                              @NonNull ImageRequestCallbackAdapter callback, boolean addDisposable) {
        RequestObserverCallback requestObserverCallback = new RequestObserverCallback<ResponseResult<Bitmap>>() {
            @Override
            public void loadStart() {
                callback.loadStart();
            }

            @Override
            public void loadFinish() {
                callback.loadFinish();
            }

            @Override
            public void loadData(ResponseResult<Bitmap> contentResult) {
                if (contentResult != null) {
                    callback.loadData(contentResult);
                } else {
                    callback.loadError(new ApiException(-1, "请求错误"));
                }
            }

            @Override
            public void loadError(ApiException e) {
                callback.loadError(e);
            }
        };
        Observable ob = observable
                .compose(ResponseTransformer.handleBlockResult())
                .subscribeOn(SchedulerProvider.getInstance().io())//在新线程中实现该方法
                .map(responseBody -> {
                    ResponseResult<Bitmap> result = null;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null && NetConstant.MiniType.APPLICATION.equals(contentType.type())) {
                        String resultContent = responseBody.string();
                        result = GsonUtil.jsonDeserializer(resultContent, ResponseResult.class);
                    } else {
                        try {
                            byte[] bys = responseBody.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                            result = new ResponseResult<>();
                            result.setSuccess(true);
                            result.setData(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return result;
                })
                .observeOn(AndroidSchedulers.mainThread());//在Android主线程中展示
        if (addDisposable) {
            ob.subscribe(addDisposable(requestObserverCallback));
        } else {
            ob.subscribe(requestObserverCallback);
        }
    }
}
