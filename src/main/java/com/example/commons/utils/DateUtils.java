package com.example.commons.utils;

import java.util.Date;

public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private DateUtils() {
    }

    public static Date now() {
        return new Date();
    }
}