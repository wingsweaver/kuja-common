package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessContextTest {

    @Test
    void create() {
        BusinessContext businessContext = BusinessContext.create();
        assertNotNull(businessContext);
        assertTrue(businessContext.asMap().isEmpty());
    }

    @Test
    void of() {
        Map<String, Object> map = MapUtil.from("id", 10, "name", "tom");
        BusinessContext businessContext = BusinessContext.of(map);
        assertNotNull(businessContext);
        assertEquals(2, businessContext.asMap().size());
        assertEquals(10, (int) businessContext.getAttribute("id"));
        assertEquals("tom", businessContext.getAttribute("name"));
    }

    @Test
    void testClone() {
        Map<String, Object> map = MapUtil.from("id", 10, "name", "tom");
        BusinessContext source = BusinessContext.of(map);

        BusinessContext businessContext = BusinessContext.clone(source);
        assertNotNull(businessContext);
        assertEquals(2, businessContext.asMap().size());
        assertEquals(10, (int) businessContext.getAttribute("id"));
        assertEquals("tom", businessContext.getAttribute("name"));

        businessContext.setAttribute("id", 1234);
        assertEquals(1234, (int) businessContext.getAttribute("id"));
        assertEquals(10, (int) source.getAttribute("id"));
    }

    @Test
    void testClone2() {
        BusinessContext businessContext = BusinessContext.clone(null);
        assertNotNull(businessContext);
        assertTrue(businessContext.asMap().isEmpty());
    }

    @Test
    void layered() {
        Map<String, Object> map = MapUtil.from("id", 10, "name", "tom");
        BusinessContext source = BusinessContext.of(map);

        BusinessContext businessContext = BusinessContext.layered(source);
        assertNotNull(businessContext);
        assertEquals(2, businessContext.asMap().size());
        assertEquals(10, (int) businessContext.getAttribute("id"));
        assertEquals("tom", businessContext.getAttribute("name"));

        businessContext.setAttribute("id", 1234);
        assertEquals(1234, (int) businessContext.getAttribute("id"));
        assertEquals(10, (int) source.getAttribute("id"));
    }
}