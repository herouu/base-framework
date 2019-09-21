package com.example.commons.zk.exception;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class ValueModifiedException extends RuntimeException {

    public ValueModifiedException() {
    }

    public ValueModifiedException(String message) {
        super(message);
    }

    public ValueModifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueModifiedException(Throwable cause) {
        super(cause);
    }

    public ValueModifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
