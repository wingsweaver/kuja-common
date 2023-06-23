package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.codec.binary.BinaryCodec;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultTagValueWriterTest {

    @Test
    void saveAsText() {
        DefaultTagValueWriter writer = new DefaultTagValueWriter();
        assertEquals(Tuple2.of(true, null), writer.saveAsText(null));
        assertEquals(Tuple2.of(true, "1"), writer.saveAsText(1));

        assertEquals(Tuple2.of(true, ""), writer.saveAsText(new Object[]{}));
        assertEquals(Tuple2.of(true, "1"), writer.saveAsText(new Object[]{1}));
        assertEquals(Tuple2.of(true, "1,2"), writer.saveAsText(new Object[]{1, 2}));

        assertEquals(Tuple2.of(true, ""), writer.saveAsText(Collections.emptyList()));
        assertEquals(Tuple2.of(true, "1"), writer.saveAsText(CollectionUtil.listOf("1")));
        assertEquals(Tuple2.of(true, "1,2"), writer.saveAsText(CollectionUtil.listOf("1", 2)));

        {
            byte[] bytes = "Hello, world".getBytes();
            String text = BinaryCodec.encodeAsNamed(bytes);
            assertEquals(Tuple2.of(true, text), writer.saveAsText(bytes));
        }
    }
}