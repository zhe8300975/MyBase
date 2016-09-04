package zhe84300975.baseframe.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe:同步执行（就主线程）
 */
public class SynchronousExecutor implements Executor {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}
