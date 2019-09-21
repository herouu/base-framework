//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.commons.entity;

import com.example.commons.annoation.*;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class EntitySqlTable {
    private static final Logger log = LoggerFactory.getLogger(EntitySqlTable.class);
    private static CommonSQLHelper commonSQLHelper;
    private String tableName;
    private List<EntitySqlColumn> sqlColumns;
    private String className;
    private Boolean userCache;
    private Class entityClass;
    private Boolean enabledRevise;
    private String[] diffExclude;

    public EntitySqlTable() {
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<EntitySqlColumn> getSqlColumns() {
        return this.sqlColumns;
    }

    public void setSqlColumns(List<EntitySqlColumn> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addColumn(EntitySqlColumn column) {
        if (this.sqlColumns == null) {
            this.sqlColumns = new ArrayList();
        }

        this.sqlColumns.add(column);
    }

    public Boolean getUserCache() {
        return this.userCache;
    }

    public void setUserCache(Boolean userCache) {
        this.userCache = userCache;
    }

    public Class getEntityClass() {
        return this.entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public String[] getDiffExclude() {
        return this.diffExclude;
    }

    public void setDiffExclude(String[] diffExclude) {
        this.diffExclude = diffExclude;
    }

    public Boolean getEnabledRevise() {
        return this.enabledRevise;
    }

    public void setEnabledRevise(Boolean enabledRevise) {
        this.enabledRevise = enabledRevise;
    }

    public void sortField(final Field[] allField) {
        this.sqlColumns.sort(new Comparator<EntitySqlColumn>() {
            @Override
            public int compare(EntitySqlColumn o1, EntitySqlColumn o2) {
                int i1 = EntitySqlTable.getIndex(o1.getAttributeName(), allField);
                int i2 = EntitySqlTable.getIndex(o2.getAttributeName(), allField);
                return i1 - i2;
            }
        });
    }

    static int getIndex(String name, Field[] allField) {
        for (int i = 0; i < allField.length; ++i) {
            if (name.equals(allField[i].getName())) {
                return i;
            }
        }

        return -1;
    }

    public EntitySqlColumn getEntitySqlColumn(String javaName) {
        Iterator var2 = this.getSqlColumns().iterator();

        EntitySqlColumn sqlColumn;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            sqlColumn = (EntitySqlColumn) var2.next();
        } while (!sqlColumn.getAttributeName().equals(javaName));

        return sqlColumn;
    }

    public Object getOgnlValue(String expr, Object data) {
        if (commonSQLHelper == null) {
            commonSQLHelper = new CommonSQLHelper();
        }

        Object obj = commonSQLHelper.getOgnlValue(expr, data);
        return obj;
    }

    public String getSQLValue(String sqlText) {
        return StringEscapeUtils.escapeSql(sqlText.replace("\\", "\\\\"));
    }

    public String getCreateTableSql() {
        boolean hasId = false;
        StringBuffer header = new StringBuffer();
        header.append("CREATE TABLE `").append(this.getTableName()).append("` (").append("\n");
        StringBuffer buffer = new StringBuffer();
        Iterator var4 = this.sqlColumns.iterator();

        while (true) {
            while (var4.hasNext()) {
                EntitySqlColumn column = (EntitySqlColumn) var4.next();
                if (column.getColumnName().equals("id")) {
                    hasId = true;
                } else {
                    buffer.append("`").append(column.getColumnName()).append("`");
                    DBColumnInfo columnInfo = (DBColumnInfo) column.getField().getAnnotation(DBColumnInfo.class);
                    if (column.getColumnName().equals("id")) {
                        buffer.append(" bigint(12) NOT NULL AUTO_INCREMENT COMMENT");
                    } else if (column.getColumnName().equals("version")) {
                        buffer.append(" int(11) DEFAULT '1' COMMENT");
                    } else {
                        Class fieldClass = column.getField().getType();
                        if (fieldClass == null) {
                            return "空的Field:" + this.getTableName() + " => " + column.getAttributeName();
                        }

                        if (fieldClass.equals(Long.class)) {
                            buffer.append(" bigint(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("20");
                            }

                            buffer.append(")");
                        } else if (fieldClass.equals(Integer.class)) {
                            buffer.append(" int(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("11");
                            }

                            buffer.append(")");
                        } else if (fieldClass.equals(String.class)) {
                            buffer.append(" varchar(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("64");
                            }

                            buffer.append(")");
                        } else if (!fieldClass.equals(BigDecimal.class)) {
                            if (fieldClass.equals(Date.class)) {
                                buffer.append(" datetime");
                            } else {
                                if (!fieldClass.equals(Boolean.class)) {
                                    return "未知的数据类型:" + this.getTableName() + " => " + fieldClass.getSimpleName() +
                                            " " + column.getAttributeName();
                                }

                                buffer.append(" tinyint(1)");
                            }
                        } else {
                            buffer.append(" decimal(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("24");
                            }

                            buffer.append(",");
                            if (columnInfo != null && columnInfo.decimal() >= 0) {
                                buffer.append(columnInfo.decimal());
                            } else {
                                buffer.append("6");
                            }

                            buffer.append(")");
                        }

                        if (columnInfo != null && !columnInfo.isDefaultNull()) {
                            buffer.append(" NOT NULL ");
                        }

                        buffer.append(" DEFAULT ");
                        if (columnInfo != null && (columnInfo.DEFAULT().length() != 0 || !columnInfo.isDefaultNull())) {
                            buffer.append("'" + columnInfo.DEFAULT() + "'");
                        } else {
                            buffer.append("NULL");
                        }

                        buffer.append(" COMMENT");
                    }

                    buffer.append(" '");
                    if (columnInfo != null && columnInfo.comment().length() > 0) {
                        buffer.append(columnInfo.comment());
                    } else {
                        ApiModelProperty apiModelProperty =
                                (ApiModelProperty) column.getField().getAnnotation(ApiModelProperty.class);
                        if (apiModelProperty != null) {
                            buffer.append(apiModelProperty.value());
                        }
                    }

                    buffer.append("',\n");
                }
            }

            if (hasId) {
                buffer.append(" PRIMARY KEY (`id`)");
            }

            DBIndex dbIndex = (DBIndex) this.getAnnotationAll(this.entityClass, DBIndex.class);
            if (dbIndex != null && (dbIndex.normal().length > 0 || dbIndex.unique().length > 0)) {
                DBNormalIndex[] var10 = dbIndex.normal();
                int var13 = var10.length;

                int var15;
                for (var15 = 0; var15 < var13; ++var15) {
                    DBNormalIndex normalIndex = var10[var15];
                    this.addDBIndex(buffer, "", normalIndex.value(), normalIndex.columns(), normalIndex.index(),
                            normalIndex.comment());
                }

                DBUniqueIndex[] var11 = dbIndex.unique();
                var13 = var11.length;

                for (var15 = 0; var15 < var13; ++var15) {
                    DBUniqueIndex uniqueIndex = var11[var15];
                    this.addDBIndex(buffer, "UNIQUE", uniqueIndex.value(), uniqueIndex.columns(), uniqueIndex.index()
                            , uniqueIndex.comment());
                }
            }

            buffer.append("\n) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='");
            DBTableComment tableComment = (DBTableComment) this.getAnnotationAll(this.entityClass,
                    DBTableComment.class);
            if (tableComment != null) {
                buffer.append(tableComment.value());
            }

            buffer.append("';\n");
            if (hasId) {
                header.append("`id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '',\n");
            }

            header.append(buffer);
            return header.toString();
        }
    }

    <T> T getAnnotationAll(Class clazz, Class<T> clazzT) {
        if (clazz != null && clazz != Object.class) {
            T dbIndex = (T) clazz.getAnnotation(clazzT);
            return dbIndex != null ? dbIndex : this.getAnnotationAll(clazz.getSuperclass(), clazzT);
        } else {
            return null;
        }
    }

    void addDBIndex(StringBuffer buffer, String unique, String name, String[] keys, String index, String comment) {
        buffer.append(",\n ").append(unique).append(unique != null && unique.length() != 0 ? " " : "").append("INDEX " +
                "`").append(name).append("`(");
        boolean first = true;
        String[] var8 = keys;
        int var9 = keys.length;

        for (int var10 = 0; var10 < var9; ++var10) {
            String c = var8[var10];
            if (first) {
                first = false;
            } else {
                buffer.append(",");
            }

            EntitySqlColumn sqlColumn = this.getEntitySqlColumn(c);
            if (sqlColumn != null) {
                c = sqlColumn.getColumnName();
            }

            buffer.append("`").append(c).append("`");
        }

        buffer.append(") USING ").append(index);
        buffer.append(" COMMENT '").append(comment).append("'");
    }

    public String getCreateTableSqlLogicBak() {
        ArrayList<String> columArr = new ArrayList();
        boolean hasId = false;
        StringBuffer header = new StringBuffer();
        header.append("CREATE TABLE `").append(this.getTableName()).append("_l_bak` (").append("\n");
        StringBuffer buffer = new StringBuffer();
        Iterator var5 = this.sqlColumns.iterator();

        while (true) {
            while (var5.hasNext()) {
                EntitySqlColumn column = (EntitySqlColumn) var5.next();
                if (column.getColumnName().equals("id")) {
                    hasId = true;
                } else {
                    buffer.append("`").append(column.getColumnName()).append("`");
                    DBColumnInfo columnInfo = (DBColumnInfo) column.getField().getAnnotation(DBColumnInfo.class);
                    columArr.add(column.getColumnName());
                    if (column.getColumnName().equals("id")) {
                        buffer.append(" bigint(12) NOT NULL AUTO_INCREMENT COMMENT");
                    } else if (column.getColumnName().equals("version")) {
                        buffer.append(" int(11) DEFAULT '1' COMMENT");
                    } else {
                        Class fieldClass = column.getField().getType();
                        if (fieldClass == null) {
                            return "空的Field:" + this.getTableName() + " => " + column.getAttributeName();
                        }

                        if (fieldClass.equals(Long.class)) {
                            buffer.append(" bigint(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("20");
                            }

                            buffer.append(")");
                        } else if (fieldClass.equals(Integer.class)) {
                            buffer.append(" int(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("11");
                            }

                            buffer.append(")");
                        } else if (fieldClass.equals(String.class)) {
                            buffer.append(" varchar(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("64");
                            }

                            buffer.append(")");
                        } else if (!fieldClass.equals(BigDecimal.class)) {
                            if (fieldClass.equals(Date.class)) {
                                buffer.append(" datetime");
                            } else {
                                if (!fieldClass.equals(Boolean.class)) {
                                    return "未知的数据类型:" + this.getTableName() + " => " + fieldClass.getSimpleName() +
                                            " " + column.getAttributeName();
                                }

                                buffer.append(" tinyint(1)");
                            }
                        } else {
                            buffer.append(" decimal(");
                            if (columnInfo != null && columnInfo.length() >= 0) {
                                buffer.append(columnInfo.length());
                            } else {
                                buffer.append("24");
                            }

                            buffer.append(",");
                            if (columnInfo != null && columnInfo.decimal() >= 0) {
                                buffer.append(columnInfo.decimal());
                            } else {
                                buffer.append("6");
                            }

                            buffer.append(")");
                        }

                        if (columnInfo != null && !columnInfo.isDefaultNull()) {
                            buffer.append(" NOT NULL ");
                        }

                        buffer.append(" DEFAULT ");
                        if (columnInfo != null && (columnInfo.DEFAULT().length() != 0 || !columnInfo.isDefaultNull())) {
                            buffer.append("'" + columnInfo.DEFAULT() + "'");
                        } else {
                            buffer.append("NULL");
                        }

                        buffer.append(" COMMENT");
                    }

                    buffer.append(" '");
                    if (columnInfo != null && columnInfo.comment().length() > 0) {
                        buffer.append(columnInfo.comment());
                    } else {
                        ApiModelProperty apiModelProperty =
                                (ApiModelProperty) column.getField().getAnnotation(ApiModelProperty.class);
                        if (apiModelProperty != null) {
                            buffer.append(apiModelProperty.value());
                        }
                    }

                    buffer.append("',\n");
                }
            }

            if (hasId) {
                buffer.append(" PRIMARY KEY (`id`)");
            }

            buffer.append("\n) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='");
            DBTableComment tableComment = (DBTableComment) this.entityClass.getAnnotation(DBTableComment.class);
            if (tableComment != null) {
                buffer.append(tableComment.value());
            }

            buffer.append("';\n");
            header.append("`id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '',\n");
            header.append("`l_bak_delete_at` datetime NOT NULL COMMENT '删除时间',\n");
            if (hasId) {
                header.append("`l_bak_id` bigint(12) NOT NULL COMMENT '原始数据id',\n");
            }

            header.append(buffer);
            header.append("DELIMITER $\n");
            header.append("create trigger t_l_bak_" + this.getTableName());
            header.append(" before delete \n");
            header.append(" on `").append(this.getTableName()).append("` for each row \n");
            header.append("begin\n");
            header.append("INSERT INTO `").append(this.getTableName()).append("_l_bak` (id,l_bak_delete_at");
            if (hasId) {
                header.append(",l_bak_id");
            }

            Iterator var10 = columArr.iterator();

            String key;
            while (var10.hasNext()) {
                key = (String) var10.next();
                header.append(",").append(key);
            }

            header.append(") VALUES (NULL,CURRENT_TIME()");
            if (hasId) {
                header.append(",old.id");
            }

            var10 = columArr.iterator();

            while (var10.hasNext()) {
                key = (String) var10.next();
                header.append(",old.").append(key);
            }

            header.append(");\n");
            header.append("end $\n");
            return header.toString();
        }
    }
}
