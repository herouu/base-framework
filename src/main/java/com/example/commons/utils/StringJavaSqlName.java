package com.example.commons.utils;

public class StringJavaSqlName {
    /**
     * userName 或则 UserName => user_name
     * @param javaName
     * @return
     */
    public static String getSQLNameFormJava(String javaName){
        StringBuffer buffer = new StringBuffer();
        //第一个大写变小写
        char c = javaName.charAt(0);
        if(Character.isUpperCase(c)){
            buffer.append(Character.toLowerCase(c));
        }else{
            buffer.append(c);
        }
        //其它大写加_小写
        for(int i=1;i<javaName.length();i++){
            c = javaName.charAt(i);
            if(Character.isUpperCase(c)){
                buffer.append("_");
                buffer.append(Character.toLowerCase(c));
            }else{
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
    /**
     * user_name => userName 或则 UserName
     * @param sqlName
     * @param firstUp
     * @return
     */
    public static String getJavaNameFormSql(String sqlName,boolean firstUp){
        StringBuffer buffer = new StringBuffer();
        //第一个小写变大写
        char c = sqlName.charAt(0);
        if(firstUp && Character.isLowerCase(c)){
            buffer.append(Character.toUpperCase(c));
        }else{
            buffer.append(c);
        }
        //其它_加其它转大写
        for(int i=1;i<sqlName.length();i++){
            c = sqlName.charAt(i);
            if(c=='_'&&i<sqlName.length()){
                i++;
                c = sqlName.charAt(i);
                buffer.append(Character.toUpperCase(c));
            }else{
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
