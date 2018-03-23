package cn.tonghao.remex.common.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 依赖于uem接口对象类型基本属性相互转换
 *
 * @author
 * @version $Id: ConvertUtil.java, v 0.1 2013-4-9 下午02:30:04  Exp $
 */
public class ConvertUtil extends BeanUtilsBean {

    /**
     * boolean类型的名称首2位前缀
     */
    private static String IS_STR = "is";

    /**
     * MapToBean
     * <p>
     * <p>数据类型bean类型转换为Map</p>
     * <p>boolean类型根据接口协议，方法中以is</p>
     *
     * @param <T>
     * @param destClass
     * @param origMap
     * @param keyMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Object map2Bean(Class<T> destClass, Map<String, String> origMap,
                                      Map<String, String> keyMap) {
        try {
            Object destObj = destClass.newInstance();
            PropertyUtilsBean proUtil = BeanUtilsBean.getInstance().getPropertyUtils();
            PropertyDescriptor descriptors[] = proUtil.getPropertyDescriptors(destObj);
            if (descriptors != null) {
                for (int i = 0; i < descriptors.length; i++) {
                    String name = descriptors[i].getName();
                    if ("class".equals(name)) {
                        continue;
                    }
                    Object value = null;
                    String temp = "";
                    if (descriptors[i].getPropertyType() == String.class) {
                        value = origMap.get(keyMap.get(name));
                    } else if (descriptors[i].getPropertyType() == boolean.class) {
                        name = IS_STR + StringUtils.capitalize(name);
                        temp = (String) origMap.get(keyMap.get(name));
                        if (StringUtils.equals("true", temp)) {
                            value = true;
                        } else {
                            value = false;
                        }
                    } else if (descriptors[i].getPropertyType().isEnum()) {
                        temp = origMap.get(keyMap.get(name));
                        T[] enumConstants = (T[]) descriptors[i].getPropertyType()
                                .getEnumConstants();
                        for (T e : enumConstants) {
                            if (((Enum<?>) e).name().equals(temp)) {
                                value = e;
                            }
                        }

                    } else if (descriptors[i].getPropertyType() == Integer.class) {
                        temp = origMap.get(keyMap.get(name));
                        value = Integer.parseInt(temp);
                    } else if (descriptors[i].getPropertyType() == Double.class) {
                        temp = origMap.get(keyMap.get(name));
                        value = Double.parseDouble(temp);
                    } else if (descriptors[i].getPropertyType() == Float.class) {
                        temp = origMap.get(keyMap.get(name));
                        value = Float.parseFloat(temp);
                    } else {
                        continue;
                    }
                    if (proUtil.isWriteable(destObj, name)) {
                        BeanUtilsBean.getInstance().setProperty(destObj, name, value);
                    }
                }
            }
            return destObj;
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        }
        return null;
    }

    /**
     * beanToMap<String,String>
     * <p>
     * <p>将同名属性的bean转换成Map类型</p>
     * <p>主要依赖字典</p>
     *
     * @param origObject
     * @return
     */
    public static <T> Map<String, String> bean2Map(Object origObject) {
        Map<String, String> destMap = new HashMap<String, String>();
        try {
            PropertyUtilsBean proUtil = BeanUtilsBean.getInstance().getPropertyUtils();
            PropertyDescriptor descriptors[] = proUtil.getPropertyDescriptors(origObject);
            if (descriptors != null) {
                for (int i = 0; i < descriptors.length; i++) {
                    String name = descriptors[i].getName();
                    if ("class".equals(name)) {
                        continue;
                    }
                    if (descriptors[i].getPropertyType() == boolean.class) {
                        name = IS_STR + StringUtils.capitalize(name);
                    }
                    if (proUtil.isReadable(origObject, name)) {
                        Object temp = proUtil.getSimpleProperty(origObject, name);
                        String value = "";
                        if (temp instanceof Enum) {
                            value = ((Enum) temp).getDeclaringClass().getName();
                        } else if (temp instanceof String | temp instanceof Boolean
                                | temp instanceof Double | temp instanceof Float | temp instanceof BigDecimal) {
                            value = temp.toString();
                        } else if (temp instanceof Date) {
                            value = DateTimeUtil.getFormatDateTime_PAY((Date) temp);
                        } else if (temp instanceof Integer) {
                            value = String.valueOf(temp);
                        }
                        destMap.put(name, value);
                    }
                }
            }
            return destMap;
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }


}