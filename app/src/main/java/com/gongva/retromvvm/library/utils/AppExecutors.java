package com.gongva.retromvvm.library.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * App线程池工具
 *
 * @author gongwei
 * @time 2020/06/23
 * @mail shmily__vivi@163.com
 */
public class AppExecutors {
    private Executor taskThread;
    private Executor mainThread;

    public AppExecutors() {
        taskThread = Executors.newCachedThreadPool();
        mainThread = new MainExecutor();
    }

    public Executor getTaskThread() {
        return taskThread;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public class MainExecutor implements Executor {

        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}