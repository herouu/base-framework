package com.example.commons.enums;

/**
 * Created by zhangxu on 2018/6/22.
 */
public enum DbOperatorType {
    EQ("等于","="),
    ISNULL("是NULL","is null"),
    NOT_NULL("不是NULL","is not null"),
    NOT_EQ("不等于","!="),
    GT("大于",">"),
    LT("小于","<"),
    GTQ("大于等于",">="),
    LTQ("小于等于","<="),
    LIKE("LIKE","like"),
    IN("IN","in"),
    NOT_IN("NOT_IN","not in"),
    LLIKE("左LIKE","like"),
    RLIKE("右LIKE","like"),
    BETWEEN("BETWEEN","between");


    private String opertionName;
    private String operationalCharacter;

    DbOperatorType(String operatorName, String operationalCharacter) {
        this.opertionName = operatorName;
        this.operationalCharacter = operationalCharacter;
    }

    public String getOperationalCharacter() {
        return this.operationalCharacter;
    }
}
