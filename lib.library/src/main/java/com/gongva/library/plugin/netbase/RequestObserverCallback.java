package com.gongva.library.plugin.netbase;

import com.gongva.library.plugin.netbase.entity.ABaseResult;
import com.gongva.library.plugin.netbase.exception.ApiException;
import com.gongva.library.plugin.netbase.exception.CustomerError;
import com.gongva.library.plugin.netbase.entity.ClassTypeEntity;
import com.hik.core.android.api.LogCat;

import io.reactivex.observers.DisposableObserver;

/**
 * 用于统一控制Rx网络请求回调
 *
 * @author yslei
 * @data 2019/3/8
 * @email leiyongsheng@hikcreate.com
 */
public abstract class RequestObserverCallback<T> extends DisposableObserver<T> {

    public static final int DEFAULT_REQUEST_TAG = -1001;
    private Class mClazzType;
    private ClassTypeEntity mClassTypeEntity;
    private int mTag;

    public RequestObserverCallback() {
        this(DEFAULT_REQUEST_TAG);
    }

    public RequestObserverCallback(int tag) {
        mTag = tag;
        mClassTypeEntity = new ClassTypeEntity();
        mClazzType = GenericityTypeUtils.getSuperClassGenericType(getClass(), mClassTypeEntity);
    }

    public int getTag() {
        return mTag;
    }

    public Class getClazzType() {
        return mClazzType;
    }

    public ClassTypeEntity getClassTypeEntity() {
        return mClassTypeEntity;
    }

    public abstract void loadStart();

    @Override
    public void onStart() {
        super.onStart();
        LogCat.d(NetConstant.TAG, "request start");
        loadStart();
    }

    @Override
    public void onComplete() {
        if (!this.isDisposed()) this.dispose();
        ARemoteDSRequestManager.clearDisposable(this);
        LogCat.d(NetConstant.TAG, "result finish");
        loadFinish();
    }

    public abstract void loadFinish();

    @Override
    public void onError(Throwable e) {
        if (!this.isDisposed()) this.dispose();
        ARemoteDSRequestManager.clearDisposable(this);
        if (e instanceof ApiException) {
            long threadId = ((ApiException) e).getThreadId();
            if (threadId >= 0) {
                e.printStackTrace();
                LogCat.d(NetConstant.TAG, "get error[" + threadId + "]:" + e.toString());
            } else {
                LogCat.d(NetConstant.TAG, "get error[unknown]:" + e.toString());
            }
            loadError((ApiException) e);
        } else {
            e.printStackTrace();
            LogCat.d(NetConstant.TAG, "get error[unknown]:" + e.getMessage());
            loadError(new ApiException(e, CustomerError.UNKNOWN));
        }
    }

    public abstract void loadError(ApiException ex);

    @Override
    public void onNext(T t) {
        LogCat.d(NetConstant.TAG, "result data:" + t);
        if (t != null) {
            if (t instanceof ABaseResult && getClassTypeEntity() != null) {
                ((ABaseResult) t).setClassTypeEntity(getClassTypeEntity().getChildTypeEntity());
            }
            loadData(t);
        } else {
            loadError(new ApiException(CustomerError.CONTENT_JSON_NULL_ERROR, "数据为空"));
        }
    }

    public abstract void loadData(T t);
}
