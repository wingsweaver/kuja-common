package com.wingsweaver.kuja.common.utils.support.codec.binary;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BinaryCodecTest {
    private static final BinaryCodecProvider DUMMY_PROVIDER = new BinaryCodecProvider() {
        @Override
        public String getName() {
            return "dummy";
        }

        @Override
        public String encode(byte[] bytes) {
            return null;
        }

        @Override
        public byte[] decode(String text) {
            return new byte[0];
        }
    };

    @Test
    void getDefaultProvider() {
        BinaryCodecProvider defaultProvider = BinaryCodec.getDefaultProvider();
        assertNotNull(defaultProvider);
        assertEquals("base64", defaultProvider.getName());
        assertEquals(defaultProvider, BinaryCodec.getProvider("BASE64"));
        assertNotEquals(defaultProvider, BinaryCodec.getProvider("HEX"));
        assertNull(BinaryCodec.getProvider("DUMMY"));
    }

    @Test
    void setDefaultProvider() {
        BinaryCodecProvider oldDefaultProvider = BinaryCodec.getDefaultProvider();
        BinaryCodec.setDefaultProvider(DUMMY_PROVIDER);
        assertEquals(DUMMY_PROVIDER, BinaryCodec.getDefaultProvider());
        BinaryCodec.setDefaultProvider(oldDefaultProvider);
        assertEquals(oldDefaultProvider, BinaryCodec.getDefaultProvider());
    }

    @Test
    void codecWithProvider() {
        byte[] bytes = "hello".getBytes();
        assertEquals("aGVsbG8=", BinaryCodec.encode(bytes, "base64"));
        assertArrayEquals(bytes, BinaryCodec.decode("aGVsbG8=", "base64"));
        assertEquals("68656c6c6f", BinaryCodec.encode(bytes, "hex"));
        assertArrayEquals(bytes, BinaryCodec.decode("68656c6c6f", "hex"));
        assertThrows(IllegalArgumentException.class, () -> BinaryCodec.encode(bytes, "dummy"));
        assertThrows(IllegalArgumentException.class, () -> BinaryCodec.decode("", ""));
    }

    @Test
    void codecWithDefault() {
        byte[] bytes = "hello".getBytes();
        String encoded = BinaryCodec.encode(bytes);
        assertArrayEquals(bytes, BinaryCodec.decode(encoded));
    }

    @Test
    void namedCodecWithDefault() {
        byte[] bytes = "world".getBytes();
        String text = BinaryCodec.encodeAsNamed(bytes);
        assertNotNull(text);
        assertArrayEquals(bytes, BinaryCodec.parseAndDecode(text));

        String text2 = BinaryCodec.encodeAsNamed(bytes, BinaryCodecTest::combineProviderResult);
        assertNotNull(text2);
        assertNotEquals(text, text2);
        assertArrayEquals(bytes, BinaryCodec.parseAndDecode(text2, BinaryCodecTest::parseProviderResult));

        assertThrows(IllegalArgumentException.class, () -> BinaryCodec.parseAndDecode(null));
        assertThrows(IllegalArgumentException.class, () -> BinaryCodec.parseAndDecode(""));
        assertThrows(IllegalArgumentException.class, () -> BinaryCodec.parseAndDecode("}{"));
        assertThrows(IllegalArgumentException.class, () -> BinaryCodec.parseAndDecode("{}"));
    }

    @Test
    void namedCodecWithProvider() {
        String providerName = "base64";
        byte[] bytes = "world".getBytes();
        String text = BinaryCodec.encodeAsNamed(bytes, providerName);
        assertNotNull(text);
        assertArrayEquals(bytes, BinaryCodec.parseAndDecode(text));

        String text2 = BinaryCodec.encodeAsNamed(bytes, providerName, BinaryCodecTest::combineProviderResult);
        assertNotNull(text2);
        assertNotEquals(text, text2);
        assertArrayEquals(bytes, BinaryCodec.parseAndDecode(text2, BinaryCodecTest::parseProviderResult));
    }

    @Test
    void namedCodecWithProvider2() {
        BinaryCodecProvider provider = BinaryCodec.getProvider("hex");
        byte[] bytes = "world".getBytes();
        String text = BinaryCodec.encodeAsNamed(bytes, provider);
        assertNotNull(text);
        assertArrayEquals(bytes, BinaryCodec.parseAndDecode(text));

        String text2 = BinaryCodec.encodeAsNamed(bytes, provider, BinaryCodecTest::combineProviderResult);
        assertNotNull(text2);
        assertNotEquals(text, text2);
        assertArrayEquals(bytes, BinaryCodec.parseAndDecode(text2, BinaryCodecTest::parseProviderResult));
    }

    static final String PREFIX = UUID.randomUUID() + "!";

    static String combineProviderResult(BinaryCodecProvider provider, String result) {
        return PREFIX + provider.getName() + ":" + result;
    }

    static Tuple2<String, String> parseProviderResult(String text) {
        if (StringUtil.isEmpty(text)) {
            throw new IllegalArgumentException("text is empty");
        }
        if (!text.startsWith(PREFIX)) {
            throw new IllegalArgumentException("text is not prefixed with " + PREFIX);
        }

        int index = text.indexOf(':');
        if (index < 0) {
            throw new IllegalArgumentException("invalid text: " + text);
        }

        String providerName = text.substring(PREFIX.length(), index);
        String result = text.substring(index + 1);
        return Tuple2.of(providerName, result);
    }
}