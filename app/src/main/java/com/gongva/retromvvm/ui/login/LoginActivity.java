package com.gongva.retromvvm.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.library.plugin.eventbus.BusProvider;
import com.gongva.retromvvm.common.event.login.AppStatusEvent;
import com.gongva.retromvvm.library.plugs.arouter.ARouterDispatcher;
import com.gongva.retromvvm.library.plugs.arouter.ARouterPath;

import org.greenrobot.eventbus.Subscribe;

/**
 * 登录
 *
 * @time 2020/06/19
 */
@Route(path = ARouterPath.Login)
public class LoginActivity extends Activity {

    @Autowired
    String targetRouteUrl;

    public static void start(String targetRouteUrl) {
        ARouter.getInstance().build(ARouterPath.Login).withString("targetRouteUrl", targetRouteUrl).navigation();
    }

    public static void start() {
        ARouter.getInstance().build(ARouterPath.Login).navigation();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.bindLifecycle(this);

        TextView main = new TextView(this);
        main.setText("login");
        setContentView(main);
    }


    @Subscribe
    public void onEvent(AppStatusEvent event) {
        if (event.getStatus() == AppStatusEvent.Status.LOGIN) {
            finish();
            ARouterDispatcher.dispatch(targetRouteUrl);
        }
    }
}
