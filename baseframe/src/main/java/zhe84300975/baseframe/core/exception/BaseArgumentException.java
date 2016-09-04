package zhe84300975.baseframe.core.exception;

/**
 * Created by zhaowencong on 16/8/19.
 * Describe: 参数异常
 */
public class BaseArgumentException extends BaseBizException{

    public BaseArgumentException(){}

    public BaseArgumentException(String detailMessage){
        super(detailMessage);
    }

    public BaseArgumentException(String message,Throwable cause){
        super(message,cause);
    }

    public BaseArgumentException(Throwable cause){
        super((cause == null ? null :cause.toString()),cause);
    }

}
