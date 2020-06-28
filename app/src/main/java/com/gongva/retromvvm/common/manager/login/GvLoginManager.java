package com.gongva.retromvvm.common.manager.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.app.ui.login.LoginManager;
import com.gongva.library.plugin.eventbus.BusProvider;
import com.gongva.library.plugin.netbase.ARemoteDSRequestManager;
import com.gongva.library.plugin.netbase.RequestObserverCallbackAdapter;
import com.gongva.library.plugin.netbase.entity.ResponseResult;
import com.gongva.library.plugin.netbase.exception.ApiException;
import com.gongva.retromvvm.common.GvContext;
import com.gongva.retromvvm.common.event.login.AppStatusEvent;
import com.gongva.retromvvm.repository.Injection;
import com.gongva.retromvvm.repository.common.remote.HeaderUtils;
import com.gongva.retromvvm.ui.login.LoginActivity;
import com.hik.core.android.api.GsonUtil;
import com.hik.core.android.api.LogCat;
import com.hik.core.android.api.io.SharePreferencesUtil;
import com.hik.core.java.security.AESUtil;
import com.hik.core.java.security.DESUtil;
import com.hik.core.java.security.MD5Util;
import com.hik.core.java.security.SecurityFactory;

/**
 * 用户登录管理器
 *
 * @author gongwei
 * @date 2019.1.18
 */
public class GvLoginManager extends LoginManager {

    private static GvLoginManager INSTANCE = null;

    protected LatestLoginUserInfo latestLoginUserInfo;//最后登录用户的基本信息
    protected LoginInfo loginInfo;

