package com.example.commons.utils;

import com.example.commons.exception.FrameworkUtilException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by zhangxu on 2018/6/29.
 */

public class BeanUtils extends org.springframework.beans.BeanUtils {
    private static Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 外部程序不能实力化
     */
    private BeanUtils() {
    }

    /**
     * 获取bean中指定字段的属性值
     *
     * @param source
     * @param filedName
     * @param <T>
     * @return
     */
    public static <T> T getPropertyValue(Object source, String filedName) {
        try {
            T t = (T) Ognl.getValue(filedName, source);
            return t;
        } catch (OgnlException e) {
            log.error("className:{}, 设置属性名：{}", source.getClass().getName(), filedName);
            throw new FrameworkUtilException(e);
        }
    }

    /**
     * 设置bean中指定字段的属性值
     *
     * @param source    bean对象
     * @param filedName 设置属性的字段名
     * @param value     设置字段值
     * @return
     */
    public static void setPropertyValue(Object source, String filedName, Object value) {
        try {
            Ognl.setValue(filedName, source, value);
        } catch (OgnlException e) {
            log.error("className:{}, 设置属性名：{}", source.getClass().getName(), filedName);
            throw new FrameworkUtilException(e);
        }
    }

    /**
     * 将Map对象转换成JavaBean对象
     * 需要注意Bean与Map对象对应的数据类型
     *
     * @param map
     * @param classType
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> classType) {
        T bean = null;
        try {
            bean = classType.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
            return bean;
        } catch (Exception e) {
            throw new FrameworkUtilException(e);
        }
    }

    /***
     * 将Bean对象转换成Map对象
     * @param bean
     * @return
     */
    public static Map<String, String> beanToMap(Object bean) {
        try {
            ConvertUtilsBean convertUtils = BeanUtilsBean.getInstance().getConvertUtils();
            DateConverter dateConverter = new DateConverter();
            dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
            convertUtils.register(dateConverter, String.class);
            return org.apache.commons.beanutils.BeanUtils.describe(bean);
        } catch (Exception e) {
            throw new FrameworkUtilException(e);
        }
    }

}
