package cn.tonghao.remex.common.util;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.FactoryBean;

/**
 * <pre>
 * 初始化ObjectMapper
 * 静态化，保证Spring容器中和用{@link #getObjectMapper()}获取到的实例为同一个
 * </pre>
 */
public class ObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {

    /**
     * 全局唯一实例
     */
    private volatile static ObjectMapper objectMapper;

    /**
     * 初始化
     */
    private static synchronized void init() {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();
            //禁止遇到未知属性时报错，支持兼容处理
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    public static ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            init();
        }

        return objectMapper;
    }

    @Override
    public ObjectMapper getObject() throws Exception {
        if (null == objectMapper) {
            init();
        }

        return objectMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}