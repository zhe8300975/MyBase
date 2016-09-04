package zhe84300975.baseframe;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;
import retrofit2.Retrofit;
import zhe84300975.baseframe.core.BaseIBiz;
import zhe84300975.baseframe.core.BaseICommonBiz;
import zhe84300975.baseframe.core.SynchronousExecutor;
import zhe84300975.baseframe.display.BaseIDisplay;
import zhe84300975.baseframe.moudles.BaseModulesManage;
import zhe84300975.baseframe.moudles.file.BaseFileCacheManage;
import zhe84300975.baseframe.moudles.methodProxy.BaseMethods;
import zhe84300975.baseframe.moudles.screen.BaseScreenManager;
import zhe84300975.baseframe.moudles.structure.BaseStructureIManage;
import zhe84300975.baseframe.moudles.systemuihider.BaseSystemUiHider;
import zhe84300975.baseframe.moudles.threadpool.BaseThreadPoolManager;
import zhe84300975.baseframe.moudles.toast.BaseToast;

/**
 * Created by zhaowencong on 16/8/17.
 * Describe:  helper 管理
 */
public class BaseHelper {

    protected volatile static BaseModulesManage mBaseModulesManage = null;

    /**
     * 单例模式-初始化BaseHelper
     *
     * @param baseModulesManage
     *            Modules
     */
    public static void with(BaseModulesManage baseModulesManage) {
        mBaseModulesManage = baseModulesManage;
    }

    /**
     * 获取管理
     *
     * @param <M>
     * @return
     */
    protected static <M> M getManage() {
        return (M) mBaseModulesManage;
    }




    /**
     * 获取启动管理器
     *
     * @param eClass
     * @param <D>
     * @return
     */
    public static <D extends BaseIDisplay> D display(Class<D> eClass) {
        return structureHelper().display(eClass);
    }

    /**
     * 获取业务
     *
     * @param service
     * @param <B>
     * @return
     */
    public static final <B extends BaseIBiz> B biz(Class<B> service) {
        return structureHelper().biz(service);
    }


    /**
     * 业务是否存在
     *
     * @param service
     * @param <B>
     * @return true 存在 false 不存在
     */
    public static final <B extends BaseIBiz> boolean isExist(Class<B> service) {
        return structureHelper().isExist(service);
    }


    /**
     * 获取业务
     *
     * @param service
     * @param <B>
     * @return
     */
    public static final <B extends BaseIBiz> List<B> bizList(Class<B> service) {
        return structureHelper().bizList(service);
    }

    /**
     * 公用
     *
     * @param service
     * @param <B>
     * @return
     */
    public static final <B extends BaseICommonBiz> B common(Class<B> service) {
        return structureHelper().common(service);
    }

    /**
     * 获取网络
     *
     * @param httpClazz
     * @param <H>
     * @return
     */
    public static final <H> H http(Class<H> httpClazz) {
        return structureHelper().http(httpClazz);
    }


    /**
     * 获取实现类
     *
     * @param implClazz
     * @param <P>
     * @return
     */
    public static final <P> P impl(Class<P> implClazz) {
        return structureHelper().impl(implClazz);
    }

    /**
     * 获取方法代理
     *
     * @return
     */
    public static final BaseMethods methodsProxy() {
        return mBaseModulesManage.getBaseMethods();
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static BaseApplication getInstance() {
        return mBaseModulesManage.getBaseApplication();
    }


    /**
     * 获取网络适配器
     *
     * @return
     */
    public static final Retrofit httpAdapter() {
        return mBaseModulesManage.getBaseRestAdapter();
    }

    /**
     * 结构管理器
     *
     * @return 管理器
     */
    public static final BaseStructureIManage structureHelper() {
        return mBaseModulesManage.getBaseStructureManage();
    }

    /**
     * activity管理器
     *
     * @return 管理器
     */
    public static final BaseScreenManager screenHelper() {
        return mBaseModulesManage.getBaseScreenManager();
    }


    /**
     * BaseThreadPoolManager 线程池管理器
     */

    public static final BaseThreadPoolManager threadPoolHelper() {
        return mBaseModulesManage.getBaseThreadPoolManager();
    }

    /**
     * MainLooper 主线程中执行
     *
     * @return
     */
    public static final SynchronousExecutor mainLooper() {
        return mBaseModulesManage.getSynchronousExecutor();
    }


    /**
     * 控制状态栏和标题栏
     *
     * @param activity
     * @param anchorView
     * @param flags
     * @return
     */
    public static final BaseSystemUiHider systemHider(AppCompatActivity activity, View anchorView, int flags) {
        return mBaseModulesManage.getBaseSystemUiHider(activity, anchorView, flags);
    }

    /**
     * Toast 提示信息
     *
     * @return
     */
    public static final BaseToast toast() {
        return mBaseModulesManage.getBaseToast();
    }

    /**
     * 判断是否是主线程
     *
     * @return true 子线程 false 主线程
     */
    public static final boolean isMainLooperThread() {
        return Looper.getMainLooper().getThread() != Thread.currentThread();
    }

    //TODO 下载器工具

    /**
     * 文件缓存管理器
     *
     * @return
     */
    public static final BaseFileCacheManage fileCacheManage() {
        return mBaseModulesManage.getBaseFileCacheManage();
    }

}
