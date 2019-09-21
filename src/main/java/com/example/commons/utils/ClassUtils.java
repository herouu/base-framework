package com.example.commons.utils;


import com.example.commons.entity.EntitySqlFactory;
import com.example.commons.entity.EntitySqlTable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxu
 *         创建时间：2018/8/29
 *         功能描述:
 *         <p>
 *         修订记录：
 * @version 1.0
 **/
public class ClassUtils {

    private ClassUtils() {
    }
    /**
     * 获取运行时真实的范型类型
     * @param  tClass 宿主类Clas
     * @param typeIndex
     * @return
     */
    public static <T> Class<T> getActualType(Class tClass,Integer typeIndex){
        int index = typeIndex == null ? 0 : typeIndex.intValue();
        Type type = tClass.getGenericSuperclass();
        Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[index];
        return clazz;
    }

    /***
     * 获取实体对应的数据库字段列表
     * @param entityClass
     * @return
     */
    public static List<String> getEntityDbFields(Class entityClass){
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(entityClass);
        return entitySqlTable.getSqlColumns().stream()
                .map(item -> item.getColumnName()).collect(Collectors.toList());
    }

    /***
     * 获取实体对应的Java字段列表
     * @param entityClass
     * @return
     */
    public static  List<String> getEntityJavaFields(Class entityClass){
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(entityClass);
        return entitySqlTable.getSqlColumns().stream()
                .map(item -> item.getAttributeName()).collect(Collectors.toList());
    }
}
