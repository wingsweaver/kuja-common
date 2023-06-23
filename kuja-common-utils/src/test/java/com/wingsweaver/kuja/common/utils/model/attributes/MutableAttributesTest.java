package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableAttributesTest {

    @Test
    void of() {
        Map<String, Object> sourceMap = MapUtil.from("name", "tom", "age", 10);
        MutableAttributes<String> attributes = MutableAttributes.of(sourceMap);
        assertNotNull(attributes);
        assertEquals(2, attributes.getKeys().size());
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
        attributes.setAttribute("name", "jerry");
        assertEquals("jerry", attributes.getAttribute("name"));
    }

    @Test
    void of2() {
        MutableAttributes<String> attributes = MutableAttributes.of(null);
        assertNotNull(attributes);
        assertTrue(attributes.getKeys().isEmpty());
        attributes.setAttribute("name", "jerry");
        assertEquals("jerry", attributes.getAttribute("name"));
    }

    @Test
    void layered() {
        MutableAttributes<String> attributes = MutableAttributes.layered(null);
        assertNotNull(attributes);
        assertTrue(attributes.getKeys().isEmpty());
    }
}