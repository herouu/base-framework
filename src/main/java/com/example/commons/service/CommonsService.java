package com.example.commons.service;


import com.example.commons.annoation.DBReadTransactional;
import com.example.commons.annoation.DBWriteTransactional;
import com.example.commons.cache.core.TableCacheDao;
import com.example.commons.constant.CommonsConstants;
import com.example.commons.dao.CommonsDao;
import com.example.commons.entity.*;
import com.example.commons.utils.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhangxu
 * Created by zhangxu on 2018/6/19.
 */
@Service
@Slf4j
public class CommonsService implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @Autowired
    private CommonsDao commonsDao;
    @Autowired
    private TableCacheDao tableCacheDao;
    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    /**
     * 初始化BeanFactory
     *
     * @param bFactory
     * @throws BeansException
     */
    @Override
    public void setBeanFactory(BeanFactory bFactory) throws BeansException {
        if (beanFactory == null && bFactory != null) {
            CommonsService.beanFactory = bFactory;
        }
    }

    /***
     *  将指定类型的javaBean数据插入到数据库中
     * @param dataObject
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> T insertData(T dataObject) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataObject.getClass());
        this.fillBeanInitValues(dataObject);
        commonsDao.insert(entitySqlTable, dataObject);
        //将数据更新到缓存中
        addDataToCache(entitySqlTable, dataObject);
        return dataObject;
    }


    /***
     * 将指定类型的javaBean数据插入到数据库中(循环插入数据，可以更新缓存）
     * @param dataList
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> Integer insertDataList(List<T> dataList) {
        Integer cnt = 0;
        if (dataList != null && dataList.size() > 0) {
            EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataList.get(0).getClass());
            for (T t : dataList) {
                this.fillBeanInitValues(t);
                cnt += commonsDao.insert(entitySqlTable, t);
                //将数据更新到缓存中
                addListDataToCache(entitySqlTable, dataList);
            }
        }
        return cnt;
    }

    /***
     * 批量将数据集合插入到数据库中(批量直接插入，效率高）
     * @param dataList
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> Integer batchInsertListData(List<T> dataList) {
        Integer cnt = 0;
        if (dataList != null && dataList.size() > 0) {
            EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataList.get(0).getClass());
            List<List<T>> listArray = ListUtils.partition(dataList, 200);
            for (List<T> itemList : listArray) {
                cnt += commonsDao.insertList(entitySqlTable, itemList);
            }
        }
        return cnt;
    }

    /***
     * 更新数据库数据
     * @param dataObject
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> Integer updateData(T dataObject) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataObject.getClass());
        //更新时添加更新者更新时间
        this.fillBeanUpdateValues(dataObject);
        Integer cnt = commonsDao.update(entitySqlTable, dataObject);
        //更新缓存中的业务数据
        updateDataToCache(entitySqlTable, dataObject);
        return cnt;
    }


    /***
     *
     * 更新数据库数据。如果字段为空，则不处理该字段。
     * <B>能够记录操作日志</B>
     * @param dataObject
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> Integer forEachUpdateExcludeNull(T dataObject) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataObject.getClass());
        T dbEntity = selectById(dataObject.getClass(), BeanUtils.getPropertyValue(dataObject, "id"));
        //判断传递过来的属性是否为空，为空使用原始数据
        for (EntitySqlColumn sqlColumn : entitySqlTable.getSqlColumns()) {
            Object value = BeanUtils.getPropertyValue(dataObject, sqlColumn.getAttributeName());
            if (value != null) {
                BeanUtils.setPropertyValue(dbEntity, sqlColumn.getAttributeName(), value);
            }
        }
        Integer cnt = commonsDao.update(entitySqlTable, dbEntity);
        //更新缓存中的业务数据
        updateDataToCache(entitySqlTable, dbEntity);
        return cnt;
    }

    /***
     * 更新数据库数据。如果字段为空，则不处理该字段。
     * <B>不能够记录操作日志</B>
     * @param dataObject
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> Integer updateExcludeNull(T dataObject) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataObject.getClass());
        //更新时添加更新者更新时间
        this.fillBeanUpdateValues(dataObject);
        Integer cnt = commonsDao.updateExcludeNull(entitySqlTable, dataObject);
        //更新缓存中的业务数据
        updateDataToCache(entitySqlTable, dataObject);
        return cnt;
    }

    /***
     * 更新数据库数据。如果字段为空，则不处理该字段。
     * <B>不能够记录操作日志</B>
     * @param dataObject
     * @param whereCondition  更新条件
     * @param <T>
     * @return
     */
    @DBWriteTransactional
    public <T> Integer updateExcludeNullWhere(T dataObject, WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(dataObject.getClass());
        //更新时添加更新者更新时间
        this.fillBeanUpdateValues(dataObject);
        Integer cnt = commonsDao.updateExcludeNullWhere(entitySqlTable, dataObject, whereCondition);
        //更新缓存中的业务数据
        updateDataToCache(entitySqlTable, dataObject, whereCondition);
        return cnt;
    }

    /**
     * 删除数据库数据
     *
     * @param tClass 类型
     * @param id     数据主键Id
     */
    @DBWriteTransactional
    public Integer deleteById(Class tClass, Long id) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        return commonsDao.delete(entitySqlTable, id);
    }

    /**
     * 删除数据库数据
     *
     * @param tClass      类型
     * @param whereString 删除数据条件 "user_name ='John'"
     */
    @DBWriteTransactional
    public Integer deleteByWhereString(Class tClass, String whereString) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        removeDataFromCacheCondition(entitySqlTable, whereString);
        return commonsDao.deleteByWhereString(entitySqlTable, whereString);
    }


    /**
     * 删除数据库数据
     *
     * @param tClass         类型
     * @param whereCondition 删除数据条件
     */
    @DBWriteTransactional
    public Integer deleteByCondition(Class tClass, WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        removeDataFromCacheCondition(entitySqlTable, whereCondition.toString());
        return commonsDao.deleteByCondition(entitySqlTable, whereCondition);
    }

    /**
     * 删除数据库数据
     *
     * @param tClass
     */
    @DBReadTransactional
    public <T> T selectById(Class tClass, Long id) {
        return findDataById(tClass, id, 1);
    }


    /***
     * 查询数据库数据
     * @param tClass
     * @param whereString 查询条件
     * @param <T>
     * @return
     */
    @DBReadTransactional
    public <T> List<T> selectByWhereString(Class tClass, String whereString) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        List<Map<String, Object>> result = commonsDao.selectByWhereString(entitySqlTable, whereString);
        return convertToBeanList(result, tClass);
    }

    /***
     * 查询数据库数据
     * @param tClass
     * @param whereString 查询条件
     * @param pageNum 页码
     * @return
     */
    @DBReadTransactional
    public <T> PageInfo<T> selectByWhereString(Class<T> tClass, Integer pageNum, String whereString) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        PageHelper.startPage(pageNum, CommonsConstants.PAGE_SIZE);
        List<Map<String, Object>> result = commonsDao.selectByWhereString(entitySqlTable, whereString);
        return new PageInfo<>(convertToBeanList(result, tClass));
    }

    /***
     * 查询数据库数据
     * @param tClass
     * @param pageSize 分页条数
     * @param pageNum 页码
     * @param whereString 查询条件
     * @return
     */
    @DBReadTransactional
    public <T> PageInfo<T> selectByCondition(Class<T> tClass, Integer pageSize, Integer pageNum, String whereString) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        setPageInfo(pageSize, pageNum);
        List<Map<String, Object>> result = commonsDao.selectByWhereString(entitySqlTable, whereString);
        return new PageInfo<>(convertToBeanList(result, tClass));
    }

    /***
     * 查询数据库数据
     * @param tClass
     * @param whereCondition 查询条件
     * @param <T>
     * @return
     */
    @DBReadTransactional
    public <T> T selectOneByCondition(Class tClass, WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        List<Map<String, Object>> result = commonsDao.selectByCondition(entitySqlTable, whereCondition);
        if (result == null || result.size() == 0) {
            return null;
        } else if (result.size() == 1) {
            return (T) BeanUtils.mapToBean(convertBeanProperty(result.get(0)), tClass);
        } else {
            throw new IllegalArgumentException("查询返回多条数据");
        }
    }

    /***
     * 查询数据库数据
     * @param tClass
     * @param whereCondition 查询条件
     * @param <T>
     * @return
     */
    @DBReadTransactional
    public <T> List<T> selectByCondition(Class tClass, WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        List<Map<String, Object>> result = commonsDao.selectByCondition(entitySqlTable, whereCondition);
        return convertToBeanList(result, tClass);
    }

    /***
     * 查询数据库数据
     * @param tClass
     * @param whereCondition 查询条件
     * @param pageNum 页码
     * @return
     */
    @DBReadTransactional
    public <T> PageInfo<T> selectByCondition(Class<T> tClass, Integer pageNum, WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        if (pageNum == null) {
            PageHelper.startPage(1, Integer.MAX_VALUE);
        } else {
            PageHelper.startPage(pageNum, CommonsConstants.PAGE_SIZE);
        }
        List<Map<String, Object>> result = commonsDao.selectByCondition(entitySqlTable, whereCondition);
        return new PageInfo<>(convertToBeanList(result, tClass));
    }

    /***
     * 查询数据库数据
     * @param tClass
     * @param pageSize 分页条数
     * @param pageNum 页码
     * @param whereCondition 查询条件
     * @return
     */
    @DBReadTransactional
    public <T> PageInfo<T> selectByCondition(Class<T> tClass, Integer pageSize, Integer pageNum,
                                             WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        setPageInfo(pageSize, pageNum);
        List<Map<String, Object>> result = commonsDao.selectByCondition(entitySqlTable, whereCondition);
        return new PageInfo<>(convertToBeanList(result, tClass));
    }

    /***
     * 根据查询条件获取数据条数
     * @param tClass
     * @param whereCondition 查询条件
     * @return
     */
    @DBReadTransactional
    public <T> int selectCountCondition(Class<T> tClass, WhereCondition whereCondition) {
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        Integer count = commonsDao.selectCountCondition(entitySqlTable, whereCondition);
        return count;
    }

    /***
     *  获取当期登陆用户信息
     * @return
     */
    @DBReadTransactional
    @Deprecated
    public CurrentEmployee getCurrentUser() {
        try {
            return currentEmployeeService.getCurrentEmployee(HttpContextUtils.getRequest());
        } catch (Exception xe) {
            return null;
        }
    }

    /***
     * 当前登录用户的UUID，全局唯一
     * @return
     */
    public String getCurrentUserID() {
        String UUID = SqlBusinessEvents.getOperatorUUID();
        if (StringUtils.isNotBlank(UUID)) {
            return UUID;
        }
        CurrentEmployee currentEmployee = getCurrentUser();
        Integer userId = currentEmployee == null ? null : currentEmployee.getId();
        if (userId == null) {
            throw new IllegalArgumentException("无法获取当前用户信息");
        }
        return String.valueOf(userId);
    }

    /***
     *  获取当期登陆用户信息(原版基础平台使用）
     * @param  defaultUserName 当期用户为null时返回defaultUserName
     * @return
     */
    @DBReadTransactional
    public String getCurrentUserName(String defaultUserName) {
        //是否使用新版IT平台
        String userUuid = SqlBusinessEvents.getOperatorUUID();
        if (StringUtils.isNotBlank(userUuid)) {
            return SqlBusinessEvents.getOperator();
        }
        //使用原始IT平台，通过原来的方式获取用户名
        CurrentEmployee currentEmployee = getCurrentUser();
        if (currentEmployee == null) {
            return defaultUserName;
        } else {
            return currentEmployee.getEmpName();
        }
    }


    /***
     *  获取当期登陆用户信息（新版基础平台使用）
     * @param  defaultUserName 当期用户为null时返回defaultUserName
     * @return
     */
    @DBReadTransactional
    public String getCurrentUserNameV2(String defaultUserName) {
        String userName = SqlBusinessEvents.getOperator();
        if (StringUtils.isBlank(userName)) {
            return defaultUserName;
        } else {
            return userName;
        }
    }

    /**
     * 获取JavaBean对象
     *
     * @param name Bean名称
     * @param <T>  Bean类型
     * @return
     */
    public static <T> T getBean(String name) {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取JavaBean对象
     *
     * @param cls Bean类型
     * @param <T> Bean类型
     * @return
     */
    public static <T> T getBean(Class cls) {
        return (T) beanFactory.getBean(cls);
    }

    /***
     * 返回当前用户登陆的Token值
     * @return
     */
    public String getUserToken() {
        String token = HttpContextUtils.getRequest().getParameter("token");
        return token;
    }

    /***
     * 填充Entity共同属性值（createdAt,createdBy,updatedBy,updatedAt)
     * @param entity
     * @param <T>
     */
    public <T> void fillBeanInitValues(T entity) {
        String currentUserName = getCurrentUserName("GuestUser");
        if (entity instanceof BaseEntity) {
            BaseEntity dataEntity = (BaseEntity) entity;
            if (Objects.isNull(dataEntity.getCreatedAt()) || Objects.isNull(dataEntity.getVersion())) {
                dataEntity.setVersion(0);
                dataEntity.setCreatedBy(currentUserName);
                dataEntity.setUpdatedBy(currentUserName);
                dataEntity.setCreatedAt(new Date());
                dataEntity.setUpdatedAt(new Date());
            }
        } else {
            Object version = BeanUtils.getPropertyValue(entity, "version");
            Object createdAt = BeanUtils.getPropertyValue(entity, "createdAt");
            if (Objects.isNull(version) || Objects.isNull(createdAt)) {
                BeanUtils.setPropertyValue(entity, "version", 0);
                BeanUtils.setPropertyValue(entity, "createdAt", DateUtils.now());
                BeanUtils.setPropertyValue(entity, "createdBy", currentUserName);
                BeanUtils.setPropertyValue(entity, "updatedAt", DateUtils.now());
                BeanUtils.setPropertyValue(entity, "updatedBy", currentUserName);
            }
        }

    }

    /***
     * 填充Entity更新时的属性值（updatedBy,updatedAt)
     * @param entity
     * @param <T>
     */
    public <T> void fillBeanUpdateValues(T entity) {
        String currentUserName = getCurrentUserName("GuestUser");
        if (entity instanceof BaseEntity) {
            BaseEntity dataEntity = (BaseEntity) entity;
            dataEntity.setUpdatedBy(currentUserName);
            dataEntity.setUpdatedAt(DateUtils.now());
        } else {
            BeanUtils.setPropertyValue(entity, "updatedAt", DateUtils.now());
            BeanUtils.setPropertyValue(entity, "updatedBy", currentUserName);
        }

    }

    //=================================================================================================================
    //=================================================================================================================

    /**
     * 删除数据库数据
     *
     * @param tClass
     * @param id         数据主键ID
     * @param sourceType 1：先从缓存中获取，缓存中没有再从数据库中获取，2:直接从数据库中获取
     */
    private <T> T findDataById(Class tClass, Long id, int sourceType) {
        long begin = System.currentTimeMillis();
        EntitySqlTable entitySqlTable = EntitySqlFactory.getClassMap(tClass);
        if (sourceType == 1) {
            //获取缓存中的数据
            T t = getDataFromCache(entitySqlTable, String.valueOf(id));
            if (t != null) {
                long end0 = System.currentTimeMillis();
                long useTime0 = end0 - begin;
                log.info("----------------------------------命中缓存 耗时：{}", useTime0);
                return t;
            }
        }
        long begin1 = System.currentTimeMillis();
        Map<String, Object> data = commonsDao.selectById(entitySqlTable, id);
        long end2 = System.currentTimeMillis();
        long useTime0 = begin1 - begin;
        long useTime1 = end2 - begin1;
        log.info("----------------------------------未命中缓存，从数据库中获取数据 前置操作耗时：{}，查询操作耗时：{}", useTime0, useTime1);
        if (data == null) {
            return null;
        }
        long begin2 = System.currentTimeMillis();
        T t = (T) BeanUtils.mapToBean(data, tClass);
        long end3 = System.currentTimeMillis();
        //如果在缓存中没有，在数据库中有数据，将查询的数据添加到缓存中
        addDataToCache(entitySqlTable, t);
        long end4 = System.currentTimeMillis();
        long useTime3 = end3 - begin2;
        long useTime4 = end4 - end3;

        long begin3 = System.currentTimeMillis();
        T t1 = (T) JsonUtils.toBean(JsonUtils.toJson(data), tClass);
        long end5 = System.currentTimeMillis();
        long useTime5 = end5 - begin3;
        log.info("----------------------------------Bean转换耗时：{}，添加缓存耗时：{},JsonUtils转换耗时：{}", useTime3, useTime4,
                useTime5);
        return t;
    }

    /**
     * 获取实体类的ID值
     *
     * @param entity
     * @return
     */
    private String getId(Object entity) {
        if (entity instanceof BaseEntity) {
            return ((BaseEntity) entity).getId().toString();
        } else {
            return BeanUtils.getPropertyValue(entity, "id").toString();
        }
    }

    /**
     * 将数据添加到缓存中
     *
     * @param entitySqlTable
     * @param dataObject
     * @param <T>
     */
    private <T> void addDataToCache(EntitySqlTable entitySqlTable, T dataObject) {
        //将数据添加到缓存中
        if (entitySqlTable.getUserCache()) {
            tableCacheDao.add(CommonsUtils.getCacheKey(entitySqlTable.getEntityClass()), getId(dataObject),
                    JsonUtils.toJson(dataObject));
        }
    }

    /**
     * 将数据添加到缓存中
     *
     * @param entitySqlTable
     * @param dataListObject
     * @param <T>
     */
    private <T> void addListDataToCache(EntitySqlTable entitySqlTable, List<T> dataListObject) {
        //将数据添加到缓存中
        if (entitySqlTable.getUserCache()) {
            for (T item : dataListObject) {
                tableCacheDao.add(CommonsUtils.getCacheKey(entitySqlTable.getEntityClass()), getId(item),
                        JsonUtils.toJson(item));
            }
        }
    }

    /**
     * 更新缓存中的数据
     *
     * @param entitySqlTable
     * @param dataObject
     * @param <T>
     */
    private <T> void updateDataToCache(EntitySqlTable entitySqlTable, T dataObject, WhereCondition whereCondition) {
        //将数据添加到缓存中(如果数据已经存在，先删除再添加)
        if (entitySqlTable.getUserCache()) {
            List<T> list = this.selectByCondition(entitySqlTable.getEntityClass(), whereCondition);
            if (CollectionUtils.isEmpty(list)) {
                for (T item : list) {
                    String key = getId(dataObject);
                    //删除缓存中的数据
                    removeDataFromCache(entitySqlTable, key);
                    //从数据库中获取数据
                    if (item == null) {
                        continue;
                    }
                    tableCacheDao.add(CommonsUtils.getCacheKey(entitySqlTable.getEntityClass()), key,
                            JsonUtils.toJson(item));
                }
            }
        }
    }


    /**
     * 更新缓存中的数据
     *
     * @param entitySqlTable
     * @param dataObject
     * @param <T>
     */
    private <T> void updateDataToCache(EntitySqlTable entitySqlTable, T dataObject) {
        //将数据添加到缓存中(如果数据已经存在，先删除再添加)
        if (entitySqlTable.getUserCache()) {
            String key = getId(dataObject);
            //删除缓存中的数据
            removeDataFromCache(entitySqlTable, key);
            //从数据库中获取数据
            T object = this.findDataById(entitySqlTable.getEntityClass(), Long.parseLong(key), 2);
            if (object == null) {
                log.error("不能正确从数据库中获取数据：className:{} ,Id:{}", entitySqlTable.getClassName(), key);
                throw new IllegalArgumentException("更新缓存失败");
            }
            tableCacheDao.add(CommonsUtils.getCacheKey(entitySqlTable.getEntityClass()), key, JsonUtils.toJson(object));
        }
    }

    /***
     * 从缓存中获取数据
     * @param entitySqlTable 实体类型
     * @param key       Id值
     * @param <T>
     * @return
     */
    private <T> T getDataFromCache(EntitySqlTable entitySqlTable, String key) {
        if (entitySqlTable.getUserCache()) {
            String data = tableCacheDao.get(CommonsUtils.getCacheKey(entitySqlTable.getEntityClass()), key);
            if (StringUtils.isBlank(data)) {
                return null;
            }
            T object = (T) JsonUtils.toBean(data, entitySqlTable.getEntityClass());
            return object;
        }
        return null;
    }

    /***
     *  删除缓存中的数据
     * @param entitySqlTable
     * @param key
     */
    private void removeDataFromCache(EntitySqlTable entitySqlTable, String key) {
        if (entitySqlTable.getUserCache()) {
            String tableName = CommonsUtils.getCacheKey(entitySqlTable.getEntityClass());
            if (tableCacheDao.exists(tableName, key)) {
                tableCacheDao.delete(tableName, key);
            }
        }
    }

    /***
     *  根据条件删除缓存中的数据
     * @param entitySqlTable
     * @param condition
     */
    private void removeDataFromCacheCondition(EntitySqlTable entitySqlTable, String condition) {
        if (entitySqlTable.getUserCache()) {
            List list = selectByWhereString(entitySqlTable.getEntityClass(), condition);
            String tableName = CommonsUtils.getCacheKey(entitySqlTable.getEntityClass());
            for (Object item : list) {
                String key = ((Object) BeanUtils.getPropertyValue(item, "id")).toString();
                if (tableCacheDao.exists(tableName, key)) {
                    tableCacheDao.delete(tableName, key);
                }
            }
        }
    }

    /***
     * 将数据库的查询结果List<Map>转换成List<Bean>形式
     * @param list
     * @param classType
     * @param <T>
     * @return
     */
    private <T> List<T> convertToBeanList(List<?> list, Class<T> classType) {
        List<T> result = new ArrayList<T>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        for (Object objItem : list) {
            Map<String, Object> item = (Map<String, Object>) objItem;
            Map<String, Object> beanItem = convertBeanProperty(item);
            T dataItem = BeanUtils.mapToBean(beanItem, classType);
            result.add(dataItem);
        }
        if (list instanceof Page) {
            list.clear();
            ((Page) list).addAll(result);
            return (List<T>) list;
        }
        return result;
    }


    /***
     * 将数据库字段值替换成Java属性名，即：user_code => userCode
     * @param queryResult
     * @return
     */
    private Map<String, Object> convertBeanProperty(Map<String, Object> queryResult) {
        if (MapUtils.isEmpty(queryResult)) {
            return queryResult;
        }
        Map<String, Object> result = new HashedMap();
        for (Map.Entry<String, Object> item : queryResult.entrySet()) {
            String key = item.getKey();
            Object value = item.getValue();
            if (key.indexOf('_') > 0) {
                key = key == null ? null : key.toLowerCase();
                key = CommonsUtils.removePropertyUnderline(key);
            }
            result.put(key, value);
        }
        return result;
    }


    /**
     * 设置分页详情
     *
     * @param pageSize 默认值= 10
     * @param pageNum  默认值= 1
     */
    private void setPageInfo(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum == null || pageNum < 1 ? 1 : pageNum,
                pageSize == null || pageSize < 1 ? CommonsConstants.PAGE_SIZE : pageSize);
    }

    /**
     * 通用条件查询
     *
     * @param baseSql
     * @param orderBySql
     * @return
     */
    // public <T> List<T> listBaseEntity(BaseEntitySql baseSql, OrderBySqlBase orderBySql) {
    //     orderBySql.sqlSimple();
    //     List<Object> list = (List<Object>) (Object) commonsDao.listBaseEntity(baseSql, orderBySql);
    //     List<T> tmp = new ArrayList<>();
    //     for (Object o : list) {
    //         T dataItem = (T) BeanUtils.mapToBean((Map<String, Object>) o,
    //                 baseSql.getEntitySqlTable("").getEntityClass());
    //         tmp.add(dataItem);
    //     }
    //     list.clear();
    //     list.addAll(tmp);
    //     return (List<T>) (T) list;
    // }

    /**
     * 通用条件查询
     *
     * @param baseSql
     * @param orderBySql
     * @return
     */
    // public <T> T oneBaseEntity(BaseEntitySql baseSql, OrderBySqlBase orderBySql) {
    //     orderBySql.sqlSimple();
    //     Map map = commonsDao.oneBaseEntity(baseSql, orderBySql);
    //     if (map != null) {
    //         T dataItem = (T) BeanUtils.mapToBean((Map<String, Object>) map,
    //                 baseSql.getEntitySqlTable("").getEntityClass());
    //         return dataItem;
    //     } else {
    //         return null;
    //     }
    //
    // }
}
