package com.example.commons.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;

/**
 * Created by zhangxu on 2018/6/19.
 */
@Slf4j
public class CommonSQLHelper {

    /**
     * 使用Ognl表达式获取变量值
     * @param expr
     * @param data
     * @return
     */
    public Object getOgnlValue(String expr,Object data){
        try {
            return Ognl.getValue(expr,data);
        } catch (OgnlException e) {
            log.warn("Ognl表达式错误:{}",expr);
        }
        return null;
    }

}
