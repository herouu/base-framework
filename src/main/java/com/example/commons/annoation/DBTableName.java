package com.example.commons.annoation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by zhangxu on 2018/6/26.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
public @interface DBTableName {
    String value() default "";
}
