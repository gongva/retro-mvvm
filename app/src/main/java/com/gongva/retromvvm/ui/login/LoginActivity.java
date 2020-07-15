package com.gongva.retromvvm.ui.login;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.library.app.ui.UI;
import com.gongva.library.plugin.eventbus.BusProvider;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.base.component.BaseVMActivity;
import com.gongva.retromvvm.base.component.viewmodel.ViewModelUtils;
import com.gongva.retromvvm.common.event.login.AppStatusEvent;
import com.gongva.retromvvm.databinding.ActivityLoginBinding;
import com.gongva.retromvvm.library.plugs.arouter.ARouterDispatcher;
import com.gongva.retromvvm.library.plugs.arouter.ARouterPath;

import org.greenrobot.eventbus.Subscribe;

/**
 * 登录页面
 *
 * @time 2020/06/19
 */
@Route(path = ARouterPath.Login)
public class LoginActivity extends BaseVMActivity<LoginViewModel, ActivityLoginBinding> {

    @Autowired
    String targetRouteUrl;

    public static void start(String targetRouteUrl) {
        ARouter.getInstance().build(ARouterPath.Login).withString("targetRouteUrl", targetRouteUrl).navigation();
    }

    public static void start() {
        ARouter.getInstance().build(ARouterPath.Login).navigation();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginViewModel initViewModel() {
        return (LoginViewModel) ViewModelUtils.obtainViewModel(this, LoginViewModel.class);
    }

    @Override
    protected void initView() {
        BusProvider.bindLifecycle(this);
        setTitle("登录");
    }

    @Override
    protected void initData() {
        mViewModel.getLoginSuccessLD().observe(this, userInfo -> {
            StringBuilder message = new StringBuilder();
            message.append("恭喜登录成功，(模拟)用户信息为：\n");
            message.append(userInfo.toString());
            UI.showConfirmDialog(LoginActivity.this, message.toString(), "OK", null);
        });
        mBinding.setLoginViewModel(mViewModel);
    }

    /**
     * 因登录拦截器拦下的routeUr，在登录成功之后继续触达
     *
     * @param event
     */
    @Subscribe
    public void onEvent(AppStatusEvent event) {
        if (event.getStatus() == AppStatusEvent.Status.LOGIN) {
            finish();
            ARouterDispatcher.dispatch(targetRouteUrl);
        }
    }
}
