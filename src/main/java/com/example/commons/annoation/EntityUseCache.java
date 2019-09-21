package com.example.commons.annoation;

/**
 * Created by zhangxu on 2018/7/2.
 */

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * 实体类是否使用缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
@Import({EntitySqlScanRegistrar.class})
public @interface EntityUseCache {
    boolean value() default false;
}
