package com.example.commons.annoation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * @author zhangxu
 * 创建时间：2018/8/2
 * 功能描述: 保存修订记录注解，用于开启某个实体表（对应的数据库表）是否开启修订记录功能
 * diffExclude：在修订记录比较时的排除字段。
 * <p>
 * 修订记录：
 * @version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
@Import({EntitySqlScanRegistrar.class})
public @interface EntityRevise {
    String[] diffExclude() default {"id","createdBy","createdAt","updatedBy","updatedAt","version"};
}
