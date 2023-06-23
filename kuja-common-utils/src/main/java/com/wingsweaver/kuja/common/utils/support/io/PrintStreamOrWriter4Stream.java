package com.wingsweaver.kuja.common.utils.support.io;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.PrintStream;

class PrintStreamOrWriter4Stream implements PrintStreamOrWriter {
    private final PrintStream printStream;

    PrintStreamOrWriter4Stream(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void print(String text) {
        this.printStream.print(text);
    }

    @Override
    public void println(String text) {
        this.printStream.println(text);
    }

    @SuppressFBWarnings("INFORMATION_EXPOSURE_THROUGH_AN_ERROR_MESSAGE")
    @Override
    public void printStackTrace(Throwable throwable) {
        throwable.printStackTrace(this.printStream);
    }

    @Override
    public void flush() throws IOException {
        this.printStream.flush();
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        return this.printStream.append(csq);
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return this.printStream.append(csq, start, end);
    }

    @Override
    public Appendable append(char c) throws IOException {
        return this.printStream.append(c);
    }
}
