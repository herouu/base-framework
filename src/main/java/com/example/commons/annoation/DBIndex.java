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
public @interface DBIndex {

    String value() default "";

    //索引
    DBNormalIndex[] normal() default {};

    //唯一性
    DBUniqueIndex[] unique() default {};

}
