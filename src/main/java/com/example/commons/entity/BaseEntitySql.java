package com.example.commons.entity;

import com.example.commons.utils.ParameterFilterUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BaseEntitySql<ChildClass extends BaseEntitySql> {
    public static String getDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String getLikeText(String text) {
        return escapeSql(ParameterFilterUtils.escapeLike(text));
    }

    static String escapeSql(String text) {
        return StringEscapeUtils.escapeSql(text.replace("\\", "\\\\"));
    }

    public Long[] inId;
    public Long[] outId;
    public Boolean nullId;
    public Date beforeCreatedAt;
    public Date afterCreatedAt;
    public Date beforeUpdatedAt;
    public Date afterUpdatedAt;
    public String[] inCreatedBy;
    public String[] outCreatedBy;
    public String likeCreatedBy;
    public String[] inUpdatedBy;
    public String[] outUpdatedBy;
    public String likeUpdatedBy;


    public ChildClass[] inChildSql;

    /**
     * inChildSql 务必开启setConditionDoubleConstAllow模式
     * public DataSource datasource() {
     * DruidDataSource druidDataSource = new DruidDataSource();
     * List<Filter> filterList=new ArrayList<>();
     * filterList.add(wallFilter());
     * druidDataSource.setProxyFilters(filterList);
     * return druidDataSource;
     * }
     *
     * @param inChildSql
     * @Bean public WallFilter wallFilter(){
     * WallFilter wallFilter=new WallFilter();
     * wallFilter.setConfig(wallConfig());
     * return  wallFilter;
     * }
     * @Bean public WallConfig wallConfig(){
     * WallConfig config =new WallConfig();
     * config.setConditionDoubleConstAllow(true);
     * config.setConditionAndAlwayTrueAllow(true);
     * return config;
     * }
     */
    public void setInChildSql(ChildClass[] inChildSql) {
        if (inChildSql != null) {
            Class thiz = this.getClass();
            for (BaseEntitySql sql : inChildSql) {
                if (sql.getClass() != thiz) {
                    throw new IllegalArgumentException("类型错误" + sql.getClass().getName() + "=>" + thiz.getName());
                }
            }
        }
        this.inChildSql = inChildSql;
    }

    public ChildClass appendChild() {
        //inChildSql.getClass().getGenericSuperclass().
        try {
            ChildClass value = (ChildClass) this.getClass().newInstance();
            if (inChildSql == null) {
                inChildSql = ArrayUtils.toArray(value);
            } else {
                inChildSql = ArrayUtils.add(inChildSql, value);
            }
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException("异常错误");
        }
    }

    @Data
    public static class BaseNode {
        public String javaName;
        public String sqlName;
        public Object value1;
        public Object value2;
        public Object[] arr;

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return o != null && o instanceof BaseNode && (((BaseNode) o).getJavaName() != null && ((BaseNode) o).getJavaName().equals(javaName));
        }
    }

    @ApiModelProperty(hidden = true)
    public BaseNode[] listLikeNode;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listInNode;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listOutNode;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listSqlDate;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listSqlNumber;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listNullNode;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listFindOneNode;
    @ApiModelProperty(hidden = true)
    public BaseNode[] listFindAllNode;
    @ApiModelProperty(hidden = true)
    private String head;

    private static Field[] allField(Class<?> calzz) {
        Field fields[] = new Field[0];
        try {
            ArrayList<Field> list = new ArrayList<>();
            while (!calzz.equals(Object.class)) {
                for (Field field : calzz.getFields()) {
                    if (!list.contains(field)) {
                        list.add(field);
                    }
                }
                for (Field field : calzz.getDeclaredFields()) {
                    if (!list.contains(field)) {
                        list.add(field);
                    }
                }
                calzz = calzz.getSuperclass();
            }
            fields = new Field[list.size()];
            list.toArray(fields);

        } catch (Exception e) {

        }
        return fields;
    }

    private String getSqlName(List<EntitySqlColumn> list, String javaName) {
        for (EntitySqlColumn column : list) {
            if (column.getAttributeName().equals(javaName)) {
                return column.getColumnName();
            }
        }
        throw new IllegalArgumentException("错误的名称" + javaName);
    }

    private void updateOrAddNode(ArrayList<BaseNode> list, BaseNode node) {
        for (BaseNode old : list) {
            if (old.getJavaName().equals(node.getJavaName())) {
                if (old.getValue1() == null && node.getValue1() != null) {
                    old.setValue1(node.getValue1());
                } else if (old.getValue2() == null && node.getValue2() != null) {
                    old.setValue2(node.getValue2());
                }
                return;
            }
        }
        list.add(node);
    }

    private BaseNode[] toArr(ArrayList<BaseNode> list) {
        BaseNode[] arr = new BaseNode[list.size()];
        list.toArray(arr);
        return arr;
    }

    private boolean isEnable(Object value) {
        if (value != null) {
            if (value instanceof String) {
                if (((String) value).length() > 0) {
                    return true;
                }
            } else if (value.getClass().isArray()) {
                if (((Object[]) value).length > 0) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public EntitySqlTable getEntitySqlTable(String head) {
        return null;
    }

    public boolean buildNode(String head) {
        try {
            if (head != null && !head.equals(this.head)) {
                listInNode = null;
            }
            if (this.head != null && !this.head.equals(head)) {
                listInNode = null;
            }
            if (listLikeNode == null || listInNode == null || listOutNode == null || listSqlDate == null || listSqlNumber == null || listNullNode == null || listFindOneNode == null || listFindAllNode == null) {
                EntitySqlTable sqlTable = getEntitySqlTable(head);
                List<EntitySqlColumn> list = sqlTable.getSqlColumns();

                //获取全部属性，名称
                Field[] arrField = allField(this.getClass());
                //获取所有指
                ArrayList<BaseNode> likeNodes = new ArrayList();
                ArrayList<BaseNode> inNodes = new ArrayList();
                ArrayList<BaseNode> outNodes = new ArrayList();
                ArrayList<BaseNode> sqlDates = new ArrayList();
                ArrayList<BaseNode> sqlNumbers = new ArrayList();
                ArrayList<BaseNode> findOneNodes = new ArrayList();
                ArrayList<BaseNode> findAllNodes = new ArrayList();
                ArrayList<BaseNode> nullNodes = new ArrayList();
                //
                for (Field field : arrField) {
                    //
                    String name = field.getName();
                    if (name.equals("inChildSql")) {
                        continue;
                    }
                    BaseNode node = new BaseNode();
                    field.setAccessible(true);
                    Object value = field.get(this);
                    //Object value = Ognl.getValue(name,this);
                    if (!isEnable(value)) {
                        continue;
                    }
                    if (name.startsWith("like")) {
                        name = name.substring("like".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setValue1(value);
                        likeNodes.add(node);
                    } else if (name.startsWith("in")) {
                        name = name.substring("in".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setArr((Object[]) value);
                        inNodes.add(node);
                    } else if (name.startsWith("out")) {
                        name = name.substring("out".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setArr((Object[]) value);
                        outNodes.add(node);
                    } else if (name.startsWith("after")) {
                        name = name.substring("after".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setValue1(value);
                        updateOrAddNode(sqlDates, node);
                    } else if (name.startsWith("before")) {
                        name = name.substring("before".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setValue2(value);
                        updateOrAddNode(sqlDates, node);
                    } else if (name.startsWith("greater")) {
                        name = name.substring("greater".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setValue1(value);
                        updateOrAddNode(sqlNumbers, node);
                    } else if (name.startsWith("less")) {
                        name = name.substring("less".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setValue2(value);
                        updateOrAddNode(sqlNumbers, node);
                    } else if (name.startsWith("null")) {
                        name = name.substring("null".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setValue1(value);
                        nullNodes.add(node);
                    } else if (name.startsWith("findOne")) {
                        name = name.substring("findOne".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setArr((Object[]) value);
                        findOneNodes.add(node);
                    } else if (name.startsWith("findAll")) {
                        name = name.substring("findAll".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        node.setJavaName(name);
                        node.setSqlName(getSqlName(list, name));
                        node.setArr((Object[]) value);
                        findAllNodes.add(node);
                    } else {
                        continue;
                    }
                }
                listLikeNode = toArr(likeNodes);
                listInNode = toArr(inNodes);
                listOutNode = toArr(outNodes);
                listSqlDate = toArr(sqlDates);
                listSqlNumber = toArr(sqlNumbers);
                listFindOneNode = toArr(findOneNodes);
                listFindAllNode = toArr(findAllNodes);
                listNullNode = toArr(nullNodes);
                this.head = head;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("错误");
        }

        return listLikeNode.length > 0 || listInNode.length > 0 || listOutNode.length > 0 || listSqlDate.length > 0 || listSqlNumber.length > 0 || listFindOneNode.length > 0 || listFindAllNode.length > 0 || listNullNode.length > 0;
    }
}
