package zhe84300975.baseframe.core.plugin;

import java.lang.reflect.Method;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe:
 */
public interface ImplEndInterceptor {
    <T> void interceptEnd(String viewName, Class<T> service, Method method, Object[] objects, Object backgroundResult);

}
