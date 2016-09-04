package zhe84300975.baseframe.core.exception;

/**
 * Created by zhaowencong on 16/8/19.
 * Describe:
 */
public class BaseBizException extends RuntimeException{

    public BaseBizException(){}

    public BaseBizException(String detailMessage){
        super(detailMessage);
    }

    public BaseBizException(String message,Throwable cause){
        super(message,cause);
    }

    public BaseBizException(Throwable cause){
        super((cause == null ? null :cause.toString()),cause);
    }
}
