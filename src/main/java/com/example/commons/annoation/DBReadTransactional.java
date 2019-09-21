package com.example.commons.annoation;

import com.example.commons.constant.CommonsConstants;
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
@Transactional(transactionManager= CommonsConstants.COMMON_TRANSACTION_MANAGER,readOnly = true,propagation = Propagation.SUPPORTS)
public @interface DBReadTransactional {

}
