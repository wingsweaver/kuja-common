package com.wingsweaver.kuja.common.utils.support.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class JsonCodecUtilTest {
    @Test
    void test() {
        JsonCodec codec = JsonCodecUtil.getJsonCodec();
        assertNotNull(codec);

        FastJson2Codec fastJson2Codec = new FastJson2Codec();
        JsonCodecUtil.setJsonCodec(fastJson2Codec);
        assertSame(fastJson2Codec, JsonCodecUtil.getJsonCodec());

        assertEquals("1234", codec.toJsonString(1234));
    }
}