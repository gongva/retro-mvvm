package com.gongva.library.plugin.eventbus;

import android.app.Activity;

import com.gongva.library.app.AppLifecycle;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus全局唯一实例
 *
 * @author gongwei 2018/12/18
 */
public class BusProvider {

    public static EventBus getInstance() {
        return EventBus.getDefault();
    }

    /**
     * EventBus普通事件
     * @param event
     */
    public static void post(Event event) {
        getInstance().post(event);
    }

    /**
     * EventBus粘性时间
     * @param event
     */
    public static void postSticky(Event event) {
        getInstance().postSticky(event);
    }
    public static void register(Object object) {
        getInstance().register(object);
    }

    public static void bindLifecycle(final Object object) {
        getInstance().register(object);
        AppLifecycle.setLifecycle(object, new AppLifecycle.LifecycleCallback() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                getInstance().unregister(object);
            }
        });
    }

    public static void unregister(Object object) {
        getInstance().unregister(object);
    }

    private BusProvider() {
        // No instances.
    }
}