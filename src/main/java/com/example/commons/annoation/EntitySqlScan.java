package com.example.commons.annoation;


import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * 扫描所有可以解析到SQL中的Entity
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
@Import({EntitySqlScanRegistrar.class})
public @interface EntitySqlScan {
    //扫描的包名，不支持模糊查找
    String[] basePackages() default {};

    //扫描的类，不支持模糊查找
    Class<?>[] basePackageClasses() default {};
}