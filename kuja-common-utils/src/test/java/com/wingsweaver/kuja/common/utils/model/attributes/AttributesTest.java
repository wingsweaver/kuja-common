package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttributesTest {

    @Test
    void of() {
        Map<String, Object> sourceMap = MapUtil.from("name", "tom", "age", 10);
        Attributes<String> attributes = Attributes.of(sourceMap);
        assertNotNull(attributes);
        assertEquals(2, attributes.getKeys().size());
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(10, (Integer) attributes.getAttribute("age"));
    }

    @Test
    void of2() {
        Attributes<String> attributes = Attributes.of(null);
        assertNotNull(attributes);
        assertTrue(attributes.getKeys().isEmpty());
    }

    @Test
    void empty() {
        Attributes<String> attributes = Attributes.empty();
        assertNotNull(attributes);
        assertTrue(attributes.getKeys().isEmpty());
    }


    @Test
    void mutate() {
        Map<String, Object> map = MapUtil.from("id", 100, "name", "tom");
        Attributes<String> attributes = Attributes.of(map);
        AttributesBuilder<String> builder = attributes.mutate();
        builder.update("id", 1234);
        builder.update(MapUtil.from("age", 18), true);
        builder.update(m -> m.put("new", true));
        Attributes<String> attributes2 = builder.build();
        assertEquals(1234, (int) attributes2.getAttribute("id"));
        assertEquals("tom", attributes2.getAttribute("name"));
        assertEquals(18, (int) attributes2.getAttribute("age"));
        assertEquals(true, attributes2.getAttribute("new"));
    }
}