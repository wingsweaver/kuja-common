package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagsTest {
    TagKey.StringKey key1 = TagKeys.ofString("key1");
    TagKey.LongKey key2 = TagKeys.ofLong("key2");

    @Test
    void testEmpty() {
        Tags tags = Tags.EMPTY;
        assertTrue(tags.isEmpty());
        tags.setTag(key1, "value1");
        assertEquals(0, tags.size());
    }

    @Test
    void ofCapacity() {
        Tags tags = Tags.of(10);
        assertEquals(0, tags.size());
        tags.setTag(key1, "value1");
    }

    @Test
    void ofMap() {
        Tags tags = Tags.of(MapUtil.from(key1, "value1",
                key2, 2));
        assertEquals(2, tags.size());
        assertEquals("value1", tags.getTag(key1));
        assertEquals(2L, tags.getTag(key2));
    }

    @Test
    void testClone() {
        Tags tags = Tags.of(MapUtil.from(key1, "value1",
                key2, 2));
        Tags tags2 = Tags.clone(tags);
        assertEquals(2, tags2.size());
        assertEquals("value1", tags2.getTag(key1));
        assertEquals(2L, tags2.getTag(key2));
    }

    @Test
    void layered() {
        Tags tags = Tags.of(MapUtil.from(key1, "value1",
                key2, 2));

        Tags tags2 = Tags.layered(tags);
        assertEquals(2, tags2.size());
        assertEquals("value1", tags2.getTag(key1));
        assertEquals(2L, tags2.getTag(key2));

        tags2.setTag(key1, "value2");
        assertEquals("value2", tags2.getTag(key1));
        assertEquals("value1", tags.getTag(key1));

        tags2.removeTag(key2);
        assertEquals(1, tags2.size());
        assertNull(tags2.getTag(key2));
        assertEquals(2L, tags.getTag(key2));
    }
}