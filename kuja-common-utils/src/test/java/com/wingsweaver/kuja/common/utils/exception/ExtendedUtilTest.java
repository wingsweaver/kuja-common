package com.wingsweaver.kuja.common.utils.exception;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

class ExtendedUtilTest {

    @Test
    void printStackTracePrintStream() {
        PrintStream printStream = System.out;
        new ExtendedException().printStackTrace(printStream);
        new ExtendedException("extended exception for print writer 2")
                .withExtendedAttribute("thread", Thread.currentThread())
                .withExtendedAttribute("timestamp", new Date())
                .printStackTrace(printStream);
    }

    @Test
    void testPrintStackTracePrintWriter() {
        PrintWriter printWriter = new PrintWriter(System.out);
        new ExtendedException(new Exception("nested exception for print writer 1")).printStackTrace(printWriter);
        new ExtendedException("extended exception for print writer 2")
                .withExtendedAttribute("thread", Thread.currentThread())
                .withExtendedAttribute("timestamp", new Date())
                .printStackTrace(printWriter);
    }
}