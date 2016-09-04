package zhe84300975.baseframe;

import android.app.Application;
import android.content.Context;

import retrofit2.Retrofit;
import zhe84300975.baseframe.moudles.BaseModulesManage;
import zhe84300975.baseframe.moudles.methodProxy.BaseMethods;
import zhe84300975.baseframe.view.common.BaseIViewCommon;

/**
 * Created by zhaowencong on 16/8/17.
 * Describe: 使用机构必须继承的
 */
public abstract class BaseApplication extends Application implements BaseIViewCommon {


    /**
     * modules 管理
     */
    BaseModulesManage mBaseModulesManage	= null;

    /**
     * 日志是否打印
     *
     * @return true 打印 false 不打印
     */
    public abstract boolean isLogOpen();


    /**
     * 获取网络适配器
     *
     * @return
     */
    public abstract Retrofit getRestAdapter(Retrofit.Builder builder);



    /**
     * 方法拦截器适配
     *
     * @param builder
     * @return
     */
    public abstract BaseMethods getMethodInterceptor(BaseMethods.Builder builder);

    /**
     * 获取配置管理器
     *
     * @return
     */
    public BaseModulesManage getModulesManage() {
        return new BaseModulesManage(this);
    }

    /**
     * 初始化帮助类
     *
     * @param baseModulesManage
     */
    public void initHelper(BaseModulesManage baseModulesManage) {
        BaseHelper.with(baseModulesManage);
    }

    /**
     * 设置全局异常处理
     *
     * @return
     */
    public Thread.UncaughtExceptionHandler getExceptionHandler() {
        return Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 初始化
        mBaseModulesManage = getModulesManage();
        // 初始化Application
        initHelper(mBaseModulesManage);
        // 初始化 HTTP
        mBaseModulesManage.initBaseRestAdapter(getRestAdapter(new Retrofit.Builder()));
        // 初始化 LOG
        mBaseModulesManage.initLog(isLogOpen());
        // 初始化 代理方法
        mBaseModulesManage.initMehtodProxy(getMethodInterceptor(new BaseMethods.Builder()));
        // 初始化统一错误处理
        Thread.setDefaultUncaughtExceptionHandler(getExceptionHandler());
    }



}
