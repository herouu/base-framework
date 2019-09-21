package com.example.commons.zk.exception;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class DistributedException extends RuntimeException {

    public DistributedException() {
    }

    public DistributedException(String message) {
        super(message);
    }

    public DistributedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedException(Throwable cause) {
        super(cause);
    }

    public DistributedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
