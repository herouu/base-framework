package com.example.commons.entity;


import com.example.commons.annoation.DBTableName;
import com.example.commons.annoation.EntityRevise;
import com.example.commons.annoation.EntityUseCache;
import com.example.commons.utils.StringJavaSqlName;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class EntitySqlFactory {
    /**
     * 注意线程安全
     */
    private static final ConcurrentHashMap<String, EntitySqlTable> clazzMap = new ConcurrentHashMap();

    public static void addClassMap(Class<?> clazz) {
        doMap(clazz);
    }

    public static EntitySqlTable getClassMap(Class<?> clazz) {
        return doMap(clazz);
    }

    public static Class getClass(String className) {
        EntitySqlTable sqlTable = clazzMap.get(className);
        if (sqlTable != null) {
            return sqlTable.getEntityClass();
        }
        return null;
    }

    public static EntitySqlTable getClassMap(String className) {
        if (clazzMap.containsKey(className)) {
            return clazzMap.get(className);
        } else {
            try {
                Class clsType = Class.forName(className);
                return doMap(clsType);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    public static String getSqlTableName(Class<?> clazz) {
        //判断是否有自定义table名字
        DBTableName dbTableName = clazz.getAnnotation(DBTableName.class);
        if (dbTableName != null && StringUtils.isNotBlank(dbTableName.value())) {
            return dbTableName.value();
        }
        //没有自定义，则默认从类名中获取
        return StringJavaSqlName.getSQLNameFormJava(clazz.getSimpleName());
    }

    static Field[] allField(Class<?> calzz) {
        Field fields[] = new Field[0];
        try {
            while (!calzz.equals(Object.class)) {
                fields = ArrayUtils.addAll(fields, calzz.getFields());
                fields = ArrayUtils.addAll(fields, calzz.getDeclaredFields());
                calzz = calzz.getSuperclass();
            }
        } catch (Exception e) {

        }
        return fields;
    }

    private static EntitySqlTable doMap(Class<?> calzz) {
        EntitySqlTable table = clazzMap.get(calzz.getName());
        if (table == null) {
            Field[] allField = allField(calzz);
            table = new EntitySqlTable();
            table.setClassName(calzz.getName());
            table.setTableName(getSqlTableName(calzz));
            table.setEntityClass(calzz);
            //添加实体类是否需要添加到缓存处理的注解
            EntityUseCache useCache = calzz.getAnnotation(EntityUseCache.class);
            if (useCache != null && useCache.value() == true) {
                table.setUserCache(Boolean.TRUE);
            } else {
                table.setUserCache(Boolean.FALSE);
            }
            //判断是否启用修订记录和diff排除字段
            EntityRevise entityRevise = calzz.getAnnotation(EntityRevise.class);
            if (entityRevise == null) {
                table.setEnabledRevise(Boolean.FALSE);
                table.setDiffExclude(null);
            } else {
                table.setEnabledRevise(Boolean.TRUE);
                table.setDiffExclude(entityRevise.diffExclude());
            }

            ArrayList<String> hasAdd = new ArrayList<String>();
            //从属性中获取
            Field[] fields = calzz.getFields();
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    String fieldName = f.getName();
                    String sqlName = StringJavaSqlName.getSQLNameFormJava(fieldName);
                    table.addColumn(new EntitySqlColumn(sqlName, fieldName, f.getType().getSimpleName(), f));
                    hasAdd.add(sqlName);
                }
            }

            //从方法中获取
            Method[] mList = calzz.getMethods();

            for (int i = 0; i < mList.length - 1; i++) {
                Method method1 = mList[i];
                if (method1 == null) {
                    continue;
                }
                if (method1.getParameterCount() > 1) {
                    continue;
                }
                if (Modifier.isStatic(method1.getModifiers())) {
                    continue;
                }
                String name1 = method1.getName();
                if (name1.length() < 4 || !Character.isUpperCase(name1.charAt(3))) {
                    continue;
                }
                String fun1 = name1.substring(0, 3);
                String fun2 = null;
                String type = null;
                if (fun1.equals("set")) {
                    fun2 = "get";
                } else if (fun1.equals("get")) {
                    fun2 = "set";
                    type = method1.getReturnType().getSimpleName();
                } else {
                    continue;
                }
                String fieldName = name1.substring(3);
                String sqlName = StringJavaSqlName.getSQLNameFormJava(fieldName);
                if (hasAdd.contains(sqlName)) {
                    continue;
                }
                for (int j = i + 1; j < mList.length; j++) {
                    Method method2 = mList[j];
                    if (method2 == null) {
                        continue;
                    }
                    if (method2.getParameterCount() > 1) {
                        //舍弃
                        mList[j] = null;
                        continue;
                    }
                    String name2 = method2.getName();
                    if (name2.length() < 4 || !Character.isUpperCase(name2.charAt(3))) {
                        //舍弃
                        mList[j] = null;
                        continue;
                    }
                    String tmp = name2.substring(0, 3);
                    if (tmp.startsWith(fun2)) {
                        //找到属性
                        String field = name1.substring(3);
                        if (field.equals(name2.substring(3))) {
                            fieldName = StringJavaSqlName.getJavaNameFormSql(sqlName, false);
                            table.addColumn(new EntitySqlColumn(sqlName, fieldName, type != null ? type :
                                    method2.getReturnType().getSimpleName(), getField(allField, fieldName)));
                            hasAdd.add(sqlName);
                        }
                    } else if (tmp.startsWith(fun1)) {
                        //保留
                    } else {
                        //舍弃
                        mList[i] = null;
                        continue;
                    }
                }
            }
            //根据allField排序
            table.sortField(allField);
            //
            clazzMap.put(calzz.getName(), table);
        }
        return table;
    }

    private static Field getField(Field fields[], String fieldName) {
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public static <T> T createObject(Map<String, Object> map, Class clazz) {
        try {
            T object = (T) clazz.newInstance();
            Map<String, Object> newMap = new HashMap();
            map.forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String key, Object value) {
                    newMap.put(StringJavaSqlName.getJavaNameFormSql(key, false), value);
                }
            });

            BeanUtils.copyProperties(object, newMap);
            return object;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String getCreateTableSql(Class... clazzs) {
        String sql = "";
        for (Class clazz : clazzs) {
            sql = sql + getClassMap(clazz).getCreateTableSql();
        }
        return sql;
    }

    public static String getCreateTableSqlLogicBak(Class... clazzs) {
        String sql = "";
        for (Class clazz : clazzs) {
            sql = sql + getClassMap(clazz).getCreateTableSqlLogicBak();
        }
        return sql;
    }

}
