package com.wingsweaver.kuja.common.utils.support.json;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;

/**
 * Json 编解码异常。
 *
 * @author wingsweaver
 */
public class JsonCodecException extends ExtendedRuntimeException {
    public JsonCodecException() {
    }

    public JsonCodecException(String message) {
        super(message);
    }

    public JsonCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonCodecException(Throwable cause) {
        super(cause);
    }
}
