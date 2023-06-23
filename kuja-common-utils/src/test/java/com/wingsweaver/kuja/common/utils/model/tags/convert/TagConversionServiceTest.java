package com.wingsweaver.kuja.common.utils.model.tags.convert;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TagConversionServiceTest {
    @Test
    void toValue() {
        assertEquals(1L, (Long) TagConversionService.toValue("1", Long.class));
        assertEquals(false, TagConversionService.toValue("false", Boolean.class));
        assertThrows(TagConversionException.class, () -> TagConversionService.toValue("false", Long.class));
        assertThrows(TagConversionException.class, () -> TagConversionService.toValue(null, Dummy.class));
        assertNotNull(TagConversionService.toValue("", Dummy.class));
        assertThrows(TagConversionException.class, () -> TagConversionService.toValue(new Object(), UUID.class));
    }

    @Test
    void saveValueAsText() {
        assertNull(TagConversionService.saveValueAsText(null));
        assertEquals("1", TagConversionService.saveValueAsText(1));
        assertEquals("true", TagConversionService.saveValueAsText(true));
        assertEquals("1,2", TagConversionService.saveValueAsText(new Object[]{1, "2"}));
        assertEquals("dummy-1", TagConversionService.saveValueAsText(new Dummy(1)));
        assertThrows(TagConversionException.class, () -> TagConversionService.saveValueAsText(new Dummy(null)));
        assertNotNull(TagConversionService.saveValueAsText(UUID.randomUUID()));
    }
}