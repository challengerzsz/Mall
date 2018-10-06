package com.bsb.util;

import com.bsb.web.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zeng
 * @Date: 2018/10/6 10:31
 */
public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    //初始化objectMapper
    static {
        //对象所有字段全部序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        //取消默认转换timestamp形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        //忽略Bean为空的情况转换json错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //设置Json序列化的时候日期的格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));


        //如果json字符串中存在某个字段但是pojo中不存在 防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static <T> String objToString(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.warn("Parse object to String error ", e);
            return null;
        }
    }

    public static <T> String objToStringPretty(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            logger.warn("Parse object to String error ", e);
            return null;
        }
    }

    public static <T> T stringToObj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            logger.warn("Parse String to Object error ", e);
            return null;
        }

    }

    /**
     * 复杂对象反序列化
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T stringToObj(String str, TypeReference<T> typeReference) {

        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }

        try {
            return typeReference.getType().equals(String.class)? (T) str : objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            logger.warn("Parse String to Object error ", e);
            return null;
        }
    }

    /**
     * 复杂对象反序列化
     * @param str
     * @param collectionClass
     * @param elementClasses
     * @param <T>
     * @return
     */
    public static <T> T stringToObj(String str, Class<?> collectionClass, Class<?>... elementClasses) {

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            logger.warn("Parse String to Object error ", e);
            return null;
        }

    }


}
