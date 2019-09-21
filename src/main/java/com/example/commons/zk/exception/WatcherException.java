package com.example.commons.zk.exception;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class WatcherException extends RuntimeException {

    public WatcherException() {
    }

    public WatcherException(String message) {
        super(message);
    }

    public WatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public WatcherException(Throwable cause) {
        super(cause);
    }

    public WatcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
