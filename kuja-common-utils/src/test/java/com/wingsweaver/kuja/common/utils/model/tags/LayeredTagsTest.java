package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class LayeredTagsTest {
    @Test
    void test() {
        LayeredTags tags = new LayeredTags(null);
        assertTrue(tags.isEmpty());
        assertEquals(0, tags.size());
        assertTrue(tags.asMap().isEmpty());
        assertTrue(tags.getTagKeys().isEmpty());
        assertNull(tags.getBase());
        assertEquals(6789L, tags.getTag(ResourceAttributes.Process.TAG_PID, 6789L));
        assertFalse(tags.hasTag(ResourceAttributes.Process.TAG_PID));

        tags.setTag(ResourceAttributes.Process.TAG_PID, 1234);
        assertEquals(1, tags.size());
        assertEquals(1, tags.asMap().size());
        assertEquals(1, tags.getTagKeys().size());
        assertEquals(1234L, tags.getTag(ResourceAttributes.Process.TAG_PID));
        assertFalse(tags.isEmpty());
        assertTrue(tags.hasTag(ResourceAttributes.Process.TAG_PID));

        tags.setTag(ResourceAttributes.Host.TAG_NAME, "localhost");
        assertEquals("localhost", tags.getTag(ResourceAttributes.Host.TAG_NAME));

        {
            Collection<TagKey<?>> tagKeys = tags.getTagKeys();
            assertEquals(2, tagKeys.size());
        }

        assertEquals(ResourceAttributes.Process.TAG_PID, tags.findTagKey(tagKey -> tagKey.getKey().contains("pid")));
        {
            Collection<TagKey<?>> tagKeys = tags.findTagKeys(tagKey -> tagKey.getKey().contains("name"));
            assertEquals(1, tagKeys.size());
            assertEquals(ResourceAttributes.Host.TAG_NAME, tagKeys.iterator().next());
        }

        Map<TagKey<?>, Object> map = tags.asMap();
        assertEquals(2, map.size());
        assertEquals(1234L, map.get(ResourceAttributes.Process.TAG_PID));
        assertEquals("localhost", map.get(ResourceAttributes.Host.TAG_NAME));

        tags.setTag(ResourceAttributes.Process.TAG_COMMAND_LINE, null);
        assertTrue(tags.hasTag(ResourceAttributes.Process.TAG_COMMAND_LINE));
        assertNull(tags.getTag(ResourceAttributes.Process.TAG_COMMAND_LINE));

        tags.forEach((tagKey, value) -> {
            if (tagKey.getKey().contains("pid")) {
                assertEquals(1234L, value);
            } else if (tagKey.getKey().contains("name")) {
                assertEquals("localhost", value);
            } else if (tagKey.getKey().contains("command")) {
                assertNull(value);
            } else {
                fail();
            }
        });

        tags.removeTag(ResourceAttributes.Process.TAG_PID);
        assertFalse(tags.hasTag(ResourceAttributes.Process.TAG_PID));
        assertNull(tags.getTag(ResourceAttributes.Process.TAG_PID));

        tags.clearTags();
        assertTrue(tags.isEmpty());
    }

    @Test
    void test2() {
        Tags base = Tags.of(MapUtil.from(ResourceAttributes.Process.TAG_PID, 5678L));
        LayeredTags tags = new LayeredTags(base, BufferSizes.TINY);
        assertSame(base, tags.getBase());
        assertFalse(tags.isEmpty());
        {
            Collection<TagKey<?>> tagKeys = tags.getTagKeys();
            assertEquals(1, tagKeys.size());
            assertEquals(ResourceAttributes.Process.TAG_PID, tagKeys.iterator().next());

            assertEquals(1, tags.size());
            Map<TagKey<?>, Object> map = tags.asMap();
            assertEquals(1, map.size());
            assertEquals(5678L, map.get(ResourceAttributes.Process.TAG_PID));
        }
        assertTrue(tags.hasTag(ResourceAttributes.Process.TAG_PID));
        assertEquals(5678L, tags.getTag(ResourceAttributes.Process.TAG_PID, 1234L));
        assertEquals(ResourceAttributes.Process.TAG_PID, tags.findTagKey(tagKey -> tagKey.getKey().contains("pid")));

        tags.setTag(ResourceAttributes.Process.TAG_PID, 1234);
        assertEquals(1234L, tags.getTag(ResourceAttributes.Process.TAG_PID));
        assertEquals(5678L, base.getTag(ResourceAttributes.Process.TAG_PID));
        assertFalse(tags.isEmpty());
        {
            Collection<TagKey<?>> tagKeys = tags.getTagKeys();
            assertEquals(1, tagKeys.size());
            assertEquals(ResourceAttributes.Process.TAG_PID, tagKeys.iterator().next());

            assertEquals(1, tags.size());
            Map<TagKey<?>, Object> map = tags.asMap();
            assertEquals(1, map.size());
            assertEquals(1234L, map.get(ResourceAttributes.Process.TAG_PID));
        }

        tags.setTag(ResourceAttributes.Host.TAG_NAME, "localhost");
        assertEquals("localhost", tags.getTag(ResourceAttributes.Host.TAG_NAME));
        assertFalse(base.hasTag(ResourceAttributes.Host.TAG_NAME));
        {
            List<TagKey<?>> tagKeys = new ArrayList<>(tags.getTagKeys());
            tagKeys.sort(Comparator.comparing(TagKey::getKey));
            assertEquals(2, tagKeys.size());
            assertEquals(ResourceAttributes.Host.TAG_NAME, tagKeys.get(0));
            assertEquals(ResourceAttributes.Process.TAG_PID, tagKeys.get(1));

            assertEquals(2, tags.size());
            Map<TagKey<?>, Object> map = tags.asMap();
            assertEquals(2, map.size());
            assertEquals(1234L, map.get(ResourceAttributes.Process.TAG_PID));
            assertEquals("localhost", map.get(ResourceAttributes.Host.TAG_NAME));
        }

        assertEquals(ResourceAttributes.Process.TAG_PID, tags.findTagKey(tagKey -> tagKey.getKey().contains("pid")));
        {
            Collection<TagKey<?>> tagKeys = tags.findTagKeys(tagKey -> tagKey.getKey().contains("name"));
            assertEquals(1, tagKeys.size());
            assertEquals(ResourceAttributes.Host.TAG_NAME, tagKeys.iterator().next());
        }

        tags.setTag(ResourceAttributes.Process.TAG_COMMAND_LINE, null);
        assertTrue(tags.hasTag(ResourceAttributes.Process.TAG_COMMAND_LINE));
        assertNull(tags.getTag(ResourceAttributes.Process.TAG_COMMAND_LINE));
        assertEquals(1, base.size());

        {
            List<TagKey<?>> tagKeys = new ArrayList<>(tags.findTagKeys(tagKey -> true));
            tagKeys.sort(Comparator.comparing(TagKey::getKey));
            assertEquals(3, tagKeys.size());
            assertEquals(ResourceAttributes.Host.TAG_NAME, tagKeys.get(0));
            assertEquals(ResourceAttributes.Process.TAG_COMMAND_LINE, tagKeys.get(1));
            assertEquals(ResourceAttributes.Process.TAG_PID, tagKeys.get(2));
        }

        tags.forEach((tagKey, value) -> {
            if (tagKey.getKey().contains("pid")) {
                assertEquals(1234L, value);
            } else if (tagKey.getKey().contains("name")) {
                assertEquals("localhost", value);
            } else if (tagKey.getKey().contains("command")) {
                assertNull(value);
            } else {
                fail();
            }
        });

        tags.removeTag(ResourceAttributes.Process.TAG_PID);
        assertFalse(tags.hasTag(ResourceAttributes.Process.TAG_PID));
        assertNull(tags.getTag(ResourceAttributes.Process.TAG_PID));
        assertEquals(5678L, base.getTag(ResourceAttributes.Process.TAG_PID));
        assertNull(tags.findTagKey(tagKey -> tagKey.getKey().contains("pid")));

        tags.clearTags();
        assertTrue(tags.isEmpty());
        assertEquals(1, base.size());
    }
}