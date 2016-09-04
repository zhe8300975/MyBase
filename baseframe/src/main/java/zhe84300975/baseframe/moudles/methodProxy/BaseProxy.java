package zhe84300975.baseframe.moudles.methodProxy;

import java.util.concurrent.ConcurrentHashMap;

import zhe84300975.baseframe.core.BaseIBiz;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe:代理类
 */
public class BaseProxy {
    public Object impl;                                // 实现类

    public Object proxy;                                // 代理类

    public ConcurrentHashMap<String, BaseMethod> methodCache = new ConcurrentHashMap();    // 方法缓存


    /**
     * 清空
     */
    public void clearProxy() {
        if(impl instanceof BaseIBiz){
            ((BaseIBiz)impl).detach();
        }
        impl = null;
        proxy = null;
        methodCache.clear();
        methodCache = null;
    }
}
