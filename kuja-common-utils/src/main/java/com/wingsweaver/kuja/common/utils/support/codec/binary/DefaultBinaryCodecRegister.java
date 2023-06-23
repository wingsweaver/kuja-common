package com.wingsweaver.kuja.common.utils.support.codec.binary;

import org.apache.commons.codec.binary.Hex;

import java.util.Base64;
import java.util.Collection;

/**
 * 默认的 {@link BinaryCodecRegister} 实现。
 *
 * @author wingsweaver
 */
class DefaultBinaryCodecRegister implements BinaryCodecRegister {
    @Override
    public void register(Collection<BinaryCodecProvider> providers) {
        providers.add(new HexCodec());
        providers.add(new Base64Codec());
    }

    /**
     * 十六进制编解码器。
     */
    static class HexCodec implements BinaryCodecProvider {
        @Override
        public String getName() {
            return "hex";
        }

        @Override
        public String encode(byte[] bytes) {
            return Hex.encodeHexString(bytes);
        }

        @Override
        public byte[] decode(String text) {
            try {
                return Hex.decodeHex(text);
            } catch (Exception e) {
                BinaryCodecException ex = new BinaryCodecException("Failed to decode hex string: " + text, e);
                ex.withExtendedAttribute("text", text);
                throw ex;
            }
        }
    }

    /**
     * Base64 编解码器。
     */
    static class Base64Codec implements BinaryCodecProvider {
        @Override
        public String getName() {
            return "base64";
        }

        @Override
        public String encode(byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes);
        }

        @Override
        public byte[] decode(String text) {
            try {
                return Base64.getDecoder().decode(text);
            } catch (Exception e) {
                BinaryCodecException ex = new BinaryCodecException("Failed to decode base64 string: " + text, e);
                ex.withExtendedAttribute("text", text);
                throw ex;
            }
        }
    }
}
