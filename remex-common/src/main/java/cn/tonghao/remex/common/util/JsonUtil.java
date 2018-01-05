package cn.tonghao.remex.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 此类描述的是：json 工具类
 */

public class JsonUtil {
    private static final ThreadLocal<ObjectMapper> INCLUDE_NULL_MAPPER = new ThreadLocal<>();
    private static final ThreadLocal<ObjectMapper> NOT_INCLUDE_NULL_MAPPER = new ThreadLocal<>();
    public static final boolean checkFiledFlag = true;
    public static final String filterKey = "filter";
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    private static ObjectMapper getMapper(boolean serializeNull) {
        ThreadLocal<ObjectMapper> tl = serializeNull ? INCLUDE_NULL_MAPPER : NOT_INCLUDE_NULL_MAPPER;
        if (null == tl.get()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            if (!serializeNull) {
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
            }

            tl.set(mapper);
        }

        return tl.get();
    }

    /**
     * 此方法描述的是：根据key取得相应的值
     *
     * @param map 欲取值的map
     * @param key key
     * @return String
     */
    public static String getString(Map<String, Object> map, String key) {
        try {
            return (String) map.get(key);
        } catch (Exception e) {
            return JsonUtil.toString(map.get(key));
        }
    }

    /**
     * 此方法描述的是：取得list
     *
     * @param map 欲取值的map
     * @param key key
     * @return List<Map<String, Object>>
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getList(Map<String, Object> map, String key) {
        try {
            return (List<Map<String, Object>>) map.get(key);
        } catch (Exception e) {
            try {
                return JsonUtil.toList(map.get(key));
            } catch (Exception e2) {
                return null;
            }
        }
    }

    /**
     * 此方法描述的是：取得list
     *
     * @param map 欲取值的map
     * @param key key
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(Map<String, Object> map, String key) {
        try {
            return (Map<String, Object>) map.get(key);
        } catch (Exception e) {
            return JsonUtil.toBean(map.get(key), Map.class);
        }

    }

    /**
     * 此方法描述的是：根据key取值int
     *
     * @param map          欲取值的map
     * @param key          key
     * @param defaultValue 默认值
     * @return int
     */
    public static int getInt(Map<String, Object> map, String key, int defaultValue) {
        try {
            return (Integer) map.get(key);
        } catch (Exception e) {
            try {
                return Integer.parseInt(JsonUtil.toString(map.get(key)));
            } catch (Exception e2) {
                return defaultValue;
            }
        }
    }

    /**
     * 此方法描述的是：根据key取BigDecial
     *
     * @param map          欲取值的map
     * @param key          key
     * @param defaultValue 默认值
     * @return BigDecimal
     */
    public static BigDecimal getBigDecimal(Map<String, Object> map, String key, BigDecimal defaultValue) {
        return BigDecimal.valueOf(getDouble(map, key, defaultValue.doubleValue()));
    }

