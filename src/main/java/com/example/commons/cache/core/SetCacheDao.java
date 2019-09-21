package com.example.commons.cache.core;


import com.example.commons.cache.config.CacheConfig;
import com.example.commons.cache.config.CacheNodes;

import java.util.Set;

/**
 * Created by gizmo on 16/2/27.
 */
public class SetCacheDao extends CacheDao {

    public SetCacheDao() {
        this(new CacheNodes(), new CacheConfig());
    }

    public SetCacheDao(CacheNodes cacheNodes) {
        this(cacheNodes, new CacheConfig());
    }

    public SetCacheDao(CacheNodes cacheNodes, CacheConfig cacheConfig) {
//        jedisCluster = new JedisCluster(cacheNodes.getNodes(), cacheConfig.getTimeout(), cacheConfig.getTimeout(),
//                cacheConfig.getMaxRedirections(), cacheConfig.getPassword(), initConfig(cacheConfig));
        initRedis(cacheNodes,cacheConfig);
    }

    public long add(String key, String... values) {
        return jedisCluster.sadd(key, values);
    }

    public long add(String key, int expire, String... values) {
        long total = jedisCluster.sadd(key, values);
        jedisCluster.expire(key, expire);

        return total;
    }

    public long delete(String key, String... values) {
        return jedisCluster.srem(key, values);
    }

    public boolean exists(String key, String value) {
        return jedisCluster.sismember(key, value);
    }

    public long size(String key) {
        return jedisCluster.scard(key);
    }

    public Set<String> getAll(String key) {
        return jedisCluster.smembers(key);
    }
}
