package zhe84300975.baseframe.moudles.methodProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe:方法是否重复 - 默认不可重复
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Repeat {

    boolean value() default false;
}
