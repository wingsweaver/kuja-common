package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;

/**
 * Tag 转换异常。
 *
 * @author wingsweaver
 */
public class TagConversionException extends ExtendedRuntimeException {
    public TagConversionException() {
    }

    public TagConversionException(String message) {
        super(message);
    }

    public TagConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagConversionException(Throwable cause) {
        super(cause);
    }
}
