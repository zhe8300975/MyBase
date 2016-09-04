package zhe84300975.baseframe.utils;

import zhe84300975.baseframe.core.exception.BaseArgumentException;
import zhe84300975.baseframe.core.exception.BaseIndexOutOfException;
import zhe84300975.baseframe.core.exception.BaseNullPointerException;
import zhe84300975.baseframe.core.exception.BaseUINullPointerException;

/**
 * Created by zhaowencong on 16/8/18.
 * Describe: 检查
 */
public class BaseCheckUtils  {

    /**
     * 验证
     *
     * @param service
     * @param <T>
     */
    public static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("该类不是接口");
        }
    }

    /**
     * 检查是否为空
     *
     * @param reference
     *
     * @param errorMessageTemplate
     * @return
     */
    public static <T> T checkNotNull(T reference, String errorMessageTemplate) {
        if (reference == null) {
            throw new BaseNullPointerException(errorMessageTemplate);
        }
        return reference;
    }

    /**
     * 检查是否为空
     *
     * @param reference
     *
     * @param errorMessageTemplate
     * @return
     */
    public static <T> T checkUINotNull(T reference, String errorMessageTemplate) {
        if (reference == null) {
            throw new BaseUINullPointerException(errorMessageTemplate);
        }
        return reference;
    }

    /**
     * 检查参数
     *
     * @param expression
     * @param errorMessageTemplate
     */
    public static void checkArgument(boolean expression, String errorMessageTemplate) {
        if (!expression) {
            throw new BaseArgumentException(errorMessageTemplate);
        }
    }

    /**
     * 检查是否越界
     *
     * @param index
     * @param size
     * @param desc
     */
    public static void checkPositionIndex(int index, int size, String desc) {
        if (index < 0 || index > size) {
            throw new BaseIndexOutOfException(desc);
        }
    }

    /**
     * 判断是否相同
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * 判断是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(CharSequence text) {
        return null == text || text.length() == 0;
    }

}
