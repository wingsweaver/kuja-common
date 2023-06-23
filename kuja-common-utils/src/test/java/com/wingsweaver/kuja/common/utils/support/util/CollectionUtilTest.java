package com.wingsweaver.kuja.common.utils.support.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CollectionUtilTest {

    @Test
    void listOf() {
        assertEquals(0, CollectionUtil.listOf().size());
        assertEquals(1, CollectionUtil.listOf("").size());
        assertEquals(2, CollectionUtil.listOf("", "").size());
        assertEquals(3, CollectionUtil.listOf("", "", "").size());
    }

    @Test
    void addAll() {
        CollectionUtil.addAll(null, null);
        CollectionUtil.addAll(Collections.emptyList(), null);
        CollectionUtil.addAll(null, new ArrayList<>());
        CollectionUtil.addAll(Collections.emptyList(), new ArrayList<>());
        CollectionUtil.addAll(Arrays.asList("a", "b"), new ArrayList<>());
    }

    @Test
    void size() {
        assertEquals(0, CollectionUtil.size(null));
        assertEquals(0, CollectionUtil.size(Collections.emptyList()));
        assertEquals(1, CollectionUtil.size(Collections.singletonList("")));
    }
}