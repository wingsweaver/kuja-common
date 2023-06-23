package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapAttributesTest {
    @Test
    void test() {
        Map<String, Object> map = MapUtil.from("name", "tom", "age", 10);
        MapAttributes<String> attributes = new MapAttributes<>(map);
        assertEquals(map, attributes.getMapOrEmpty());
        assertEquals(map, attributes.getMap());

        String[] keys = attributes.getKeys().stream().sorted().toArray(String[]::new);
        assertArrayEquals(new String[]{"age", "name"}, keys);

        assertTrue(attributes.hasAttribute("name"));
        assertFalse(attributes.hasAttribute("id"));

        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals("tom", attributes.getAttribute("name", "jerry"));
        assertEquals("jerry", attributes.getAttribute("id", "jerry"));
        assertEquals("jerry", attributes.getAttribute("id", key -> "jerry"));
        assertEquals(10, (Integer) attributes.getAttribute("age", key -> 20));

        attributes.forEach((key, value) -> {
        });

        Map<String, Object> map2 = attributes.asMap();
        assertEquals(map.size(), map2.size());
        map.forEach((key, value) -> assertEquals(value, map2.get(key)));

        attributes.setMap(null);
        assertNull(attributes.getMap());
        assertNotNull(attributes.getMapOrEmpty());
        assertTrue(attributes.getMapOrEmpty().isEmpty());
    }
}