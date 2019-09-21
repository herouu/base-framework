package com.example.commons.zk.client;

import com.example.commons.zk.config.ZKConfiguration;
import com.example.commons.zk.exception.ValueModifiedException;
import com.example.commons.zk.exception.ZKException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class ZKClient extends ZKAbstractClient {

    public ZKClient(ZKConfiguration zkConfiguration) {
        super(zkConfiguration);
    }

    public String get(String path) {
        checkParameter(path);

        Stat stat = getStat(path);
        if (null == stat) {
            return null;
        }

        return getValue(path);
    }

    public ZKValuePair getWithVersion(String path) {
        checkParameter(path);

        Stat stat = getStat(path);
        if (null == stat) {
            return null;
        }

        String data = getValue(path);
        return new ZKValuePair(data, stat.getVersion());
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

    public void addSequential(String path) {
        try {
            checkParameter(path);
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(path);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public void add(String path, String value) {
        try {
            checkAdd(path, value);
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                    .forPath(path, value.getBytes());
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public void addEphemeral(String path) {
        try {
            checkParameter(path);
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public void addEphemeral(String path, String value) {
        try {
            checkAdd(path, value);
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                    .forPath(path, value.getBytes());
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public String addSequentialEphemeral(String path) {
        try {
            checkParameter(path);
            return curatorFramework.create().creatingParentsIfNeeded().withProtection()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public String addSequentialEphemeral(String path, String value) {
        try {
            checkAdd(path, value);
            return curatorFramework.create().creatingParentsIfNeeded().withProtection()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, value.getBytes());
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    public String update(String path, String value) {
        Stat stat = checkUpdate(path, value);
        return doUpdate(path, value, stat.getVersion());
    }

    public String updateWithVersion(String path, String value, int version) {
        checkUpdate(path, value);
        return doUpdate(path, value, version);
    }

    public String delete(String path) {
        Stat stat = checkDelete(path);
        if (null == stat) {
            return null;
        }

        return doDelete(path, stat.getVersion());
    }

    public String deleteWithVersion(String path, int version) {
        Stat stat = checkDelete(path);
        if (null == stat) {
            return null;
        }

        return doDelete(path, version);
    }

    private String getValue(String path) {
        try {
            byte[] bytes = curatorFramework.getData().forPath(path);
            return new String(bytes, CHARSET_UTF8);
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }

    private String doUpdate(String path, String value, int version) {
        try {
            String oldValue = new String(curatorFramework.getData().forPath(path), CHARSET_UTF8);
            curatorFramework.setData().withVersion(version).forPath(path, value.getBytes());
            return oldValue;
        } catch (KeeperException.BadVersionException exception) {
            throw new ValueModifiedException("the value of " + path + " is modified by another thread");
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
            throw new ValueModifiedException("the value of " + path + " is modified by another thread");
        } catch (Exception e) {
            throw new ZKException(e);
        }
    }
}