    public synchronized static GvLoginManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GvLoginManager();
            INSTANCE.initLoginInfo();
        }
        return INSTANCE;
    }

    protected void initLoginInfo() {
        String jsonLatestUser = SharePreferencesUtil.getString(GvContext.SPF_LOGIN_LATEST_USER);
        latestLoginUserInfo = GsonUtil.jsonDeserializer(jsonLatestUser, LatestLoginUserInfo.class);
        if (latestLoginUserInfo == null) {
            latestLoginUserInfo = new LatestLoginUserInfo();
        }

        String jsonLoginInfo = SharePreferencesUtil.getString(GvContext.SPF_LOGIN_USER);
        if (!TextUtils.isEmpty(jsonLoginInfo)) {
            try {
                String jsonLoginInfoDecode = DESUtil.decrypt(jsonLoginInfo, TinkerApplicationCreate.getApplication());
                loginInfo = GsonUtil.jsonDeserializer(jsonLoginInfoDecode, LoginInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setLatestLoginUserInfo(LoginInfo loginInfo) {
        if (loginInfo != null) {
            setLatestLoginUserInfo(loginInfo.getUserInfo());
        }
    }

    public void setLatestLoginUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            this.latestLoginUserInfo.setPhone(userInfo.getPhone());
            this.latestLoginUserInfo.setAvatar(userInfo.getAvatar());
        }
    }

    public void saveAccount(String phone) {
        this.latestLoginUserInfo.setPhone(phone);
        this.latestLoginUserInfo.setAvatar(null);
        save();
    }

    public void saveUserInfo(UserInfo userInfo) {
        setLatestLoginUserInfo(userInfo);
        if (this.loginInfo != null) {
            this.loginInfo.userInfo = userInfo;
        }
        save();
    }

    public void saveLoginInfo(LoginInfo loginInfo) {
        setLatestLoginUserInfo(loginInfo);
        this.loginInfo = loginInfo;
        save();
    }

    public void save() {
        SharePreferencesUtil.setString(GvContext.SPF_LOGIN_LATEST_USER, GsonUtil.jsonSerializer(latestLoginUserInfo));
        String jsonLoginInfoEncode = DESUtil.encrypt(GsonUtil.jsonSerializer(loginInfo), TinkerApplicationCreate.getApplication());
        SharePreferencesUtil.setString(GvContext.SPF_LOGIN_USER, jsonLoginInfoEncode);
    }

    /**
     * 清除上次保存的账号信息
     */
    public void clearLatestLoginAccountInfo() {
        this.latestLoginUserInfo.setPhone(null);
        this.latestLoginUserInfo.setAvatar(null);
    }

    /**
     * 是否已登录
     *
     * @return
     */
    @Override
    public boolean isLoginImpl() {
        return loginInfo != null;
    }

    /**
     * 获取用户ID
     *
     * @return userId or null
     */
    @Override
    protected String getUserIdImpl() {
        if (loginInfo != null && loginInfo.userInfo != null) {
            return loginInfo.userInfo.id;
        }
        return null;
    }

    /**
     * 获取用户信息
     *
     * @return userInfo or null
     */
    public UserInfo getUserInfo() {
        if (loginInfo != null) {
            return loginInfo.userInfo;
        }
        return null;
    }

    /**
     * 获取用户手机号
     *
     * @return phone or null
     */
    public String getUserPhone() {
        if (loginInfo != null && loginInfo.userInfo != null) {
            return loginInfo.getUserInfo().getPhone();
        }
        return null;
    }

    /**
     * 获取用户token
     *
     * @return token
     */
    public String getToken() {
        if (loginInfo != null) {
            return loginInfo.token;
        }
        return null;
    }

    /**
     * 获取最近登录者账号
     *
     * @return
     */
    public String getLoginAccount() {
        return latestLoginUserInfo.getPhone();
    }

    /**
     * 获取最近登录者头像
     *
     * @return
     */
    public String getAvatar() {
        return latestLoginUserInfo.getAvatar();
    }

    /**
     * 密码加密
     * ①des加密(加密-Base64-URLEncode)，秘钥为逆序的密码
     * ②md5
     * ③逆序
     *
     * @param password
     * @return
     */
    public String encodePassword(String password) {
        String keyFromNative = SecurityFactory.getKeyFromNative(TinkerApplicationCreate.getApplication());
        String encoded = AESUtil.encrypt(password, keyFromNative);
        String md5ed = MD5Util.getMD5(encoded);
        String result = new StringBuffer(md5ed).reverse().toString();
        return result;
    }

    /**
     * 字符密码登录
     *
     * @param phone
     * @param
     * @param hashCode
     * @param callback
     */
    public static void login(@NonNull String phone, @NonNull String keyboardPwd, int hashCode, final LoginCallback callback) {
        Injection.provideLoginRemoteDataSource().login(phone, keyboardPwd, new RequestObserverCallbackAdapter<ResponseResult<LoginInfo>>(hashCode) {
            @Override
            public void loadData(ResponseResult<LoginInfo> loginInfoResponseResult) {
                super.loadData(loginInfoResponseResult);
                LoginInfo loginInfo = loginInfoResponseResult.getData();
                handleLoginSuccess(loginInfo, callback);
            }

            @Override
            public void loadError(ApiException e) {
                super.loadError(e);
                if (callback != null) {
                    callback.onError(e.getDisplayMessage());
                }
            }
        });
    }

    /**
     * 登录成功的数据处理
     *
     * @param loginInfo
     * @param callback
     */
    private static void handleLoginSuccess(LoginInfo loginInfo, LoginCallback callback) {
        if (loginInfo != null && loginInfo.getUserInfo() != null) {
            //保存用户信息
            getInstance().saveLoginInfo(loginInfo);
            //发送登录成功的Event
            if (callback != null) {
                callback.onSuccess();
            }
            BusProvider.post(new AppStatusEvent(AppStatusEvent.Status.LOGIN));
        } else {
            if (callback != null) {
                callback.onError("未获取到登录信息");
            }
        }
    }

    /**
     * 注销，带logout的Api请求
     */
    public static void logoutWithApi() {
        clearAllRequest();
        //为避免logout接口被清理调，所以先Clear，再调用logout接口
        String headerToken = HeaderUtils.getHeaderToken();
        if (!TextUtils.isEmpty(headerToken)) {
            Injection.provideLoginRemoteDataSource().logout(headerToken, new RequestObserverCallbackAdapter());
        }
        clearAllUserData(null);
    }

    /**
     * 注销
     */
    public static void logout(String tipMsg) {
        clearAllRequest();
        clearAllUserData(tipMsg);
    }

    /**
     * 关闭网络请求
     */
    private static void clearAllRequest() {
        ARemoteDSRequestManager.clearAllDisposable();
    }

    /**
     * 清空用户相关的所有数据，并总线通知AppStatusEvent变更为IDLE
     *
     * @param tipMsg
     */
    private static void clearAllUserData(String tipMsg) {
        //清空本地用户缓存
        GvLoginManager.getInstance().exitAccount();
        //重置应用状态为IDLE
        BusProvider.post(new AppStatusEvent(AppStatusEvent.Status.IDLE, tipMsg));
    }

    /**
     * 退出账号
     */
    public void exitAccount() {
        saveLoginInfo(null);
    }

    /**
     * 分发登录方式
     */
    public void dispatchLoginWay() {
        dispatchLoginWay(null);
    }

    /**
     * 分发登录方式
     */
    public void dispatchLoginWay(String targetRouteUrl) {
        LoginActivity.start(targetRouteUrl);
    }

    /**
     * 检查是否为已登录，未登录直接跳转登录并返回false
     *
     * @return
     */
    public static boolean checkLoginPermission() {
        return checkLoginPermission(null);
    }

    public static boolean checkLoginPermission(String targetUrl) {
        if (!GvLoginManager.isLogin()) {
            GvLoginManager.getInstance().dispatchLoginWay(targetUrl);
            return false;
        }
        return true;
    }
}
