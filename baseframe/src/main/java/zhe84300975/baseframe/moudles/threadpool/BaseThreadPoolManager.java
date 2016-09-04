package zhe84300975.baseframe.moudles.threadpool;

import java.util.concurrent.ExecutorService;

import zhe84300975.baseframe.moudles.log.L;

/**
 * Created by zhaowencong on 16/8/17.
 * Describe: 线程池管理
 */
public class BaseThreadPoolManager {
    /** 线程服务-网络线程池 **/
    private BaseHttpExecutorService baseHttpExecutorService;

    /** 线程服务-并行工作线程池 **/
    private BaseWorkExecutorService baseWorkExecutorService;

    /** 线程服务-串行工作线程池 **/
    private BaseSingleWorkExecutorServiece baseSingleWorkExecutorServiece;


    public synchronized ExecutorService getHttpExecutorService() {
        if (baseHttpExecutorService == null) {
            baseHttpExecutorService = new BaseHttpExecutorService();
        }
        return baseHttpExecutorService;
    }

    public synchronized ExecutorService getSingleWorkExecutorService() {
        if (baseSingleWorkExecutorServiece == null) {
            baseSingleWorkExecutorServiece = new BaseSingleWorkExecutorServiece();
        }
        return baseSingleWorkExecutorServiece;
    }

    public synchronized ExecutorService getWorkExecutorService() {
        if (baseWorkExecutorService == null) {
            baseWorkExecutorService = new BaseWorkExecutorService();
        }
        return baseWorkExecutorService;
    }



    public synchronized void finish() {
        L.tag("BaseThreadPoolManager");
        L.i("finish()");
        if (baseHttpExecutorService != null) {
            L.tag("BaseThreadPoolManager");
            L.i("BaseHttpExecutorService.shutdown()");
            baseHttpExecutorService.shutdown();
            baseHttpExecutorService = null;
        }
        if (baseSingleWorkExecutorServiece != null) {
            L.tag("BaseThreadPoolManager");
            L.i("BaseSingleWorkExecutorServiece.shutdown()");
            baseSingleWorkExecutorServiece.shutdown();
            baseSingleWorkExecutorServiece = null;
        }
        if (baseWorkExecutorService != null) {
            L.tag("BaseThreadPoolManager");
            L.i("BaseWorkExecutorService.shutdown()");
            baseWorkExecutorService.shutdown();
            baseWorkExecutorService = null;
        }
    }
}
