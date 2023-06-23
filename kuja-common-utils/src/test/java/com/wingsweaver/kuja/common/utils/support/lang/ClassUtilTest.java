package com.wingsweaver.kuja.common.utils.support.lang;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassUtilTest {
    @Test
    void exists() {
        assertTrue(ClassUtil.exists(ClassUtilTest.class.getName()));
        assertFalse(ClassUtil.exists(ClassUtilTest.class.getName() + "#"));
        assertTrue(ClassUtil.exists(ClassUtilTest.class.getName(), ClassUtil.class.getClassLoader()));
        assertFalse(ClassUtil.exists(ClassUtilTest.class.getName() + "#", ClassUtil.class.getClassLoader()));
    }

    @Test
    void existsAll() {
        assertFalse(ClassUtil.existsAll());
        assertFalse(ClassUtil.existsAll());
        assertTrue(ClassUtil.existsAll(ClassUtilTest.class.getName()));
        assertFalse(ClassUtil.existsAll(ClassUtilTest.class.getName(), ClassUtilTest.class.getName() + "#"));
    }

    @Test
    void existsAny() {
        assertFalse(ClassUtil.existsAny());
        assertFalse(ClassUtil.existsAny());
        assertTrue(ClassUtil.existsAny(ClassUtilTest.class.getName()));
        assertTrue(ClassUtil.existsAny(ClassUtilTest.class.getName(), ClassUtilTest.class.getName() + "#"));
        assertFalse(ClassUtil.existsAny(ClassUtilTest.class.getName() + "#"));
    }

    @Test
    void forName() {
        assertNull(ClassUtil.forName(null));
        assertNull(ClassUtil.forName(""));
        assertSame(ClassUtilTest.class, ClassUtil.forName(ClassUtilTest.class.getName()));
        assertNull(ClassUtil.forName(ClassUtilTest.class.getName() + "#"));
    }

    @Test
    void resolveKey() {
        Type type = new ParameterizedTypeReference<List<String>>() {
        }.getType();
        Class<?> clazz = List.class;
        assertNotEquals(ClassUtil.resolveKey(type), ClassUtil.resolveKey(clazz));
        assertNotEquals(ClassUtil.resolveKey(type, "id"), ClassUtil.resolveKey(clazz, "id"));
        assertNotEquals("id", ClassUtil.resolveKey(type, "id"));
        assertNotEquals("id", ClassUtil.resolveKey(clazz, "id"));
    }
}