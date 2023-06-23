package com.wingsweaver.kuja.common.utils.support.io;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.PrintWriter;

class PrintStreamOrWriter4Writer implements PrintStreamOrWriter {
    private final PrintWriter printWriter;

    PrintStreamOrWriter4Writer(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    @Override
    public void print(String text) {
        this.printWriter.print(text);
    }

    @Override
    public void println(String text) {
        this.printWriter.println(text);
    }

    @SuppressFBWarnings("INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE")
    @Override
    public void printStackTrace(Throwable throwable) {
        throwable.printStackTrace(this.printWriter);
    }

    @Override
    public void flush() throws IOException {
        this.printWriter.flush();
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        return this.printWriter.append(csq);
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return this.printWriter.append(csq, start, end);
    }

    @Override
    public Appendable append(char c) throws IOException {
        return this.printWriter.append(c);
    }
}
