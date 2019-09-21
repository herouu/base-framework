package com.example.commons.annoation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
public @interface DBTableComment {
    /**
     * 描述
     * @return
     */
    String value() default "";
}
