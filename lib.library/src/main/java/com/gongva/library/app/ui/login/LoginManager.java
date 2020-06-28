package com.gongva.library.app.ui.login;

/**
 * 登录管理器
 *
 * @author gongwei
 * @date 2019/4/3
 * @mail gongwei5@hikcreate.com
 */
public abstract class LoginManager {

    private static LoginManager INSTANCE = null;

    public static void setLoginManager(LoginManager loginManager) {
        INSTANCE = loginManager;
    }

    /**
     * 获取用户ID
     *
     * @return userId or null
     */
    public static String getUserId() {
        if (INSTANCE != null) {
            return INSTANCE.getUserIdImpl();
        }
        return null;
    }

    /**
     * 是否已登录
     *
     * @return
     */
    public static boolean isLogin() {
        if (INSTANCE != null) {
            return INSTANCE.isLoginImpl();
        }
        return false;
    }

    protected abstract String getUserIdImpl();

    protected abstract boolean isLoginImpl();

    public interface LoginCallback {
        void onSuccess();

        void onError(String response);
    }
}
