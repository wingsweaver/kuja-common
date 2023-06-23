package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapMutableAttributesTest {
    @Test
    void test() {
        Map<String, Object> sourceMap = MapUtil.from("name", "tom", "age", 10);
        Map<String, Object> map = new HashMap<>(sourceMap);
        MapMutableAttributes<String> attributes = new MapMutableAttributes<>(map);

        attributes.setAttribute("name", "jerry");
        assertEquals("jerry", attributes.getAttribute("name"));

        assertEquals(20, (Integer) attributes.updateAttribute("age", (key, value) -> 20));
        assertEquals("jerry", attributes.setAttributeIfAbsent("name", "elsa"));
        assertNull(attributes.setAttributeIfAbsent("brother", "davy"));

        attributes.removeAttribute("age");
        assertFalse(attributes.hasAttribute("age"));
        assertNull(attributes.getAttribute("age"));

        attributes.clearAttributes();
        assertEquals(map, attributes.getMap());
        assertTrue(attributes.getMap().isEmpty());

        attributes.setMap(null);
        assertNull(attributes.getMap());
        Map<String, Object> tempMap = attributes.getMapOrEmpty();
        assertNotNull(tempMap);
        Map<String, Object> tempMap2 = attributes.getMap(true);
        assertNotNull(tempMap2);
        assertNotSame(tempMap, tempMap2);
        assertNotSame(map, tempMap2);
        assertSame(tempMap2, attributes.getMap(true));

        attributes.importMap(sourceMap, true);
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));

        Map<String, Object> sourceMap2 = MapUtil.from("name", "jerry", "id", 1234);
        attributes.importMap(sourceMap2, false);
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        assertEquals(1234, (Integer) attributes.getAttribute("id"));

        Map<String, Object> sourceMap3 = MapUtil.from("name", "kate", "grade", 8);
        attributes.importMap(sourceMap3, true);
        assertEquals("kate", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        assertEquals(1234, (Integer) attributes.getAttribute("id"));
        assertEquals(8, (Integer) attributes.getAttribute("grade"));
    }
}