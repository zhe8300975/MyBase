package zhe84300975.baseframe.core.plugin;

import java.lang.reflect.Method;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 执行前拦截
 */
public interface BizStartInterceptor {
    <T> void interceptStart(String viewName, Class<T> service, Method method, int interceptor, Object[] objects);
}
