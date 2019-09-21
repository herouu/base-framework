package com.example.commons.zk.client;

import java.util.Objects;

/**
 * Created by wanghongmeng on 2016/5/21.
 */
public class ZKValuePair {
    private final String data;
    private final int version;

    public ZKValuePair(String data, int version) {
        this.data = data;
        this.version = version;
    }

    public String getData() {
        return data;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZKValuePair)) return false;
        ZKValuePair that = (ZKValuePair) o;
        return getVersion() == that.getVersion() &&
                Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getVersion());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZKValuePair{");
        sb.append("data='").append(data).append('\'');
        sb.append(", version=").append(version);
        sb.append('}');
        return sb.toString();
    }
}
