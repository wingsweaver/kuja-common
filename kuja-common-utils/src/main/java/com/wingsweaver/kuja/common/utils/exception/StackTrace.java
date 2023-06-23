package com.wingsweaver.kuja.common.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * {@link StackTraceElement} 的可序列化版本。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StackTrace implements Serializable {
    /**
     * 类名。
     */
    private String className;

    /**
     * 方法名。
     */
    private String methodName;

    /**
     * 文件名。
     */
    private String fileName;

    /**
     * 行号。
     */
    private int lineNumber;

    public StackTrace(StackTraceElement stackTraceElement) {
        this.className = stackTraceElement.getClassName();
        this.methodName = stackTraceElement.getMethodName();
        this.fileName = stackTraceElement.getFileName();
        this.lineNumber = stackTraceElement.getLineNumber();
    }
}
