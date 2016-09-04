package zhe84300975.baseframe.core.exception;

/**
 * Created by zhaowencong on 16/8/19.
 * Describe: 状态异常
 */
public class BaseIndexOutOfException extends BaseBizException{

    public BaseIndexOutOfException() {}

    public BaseIndexOutOfException(String detailMessage) {
        super(detailMessage);
    }

    public BaseIndexOutOfException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseIndexOutOfException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }

}
