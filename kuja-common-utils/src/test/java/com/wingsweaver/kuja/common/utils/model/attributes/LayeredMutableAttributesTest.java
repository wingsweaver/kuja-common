package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayeredMutableAttributesTest {
    Map<String, Object> sourceMap = MapUtil.from("name", "tom", "age", 10);
    Map<String, Object> sourceMap2 = MapUtil.from("name", "jerry", "id", 1234);
    Map<String, Object> sourceMap3 = MapUtil.from("name", "kate", "grade", 8);

    @Test
    void test() {
        MapAttributes<String> parent = new MapAttributes<>(sourceMap);
        LayeredMutableAttributes<String> attributes = new LayeredMutableAttributes<>(parent);
        assertNotNull(attributes);
        assertSame(parent, attributes.getParent());

        assertEquals(sourceMap.size(), attributes.getKeys().size());
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));

        attributes.setAttribute("name", "jerry");
        assertEquals("jerry", attributes.getAttribute("name"));
        assertEquals("tom", parent.getAttribute("name"));

        attributes.setAttributeIfAbsent("id", 1234);
        assertEquals(1234, (Integer) attributes.getAttribute("id"));
        assertNull(parent.getAttribute("id"));

        attributes.removeAttribute("age");
        assertTrue(attributes.isAttributeRemoved("age"));
        assertFalse(attributes.hasAttribute("age"));
        assertNull(attributes.getAttribute("age"));
        assertEquals(10, parent.getAttribute("age", 10));

        Map<String, ?> tempMap = attributes.asMap();
        assertNotNull(tempMap);
        assertEquals(2, tempMap.size());
        assertEquals("jerry", tempMap.get("name"));
        assertEquals(1234, tempMap.get("id"));

        attributes.clearAttributes();
        assertTrue(CollectionUtils.isEmpty(attributes.getKeys()));
        assertEquals(sourceMap.size(), parent.getKeys().size());
        assertTrue(CollectionUtils.isEmpty(attributes.asMap()));

        attributes.importMap(sourceMap, true);
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));

        attributes.importMap(sourceMap2, false);
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        assertEquals(1234, (Integer) attributes.getAttribute("id"));

        attributes.importMap(sourceMap3, true);
        assertEquals("kate", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        assertEquals(1234, (Integer) attributes.getAttribute("id"));
        assertEquals(8, (Integer) attributes.getAttribute("grade"));
    }

    @Test
    void testNull() {
        LayeredMutableAttributes<String> attributes = new LayeredMutableAttributes<>(null);
        assertNotNull(attributes);
        assertNull(attributes.getParent());
        assertNotNull(attributes.getParentOrEmpty());

        assertTrue(CollectionUtils.isEmpty(attributes.getKeys()));
        assertFalse(attributes.hasAttribute("name"));
        assertFalse(attributes.isAttributeRemoved("name"));
        assertNull(attributes.getAttribute("name"));
        assertEquals("tom", attributes.getAttribute("name", "tom"));
        assertEquals("jerry", attributes.getAttribute("name", key -> "jerry"));
        assertNull(attributes.getAttribute("name"));

        Map<String, ?> tempMap = attributes.asMap();
        assertNotNull(tempMap);
        assertTrue(tempMap.isEmpty());

        attributes.forEach((key, value) -> {
        });

        attributes.setAttribute("name", "kate");
        assertEquals("kate", attributes.getAttribute("name"));
        attributes.setAttributeIfAbsent("name", "lisa");
        assertEquals("kate", attributes.getAttribute("name"));
        attributes.setAttributeIfAbsent("age", 10);
        assertEquals(10, (Integer) attributes.getAttribute("age"));

        assertEquals(20, (Integer) attributes.updateAttribute("age", (k, v) -> 20));
        assertTrue(attributes.hasAttribute("name"));
        assertTrue(attributes.hasAttribute("age"));

        Map<String, ?> tempMap2 = attributes.asMap();
        assertNotNull(tempMap2);
        assertEquals(2, tempMap2.size());
        assertEquals("kate", tempMap2.get("name"));
        assertEquals(20, tempMap2.get("age"));

        assertFalse(attributes.hasAttribute("id"));
        attributes.removeAttribute("id");
        attributes.removeAttribute("name");
        assertFalse(attributes.hasAttribute("name"));
        assertTrue(attributes.isAttributeRemoved("name"));

        attributes.clearAttributes();
        assertTrue(CollectionUtils.isEmpty(attributes.getKeys()));

        attributes.importMap(sourceMap, true);
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));

        attributes.importMap(sourceMap2, false);
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        assertEquals(1234, (Integer) attributes.getAttribute("id"));

        attributes.importMap(sourceMap3, true);
        assertEquals("kate", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        assertEquals(1234, (Integer) attributes.getAttribute("id"));
        assertEquals(8, (Integer) attributes.getAttribute("grade"));
    }
}