package com.gongva.retromvvm.ui.login;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.gongva.library.app.ui.UI;
import com.gongva.library.plugin.netbase.scheduler.SchedulerProvider;
import com.gongva.retromvvm.BR;
import com.gongva.retromvvm.base.component.viewmodel.GeneralViewModel;
import com.gongva.retromvvm.common.manager.login.UserInfo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * 登录ViewModel
 *
 * @author gongwei
 * @time 2020/07/15
 */
public class LoginViewModel extends GeneralViewModel {

    private final MutableLiveData<UserInfo> loginSuccessLD;
    private final LoginObservable loginOb;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginSuccessLD = new MutableLiveData<>();
        loginOb = new LoginObservable();
    }

    public MutableLiveData<UserInfo> getLoginSuccessLD() {
        return loginSuccessLD;
    }

    public LoginObservable getLoginOb() {
        return loginOb;
    }

    /**
     * 提交登录
     */
    public void performLogin() {
        // demo code
        // real cod is : LoginManager.login()
        UI.showToast(String.format("username=%s,password=%s", loginOb.getUsername(), loginOb.getPassword()));
        startLoading("Loading...");
        Observable.timer(1500, TimeUnit.MILLISECONDS).compose(SchedulerProvider.applySchedulers()).subscribe(aLong -> {
            stopLoading();
            UserInfo userInfo = UserInfo.createTemp();
            loginSuccessLD.setValue(userInfo);
        });
    }

    /**
     * 登录数据
     */
    public class LoginObservable extends BaseObservable {
        private String username;
        private String password;

        @Bindable
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
            notifyPropertyChanged(BR.username);
        }

        @Bindable
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
            notifyPropertyChanged(BR.password);
        }
    }
}
