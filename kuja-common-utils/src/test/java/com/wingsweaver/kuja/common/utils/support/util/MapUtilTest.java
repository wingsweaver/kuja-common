package com.wingsweaver.kuja.common.utils.support.util;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapUtilTest {

    @Test
    void hashInitCapacity() {
        assertEquals(16, MapUtil.hashInitCapacity(12));
        assertEquals(12, MapUtil.hashInitCapacity(12, MapUtil.FULL_LOAD_FACTOR));
    }

    @Test
    void put() {
        Map<String, Object> map = MapUtil.from("id", 1234, "name", "tom");
        MapUtil.put(null, "id", 100);
        MapUtil.put(map, null, 100);
        MapUtil.put(map, "id", null);
        assertNull(map.get("id"));
        MapUtil.put(map, "name", "jerry");
        assertEquals("jerry", map.get("name"));
    }

    @Test
    void putIfAbsent() {
        Map<String, Object> map = MapUtil.from("id", 1234, "name", "tom");
        MapUtil.putIfAbsent(null, "id", 100);
        MapUtil.putIfAbsent(map, null, 100);
        MapUtil.putIfAbsent(map, "id", null);
        assertEquals(1234, map.get("id"));
        MapUtil.putIfAbsent(map, "name", "jerry");
        assertEquals("tom", map.get("name"));
        MapUtil.putIfAbsent(map, "age", 10);
        assertEquals(10, map.get("age"));
    }

    @Test
    void copy() {
        MapUtil.copy(null, null, true);
        MapUtil.copy(Collections.emptyMap(), new HashMap<>(), true);

        Map<String, Object> source = MapUtil.from("id", 100, "name", "tom");
        Map<String, Object> target = MapUtil.from("id", 200, "age", 10);
        MapUtil.copy(source, target, false);
        assertEquals(3, target.size());
        assertEquals(200, target.get("id"));
        assertEquals(10, target.get("age"));
        assertEquals("tom", target.get("name"));
        MapUtil.copy(source, target, true);
        assertEquals(100, target.get("id"));

    }

    @Test
    void from() {
        assertThrows(IllegalArgumentException.class, () -> MapUtil.from("id", 100, "name"));

        {
            Map<Object, Object> map = MapUtil.from("id", 1234, "name", "tom");
            assertEquals(2, map.size());
            assertEquals(1234, map.get("id"));
            assertEquals("tom", map.get("name"));
        }
    }

    @Test
    void fromArray() {
        assertThrows(IllegalArgumentException.class, () -> MapUtil.fromArray(null, null));
        assertThrows(IllegalArgumentException.class, () -> MapUtil.fromArray(new Object[0], null));
        assertThrows(IllegalArgumentException.class, () -> MapUtil.fromArray(null, new Object[0]));
        assertThrows(IllegalArgumentException.class, () -> MapUtil.fromArray(new Object[0], new Object[1]));

        assertEquals(0, MapUtil.fromArray(new Object[0], new Object[0]).size());
        {
            Map<Object, Object> map = MapUtil.fromArray(new Object[]{"id"}, new Object[]{1234});
            assertEquals(1, map.size());
            assertEquals(1234, map.get("id"));
        }
    }
}