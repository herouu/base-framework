package com.example.commons.dao;


import com.example.commons.entity.EntitySqlTable;
import com.example.commons.entity.WhereCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 共同数据库操作Dao接口
 * 本接口根据Entity实体类按规则直接操作数据库
 *
 * @author zhangxu
 */
@Mapper
public interface CommonsDao {

    /**
     * 根据主键Id查询数据
     *
     * @param ctx
     * @param id
     * @return
     */
    Map<String, Object> selectById(EntitySqlTable ctx, @Param("id") Long id);

    /**
     * 根据条件查询数据
     *
     * @param ctx
     * @param whereCondition
     * @return
     */
    List<Map<String, Object>> selectByWhereString(EntitySqlTable ctx, String whereCondition);

    /**
     * 根据条件查询数据
     *
     * @param ctx
     * @param whereCondition
     * @return
     */
    List<Map<String, Object>> selectByCondition(EntitySqlTable ctx, WhereCondition whereCondition);

    /***
     * 根据条件查询数据条数
     * @param ctx
     * @param whereCondition
     * @return
     */
    Integer selectCountCondition(EntitySqlTable ctx, WhereCondition whereCondition);

    /***
     * 根据条件查询数据条数
     * @param ctx
     * @param whereCondition
     * @return
     */
    Integer selectCountWhereString(EntitySqlTable ctx, String whereCondition);

    /**
     * 插入数据
     *
     * @param ctx
     * @param data
     * @return
     */
    Integer insert(EntitySqlTable ctx, @Param("param2") Object data);

    /**
     * 批量插入数据（不能返回插入的主键值)
     *
     * @param ctx
     * @param dataList
     */
    Integer insertList(EntitySqlTable ctx, List<?> dataList);

    /**
     * 更新数据库数据
     *
     * @param ctx
     * @param data
     * @return
     */
    Integer update(EntitySqlTable ctx, Object data);


    /***
     * 更新非空数据(于修订记录数据不能保持一致，不建议使用）
     * @param ctx
     * @param data
     * @return
     */
    Integer updateExcludeNull(EntitySqlTable ctx, Object data);

    /***
     * 更新非空数据(于修订记录数据不能保持一致，不建议使用）
     * @param ctx
     * @param data
     * @param whereCondition 更新条件
     * @return
     */
    Integer updateExcludeNullWhere(EntitySqlTable ctx, Object data, WhereCondition whereCondition);

    /**
     * 删除数据
     *
     * @param ctx
     * @param id
     * @return
     */
    Integer delete(EntitySqlTable ctx, @Param("id") Long id);

    /**
     * 根据条件删除数据
     *
     * @param ctx
     * @param whereCondition
     * @return
     */
    Integer deleteByCondition(EntitySqlTable ctx, WhereCondition whereCondition);


    /**
     * 根据条件删除数据
     *
     * @param ctx
     * @param whereCondition
     * @return
     */
    Integer deleteByWhereString(EntitySqlTable ctx, String whereCondition);

    /**
     * 通用条件查询
     * @param baseSql
     * @param orderBySql
     * @return
     */
    // List<Map> listBaseEntity(@Param("baseSql") BaseEntitySql baseSql, @Param("orderBySql") OrderBySqlBase
    // orderBySql);

    /**
     * 通用条件查询
     * @param baseSql
     * @param orderBySql
     * @return
     */
    // Map oneBaseEntity(@Param("baseSql") BaseEntitySql baseSql, @Param("orderBySql") OrderBySqlBase orderBySql);

}
