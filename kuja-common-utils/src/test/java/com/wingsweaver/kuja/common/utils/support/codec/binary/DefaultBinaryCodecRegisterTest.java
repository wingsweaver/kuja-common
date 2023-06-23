package com.wingsweaver.kuja.common.utils.support.codec.binary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultBinaryCodecRegisterTest {
    @Test
    void testHex() {
        BinaryCodecProvider provider = new DefaultBinaryCodecRegister.HexCodec();
        assertEquals("hex", provider.getName());
        assertEquals("68656c6c6f", provider.encode("hello".getBytes()));
        assertArrayEquals("hello".getBytes(), provider.decode("68656c6c6f"));
        assertThrows(BinaryCodecException.class, () -> provider.decode("tom"));
    }

    @Test
    void testBase64() {
        BinaryCodecProvider provider = new DefaultBinaryCodecRegister.Base64Codec();
        assertEquals("base64", provider.getName());
        assertEquals("aGVsbG8=", provider.encode("hello".getBytes()));
//        assertArrayEquals("hello".getBytes(), provider.decode("aGVsbG8="));
        assertThrows(BinaryCodecException.class, () -> provider.decode("?hello?"));
    }
}