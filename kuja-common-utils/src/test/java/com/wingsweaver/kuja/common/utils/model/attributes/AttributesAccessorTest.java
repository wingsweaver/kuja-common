package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttributesAccessorTest {
    @Test
    void testAttributes() {
        Map<String, Object> map = MapUtil.from("name", "tom", "age", 10);
        MapAttributes<String> attributes = new MapAttributes<>(map);
        AttributesAccessor<String> accessor = new AttributesAccessor<>(attributes);
        assertEquals(attributes, accessor.getAttributes());
        assertThrows(UnsupportedOperationException.class, accessor::getMutableAttributes);

        assertEquals("tom", accessor.getAttribute("name"));
        assertEquals(10, (Integer) accessor.getAttribute("age"));
        assertNull(accessor.getAttribute("id"));
        assertEquals("jerry", accessor.getAttribute("sister", "jerry"));
        assertEquals("jerry", accessor.getAttribute("sister", key -> "jerry"));

        assertThrows(UnsupportedOperationException.class, () -> accessor.setAttribute("age", 20));
        assertThrows(UnsupportedOperationException.class, () -> accessor.setAttributeIfAbsent("age", 30));
        assertThrows(UnsupportedOperationException.class, () -> accessor.updateAttribute("age", (key, value) -> 40));
        assertThrows(UnsupportedOperationException.class, () -> accessor.removeAttribute("age"));
        assertEquals(10, (Integer) accessor.getAttribute("age"));
    }

    @Test
    void testMutableAttributes() {
        Map<String, Object> map = MapUtil.from("name", "tom", "id", 100);
        MapMutableAttributes<String> attributes = new MapMutableAttributes<>(map);
        AttributesAccessor<String> accessor = new AttributesAccessor<>(attributes);
        assertEquals(attributes, accessor.getAttributes());
        assertEquals(attributes, accessor.getMutableAttributes());

        assertEquals("tom", accessor.getAttribute("name"));
        assertEquals(100, (Integer) accessor.getAttribute("id"));
        assertNull(accessor.getAttribute("age"));
        assertEquals("jerry", accessor.getAttribute("sister", "jerry"));
        assertEquals("jerry", accessor.getAttribute("sister", key -> "jerry"));

        accessor.setAttribute("age", 20);
        assertEquals(20, (Integer) accessor.getAttribute("age"));
        assertEquals(20, accessor.setAttributeIfAbsent("age", 30));
        assertEquals(40, (Integer) accessor.updateAttribute("age", (key, value) -> 40));
        accessor.removeAttribute("age");
        assertNull(accessor.getAttribute("age"));
    }
}