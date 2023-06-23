package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayeredBusinessContextTest {
    @Test
    void test() {
        LayeredBusinessContext businessContext = new LayeredBusinessContext(null);
        assertTrue(businessContext.asMap().isEmpty());
    }

    @Test
    void test2() {
        Map<String, Object> map = MapUtil.from("id", 10, "name", "tom");
        BusinessContext parent = BusinessContext.of(map);
        LayeredBusinessContext businessContext = new LayeredBusinessContext(parent);
        assertSame(parent, businessContext.getParent());
        assertEquals(2, businessContext.asMap().size());
        assertEquals(10, (int) businessContext.getAttribute("id"));
        assertEquals("tom", businessContext.getAttribute("name"));

        businessContext.setAttribute("id", 20);
        assertEquals(20, (int) businessContext.getAttribute("id"));
        assertEquals(10, (int) parent.getAttribute("id"));
    }
}