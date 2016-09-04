package zhe84300975.baseframe.moudles.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe:工作线程池
 */
public class BaseWorkExecutorService extends ThreadPoolExecutor{

    BaseWorkExecutorService() {
        super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }
}
