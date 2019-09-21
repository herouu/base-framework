package com.example.commons.cache.core;

import com.example.commons.cache.config.CacheConfig;
import com.example.commons.cache.config.CacheNodes;
import redis.clients.jedis.*;

import java.io.IOException;

/**
 * Created by gizmo on 16/2/27.
 */
public abstract class CacheDao {
    protected JedisCommands jedisCluster;

    protected JedisPoolConfig initConfig(CacheConfig cacheConfig) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(cacheConfig.getMinIdle());
        jedisPoolConfig.setMaxIdle(cacheConfig.getMaxIdle());
        jedisPoolConfig.setMaxTotal(cacheConfig.getMaxSize());
        jedisPoolConfig.setMaxWaitMillis(cacheConfig.getTimeout());
        jedisPoolConfig.setFairness(cacheConfig.isFairness());

        return jedisPoolConfig;
    }

    protected void initRedis(CacheNodes cacheNodes, CacheConfig cacheConfig) {
        if (cacheNodes.getNodes().size() > 1) {
            jedisCluster = new JedisCluster(cacheNodes.getNodes(), cacheConfig.getTimeout(), cacheConfig.getTimeout(),
                    cacheConfig.getMaxRedirections(), cacheConfig.getPassword(), initConfig(cacheConfig));
        } else if (cacheNodes.getNodes().size() == 1) {
            HostAndPort hostAndPort = (HostAndPort) cacheNodes.getNodes().toArray()[0];
            Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), cacheConfig.getTimeout(),
                    cacheConfig.getTimeout());
            jedis.auth(cacheConfig.getPassword());
            jedisCluster = jedis;
        }
    }

    public JedisCommands getJedisCluster() {
        return jedisCluster;
    }

    public long delete(String key) {
        return jedisCluster.del(key);
    }

    public void close() throws IOException {
        if (null != jedisCluster && jedisCluster instanceof JedisCluster) {
            ((JedisCluster) jedisCluster).close();
        }
    }
}
