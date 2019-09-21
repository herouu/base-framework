package com.example.commons.zk.handler;

public interface PathChangeHandler {

    void handleAdd(String path, String value);

    void handleUpdate(String path, String value);

    void handleDelete(String path, String value);
}