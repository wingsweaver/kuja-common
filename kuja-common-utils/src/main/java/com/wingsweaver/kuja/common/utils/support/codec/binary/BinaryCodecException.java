package com.wingsweaver.kuja.common.utils.support.codec.binary;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;

/**
 * 二进制编码解码异常。
 *
 * @author wingsweaver
 */
public class BinaryCodecException extends ExtendedRuntimeException {
    public BinaryCodecException() {
    }

    public BinaryCodecException(String message) {
        super(message);
    }

    public BinaryCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public BinaryCodecException(Throwable cause) {
        super(cause);
    }
}
