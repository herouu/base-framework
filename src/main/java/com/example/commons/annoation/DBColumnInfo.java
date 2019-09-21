package com.example.commons.annoation;

import java.lang.annotation.*;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DBColumnInfo {
    /**
     * 列名字，暂时无效，默认为java属性名字转“_小写”
     * @return
     */
    String value() default "";

    /**
     * 备注，暂时无效，默认为注解ApiModelProperty值
     * @return
     */
    String comment() default "";

    /**
     * 默认值，暂时无效。 默认为NULL
     * @return
     */
    String DEFAULT() default "";

    /**
     * 默认值是否为空,当DEFAULT=""时，生效
     * @return
     */
    boolean isDefaultNull() default true;

    /**
     * 全部长度，含小数长度，默认-1，字符串64，int 11, BigDecimal 24, long 20
     * @return
     */
    int length() default -1;


    /**
     * 小数长度,默认-1，BigDecimal 6，其它0
     * @return
     */
    int decimal() default -1;


}
