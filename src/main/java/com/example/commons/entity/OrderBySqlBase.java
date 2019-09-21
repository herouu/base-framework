package com.example.commons.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排序字段参考类：AppEmployeeRoleMenuRelationDto
 * 依据用户id升序排序，则column=user.id、asc=true
 * 依据员工id升序排序，则column=employee.id、asc=true
 * 支持多个排序条件
 */
@Data
public class OrderBySqlBase {
    @Data
    public class OrderByNode{
        @ApiModelProperty("OrderBy")
        String column;
        @ApiModelProperty("是否升序 true:ASC,false:DESC")
        public Boolean asc = true;
        public OrderByNode(){

        }
        public OrderByNode(String column,Boolean asc){
            checkColumn(column,asc,false,null,false);
            this.column=column;
            this.asc=asc;
        }
    }
    @ApiModelProperty(value="排序字段参考类：AppEmployeeRoleMenuRelationDto \n" +
            "依据用户id升序排序，则column=user.id、asc=true \n" +
            "依据员工id升序排序，则column=employee.id、asc=true \n" +
            "支持多个排序条件",hidden = true)
    private OrderByNode sql[];
    @ApiModelProperty(hidden = true)
    private String javaConnector=".";
    @ApiModelProperty(hidden = true)
    private String sqlConnector="__";
    @ApiModelProperty(hidden = true)
    private String javaHeader=null;
    @ApiModelProperty(hidden = true)
    private String sqlHeader=null;

    public void sqlSimple(){
        setSqlHeader("");
        setSqlConnector("");
        setJavaHeader("");
        setJavaConnector("");
    }
    public void sqlDefault(){
        setSqlHeader(null);
        setSqlConnector("__");
        setJavaHeader(null);
        setJavaConnector(".");
    }
    @Data
    public static class OrderBy{
        @ApiModelProperty("OrderBy")
        String column;
        @ApiModelProperty("是否升序 true:ASC,false:DESC")
        public Boolean asc = true;
    }
    @ApiModelProperty("排序规则。如按申请单的创建时间升序排序[{\"column\":\"apply.createdAt\",\"asc\":true}]")
    private OrderBy orderList[];
    public void setOrderList(OrderBy[] sql){
        orderList = sql;
        if(orderList!=null){
            for(OrderBy node:orderList){
                if(node.asc){
                    addOrderByASC(node.column);
                }else {
                    addOrderByDESC(node.column);
                }
            }
        }
    }
    public OrderBy[] getOrderList(){
        return orderList;
    }

    /**
     * 参数升序排列，按添加次序计算排列
     * @param params
     */
    public void addOrderByASC(Object ... params){
        addOrderByASCOrDESC(true,params);
    }
    /**
     * 参数降序排序，按添加次序计算排列
     * @param params
     */
    public void addOrderByDESC(Object ... params){
        addOrderByASCOrDESC(false,params);
    }
    private void addOrderByASCOrDESC(boolean ad,Object ... param){
        Object[] list = new Object[param.length*2];
        for(int i=0;i<param.length;i++){
            list[i*2]=param[i];
            list[i*2+1]=ad;
        }
        addOrderBy(list);
    }
    /**
     *
     * AppEmployeeRoleMenuRelationDto
     * putOrderBy(user.id,true,employee.id,true);
     * @param param
     */
    public synchronized void addOrderBy(Object ... param){
        OrderByNode orderBy[]=null;
        if(param.length>0){
            if(param.length%2==1){
                throw new IllegalArgumentException("排序参数个数错误：length="+param.length);
            }
            int startIndex=0;
            if(sql!=null&&sql.length>0){
                orderBy = new OrderByNode[sql.length + param.length/2];
                System.arraycopy(sql,0,orderBy,0,sql.length);
                startIndex = sql.length;
            }else {
                orderBy = new OrderByNode[param.length/2];
                startIndex=0;
            }
            for(int i=0;i<param.length;i+=2){
                if(param[i] instanceof String && param[i+1] instanceof Boolean){
                    orderBy[startIndex+i/2]= new OrderByNode((String)param[i],(Boolean)param[i+1]);
                }else {
                    throw new IllegalArgumentException("排序参数类型错误：index="+i+" - "+(i+1));
                }
            }
            setSql(orderBy);
        }
    }
    public Boolean hasSql(){
        return sql!=null&&sql.length>0;
    }
    //只读，无线程安全
    final static Map<String,Class<? extends BaseEntity>> sqlBaseEntity = new HashMap<>();
    {
        //sqlBaseEntity.put("user", UserAccount.class);
        //sqlBaseEntity.put("employee", EmployeeInfo.class);
        //sqlBaseEntity.put("role", AppRole.class);
        //sqlBaseEntity.put("menu", AppMenu.class);
        //sqlBaseEntity.put("feature", AppFeature.class);
    }

