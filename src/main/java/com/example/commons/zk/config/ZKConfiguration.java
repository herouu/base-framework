package com.example.commons.zk.config;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class ZKConfiguration {
    private final StringBuilder nodesBuilder = new StringBuilder();

    public static ZKConfiguration newInstance() {
        return new ZKConfiguration();
    }

    public ZKConfiguration addNode(String host, int port) {
        nodesBuilder.append(host).append(":").append(port).append(",");
        return this;
    }

    public String getConnectString() {
        if (nodesBuilder.length() <= 1) {
            return nodesBuilder.toString();
        }

        return nodesBuilder.substring(0, nodesBuilder.length() - 1);
    }
}
