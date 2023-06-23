package com.wingsweaver.kuja.common.utils.support.lang;

/**
 * 非抛出异常的可关闭接口。
 *
 * @author wingsweaver
 */
public interface NonThrowableCloseable extends AutoCloseable {
    /**
     * 关闭资源。
     */
    @Override
    void close();
}
