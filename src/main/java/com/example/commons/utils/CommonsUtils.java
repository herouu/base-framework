package com.example.commons.utils;

import com.example.commons.constant.CommonsConstants;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhangxu on 2018/7/2.
 * @author zhangxu
 */
public class CommonsUtils {

    private CommonsUtils(){
    }

    /***
     * 根据class类型获取缓存Key值
     * @param classType
     * @return
     */
    public static String getCacheKey(Class<?> classType){
        return MessageFormat.format(CommonsConstants.COMMONS_TABLE_CACHE_KEY,classType.getName());
    }
    /**
     * 条件判断
     * @param data
     * @param args
     * @return
     */
    public static  boolean in(Object data, Object ... args){
        if(args!=null && args.length>0){
            for(Object arg:args){
                if(arg.equals(data)){
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * 将List数据转换成字用"，"分割的符串
     * 返回值如：ab,12,5,6
     * @param list
     * @return
     */
    public static String listToString(List<?> list){
        return list.stream().map(item -> item.toString()).collect(Collectors.joining(","));
    }

    /***
     * 将List转换成数据库可以用in条件查询的字符串
     * 结果如：'a','b','c'
     * @param list
     * @return
     */
    public static String listToDbInString(List<?> list){
        if(CollectionUtils.isEmpty(list)){
            return "''";
        }
        return list.stream().map(item -> String.format("'%s'",filterSQLValue(item.toString()))).collect(Collectors.joining(","));
    }

    /***
     * 将List转换成数据库可以用in条件查询的字符串
     * 结果如：'a','b','c'
     * @param args
     * @return
     */
    public static <T> String arraysToDbInString(T[] args){
        if(ArrayUtils.isEmpty(args)){
            return "''";
        }
        return Arrays.stream(args).map(item -> String.format("'%s'",filterSQLValue(item.toString()))).collect(Collectors.joining(","));
    }

    /**
     * 防止SQL注入攻击
     * @param value
     * @return
     */
    public static Object filterSQLValue(Object value){
        //防止SQL注入攻击
        if(value instanceof String && value != null){
            return StringEscapeUtils.escapeSql(String.valueOf(value).replace("\\","\\\\").replace("%","\\%"));
        }
        return value;
    }


    /***
     * 转换成驼峰命名的java属性名
     * @param property
     * @return
     */
    public static String removePropertyUnderline(String property) {
        AssertUtils.notBlank(property,"数据库字段名不能为空");
        String[] words = property.toLowerCase().split("_");
        String convertProperty = words[0];

        if (words.length >= 2) {
            for (int a = 1; a < words.length; a++) {
                String tmp = words[a];
                tmp = Character.toUpperCase(tmp.charAt(0)) + tmp.substring(1);
                convertProperty += tmp;
            }
        }

        return convertProperty;
    }

}