    /**
     * 此方法描述的是：根据key取BigDecial
     *
     * @param map 欲取值的map
     * @param key key
     * @return BigDecimal
     */
    public static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        return BigDecimal.valueOf(getDouble(map, key));
    }

    /**
     * 此方法描述的是：根据key取值int
     *
     * @param map 欲取值的map
     * @param key key
     * @return int
     */
    public static int getInt(Map<String, Object> map, String key) {
        return JsonUtil.getInt(map, key, 0);
    }

    /**
     * 此方法描述的是：根据key取得boolean值
     *
     * @param map          欲取值的map
     * @param key          key
     * @param defaultValue 默认值
     * @return boolean
     */
    public static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        try {
            return (Boolean) map.get(key);
        } catch (Exception e) {
            try {
                return Boolean.parseBoolean(JsonUtil.toString(map.get(key)));
            } catch (Exception e2) {
                return defaultValue;
            }
        }
    }

    /**
     * 此方法描述的是：根据key取得boolean值,默认为false
     *
     * @param map 欲取值的map
     * @param key key
     * @return boolean
     */
    public static boolean getBoolean(Map<String, Object> map, String key) {
        return getBoolean(map, key, false);
    }

    /**
     * 此方法描述的是：向obj数组中加新元素,
     *
     * @param list list
     * @param obj  增加的元素
     */
    @SuppressWarnings("unchecked")
    public static void add(List<HashMap<String, Object>> list, Object obj) {
        list.add(JsonUtil.toBean(list, HashMap.class));
    }

    /**
     * 此方法描述的是：根据key取得double值
     *
     * @param map          欲取值的map
     * @param key          key
     * @param defaultValue 默认值
     * @return double
     */
    public static double getDouble(Map<String, Object> map, String key, double defaultValue) {
        try {
            return (Double) map.get(key);
        } catch (Exception e) {
            try {
                return Double.parseDouble(JsonUtil.toString(map.get(key)));
            } catch (Exception e2) {
                return defaultValue;
            }
        }

    }

    /**
     * 此方法描述的是：根据key取得short值
     *
     * @param map          欲取值的map
     * @param key          key
     * @param defaultValue 默认值
     * @return short
     */
    public static short getShort(Map<String, Object> map, String key, short defaultValue) {
        try {
            return (Short) map.get(key);
        } catch (Exception e) {
            try {
                return Short.parseShort(JsonUtil.toString(map.get(key)));
            } catch (Exception e2) {
                return defaultValue;
            }
        }

    }

    /**
     * 此方法描述的是：根据key取得double值
     *
     * @param map 欲取值的map
     * @param key key
     * @return double
     */
    public static double getDouble(Map<String, Object> map, String key) {
        return getDouble(map, key, 0D);
    }

    /**
     * 此方法描述的是：根据key取得double值
     *
     * @param map          欲取值的map
     * @param key          key
     * @param defaultValue 默认值
     * @return long
     */
    public static long getLong(Map<String, Object> map, String key, long defaultValue) {
        try {
            return (Long) map.get(key);
        } catch (Exception e) {
            try {
                return Long.parseLong(JsonUtil.toString(map.get(key)));
            } catch (Exception e2) {
                return defaultValue;
            }
        }
    }

    /**
     * 此方法描述的是：根据key取得double值
     *
     * @param map 欲取值的map
     * @param key key
     * @return long
     */
    public static long getLong(Map<String, Object> map, String key) {
        return getLong(map, key, 0L);
    }

    /**
     * 此方法描述的是：将Object转化为Json格式字符串
     *
     * @param obj 欲转换的对象
     * @return String
     */
    public static String toString(Object obj) {
        return toString(obj, true);
    }

    /**
     * 此方法描述的是：将Object转化为Json格式字符串
     *
     * @param includeNull true 包含null的参数，false 不包含null的参数
     * @param obj         欲转换的对象
     * @return String
     */
    public static String toString(Object obj, boolean includeNull) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return getMapper(includeNull).writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            LOG.error("JsonGenerationException error", e);
        } catch (JsonMappingException e) {
            LOG.error("JsonMappingException error", e);
        } catch (IOException e) {
            LOG.error("IOException error", e);
        }
        return null;
    }


    /**
     * 此方法描述的是：将Object转化为Json格式字符串
     *
     * @param obj        欲转换的对象
     * @param dateFormat 日期format
     * @return String
     */
    public static String toString(Object obj, DateFormat dateFormat) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            setDateFormat(dateFormat);
            return getMapper(true).writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            LOG.error("JsonGenerationException error", e);
        } catch (JsonMappingException e) {
            LOG.error("JsonMappingException error", e);
        } catch (IOException e) {
            LOG.error("IOException error", e);
        }
        return null;
    }

    private static void setDateFormat(DateFormat dateFormat) {
        getMapper(true).setDateFormat(dateFormat);
        getMapper(false).setDateFormat(dateFormat);
    }

    /**
     * 此方法描述的是：将传入的对象转换成指定的对象
     *
     * @param <T> 模板类
     * @param cls 与转化的类
     * @param obj 被转换的对象
     * @return T
     */
    public static <T> T toBean(Object obj, Class<T> cls) {
        if (null == obj) {
            return null;
        }
        if (obj.getClass() == cls && !(obj instanceof Map)) {
            return (T) obj;
        }
        try {
            return getMapper(true).readValue(JsonUtil.toString(obj), cls);
        } catch (JsonParseException e) {
            LOG.error("JsonParseException error", e);
        } catch (JsonMappingException e) {
            LOG.error("JsonMappingException error", e);
        } catch (JsonGenerationException e) {
            LOG.error("JsonGenerationException error", e);
        } catch (IOException e) {
            LOG.error("IOException error", e);
        }
        return null;
    }

    /**
     * 此方法描述的是：字符串转换为List<map>
     *
     * @param obj 与转换的对象
     * @return List<Map<String, Object>>
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> toList(Object obj) {
        List<Map<String, Object>> lists = new LinkedList<Map<String, Object>>();
        List<Object> list = JsonUtil.toBean(obj, List.class);
        if (null != list) {
            for (Object object : list) {
                Map<String, Object> map = JsonUtil.toBean(object, HashMap.class);
                if (null != map) {
                    lists.add(map);
                }
            }
        }
        return lists;
    }

    /**
     * 此方法描述的是：字符串转换为List
     *
     * @param <T> 模板类
     * @param cls 与转化的类
     * @param obj 被转换的对象
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object obj, Class<T> cls) {
        List<T> lists = new LinkedList<T>();
        List<Object> list = JsonUtil.toBean(obj, List.class);
        if (null != list) {
            for (Object object : list) {
                if (object.getClass() == cls) {
                    lists.add((T) object);
                } else {
                    T t = JsonUtil.toBean(object, cls);
                    if (null != t) {
                        lists.add(t);
                    }
                }
            }
        }
        return lists;
    }

    /**
     * 使用TypeReference生成list，更加简洁高效
     * 用来替换toList方法
     *
     * @param object 对象
     * @param cls    目标类
     */
    public static <T> List<T> toList2(Object object, Class<T> cls) {
        String content = null;
        if (object instanceof String) {
            content = (String) object;
        } else {
            content = toString(object);
        }

        List<T> result = toObject(content, new TypeReference<List<T>>() {
        });
        if (result == null) {
            return new ArrayList<>();
        }
        return result;
    }

    public static <T> T toObject(String content, TypeReference valueTypeRef) {
        try {
            return getMapper(true).readValue(content, valueTypeRef);
        } catch (IOException e) {
            LOG.error("json parse error, {}", e.getMessage());
        }
        return null;
    }

    /**
     * 根据字符串获取指定key对应的值
     *
     * @param data
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static int getIntFromStringData(String data, String key) {
        int result = 0;
        Map<String, Object> paramMap = JsonUtil.toBean(data, Map.class);
        result = JsonUtil.getInt(paramMap, key);
        return result;
    }

    /**
     * Description: json字符串转map<br>
     *
     * @param json json字符串
     * @return map<br>
     * @author shiming<br>
     * @taskId <br>
     */
    public static <K, V> Map<K, V> toMap(String json) {
        if (json == null) {
            return Collections.emptyMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper = setMapperConfig(mapper);
        try {
            return mapper.readValue(json, new TypeReference<Map<K, V>>() {
            });
        } catch (JsonParseException e) {
            LOG.error("JsonParseException error", e);
        } catch (JsonMappingException e) {
            LOG.error("JsonMappingException error", e);
        } catch (IOException e) {
            LOG.error("IOException error", e);
        }
        return Collections.emptyMap();
    }

    /**
     * Description: setMapperConfig<br>
     *
     * @param mapper ObjectMapper
     * @return ObjectMapper<br>
     * @author shiming<br>
     * @taskId <br>
     */
    @SuppressWarnings("deprecation")
    private static ObjectMapper setMapperConfig(ObjectMapper mapper) {
        Set<String> filterSet = new HashSet<String>(0);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        if (checkFiledFlag) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterKey,
                SimpleBeanPropertyFilter.serializeAllExcept(filterSet));
        mapper.setFilters(filterProvider);

        return mapper;
    }
}
