package zhe84300975.baseframe.core.plugin;

import java.lang.reflect.Method;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 错误拦截
 */
public interface BaseErrorInterceptor {
    <T> void interceptorError(String viewName, Class<T> service, Method method, int interceptor, Throwable throwable);

}
