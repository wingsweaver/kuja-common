package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ParameterizedTypeImpl;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultTagValueListConverterTest {

    @Test
    void convertTo() {
        DefaultTagValueListConverter converter = new DefaultTagValueListConverter();
        assertEquals(Tuple2.of(false, null), converter.convertTo(null, Date.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(null, HashMap.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(null, Set.class));
        assertEquals(Tuple2.of(false, null), converter.convertTo(null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }.getType()));

        ParameterizedType type = new ParameterizedTypeImpl(List.class, new Class[]{Long.class});
        assertEquals(Tuple2.of(true, null), converter.convertTo(null, type));
        {
            Tuple2<Boolean, Object> result = converter.convertTo("1,2", type);
            assertTrue(result.getT1());
            assertIterableEquals(CollectionUtil.listOf(1L, 2L), (Collection<?>) result.getT2());
        }
        {
            Tuple2<Boolean, Object> result = converter.convertTo("", type);
            assertTrue(result.getT1());
            assertTrue(((Collection<?>) result.getT2()).isEmpty());
        }
        {
            Tuple2<Boolean, Object> result = converter.convertTo(new Object[]{"1", 2}, type);
            assertTrue(result.getT1());
            assertIterableEquals(CollectionUtil.listOf(1L, 2L), (Collection<?>) result.getT2());
        }
        {
            Tuple2<Boolean, Object> result = converter.convertTo(new Object[0], type);
            assertTrue(result.getT1());
            assertTrue(((Collection<?>) result.getT2()).isEmpty());
        }
        {
            Tuple2<Boolean, Object> result = converter.convertTo(CollectionUtil.listOf(1, "2"), type);
            assertTrue(result.getT1());
            assertIterableEquals(CollectionUtil.listOf(1L, 2L), (Collection<?>) result.getT2());
        }

        {
            Tuple2<Boolean, Object> result = converter.convertTo(Collections.emptyList(), type);
            assertTrue(result.getT1());
            assertTrue(((Collection<?>) result.getT2()).isEmpty());
        }
    }
}