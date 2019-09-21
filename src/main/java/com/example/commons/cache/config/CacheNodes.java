package com.example.commons.cache.config;

import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by gizmo on 16/2/27.
 */
public class CacheNodes {
    private Set<HostAndPort> nodes = new HashSet<>();

    public CacheNodes() {
        //默认无参
    }

    public CacheNodes(Set<String> nodes) {
        for (String node : nodes) {
            StringTokenizer tokenizer = new StringTokenizer(node, ":");
            String host = tokenizer.nextToken();
            String port = tokenizer.nextToken();
            addNode(host, Integer.parseInt(port));
        }
    }

    public static CacheNodes newInstance() {
        return new CacheNodes();
    }

    public Set<HostAndPort> getNodes() {
        return nodes;
    }

    public CacheNodes setNodes(Set<HostAndPort> nodes) {
        this.nodes = nodes;
        return this;
    }

    public CacheNodes addNode(String host, int port) {
        nodes.add(new HostAndPort(host, port));
        return this;
    }
}
