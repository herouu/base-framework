package com.example.commons.annoation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * 在实体上注解数据库的索引,使用默认的BTREE索引方式
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
public @interface DBUniqueIndex {
    /**
     * 名称
     * @return
     */
    String value() default "";

    /**
     * 列名
     * @return
     */
    String[] columns() default {};

    /**
     * 索引方法
     * @return
     */
    String index() default "BTREE";

    /**
     * 备注
     * @return
     */
    String comment() default "";
}
