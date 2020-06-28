package com.gongva.retromvvm.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.gongva.library.app.TinkerApplicationCreate;
import com.gongva.retromvvm.library.plugs.arouter.ARouterPath;

import java.util.List;

/**
 * App主页
 *
 * @time 2020/06/19
 */
@Route(path = ARouterPath.MainPage)
public class MainPageActivity extends Activity {

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView main = new TextView(this);
        main.setText("main");
        setContentView(main);
    }
}
