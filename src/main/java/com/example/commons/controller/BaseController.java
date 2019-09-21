package com.example.commons.controller;

import com.example.commons.entity.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public BaseController() {
    }

    protected JsonResponse jsonData(Object data) {
        return new JsonResponse(true, (String) null, data);
    }
}
