package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultTagsTest {
    @Test
    void test() {
        DefaultTags tags = new DefaultTags();
        assertTrue(tags.isEmpty());
        assertEquals(0, tags.size());
        assertEquals(0, tags.getTagKeys().size());
        assertTrue(tags.getTagKeys().isEmpty());

        tags.setTag(ResourceAttributes.Host.TAG_NAME, "localhost");
        assertTrue(tags.hasTag(ResourceAttributes.Host.TAG_NAME));
        assertEquals("localhost", tags.getTag(ResourceAttributes.Host.TAG_NAME));
        assertSame(ResourceAttributes.Host.TAG_NAME, tags.findTagKey(key ->
                ResourceAttributes.Host.KEY_NAME.equals(key.getKey())));

        tags.setTag(ResourceAttributes.Browser.TAG_BRANDS, "lenovo,hp,dell");
        assertTrue(tags.hasTag(ResourceAttributes.Browser.TAG_BRANDS));
        assertIterableEquals(CollectionUtil.listOf("lenovo", "hp", "dell"), tags.getTag(ResourceAttributes.Browser.TAG_BRANDS));
        Collection<TagKey<?>> tagKeys = tags.getTagKeys();
        List<TagKey<?>> sortedTagKeys = new ArrayList<>(tagKeys);
        sortedTagKeys.sort(Comparator.comparing(TagKey::getKey));
        assertIterableEquals(CollectionUtil.listOf(ResourceAttributes.Browser.TAG_BRANDS, ResourceAttributes.Host.TAG_NAME), sortedTagKeys);

        Collection<TagKey<?>> tagKeysCollection = tags.findTagKeys(key -> key.getKey().contains("brand"));
        assertEquals(1, tagKeysCollection.size());
        assertEquals(ResourceAttributes.Browser.TAG_BRANDS, tagKeysCollection.iterator().next());

        tags.forEach((key, value) -> {
            assertTrue(key.getKey().contains("brand") || key.getKey().contains("name"));
            assertTrue(value instanceof String || value instanceof Collection);
        });

        tags.removeTag(ResourceAttributes.Process.TAG_PID);
        tags.removeTag(ResourceAttributes.Browser.TAG_BRANDS);
        assertFalse(tags.hasTag(ResourceAttributes.Process.TAG_PID));
        assertFalse(tags.hasTag(ResourceAttributes.Browser.TAG_BRANDS));
        assertNull(tags.getTag(ResourceAttributes.Process.TAG_PID));
        assertNull(tags.getTag(ResourceAttributes.Browser.TAG_BRANDS));

        TagKey.StringKey tagName = TagKeys.ofString(ResourceAttributes.Host.KEY_NAME);
        assertTrue(tags.hasTag(tagName));
        assertEquals("localhost", tags.getTag(tagName));

        TagKey.LongKey tagName2 = TagKeys.ofLong(ResourceAttributes.Host.KEY_NAME);
        assertFalse(tags.hasTag(tagName2));

        tags.clearTags();
        assertTrue(tags.isEmpty());
        assertEquals(0, tags.size());
    }

    @Test
    void test2() {
        DefaultTags tags = new DefaultTags(BufferSizes.TINY);
        assertNotNull(tags.asMap());
        assertEquals("unknown", tags.getTag(ResourceAttributes.Host.TAG_NAME, "unknown"));

        DefaultTags tags2 = new DefaultTags(MapUtil.from(ResourceAttributes.Host.TAG_NAME, "localhost",
                ResourceAttributes.Process.TAG_PID, 1234));
        assertEquals(2, tags2.size());
        assertEquals(2, tags2.getTagKeys().size());
        assertTrue(tags2.hasTag(ResourceAttributes.Host.TAG_NAME));
        assertTrue(tags2.hasTag(ResourceAttributes.Process.TAG_PID));
        assertEquals("localhost", tags2.getTag(ResourceAttributes.Host.TAG_NAME));
        assertEquals(1234L, tags2.getTag(ResourceAttributes.Process.TAG_PID));

        DefaultTags tags3 = new DefaultTags(tags2);
        assertEquals(2, tags3.size());
        assertEquals(2, tags3.getTagKeys().size());
        assertTrue(tags3.hasTag(ResourceAttributes.Host.TAG_NAME));
        assertTrue(tags3.hasTag(ResourceAttributes.Process.TAG_PID));
        assertEquals("localhost", tags3.getTag(ResourceAttributes.Host.TAG_NAME));
        assertEquals(1234L, tags3.getTag(ResourceAttributes.Process.TAG_PID));

        DefaultTags tags4 = new DefaultTags((Tags) null);
        assertTrue(tags4.isEmpty());
        assertEquals("unknown", tags4.getTag(ResourceAttributes.Host.TAG_NAME, "unknown"));

        DefaultTags tags5 = new DefaultTags((Map<TagKey<?>, Object>) null);
        assertTrue(tags5.isEmpty());
        assertEquals("unknown", tags5.getTag(ResourceAttributes.Host.TAG_NAME, "unknown"));
    }
}