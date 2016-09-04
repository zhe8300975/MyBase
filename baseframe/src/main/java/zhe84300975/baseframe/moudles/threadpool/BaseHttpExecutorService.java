package zhe84300975.baseframe.moudles.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: 网络线程池
 */
public class BaseHttpExecutorService extends ThreadPoolExecutor {

    private static final int	DEFAULT_THREAD_COUNT	= 5;

    BaseHttpExecutorService() {
        super(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
