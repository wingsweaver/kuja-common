package com.wingsweaver.kuja.common.web.exception;

/**
 * 未知的 Web 异常。
 *
 * @author wingsweaver
 */
public class UnknownWebException extends RuntimeException {
    public UnknownWebException() {
    }

    public UnknownWebException(String message) {
        super(message);
    }

    public UnknownWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownWebException(Throwable cause) {
        super(cause);
    }
}
