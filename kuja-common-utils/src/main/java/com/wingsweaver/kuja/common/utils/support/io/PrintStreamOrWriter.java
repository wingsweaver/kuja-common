package com.wingsweaver.kuja.common.utils.support.io;

import java.io.Flushable;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * {@link PrintStream} 和 {@link PrintWriter} 的整合接口。
 *
 * @author wingsweaver
 */
public interface PrintStreamOrWriter extends Appendable, Flushable {
    /**
     * 输出指定字符串。
     *
     * @param text 字符串
     */
    void print(String text);

    /**
     * 输出指定字符串并且换行。
     *
     * @param text 字符串
     */
    void println(String text);

    /**
     * 输出指定的异常。
     *
     * @param throwable 异常
     */
    void printStackTrace(Throwable throwable);

    /**
     * 生成 PrintStream 的封装实例。
     *
     * @param printStream PrintStream 实例
     * @return 封装结果
     */
    static PrintStreamOrWriter of(PrintStream printStream) {
        return new PrintStreamOrWriter4Stream(printStream);
    }

    /**
     * 生成 PrintWriter 的封装实例。
     *
     * @param printWriter PrintWriter 实例
     * @return 封装结果
     */
    static PrintStreamOrWriter of(PrintWriter printWriter) {
        return new PrintStreamOrWriter4Writer(printWriter);
    }
}
