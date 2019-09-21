package com.example.commons.entity;


import java.lang.reflect.Field;

public class EntitySqlColumn {
    private String columnName;
    private String attributeName;
    private String attributeType;
    private Field field;

    public EntitySqlColumn(String columnName, String attributeName, String attributeType, Field field) {
        setColumnName(columnName);
        setAttributeName(attributeName);
        setAttributeType(attributeType);
        setField(field);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
