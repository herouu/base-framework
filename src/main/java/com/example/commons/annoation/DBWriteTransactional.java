package com.example.commons.annoation;

import com.example.commons.constant.CommonsConstants;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * @author zhangxu
 * Created by zhangxu on 2018/7/4.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional(value = CommonsConstants.COMMON_TRANSACTION_MANAGER,isolation = Isolation.DEFAULT
        ,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public @interface DBWriteTransactional {
    @AliasFor(annotation = Transactional.class)
    String transactionManager() default CommonsConstants.COMMON_TRANSACTION_MANAGER;

    @AliasFor(annotation = Transactional.class,attribute = "rollbackFor")
    Class<? extends Throwable>[] rollbackFor() default {Exception.class};

    @AliasFor(annotation = Transactional.class)
    Class<? extends Throwable>[] noRollbackFor() default {};
}
