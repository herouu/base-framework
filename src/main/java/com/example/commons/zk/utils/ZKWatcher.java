package com.example.commons.zk.utils;

import com.example.commons.zk.client.ZKClient;
import com.example.commons.zk.exception.WatcherException;
import com.example.commons.zk.exception.ZKException;
import com.example.commons.zk.handler.NodeChangeHandler;
import com.example.commons.zk.handler.PathChangeHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

import java.io.IOException;

/**
 * Created by wanghongmeng on 2016/5/23.
 */
public class ZKWatcher {
    private final ZKClient zkClient;
    private NodeCache nodeCache;
    private PathChildrenCache pathChildrenCache;

    public ZKWatcher(ZKClient zkClient) {
        this.zkClient = zkClient;
    }

    public void watchPath(String path, final NodeChangeHandler nodeChangeHandler) {
        try {
            nodeCache = new NodeCache(zkClient.getCuratorFramework(), path, false);
            nodeCache.start();
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    ChildData childData = nodeCache.getCurrentData();
                    if (null == childData) {
                        nodeChangeHandler.handleChange(null);
                    } else {
                        nodeChangeHandler.handleChange(new String(nodeCache.getCurrentData().getData(), "UTF-8"));
                    }
                }
            });
        } catch (Exception e) {
            throw new WatcherException(e);
        }
    }

    public void watchPathChildren(final String path, final PathChangeHandler pathChangeHandler) {
        try {
            pathChildrenCache = new PathChildrenCache(zkClient.getCuratorFramework(), path, true);
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    String path = event.getData().getPath();
                    String value = new String(event.getData().getData(), "UTF-8");
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            pathChangeHandler.handleAdd(path, value);
                            break;
                        case CHILD_UPDATED:
                            pathChangeHandler.handleUpdate(path, value);
                            break;
                        case CHILD_REMOVED:
                            pathChangeHandler.handleDelete(path, value);
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            throw new WatcherException(e);
        }
    }

    public void closeNodeWatch() {
        try {
            if (null != nodeCache) {
                nodeCache.close();
            }
        } catch (IOException e) {
            throw new ZKException(e);
        }
    }

    public void closePathWatch() {
        try {
            if (null != pathChildrenCache) {
                pathChildrenCache.close();
            }
        } catch (IOException e) {
            throw new ZKException(e);
        }
    }
}
