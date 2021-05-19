package com.wfw.framework.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author liaonanzhou
 * @date 2021/5/19 10:32
 * @description
 **/
@Slf4j
public class ObjectMapperUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper
                .registerModule(new JavaTimeModule())
                // 是否需要排序
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                // 忽略空bean转json的错误
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 取消默认转换timestamps形式
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 序列化的时候，过滤null属性
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 忽略在json字符串中存在，但在java对象中不存在对应属性的情况，防止错误
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 忽略空bean转json的错误
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                // the parameter WRITE_DATES_AS_TIMESTAMPS tells the mapper to represent a Date as a String in JSON
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private ObjectMapperUtil() {

    }

    /**
     * 对象转json字符串
     *
     * @param obj 对象
     * @param <T> 对象泛型
     * @return json字符串
     */
    public static <T> String obj2Json(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("对象解析为json字符串异常", e);
        }
        return null;
    }

    /**
     * 复杂对象反序列化
     * 使用例子List<User> list = JsonUtil.string2Obj(str, new TypeReference<List<User>>() {});
     *
     * @param str           json对象
     * @param typeReference 引用类型
     * @param <T>           返回值类型
     * @return 反序列化对象
     */
    public static <T> T str2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(str) || typeReference == null) {
            return null;
        }
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            log.warn("json字符串解析为对象错误", e);
            return null;
        }
    }

    /**
     * 复杂对象反序列化
     * 使用例子List<User> list = JsonUtil.string2Obj(str, new TypeReference<List<User>>() {});
     *
     * @param object        对象
     * @param typeReference 引用类型
     * @param <T>           返回值类型
     * @return 反序列化对象
     */
    public static <T> T str2Obj(Object object, TypeReference<T> typeReference) {
        String str = obj2Json(object);
        return str2Obj(str, typeReference);
    }

}
