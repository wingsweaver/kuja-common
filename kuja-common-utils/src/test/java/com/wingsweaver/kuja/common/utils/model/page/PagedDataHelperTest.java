package com.wingsweaver.kuja.common.utils.model.page;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PagedDataHelperTest {
    @Test
    void test_totalCount_collection() {
        Collection<Integer> collection = makeList(100, 200);
        Queue<Integer> queue = new ArrayDeque<>(collection);

        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(10, 1, 5, queue);
            assertEquals(10, pagedData.getPageSize());
            assertEquals(1, pagedData.getPageNumber());
            assertEquals(5, pagedData.getTotalCount());
            assertEquals(1, pagedData.getTotalPageCount());
            assertEquals(0, pagedData.getPageStartIndex());
            assertEquals(10, pagedData.getPageEndIndex());
            assertEquals(5, pagedData.getPageLastRecordIndex());
            assertFalse(pagedData.hasPreviousPage());
            assertFalse(pagedData.hasNextPage());
            assertIterableEquals(makeList(100, 105), pagedData.getList());
        }
    }

    @Test
    void test_collection_offset() {
        Collection<Integer> collection = makeList(100, 200);

        // 首页
        {
            assertThrows(IllegalArgumentException.class, () -> PagedDataHelper.makePagedData(10, 1, 100, collection, 3));
        }
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(10, 2, collection, 5);
            assertEquals(10, pagedData.getPageSize());
            assertEquals(2, pagedData.getPageNumber());
            assertEquals(105, pagedData.getTotalCount());
            assertEquals(11, pagedData.getTotalPageCount());
            assertEquals(10, pagedData.getPageStartIndex());
            assertEquals(20, pagedData.getPageEndIndex());
            assertEquals(20, pagedData.getPageLastRecordIndex());
            assertTrue(pagedData.hasPreviousPage());
            assertTrue(pagedData.hasNextPage());
            assertIterableEquals(makeList(105, 115), pagedData.getList());
        }
    }

    @Test
    void test_collection() {
        Collection<Integer> collection = makeList(100, 200);

        // 首页
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(10, 1, null);
            assertEquals(10, pagedData.getPageSize());
            assertEquals(1, pagedData.getPageNumber());
            assertEquals(0, pagedData.getTotalCount());
            assertEquals(0, pagedData.getTotalPageCount());
            assertEquals(0, pagedData.getPageStartIndex());
            assertEquals(10, pagedData.getPageEndIndex());
            assertFalse(pagedData.hasPreviousPage());
            assertFalse(pagedData.hasNextPage());
            assertTrue(pagedData.getList().isEmpty());
        }
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(10, 1, collection);
            assertEquals(10, pagedData.getPageSize());
            assertEquals(1, pagedData.getPageNumber());
            assertEquals(collection.size(), pagedData.getTotalCount());
            assertEquals(10, pagedData.getTotalPageCount());
            assertEquals(0, pagedData.getPageStartIndex());
            assertEquals(10, pagedData.getPageEndIndex());
            assertFalse(pagedData.hasPreviousPage());
            assertTrue(pagedData.hasNextPage());
            assertIterableEquals(makeList(100, 110), pagedData.getList());
        }
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(100, 1, collection);
            assertEquals(100, pagedData.getPageSize());
            assertEquals(1, pagedData.getPageNumber());
            assertEquals(collection.size(), pagedData.getTotalCount());
            assertEquals(1, pagedData.getTotalPageCount());
            assertEquals(0, pagedData.getPageStartIndex());
            assertEquals(100, pagedData.getPageEndIndex());
            assertEquals(100, pagedData.getPageLastRecordIndex());
            assertFalse(pagedData.hasPreviousPage());
            assertFalse(pagedData.hasNextPage());
            assertIterableEquals(collection, pagedData.getList());
        }
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(200, 1, collection);
            assertEquals(200, pagedData.getPageSize());
            assertEquals(1, pagedData.getPageNumber());
            assertEquals(collection.size(), pagedData.getTotalCount());
            assertEquals(1, pagedData.getTotalPageCount());
            assertEquals(0, pagedData.getPageStartIndex());
            assertEquals(200, pagedData.getPageEndIndex());
            assertEquals(100, pagedData.getPageLastRecordIndex());
            assertFalse(pagedData.hasPreviousPage());
            assertFalse(pagedData.hasNextPage());
            assertIterableEquals(collection, pagedData.getList());
        }

        // 中间页
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(15, 3, collection);
            assertEquals(15, pagedData.getPageSize());
            assertEquals(3, pagedData.getPageNumber());
            assertEquals(collection.size(), pagedData.getTotalCount());
            assertEquals(7, pagedData.getTotalPageCount());
            assertEquals(30, pagedData.getPageStartIndex());
            assertEquals(45, pagedData.getPageEndIndex());
            assertEquals(45, pagedData.getPageLastRecordIndex());
            assertTrue(pagedData.hasPreviousPage());
            assertTrue(pagedData.hasNextPage());
            assertIterableEquals(makeList(130, 145), pagedData.getList());
        }

        // 尾页
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(5, 20, collection);
            assertEquals(5, pagedData.getPageSize());
            assertEquals(20, pagedData.getPageNumber());
            assertEquals(collection.size(), pagedData.getTotalCount());
            assertEquals(20, pagedData.getTotalPageCount());
            assertEquals(95, pagedData.getPageStartIndex());
            assertEquals(100, pagedData.getPageEndIndex());
            assertEquals(100, pagedData.getPageLastRecordIndex());
            assertTrue(pagedData.hasPreviousPage());
            assertFalse(pagedData.hasNextPage());
            assertIterableEquals(makeList(195, 200), pagedData.getList());
        }
        {
            PagedData<Integer> pagedData = PagedDataHelper.makePagedData(16, 7, collection);
            assertEquals(16, pagedData.getPageSize());
            assertEquals(7, pagedData.getPageNumber());
            assertEquals(collection.size(), pagedData.getTotalCount());
            assertEquals(7, pagedData.getTotalPageCount());
            assertEquals(96, pagedData.getPageStartIndex());
            assertEquals(112, pagedData.getPageEndIndex());
            assertEquals(100, pagedData.getPageLastRecordIndex());
            assertTrue(pagedData.hasPreviousPage());
            assertFalse(pagedData.hasNextPage());
            assertIterableEquals(makeList(196, 200), pagedData.getList());
        }

        // 超过范围的页
        {
            assertThrows(IllegalArgumentException.class, () -> PagedDataHelper.makePagedData(16, 8, collection));
        }
    }

    List<Integer> makeList(int startInclusive, int endExclusive) {
        int count = endExclusive - startInclusive;
        List<Integer> list = new ArrayList<>(count);
        for (int i = startInclusive; i < endExclusive; i++) {
            list.add(i);
        }
        return list;
    }
}