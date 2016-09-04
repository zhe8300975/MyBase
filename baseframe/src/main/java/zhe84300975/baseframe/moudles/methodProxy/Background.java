package zhe84300975.baseframe.moudles.methodProxy;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import zhe84300975.baseframe.moudles.threadpool.BackgroundType;


/**
 * Created by zhaowencong on 16/8/24.
 * Describe:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Background {
    BackgroundType value() default BackgroundType.HTTP;
}
