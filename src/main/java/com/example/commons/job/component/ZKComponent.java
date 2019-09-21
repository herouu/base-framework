package com.example.commons.job.component;

import com.example.commons.job.exception.ZKMutexLockException;
import com.example.commons.job.exception.ZKValueModifiedException;
import com.example.commons.zk.exception.ZKException;
import com.google.common.base.Strings;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by wanghongmeng on 2016/6/2.
 */

public class ZKComponent {

    protected static final Logger log = LoggerFactory.getLogger(ZKComponent.class);
    public interface MasterCompeteHandler {
        void handleMaster();
    }

    public static class ZKClient {
        private static final String CHARSET_UTF8 = "UTF-8";
        private static final int BASE_SLEEP_MILLIONS = 200;
        private static final int MAX_SLEEP_MILLIONS = 3000;
        private static final int MAX_RETRIES = 10;
        private static final int TIMEOUT_MILLIONS = 5000;
        private CuratorFramework curatorFramework;

        public ZKClient(Set<String> zkNodes) {
            if (null == zkNodes) {
                throw new NullPointerException("zkNodes is null");
            }

            StringBuilder connectionBuilder = new StringBuilder();
            String connectString = "";
            for (String node : zkNodes) {
                String[] hostAndPort = node.split(":");
                connectionBuilder.append(hostAndPort[0]).append(":").append(hostAndPort[1]).append(",");
            }
            if (connectionBuilder.length() > 1) {
                connectString = connectionBuilder.substring(0, connectionBuilder.length() - 1);
            }

            RetryPolicy retryPolicy = new BoundedExponentialBackoffRetry(BASE_SLEEP_MILLIONS, MAX_SLEEP_MILLIONS, MAX_RETRIES);
            curatorFramework = CuratorFrameworkFactory.builder().connectString(connectString)
                    .connectionTimeoutMs(TIMEOUT_MILLIONS).sessionTimeoutMs(TIMEOUT_MILLIONS)
                    .retryPolicy(retryPolicy).build();
            curatorFramework.start();
        }

        public CuratorFramework getCuratorFramework() {
            return curatorFramework;
        }

        public String get(String path) {
            checkParameter(path);

            Stat stat = getStat(path);
            if (null == stat) {
                return null;
            }

            return getValue(path);
        }

        public void add(String path) {
            try {
                checkParameter(path);
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                        .forPath(path);
            } catch (Exception e) {
                throw new ZKException(e);
            }
        }

        public String delete(String path) {
            Stat stat = checkDelete(path);
            if (null == stat) {
                return null;
            }

            return doDelete(path, stat.getVersion());
        }

        public boolean exist(String path) {
            return null != getStat(path);
        }

        private String getValue(String path) {
            try {
                byte[] bytes = curatorFramework.getData().forPath(path);
                return new String(bytes, CHARSET_UTF8);
            } catch (Exception e) {
                throw new ZKException(e);
            }
        }

        private String doDelete(String path, int version) {
            try {
                String oldValue = new String(curatorFramework.getData().forPath(path), CHARSET_UTF8);
                curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().withVersion(version).forPath(path);
                return oldValue;
            } catch (KeeperException.BadVersionException exception) {
                throw new ZKValueModifiedException("the value of " + path + " is modified by another thread");
            } catch (Exception e) {
                throw new ZKException(e);
            }
        }

        private Stat checkDelete(String path) {
            checkParameter(path);
            return getStat(path);
        }

        private Stat getStat(String path) {
            try {
                return curatorFramework.checkExists().forPath(path);
            } catch (Exception e) {
                throw new ZKException(e);
            }
        }

        private void checkParameter(String path) {
            if (Strings.isNullOrEmpty(path)) {
                throw new NullPointerException(path + " is blank");
            }
        }
    }

    public static class ZKMaster {
        private final ZKComponent.ZKClient zkClient;
        private LeaderSelector leaderSelector;

        public ZKMaster(ZKComponent.ZKClient zkClient) {
            this.zkClient = zkClient;
        }

        public void competeMaster(String path, final MasterCompeteHandler masterCompeteHandler) {
            try {
                leaderSelector = new LeaderSelector(zkClient.getCuratorFramework(), path,
                        new LeaderSelectorListenerAdapter() {
                            @Override
                            public void takeLeadership(CuratorFramework client) throws Exception {
                                masterCompeteHandler.handleMaster();
                            }
                        });
                leaderSelector.autoRequeue();
                leaderSelector.start();
            } catch (Exception e) {
                throw new ZKMutexLockException(e);
            }
        }
        public void close() {
            if (null != leaderSelector) {
                leaderSelector.close();
            }
        }
    }
}
