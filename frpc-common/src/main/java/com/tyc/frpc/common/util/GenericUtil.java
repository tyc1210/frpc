package com.tyc.frpc.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 将object转为泛型
 *
 * @author tyc
 * @version 1.0
 * @date 2023-05-09 14:04:44
 */
public class GenericUtil {
    /**
     * 将 Object 对象转为指定的泛型类型
     *
     * @param obj  需要转换的对象
     * @param type 泛型类型的 Class 对象
     * @return 转换后的泛型对象
     */
    public static <T> T convertObjectToGenericType(Object obj, Class<T> type) {
        try {
            // 使用反射创建泛型对象
            T result = type.newInstance();
            // 获取泛型类的所有属性
            Field[] fields = type.getDeclaredFields();
            // 遍历属性并设置值
            for (Field field : fields) {
                // 设置属性的可见性
                field.setAccessible(true);
                // 获取属性名
                String fieldName = field.getName();
                // 获取 Object 对象中相应的属性值
                Object fieldValue = getFieldValue(obj, fieldName);
                // 如果属性值不为 null，设置属性值
                if (fieldValue != null) {
                    field.set(result, fieldValue);
                }
            }
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to convert object to generic type", e);
        }
    }

    /**
     * 获取 Object 对象中指定属性的值
     *
     * @param obj        需要获取属性值的对象
     * @param fieldName  属性名
     * @return  属性值
     */
    private static Object getFieldValue(Object obj, String fieldName) {
        try {
            // 获取 Object 对象中指定属性的 getter 方法名
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            // 获取 getter 方法所在的 Class 对象
            Class<?> clazz = obj.getClass();
            // 获取 getter 方法
            Method method = clazz.getMethod(methodName);
            // 调用 getter 方法获取属性值
            return method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}

