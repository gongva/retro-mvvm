package com.gongva.retromvvm.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.library.plugin.eventbus.BusProvider;
import com.gongva.library.plugin.netbase.scheduler.SchedulerProvider;
import com.gongva.retromvvm.R;
import com.gongva.retromvvm.base.component.BaseActivity;
import com.gongva.retromvvm.databinding.ActivityMainPageBinding;
import com.gongva.retromvvm.library.plugs.arouter.ARouterPath;
import com.gongva.retromvvm.ui.common.web.AppH5WebActivity;
import com.gongva.retromvvm.ui.common.web.AppX5WebActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * App主页
 *
 * @time 2020/06/19
 */
@Route(path = ARouterPath.MainPage)
public class MainPageActivity extends BaseActivity<ActivityMainPageBinding> {

    public static void start() {
        ARouter.getInstance().build(ARouterPath.MainPage).navigation();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainPageActivity.class));
    }

    /**
     * 判断当前Activity栈中是否存在MainPageActivity
     *
     * @return
     */
    public static boolean hasMainPage() {
        List<Activity> allActivities = TinkerApplicationCreate.getInstance().getActivityList();
        boolean hasMainPage = false;
        for (Activity temp : allActivities) {
            if (temp instanceof MainPageActivity) {
                hasMainPage = true;
                break;
            }
        }
        return hasMainPage;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main_page;
    }

    @Override
    protected void initView() {
        setTitle("主页");
        hideBack();

        mBinding.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_h5_test:
                        EditText editH5 = findViewById(R.id.edt_web_test);
                        AppH5WebActivity.start(MainPageActivity.this, editH5.getText().toString());
                        break;
                    case R.id.btn_x5_test:
                        EditText editX5 = findViewById(R.id.edt_x5_test);
                        AppX5WebActivity.start(MainPageActivity.this, editX5.getText().toString());
                        break;
                }
            }
        });

        //test code
        showInitLoading();
        Observable.timer(2000, TimeUnit.MILLISECONDS).compose(SchedulerProvider.applySchedulers()).subscribe((aLong) -> {
            dismissInitLoading();
        });
    }

    @Override
    protected void initData() {

    }
}
