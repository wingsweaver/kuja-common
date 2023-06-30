package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
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
public class StackTrace extends AbstractPojo implements Serializable {
    /**
     * ClassLoader 名称。<br>
     * 需要 JDK 17。
     */
    private String classLoaderName;

    /**
     * 模块名称。<br>
     * 需要 JDK 17。
     */
    private String moduleName;

    /**
     * 模块版本。<br>
     * 需要 JDK 17。
     */
    private String moduleVersion;

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

    /**
     * 对应的文本。
     */
    private String text;

    public StackTrace(StackTraceElement stackTraceElement) {
        AssertArgs.Named.notNull("stackTraceElement", stackTraceElement);
        this.className = stackTraceElement.getClassName();
        this.methodName = stackTraceElement.getMethodName();
        this.fileName = stackTraceElement.getFileName();
        this.lineNumber = stackTraceElement.getLineNumber();
        this.text = stackTraceElement.toString();

        // 额外的初始化处理
        this.init(stackTraceElement);
    }

    private void init(StackTraceElement stackTraceElement) {
        try {
            Class<?> clazz = stackTraceElement.getClass();
            this.classLoaderName = (String) clazz.getDeclaredMethod("getClassLoaderName").invoke(stackTraceElement);
            this.moduleName = (String) clazz.getDeclaredMethod("getModuleName").invoke(stackTraceElement);
            this.moduleVersion = (String) clazz.getDeclaredMethod("getModuleVersion").invoke(stackTraceElement);
        } catch (Exception ignored) {
            // 忽略此处的错误
        }
    }

    /**
     * 检查是否是本地方法。
     *
     * @return 如果是本地方法，返回 true；否则返回 false。
     */
    public boolean isNativeMethod() {
        return lineNumber < 0;
    }

    @Override
    public String toString() {
        if (StringUtil.isNotEmpty(this.text)) {
            return this.text;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(className).append(".").append(methodName).append("(");
        if (this.isNativeMethod()) {
            sb.append("Native Method)");
        } else {
            if (fileName != null && lineNumber >= 0) {
                sb.append(fileName).append(":").append(lineNumber).append(")");
            } else if (fileName != null) {
                sb.append(fileName).append(")");
            } else {
                sb.append("Unknown Source)");
            }
        }
        return sb.toString();
    }
}
