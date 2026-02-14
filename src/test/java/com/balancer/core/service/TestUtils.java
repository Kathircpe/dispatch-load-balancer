package com.balancer.core.service;

import java.lang.reflect.Field;

public class TestUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }
}
