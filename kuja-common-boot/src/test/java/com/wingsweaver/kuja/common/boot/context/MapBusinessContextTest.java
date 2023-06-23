package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MapBusinessContextTest {
    @Test
    void test() {
        MapBusinessContext context = new MapBusinessContext();
        assertFalse(context.hasAttribute("id"));
        context.setAttribute("id", 100);
        assertEquals(100, (int) context.getAttribute("id"));
    }

    @Test
    void test2() {
        MapBusinessContext context = new MapBusinessContext(10);
        assertFalse(context.hasAttribute("id"));
        context.setAttribute("id", 100);
        assertEquals(100, (int) context.getAttribute("id"));
    }

    @Test
    void test3() {
        Map<String, Object> map = MapUtil.from("id", 100);
        MapBusinessContext context = new MapBusinessContext(map);
        assertEquals(100, (int) context.getAttribute("id"));
    }
}