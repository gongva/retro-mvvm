package com.gongva.retromvvm.repository.login;

import android.support.annotation.NonNull;

import com.gongva.library.plugin.netbase.RequestObserverCallbackAdapter;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.retromvvm.common.manager.login.LoginInfo;
import com.gongva.retromvvm.repository.common.remote.ARemoteDSRequestWrapper;
import com.gongva.retromvvm.repository.common.remote.ApiGenerator;
import com.gongva.retromvvm.repository.common.remote.IApi;
import com.gongva.retromvvm.repository.common.remote.RequestUtils;
import com.google.gson.JsonObject;

/**
 * 用户登录相关的Remote Api
 *
 * @author gongwei
 * @date 2019/6/25
 * @mail gongwei5@hikcreate.com
 */
public class LoginRemoteDS extends ARemoteDSRequestWrapper {
    private static volatile LoginRemoteDS INSTANCE;
    private IApi mApi;

    public static LoginRemoteDS getInstance() {
        if (INSTANCE == null) {
            synchronized (LoginRemoteDS.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoginRemoteDS();
                }
            }
        }
        return INSTANCE;
    }

    private LoginRemoteDS() {
        mApi = ApiGenerator.getApi();
    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @param callback
     */
    public void login(@NonNull String phone, @NonNull String password, @NonNull RequestObserverCallbackAdapter<ResponseResult<LoginInfo>> callback) {
        JsonObject param = new JsonObject();
        param.addProperty("phone", phone);
        param.addProperty("password", password);
        wrapperRequestObservable(mApi.login(RequestUtils.convertFromJsonObject(param)), callback);
    }

    /**
     * 登出
     *
     * @param callback
     */
    public void logout(@NonNull String token, @NonNull RequestObserverCallbackAdapter<ResponseResult<Void>> callback) {
        wrapperRequestObservable(mApi.logout(token), callback);
    }
}
