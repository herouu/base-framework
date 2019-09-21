package com.example.commons.zk.utils;

import com.example.commons.job.component.ZKComponent;
import com.example.commons.zk.client.ZKClient;
import com.example.commons.zk.exception.MutexLockException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

/**
 * Created by wanghongmeng on 2016/5/23.
 */
public class ZKMaster {
    private final ZKClient zkClient;
    private LeaderSelector leaderSelector;

    public ZKMaster(ZKClient zkClient) {
        this.zkClient = zkClient;
    }

    public void competeMaster(String path, final ZKComponent.MasterCompeteHandler masterCompeteHandler) {
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
            throw new MutexLockException(e);
        }
    }

    public void close() {
        if (null != leaderSelector) {
            leaderSelector.close();
        }
    }
}
