package cn.tonghao.remex.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by howetong on 2017/12/27.
 */
public class ReflectUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ReflectUtils.class);

    public ReflectUtils() {
    }

    public static Object getValueByFieldName(Object obj, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if(field != null) {
            if(field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }

        return value;
    }

    public static Field getFieldByFieldName(Object obj, String fieldName) {
        Class superClass = obj.getClass();

        while(superClass != Object.class) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        LOG.warn("no such field {} in class {}", fieldName, obj.getClass());
        return null;
    }

    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList();

        for(Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            Field[] var4 = fields;
            int var5 = fields.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Field field = var4[var6];
                int modifiers = field.getModifiers();
                if(!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                    allFields.add(field);
                }
            }
        }

        return allFields;
    }

    public static void setValueByFieldName(Object obj, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if(field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }

    }

    public static void setFieldValue(Object target, Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
        if(field.isAccessible()) {
            field.set(target, value);
        } else {
            field.setAccessible(true);
            field.set(target, value);
            field.setAccessible(false);
        }

    }

    public static void invokeSetterMethod(Object target, String fieldName, Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if(value != null) {
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method method = target.getClass().getMethod(methodName, new Class[]{value.getClass()});
            method.invoke(target, new Object[]{value});
        }
    }

    public static Object invokeGetterMethod(Object target, String fieldName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = target.getClass().getMethod(methodName, new Class[0]);
        if(method == null) {
            methodName = methodName.replace("get", "is");
            method = target.getClass().getMethod(methodName, new Class[0]);
        }

        return method.invoke(target, new Object[0]);
    }
}