//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.commons.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.commons.exception.FrameworkUtilException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public final class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static boolean hiddenNull = false;

    private JsonUtils() {
    }

    public static String toJson(Object object) {
        return toJson(object, true);
    }

    public static String toJson(Object object, boolean hiddenNull) {
        if (hiddenNull) {
            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        } else {
            objectMapper.setSerializationInclusion(Include.ALWAYS);
        }

        try {
            return null == object ? "" : objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var3) {
            throw new FrameworkUtilException(var3);
        }
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            return StringUtils.isBlank(json) ? null : objectMapper.readValue(json, clazz);
        } catch (IOException var3) {
            throw new FrameworkUtilException(var3);
        }
    }

    public static <T> List<T> toBeanList(String json, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(json)) {
                return Collections.EMPTY_LIST;
            } else {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class,
                        new Class[]{clazz});
                return (List) objectMapper.readValue(json, javaType);
            }
        } catch (IOException var3) {
            throw new FrameworkUtilException(var3);
        }
    }

    public static String getString(String jsonStr, String property) {
        if (!StringUtils.isEmpty(jsonStr) && !StringUtils.isEmpty(property)) {
            try {
                String result = jsonStr;

                JSONObject jsonObject;
                for (StringTokenizer tokenizer = new StringTokenizer(property, "\\."); tokenizer.hasMoreTokens(); result = jsonObject.get(tokenizer.nextElement()).toString()) {
                    jsonObject = JSONObject.parseObject(result);
                }

                return result;
            } catch (Exception var5) {
                return null;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
}
