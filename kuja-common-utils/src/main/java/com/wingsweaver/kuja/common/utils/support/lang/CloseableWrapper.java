package com.wingsweaver.kuja.common.utils.support.lang;

import lombok.Getter;

/**
 * 可关闭的包装器。
 *
 * @param <T> 被包装的对象类型
 * @author wingsweaver
 */
public class CloseableWrapper<T> implements NonThrowableCloseable {
    @Getter
    private final T delegate;

    public CloseableWrapper(T delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() {
        if (delegate instanceof AutoCloseable) {
            try {
                ((AutoCloseable) delegate).close();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
