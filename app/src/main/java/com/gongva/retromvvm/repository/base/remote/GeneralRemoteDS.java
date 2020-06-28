package com.gongva.retromvvm.repository.base.remote;

import android.support.annotation.NonNull;

import com.gongva.library.plugin.netbase.RequestObserverCallbackAdapter;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.retromvvm.common.SystemConfig;
import com.gongva.retromvvm.repository.common.remote.ARemoteDSRequestWrapper;
import com.gongva.retromvvm.repository.common.remote.ApiGenerator;
import com.gongva.retromvvm.repository.common.remote.IApi;

/**
 * 通用公网远端数据源
 *
 * @data 2019/3/13
 */
public class GeneralRemoteDS extends ARemoteDSRequestWrapper {

    private volatile static GeneralRemoteDS INSTANCE;
    private IApi mIApi;

    public static GeneralRemoteDS getInstance() {
        if (INSTANCE == null) {
            synchronized (GeneralRemoteDS.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GeneralRemoteDS();
                }
            }
        }
        return INSTANCE;
    }

    public GeneralRemoteDS() {
        mIApi = ApiGenerator.getApi();
    }

    /**
     * 获取系统配置
     *
     * @param callback
     */
    public void getSysConfig(@NonNull RequestObserverCallbackAdapter<ResponseResult<SystemConfig>> callback) {
        wrapperRequestObservable(mIApi.getSysConfig(), callback, false);
    }
}
