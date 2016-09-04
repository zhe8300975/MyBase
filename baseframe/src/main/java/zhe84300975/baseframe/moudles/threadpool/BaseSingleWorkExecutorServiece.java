package zhe84300975.baseframe.moudles.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: 串行线程池
 */
public class BaseSingleWorkExecutorServiece extends ThreadPoolExecutor {
    BaseSingleWorkExecutorServiece() {
        super(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
