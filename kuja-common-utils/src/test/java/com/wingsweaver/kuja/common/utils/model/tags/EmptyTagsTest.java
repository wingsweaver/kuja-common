package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptyTagsTest {
    @Test
    void test() {
        EmptyTags tags = EmptyTags.INSTANCE;

        assertTrue(tags.isEmpty());
        assertEquals(0, tags.size());
        assertEquals(0, tags.getTagKeys().size());
        assertTrue(tags.getTagKeys().isEmpty());
        assertNull(tags.findTagKey(key -> true));
        assertTrue(tags.findTagKeys(key -> true).isEmpty());

        assertNull(tags.getTag(ResourceAttributes.Host.TAG_NAME));
        assertEquals("localhost", tags.getTag(ResourceAttributes.Host.TAG_NAME, "localhost"));

        tags.setTag(ResourceAttributes.Host.TAG_NAME, "localhost");
        assertFalse(tags.hasTag(ResourceAttributes.Host.TAG_NAME));
        assertTrue(tags.asMap().isEmpty());

        tags.forEach((key, value) -> {
        });

        tags.removeTag(ResourceAttributes.Host.TAG_NAME);
        tags.clearTags();
    }
}