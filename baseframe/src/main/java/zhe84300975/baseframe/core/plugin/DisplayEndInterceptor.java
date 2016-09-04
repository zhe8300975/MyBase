package zhe84300975.baseframe.core.plugin;

import java.lang.reflect.Method;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 执行结束拦截
 */
public interface DisplayEndInterceptor {
    <T> void interceptEnd(String viewName, Class<T> service, Method method, int interceptor, String intent, Object[] objects, Object backgroundResult);

}
