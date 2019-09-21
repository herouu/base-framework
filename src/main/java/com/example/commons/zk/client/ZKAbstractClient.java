package com.example.commons.zk.client;

import com.example.commons.zk.config.ZKConfiguration;
import com.example.commons.zk.exception.PathExistException;
import com.example.commons.zk.exception.PathNotExistException;
import com.example.commons.zk.exception.ZKException;
import com.google.common.base.Strings;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
abstract class ZKAbstractClient {
    protected static final String CHARSET_UTF8 = "UTF-8";
    private static final Logger log = LoggerFactory.getLogger(ZKAbstractClient.class);
    private static final int BASE_SLEEP_MILLIONS = 200;
    private static final int MAX_SLEEP_MILLIONS = 1000;
    private static final int MAX_RETRIES = 5;
    private static final int CONNECT_TIMEOUT_MILLIONS = 10000;
    private static final int SESSION_TIMEOUT_MILLIONS = 30000;
    protected CuratorFramework curatorFramework;

    public ZKAbstractClient(ZKConfiguration zkConfiguration) {
        if (null == zkConfiguration) {
            throw new NullPointerException("zkConfiguration is null");
        }
        if (Strings.isNullOrEmpty(zkConfiguration.getConnectString())) {
            throw new NullPointerException("did not set connect nodes");
        }

        String connectString = zkConfiguration.getConnectString();
        RetryPolicy retryPolicy = new BoundedExponentialBackoffRetry(BASE_SLEEP_MILLIONS, MAX_SLEEP_MILLIONS,
                MAX_RETRIES);

        curatorFramework = CuratorFrameworkFactory.builder().connectString(connectString)
                .connectionTimeoutMs(CONNECT_TIMEOUT_MILLIONS).sessionTimeoutMs(SESSION_TIMEOUT_MILLIONS)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
        log.info("connecting to zookeeper servers");

        try {
            boolean isConnected = curatorFramework.blockUntilConnected(CONNECT_TIMEOUT_MILLIONS, TimeUnit.MILLISECONDS);
            if (isConnected) {
                log.info("connected to zookeeper servers success");
            } else {
                log.error("can't connect to zookeeper servers after " + CONNECT_TIMEOUT_MILLIONS + " million seconds");
            }
        } catch (InterruptedException e) {
            log.error("connecting is interrupted, exception:{}", e);
            throw new ZKException(e);
        }
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }

    protected void checkParameter(String path) {
        if (Strings.isNullOrEmpty(path)) {
            throw new NullPointerException(path + " is blank");
        }
    }

    protected void checkAdd(String path, String value) {
        checkParameter(path);
        checkParameter(value);

        Stat stat = getStat(path);
        if (null != stat) {
            throw new PathExistException(path + " exists");
        }
    }

    protected Stat getStat(String path) {
        try {
            return curatorFramework.checkExists().forPath(path);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    protected Stat checkUpdate(String path, String value) {
        checkParameter(path);
        checkParameter(value);

        Stat stat = getStat(path);
        if (null == stat) {
            throw new PathNotExistException(path + " not exists");
        }
        return stat;
    }

    protected Stat checkDelete(String path) {
        checkParameter(path);
        return getStat(path);
    }

    public boolean exist(String path) {
        return null != getStat(path);
    }

    public List<String> getChildrenPath(String path) {
        try {
            return ZKPaths.getSortedChildren(curatorFramework.getZookeeperClient().getZooKeeper(), path);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public void close() {
        curatorFramework.close();
    }
}
