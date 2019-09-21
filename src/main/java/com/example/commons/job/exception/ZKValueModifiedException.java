package com.example.commons.job.exception;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class ZKValueModifiedException extends RuntimeException {

    public ZKValueModifiedException() {
    }

    public ZKValueModifiedException(String message) {
        super(message);
    }

    public ZKValueModifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZKValueModifiedException(Throwable cause) {
        super(cause);
    }

    public ZKValueModifiedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
