package com.example.commons.entity;

import com.example.commons.entity.dto.SqlBusinessEventsDto;
import com.example.commons.utils.EncryptUtils;

import java.util.UUID;

/**
 * 记录数据库操作人事件
 */
public class SqlBusinessEvents {
    //
    private static final ThreadLocal<SqlBusinessEventsDto> Local = new ThreadLocal();

    private static SqlBusinessEventsInterface sqlBusinessEventsInterface;

    public static SqlBusinessEventsInterface getSqlBusinessEventsInterface() {
        return sqlBusinessEventsInterface;
    }

    public static void setSqlBusinessEventsInterface(SqlBusinessEventsInterface sqlBusinessEventsInterface) {
        SqlBusinessEvents.sqlBusinessEventsInterface = sqlBusinessEventsInterface;
    }

    private static SqlBusinessEventsDto getLocal() {
        if (sqlBusinessEventsInterface != null) {
            return sqlBusinessEventsInterface.getCurrentSqlBusinessEventsDto();
        }
        return Local.get();
    }

    private static SqlBusinessEventsDto getLocalOrCreate() {
        SqlBusinessEventsDto dto = Local.get();
        if (dto == null) {
            dto = new SqlBusinessEventsDto();
            Local.set(dto);
        }
        return dto;
    }

    /**
     * 设置数据库操作人
     *
     * @param name
     */
    public static void setOperator(String name) {
        getLocalOrCreate().setOperator(name);
    }

    /**
     * 获取数据库操作人
     */
    public static String getOperator() {
        SqlBusinessEventsDto dto = getLocal();
        if (dto != null) {
            return dto.getOperator();
        }
        return null;
    }

    /**
     * 获取数据库操作人UUID
     */
    public static String getOperatorUUID() {
        SqlBusinessEventsDto dto = getLocal();
        if (dto != null) {
            return dto.getOperatorUUID();
        }
        return null;
    }

    /**
     * 获取数据库操作人UUID
     */
    public static void setOperatorUUID(String uuid) {
        getLocalOrCreate().setOperatorUUID(uuid);
    }

    /**
     * 获取数据库操作人ID
     */
    public static void setOperatorID(Long id) {
        getLocalOrCreate().setOperatorId(id);
    }

    /**
     * 获取数据库操作人UUID
     */
    public static Long getOperatorID() {
        SqlBusinessEventsDto dto = getLocal();
        if (dto != null) {
            return dto.getOperatorId();
        }
        return null;
    }


    /**
     * 设置数据库事件
     *
     * @param key
     */
    public static void setEventsKey(String key) {
        getLocalOrCreate().setEventKey(key);
    }

    /**
     * 获取数据库操事件Key
     */
    public static String getEventsKey() {
        SqlBusinessEventsDto dto = getLocal();
        if (dto != null) {
            return dto.getEventKey();
        }
        return null;
    }

    /**
     * 获取数据库操事件Key，不存在则创建
     */
    public static String getEventsKeyNoAutoCreate() {
        String key = getEventsKey();
        if (key == null) {
            key = "auto" + EncryptUtils.md516(UUID.randomUUID().toString());
            setEventsKey(key);
        }
        return key;
    }

    /**
     * 设置数据库事件id
     *
     * @param id
     */
    public static void setEventsId(String id) {
        getLocalOrCreate().setEventId(id);
    }

    /**
     * 获取数据库操事件id
     */
    public static String getEventsId() {
        SqlBusinessEventsDto dto = getLocal();
        if (dto != null) {
            return dto.getEventId();
        }
        return null;
    }

    /**
     * 设置数据库事件名称
     *
     * @param name
     */
    public static void setEventsName(String name) {
        getLocalOrCreate().setEventName(name);
    }

    /**
     * 获取数据库操事件id
     */
    public static String getEventsName() {
        SqlBusinessEventsDto dto = getLocal();
        if (dto != null) {
            return dto.getEventName();
        }
        return null;
    }


    public static void remove() {
        if (sqlBusinessEventsInterface != null) {
            sqlBusinessEventsInterface.removeCurrentSqlBusinessEventsDto();
        }
        Local.remove();
    }

}
