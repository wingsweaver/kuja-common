package com.wingsweaver.kuja.common.utils.support.io;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintStreamOrWriter4WriterTest {
    @Test
    void test() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BufferSizes.SMALL)) {
            try (PrintWriter printWriter = new PrintWriter(outputStream)) {
                PrintStreamOrWriter4Writer psw = new PrintStreamOrWriter4Writer(printWriter);
                psw.print("hello, no new line");
                psw.println("hello, new line");
                psw.append('a');
                psw.append("tom");
                psw.append("jerry", 1, 3);
                psw.flush();
            }

            String text = outputStream.toString();
            assertEquals("hello, no new linehello, new line" + System.lineSeparator() + "atomer", text);
        }

        PrintStreamOrWriter4Writer psw = new PrintStreamOrWriter4Writer(new PrintWriter(System.out));
        psw.printStackTrace(new Exception("exception for PrintStreamOrWriter4WriterTest"));
    }
}