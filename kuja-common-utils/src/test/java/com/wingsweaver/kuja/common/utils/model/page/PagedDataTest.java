package com.wingsweaver.kuja.common.utils.model.page;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PagedDataTest {
    @Test
    void test() {
        PagedData<String> pagedData = new PagedData<>();
        pagedData.setPageNumber(1);
        pagedData.setPageSize(5);
        pagedData.setTotalCount(100L);
        assertEquals(1, pagedData.getPageNumber());
        assertEquals(5, pagedData.getPageSize());
        assertEquals(100L, pagedData.getTotalCount());
        assertEquals(0, pagedData.getPageStartIndex());
        assertEquals(5, pagedData.getPageEndIndex());
        assertEquals(20, pagedData.getTotalPageCount());
        assertFalse(pagedData.hasPreviousPage());
        assertTrue(pagedData.hasNextPage());

        pagedData.setPageNumber(7);
        pagedData.setPageSize(9);
        assertEquals(7, pagedData.getPageNumber());
        assertEquals(9, pagedData.getPageSize());
        assertEquals(100L, pagedData.getTotalCount());
        assertEquals(54, pagedData.getPageStartIndex());
        assertEquals(63, pagedData.getPageEndIndex());
        assertEquals(12, pagedData.getTotalPageCount());
        assertTrue(pagedData.hasPreviousPage());
        assertTrue(pagedData.hasNextPage());

        pagedData.setPageNumber(pagedData.getTotalPageCount());
        assertEquals(99, pagedData.getPageStartIndex());
        assertEquals(108, pagedData.getPageEndIndex());
        assertEquals(12, pagedData.getTotalPageCount());
        assertTrue(pagedData.hasPreviousPage());
        assertFalse(pagedData.hasNextPage());

        assertNull(pagedData.getList());
        pagedData.setList(Collections.emptyList());
        assertTrue(pagedData.getList().isEmpty());
    }
}