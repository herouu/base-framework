package com.example.commons.cache.lock;

import com.example.commons.cache.core.CacheDao;
import redis.clients.jedis.JedisCommands;

import java.util.UUID;

/**
 * Created by wanghongmeng on 16/4/27.
 */
public class CacheLock {
    private static final int DEFAULT_TTL_SECONDS = 60;
    private static final long SLEEP_MILLION_SECONDS = 50;
    private JedisCommands jedisCluster;

    public CacheLock(CacheDao cacheDao) {
        this.jedisCluster = cacheDao.getJedisCluster();
    }

    public boolean lock(String key) {
        return doLock(key, DEFAULT_TTL_SECONDS);
    }

    public boolean lock(String key, int ttlSeconds) {
        return doLock(key, ttlSeconds);
    }

    private boolean doLock(String key, int ttl) {
        if (null == key || "".equals(key)) {
            throw new NullPointerException();
        }
        if (ttl < 0) {
            throw new IllegalArgumentException();
        }

        String value = generateRandom();
        Long result = jedisCluster.setnx(key, value);

        if (result == 1) {
            jedisCluster.expire(key, ttl);
            return true;
        } else {
            Long cacheTTL = jedisCluster.ttl(key);
            if (cacheTTL < 0) {
                jedisCluster.expire(key, ttl);
            }
            return false;
        }
    }

    public boolean tryLock(String key, int timeoutSeconds) throws InterruptedException {
        return doTryLock(key, timeoutSeconds, DEFAULT_TTL_SECONDS);
    }

    public boolean tryLock(String key, int timeoutSeconds, int ttlSeconds) throws InterruptedException {
        return doTryLock(key, timeoutSeconds, ttlSeconds);
    }

    private boolean doTryLock(String key, int timeout, int ttlSeconds) throws InterruptedException {
        if (null == key || "".equals(key)) {
            throw new NullPointerException();
        }
        if (timeout < 0) {
            throw new IllegalArgumentException();
        }
        if (ttlSeconds < 0) {
            throw new IllegalArgumentException();
        }

        long timeoutMillionSeconds = timeout * 1000L;
        boolean isLock = false;
        while (true) {
            if (timeoutMillionSeconds <= 0) {
                break;
            }

            isLock = doLock(key, ttlSeconds);
            if (isLock) {
                break;
            }

            Thread.sleep(SLEEP_MILLION_SECONDS);
            timeoutMillionSeconds -= SLEEP_MILLION_SECONDS;
        }

        return isLock;
    }

    public String getLock(String key) {
        return jedisCluster.get(key);
    }

    public void release(String key) {
        jedisCluster.del(key);
    }

    private String generateRandom() {
        String uuid = UUID.randomUUID().toString();
        Long timestamp = System.nanoTime();
        return uuid + "-" + timestamp;
    }
}
