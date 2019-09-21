//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.commons.entity;

import java.util.Date;
import java.util.Objects;

public class JsonResponse {
    private boolean success = true;
    /** @deprecated */
    @Deprecated
    private String errorCode;
    private String message;
    private Object data;
    private Date timestamp = new Date();
    private Integer code;
    private String param;

    public JsonResponse() {
    }

    public JsonResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public JsonResponse(String message) {
        this.message = message;
    }

    public JsonResponse(boolean success, String errorCode, String message) {
        this.errorCode = errorCode;
        this.success = success;
        this.message = message;
    }

    public JsonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public JsonResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public JsonResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static JsonResponse newInstance() {
        return new JsonResponse();
    }

    public JsonResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public JsonResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public JsonResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return this.data;
    }

    public JsonResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public JsonResponse setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public JsonResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public JsonResponse setParam(String param) {
        this.param = param;
        return this;
    }

    public String getParam() {
        return this.param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof JsonResponse)) {
            return false;
        } else {
            JsonResponse that = (JsonResponse)o;
            return this.isSuccess() == that.isSuccess() && Objects.equals(this.getErrorCode(), that.getErrorCode()) && Objects.equals(this.getMessage(), that.getMessage()) && Objects.equals(this.getData(), that.getData()) && Objects.equals(this.getCode(), that.getCode()) && Objects.equals(this.getParam(), that.getParam()) && Objects.equals(this.getTimestamp(), that.getTimestamp());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[]{this.isSuccess(), this.getErrorCode(), this.getMessage(), this.getData(), this.getTimestamp(), this.getCode(), this.getParam()});
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("JsonResponse{");
        sb.append("success=").append(this.success);
        sb.append(", errorCode='").append(this.errorCode).append('\'');
        sb.append(", message='").append(this.message).append('\'');
        sb.append(", data=").append(this.data);
        sb.append(", timestamp=").append(this.timestamp);
        sb.append(", code=").append(this.code);
        sb.append(", param='").append(this.param).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
