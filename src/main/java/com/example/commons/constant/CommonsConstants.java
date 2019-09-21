package com.example.commons.constant;

import java.util.ArrayList;

/**
 * Created by zhangxu on 2018/6/13.
 */
public class CommonsConstants {

    private CommonsConstants() {
        throw new UnsupportedOperationException("不允许实例化");
    }

    public static final int PAGE_SIZE = 10;

    // public static List<ResponseMessage> responseMessageList() {
    //     List<ResponseMessage> responseMessageList = new ArrayList<>();
    //     responseMessageList.add(new ResponseMessageBuilder().code(200).message("请求成功").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(400).message("非json请求").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(401).message("鉴权失败").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(403).message("禁止访问").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(405).message("请求方法错误").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(406).message("请求参数有误").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(408).message("更新数据过期").build());
    //     responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务异常").build());
    //
    //     return responseMessageList;
    // }

    /**事物管理器键值*/
    public static final String COMMON_TRANSACTION_MANAGER = "transactionManager";
    /**数据库表Key值*/
    public final static String COMMONS_TABLE_CACHE_KEY = "commons:table:cache:{0}";

    /**操作记录缓存Key值*/
    public final static String OPERATE_RECORD_CACHE_KEY = "commons:operate:record:key";
    /**操作记录缓存Key值过期时间(秒)*/
    public final static int OPERATE_RECORD_EXPIRE = 30;

    /**修订记录比较排除字段设置*/
    public final static String[] COMMONS_REVISE_DIFF_EXCEPT_FIELDS = {"createdBy","createdAt","updatedBy","updatedAt","version"};

}
