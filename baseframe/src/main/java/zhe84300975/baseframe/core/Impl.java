package zhe84300975.baseframe.core;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe:
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Impl {
    Class value();
}
