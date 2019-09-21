package com.example.commons.cache.config;

import java.util.Objects;

/**
 * Created by gizmo on 16/2/21.
 */
public class CacheConfig {
    private int minIdle = 0;
    private int maxIdle = 10;
    private int maxSize = 50;
    private int timeout = 5000;
    private int maxRedirections = 10;
    private boolean fairness = true;
    private String password;

    public static CacheConfig newInstance() {
        return new CacheConfig();
    }

    public int getMinIdle() {
        return minIdle;
    }

    public CacheConfig setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        return this;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public CacheConfig setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
        return this;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public CacheConfig setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public CacheConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public CacheConfig setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
        return this;
    }

    public boolean isFairness() {
        return fairness;
    }

    public CacheConfig setFairness(boolean fairness) {
        this.fairness = fairness;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CacheConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CacheConfig)) {
            return false;
        }
        CacheConfig that = (CacheConfig) o;
        return getMinIdle() == that.getMinIdle() &&
                getMaxIdle() == that.getMaxIdle() &&
                getMaxSize() == that.getMaxSize() &&
                getTimeout() == that.getTimeout() &&
                getMaxRedirections() == that.getMaxRedirections() &&
                isFairness() == that.isFairness() &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinIdle(), getMaxIdle(), getMaxSize(), getTimeout(), getMaxRedirections(),
                isFairness(), getPassword());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CacheConfig{");
        sb.append("minIdle=").append(minIdle);
        sb.append(", maxIdle=").append(maxIdle);
        sb.append(", maxSize=").append(maxSize);
        sb.append(", timeout=").append(timeout);
        sb.append(", maxRedirections=").append(maxRedirections);
        sb.append(", fairness=").append(fairness);
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
