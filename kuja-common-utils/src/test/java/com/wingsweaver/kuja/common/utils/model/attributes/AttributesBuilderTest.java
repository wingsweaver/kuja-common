package com.wingsweaver.kuja.common.utils.model.attributes;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributesBuilderTest {
    @Test
    void test() {
        Map<String, Object> map = MapUtil.from("id", 100, "name", "tom");
        AttributesBuilder<String> builder = new AttributesBuilder<>(map);
        builder.update("id", 1234);
        builder.update(MapUtil.from("age", 18), true);
        builder.update(m -> m.put("new", true));
        Attributes<String> attributes = builder.build();
        assertEquals(1234, (int) attributes.getAttribute("id"));
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(18, (int) attributes.getAttribute("age"));
        assertEquals(true, attributes.getAttribute("new"));
    }

    @Test
    void test2() {
        Map<String, Object> map = MapUtil.from("id", 100, "name", "tom");
        AttributesBuilder<String> builder = new AttributesBuilder<>(Attributes.of(map));
        builder.update("id", 1234);
        builder.update(MapUtil.from("age", 18), true);
        builder.update(m -> m.put("new", true));
        Attributes<String> attributes = builder.build();
        assertEquals(1234, (int) attributes.getAttribute("id"));
        assertEquals("tom", attributes.getAttribute("name"));
        assertEquals(18, (int) attributes.getAttribute("age"));
        assertEquals(true, attributes.getAttribute("new"));
    }
}