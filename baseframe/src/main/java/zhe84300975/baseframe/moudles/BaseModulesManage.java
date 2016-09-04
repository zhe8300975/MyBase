package zhe84300975.baseframe.moudles;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import zhe84300975.baseframe.BaseApplication;
import zhe84300975.baseframe.core.SynchronousExecutor;
import zhe84300975.baseframe.moudles.file.BaseFileCacheManage;
import zhe84300975.baseframe.moudles.log.L;
import zhe84300975.baseframe.moudles.methodProxy.BaseMethods;
import zhe84300975.baseframe.moudles.screen.BaseScreenManager;
import zhe84300975.baseframe.moudles.structure.BaseStructureManage;
import zhe84300975.baseframe.moudles.systemuihider.BaseSystemUiHider;
import zhe84300975.baseframe.moudles.threadpool.BaseThreadPoolManager;
import zhe84300975.baseframe.moudles.toast.BaseToast;
import retrofit2.Retrofit;

/**
 * Created by zhaowencong on 16/8/17.
 * Describe: Modules 管理
 */
public class BaseModulesManage {

    private final BaseApplication mBaseApplication;                    //全局上下文

    private final BaseScreenManager mBaseScreenManager;                //Activity堆栈管理

    private final BaseThreadPoolManager mBaseThreadPoolManager;        //线程池管理

    private final BaseStructureManage mBaseStructureManage;            // 结构管理器

    private final SynchronousExecutor mSynchronousExecutor;            // 主线程

    private final BaseToast mBaseToast;                                // 提示信息

    private BaseSystemUiHider mBaseSystemUiHider;                      // 标题栏和状态栏控制

    private L.DebugTree mDebugTree;                                    // 打印信息

    private BaseMethods mBaseMethods;                                  // 方法代理
    //TODO 下载管理


    private Retrofit mBaseRestAdapter;                                  // 网络适配器

    private BaseFileCacheManage mBaseFileCacheManage;                    // 文件缓存管理器


    public BaseModulesManage(BaseApplication baseApplication) {
        this.mBaseApplication = baseApplication;
        this.mBaseScreenManager = new BaseScreenManager();
        this.mBaseStructureManage = new BaseStructureManage();
        this.mBaseThreadPoolManager = new BaseThreadPoolManager();
        this.mSynchronousExecutor = new SynchronousExecutor();
        this.mBaseToast = new BaseToast();
        this.mBaseFileCacheManage = new BaseFileCacheManage();
    }


    public BaseApplication getBaseApplication() {
        return this.mBaseApplication;
    }


    /**
     * 初识化网络请求
     * @param baseRestAdapter
     */
    public void initBaseRestAdapter(Retrofit baseRestAdapter) {
        this.mBaseRestAdapter = baseRestAdapter;
    }

    /**
     * 初始化Log
     * @param logOpen
     */
    public void initLog(boolean logOpen) {
        if (logOpen) {
            if (mDebugTree == null) {
                mDebugTree = new L.DebugTree();
            }
            L.plant(mDebugTree);
        }
    }


    /**
     * 初始化方法拦截器
     * @param methodInterceptor
     */
    public void initMehtodProxy(BaseMethods methodInterceptor){
        this.mBaseMethods=methodInterceptor;
    }


    public BaseMethods getBaseMethods() {
        return mBaseMethods;
    }

    public Retrofit getBaseRestAdapter() {
        return this.mBaseRestAdapter;
    }

    public BaseScreenManager getBaseScreenManager() {
        return this.mBaseScreenManager;
    }

    public BaseThreadPoolManager getBaseThreadPoolManager() {
        return this.mBaseThreadPoolManager;
    }

    public SynchronousExecutor getSynchronousExecutor() {
        return this.mSynchronousExecutor;
    }

    public BaseStructureManage getBaseStructureManage() {
        return this.mBaseStructureManage;
    }

    public BaseToast getBaseToast() {
        return this.mBaseToast;
    }

    public BaseSystemUiHider getBaseSystemUiHider(AppCompatActivity activity, View anchorView, int flags) {
        if (this.mBaseSystemUiHider == null) {
            synchronized (this) {
                if (this.mBaseSystemUiHider == null) {
                    this.mBaseSystemUiHider = BaseSystemUiHider.getInstance(activity, anchorView, flags);
                }
            }
        }
        return this.mBaseSystemUiHider;
    }

    public BaseFileCacheManage getBaseFileCacheManage() {
        return this.mBaseFileCacheManage;
    }


}
