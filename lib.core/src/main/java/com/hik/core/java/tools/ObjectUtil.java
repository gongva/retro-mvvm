package com.hik.core.java.tools;

import com.hik.core.android.api.GsonUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Object相关工具库
 *
 * @author gongwei
 * @time 2019/10/12
 * @mail shmily__vivi@163.com
 */
public class ObjectUtil {

    /**
     * 比较两个对象是否相等
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static void objectEquals(Object obj1, Object obj2, ObjectEqualsCallBack callBack) {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            if (obj1 == obj2) {
                e.onNext(true);
            } else {
                String json1 = GsonUtil.jsonSerializer(obj1);
                String json2 = GsonUtil.jsonSerializer(obj2);
                e.onNext(json1.equals(json2));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(equals -> {
                    if (callBack != null) {
                        callBack.result(equals);
                    }
                }, throwable -> {
                    if (callBack != null) {
                        callBack.result(false);
                    }
                });
    }

    public interface ObjectEqualsCallBack {
        void result(boolean equals);
    }
}
