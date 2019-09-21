package com.example.commons.zk.exception;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class PathExistException extends RuntimeException {

    public PathExistException() {
    }

    public PathExistException(String message) {
        super(message);
    }

    public PathExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathExistException(Throwable cause) {
        super(cause);
    }

    public PathExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
