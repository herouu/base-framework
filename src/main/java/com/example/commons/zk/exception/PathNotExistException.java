package com.example.commons.zk.exception;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class PathNotExistException extends RuntimeException {

    public PathNotExistException() {
    }

    public PathNotExistException(String message) {
        super(message);
    }

    public PathNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathNotExistException(Throwable cause) {
        super(cause);
    }

    public PathNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