    final static String DefaultRoot ="_root";
    /**
     * 使用例子
     * addOrderMap("menu", AppMenu.class);
     * addOrderMap("feature", AppFeature.class);
     * //
     * addOrderBy("menu.id",true,"feature.name",false);
     * @param key
     * @param clazz
     */
    public static void addOrderMap(String key,Class clazz){
        if(key==null||key.length()==0){
            key=DefaultRoot;
        }
        sqlBaseEntity.put(key, clazz);
    }

    /**
     * 使用例子
     * addRootMap(AppMenu.class);
     * //
     * addOrderBy("id",true,"name",false);
     * @param clazz
     */
    public static void addRootMap(Class clazz){
        addOrderMap(null,clazz);
    }
    private static void checkColumnPrefix(String columnPrefix){
        if(columnPrefix==null||(columnPrefix.length()!=0&&!columnPrefix.matches("^[A-Za-z0-9_.]*$"))){
            throw new IllegalArgumentException("排序参数错误："+columnPrefix);
        }
    }
    private StringBuffer checkColumn(String column,Boolean asc,Boolean backBuffer,String columnPrefix,boolean useJavaName){
        if(asc==null || column==null){
            throw new IllegalArgumentException("排序参数错误："+column);
        }
        //默认
        if(!column.contains(".")){
            column=DefaultRoot+"."+column;
            if(columnPrefix==null){
                columnPrefix="";
            }
        }
        String colum[]=column.split("\\.");
        Class entity = sqlBaseEntity.get(colum[0]);
        if(entity==null){
            throw new IllegalArgumentException("排序参数错误："+column);
        }
        //检查前缀特殊字符
        if(backBuffer&&columnPrefix!=null){
            checkColumnPrefix(columnPrefix);
        }
        EntitySqlTable sqlTable = EntitySqlFactory.getClassMap(entity);
        List<EntitySqlColumn> sqlColumns =  sqlTable.getSqlColumns();
        for(EntitySqlColumn c: sqlColumns){
            if(c.getAttributeName().equals(colum[1])){
                if(backBuffer){
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("`");
                    if(columnPrefix==null){
                        if(useJavaName){
                            buffer.append(getJavaHeader()==null?colum[0]:getJavaHeader());
                            buffer.append(getJavaConnector());
                        }else {
                            buffer.append(getSqlHeader()==null?colum[0]:getSqlHeader());
                            buffer.append(getSqlConnector());
                        }
                    }else {
                        buffer.append(columnPrefix);
                    }
                    if(useJavaName){
                        buffer.append(c.getAttributeName());
                    }else {
                        buffer.append(c.getColumnName());
                    }
                    buffer.append("` ");
                    buffer.append(asc==true?"ASC":"DESC");
                    return buffer;
                }
                return null;
            }
        }
        throw new IllegalArgumentException("排序参数错误："+column);
    }

    /**
     * 不包含order by
     * @return
     */
    public String orderBySql(){
        return orderBySql(null);
    }
    /**
     * 包含order by
     * @return
     */
    public String orderBy(){
        return orderBy(false);
    }
    public String orderBy(boolean useJavaName){
        return orderBy(null,useJavaName);
    }
    /**
     * 不包含order by
     * @param columnPrefix 列的前缀，null时，表示使用默认:user__、employee__、role__、menu__、feature__
     * @return
     */
    public String orderBySql(String columnPrefix){
        return orderBySql(columnPrefix,false);
    }
    public String orderBySql(String columnPrefix,boolean useJavaName){
        StringBuffer buffer = null;
        if(hasSql()){
            for(OrderByNode node:sql){
                if(buffer==null){
                    buffer = new StringBuffer();
                }else {
                    buffer.append(",");
                }
                buffer.append(checkColumn(node.getColumn(),node.getAsc(),true,columnPrefix,useJavaName));
            }
        }
        if(buffer==null){
            return null;
        }
        return buffer.toString();
    }
    /**
     * 包换order by
     * @param columnPrefix 列的前缀，null时，表示使用默认:user__、employee__、role__、menu__、feature__
     * @return
     */
    public String orderBy(String columnPrefix){
        return orderBy(columnPrefix,false);
    }
    public String orderBy(String columnPrefix,boolean useJavaName){
        if(hasSql()){
            return "order by"+" " +orderBySql(columnPrefix,useJavaName);
        }
        return null;
    }
}
