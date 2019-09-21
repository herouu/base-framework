package com.example.commons.zk.config;


import com.example.commons.zk.client.ZKAsyncClient;
import com.example.commons.zk.client.ZKClient;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class ZKClientFactory {
    private ZKClientFactory() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ZKConfiguration zkConfiguration = new ZKConfiguration();

        public Builder node(String host, int port) {
            zkConfiguration.addNode(host, port);
            return this;
        }

        public ZKClient build() {
            return new ZKClient(zkConfiguration);
        }

        public ZKAsyncClient buildAsync() {
            return new ZKAsyncClient(zkConfiguration);
        }
    }
}
