package com.example.commons.entity;


import com.example.commons.enums.DbOperatorType;
import com.example.commons.utils.ClassUtils;
import com.example.commons.utils.CommonsUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhangxu on 2018/6/19.
 */
public class WhereCondition implements Serializable {
    private StringBuilder condition = new StringBuilder();
    private StringBuilder orderBy = new StringBuilder();
    private String groupBy = null;

    /**
     * 全部字段标识
     */
    private boolean allField = true;
    /**
     * 查询字段列表
     */
    private List<String> queryFields = null;

    public WhereCondition() {

    }

    public WhereCondition(String condition) {
        this.condition.append(condition);
    }

    /***
     * 添加查询字段
     */
    public WhereCondition addQueryField(String fieldName) {
        if (queryFields == null) {
            this.queryFields = new ArrayList<>();
        }
        this.queryFields.add(fieldName);
        allField = false;
        return this;
    }

    /***
     * 添加查询字段数组
     * @param fieldNames
     * @return
     */
    public WhereCondition addQueryFields(String... fieldNames) {
        if (queryFields == null) {
            this.queryFields = new ArrayList<>();
        }
        this.queryFields.addAll(Arrays.asList(fieldNames));
        allField = false;
        return this;
    }


    /**
     * 设置查询不包含的字段名
     *
     * @param entityClass
     * @param excludeFields
     * @return
     */
    public WhereCondition setQueryExcludeFields(Class entityClass, String... excludeFields) {
        List<String> dbFields = ClassUtils.getEntityDbFields(entityClass);
        List<String> excludeList = Arrays.asList(excludeFields);
        dbFields.stream().filter(item -> !excludeList.contains(item)).forEach(item -> addQueryField(item));
        return this;
    }

    /***
     *  返回查询字段列表
     * @return
     */
    public String getQueryFields() {
        if (allField) {
            return "*";
        } else {
            List<String> selectFields = queryFields.stream()
                    .map(item -> selectField(item)).collect(Collectors.toList());
            return CommonsUtils.listToString(selectFields);
        }
    }


    /**
     * 转换成数据库查询字段
     * 结果：+ as + Java字段名
     *
     * @param selectDBField
     * @return
     */
    private String selectField(String selectDBField) {
        return selectDBField + " as " + CommonsUtils.removePropertyUnderline(selectDBField);
    }

    /**
     * 添加查询条件
     *
     * @param dbFiledName    数据库字段名
     * @param dbOperatorType 逻辑运算符
     * @param value          条件值
     * @return
     */
    public WhereCondition addCondition(String dbFiledName, DbOperatorType dbOperatorType, Object... value) {
        if (condition.length() > 0) {
            condition.append(" and ");
        }
        condition.append(dbFiledName).append(" ")
                .append(dbOperatorType.getOperationalCharacter()).append(" ")
                .append(getConditionValue(dbOperatorType, value));
        return this;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void addOrderBy(String orderBy) {
        this.orderBy.append(orderBy);
    }

    @Override
    public String toString() {
        return (StringUtils.isBlank(condition.toString()) ? "" : " WHERE " + condition.toString()) +
                (StringUtils.isBlank(groupBy) ? "" : " GROUP BY " + groupBy) +
                (StringUtils.isBlank(orderBy.toString()) ? "" : " order by " + orderBy.toString());
    }

    public String getWhere() {
        return this.toString();
    }


    public String getConditionValue(DbOperatorType dbOperatorType, Object... value) {
        String LEFT_LIKE = "'%s%%'";
        String LIKE = "'%%%s%%'";
        String IN = "(%s)";
        String RIGHT_LIKE = "'%%%s'";
        String BETWEEN_STRING = "'%s' and '%s'";
        String BETWEEN_NUMBER = "%s and %s";
        String OTHER_STRING = "'%s'";
        String OTHER_NUMBER = "%s";
        if (dbOperatorType == DbOperatorType.LLIKE) {
            return String.format(LEFT_LIKE, filterValue(value[0]));
        } else if (dbOperatorType == DbOperatorType.RLIKE) {
            return String.format(RIGHT_LIKE, filterValue(value[0]));
        } else if (dbOperatorType == DbOperatorType.LIKE) {
            return String.format(LIKE, filterValue(value[0]));
        } else if (dbOperatorType == DbOperatorType.BETWEEN && value[0] instanceof String) {
            return String.format(BETWEEN_STRING, filterValue(value[0]), filterValue(value[1]));
        } else if (dbOperatorType == DbOperatorType.BETWEEN && !(value[0] instanceof String)) {
            return String.format(BETWEEN_NUMBER, filterValue(value[0]), filterValue(value[1]));
        } else if (dbOperatorType == DbOperatorType.IN || dbOperatorType == DbOperatorType.NOT_IN) {
            if (value[0] instanceof List) {
                return String.format(IN, CommonsUtils.listToDbInString((List) value[0]));
            } else {
                return String.format(IN, CommonsUtils.arraysToDbInString(value));
            }
        } else if (dbOperatorType == DbOperatorType.ISNULL) {
            return StringUtils.EMPTY;
        } else if (dbOperatorType == DbOperatorType.NOT_NULL) {
            return StringUtils.EMPTY;
        } else if (value[0] instanceof String) {
            return String.format(OTHER_STRING, filterValue(value[0]));
        } else if (value[0] instanceof Boolean) {
            return String.format(OTHER_NUMBER, (Boolean) value[0] == true ? "1" : "0");
        } else if (value[0] instanceof Date) {
            return String.format("'%s'", DateFormatUtils.format((Date) value[0], "yyyy-MM-dd HH:mm:ss"));
        } else if (!(value[0] instanceof String)) {
            return String.format(OTHER_NUMBER, filterValue(value[0]));
        }
        return "";
    }

    /**
     * 验证是否是指定查询字段列表
     *
     * @return
     */
    public boolean getAllField() {
        return allField;
    }

    /**
     * @param value
     * @return
     */
    private Object filterValue(Object value) {
        //防止SQL注入攻击
        return CommonsUtils.filterSQLValue(value);
    }
}
