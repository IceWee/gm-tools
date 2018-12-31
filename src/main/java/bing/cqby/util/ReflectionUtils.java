package bing.cqby.util;

import java.lang.reflect.Field;

/**
 * 反射工具
 *
 * @author: bing
 * @date: 2018-12-31
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        super();
    }

    /**
     * 设置对象属性值
     *
     * @param clazz
     * @param bean
     * @param propertyName
     * @param value
     * @param <T>
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <T> void setPropertyValue(Class<T> clazz, T bean, String propertyName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(propertyName);
        field.setAccessible(true);
        field.set(bean, value);
    }

}
