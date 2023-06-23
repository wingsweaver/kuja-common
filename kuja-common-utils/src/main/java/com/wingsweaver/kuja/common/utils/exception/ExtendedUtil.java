package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.support.io.PrintStreamOrWriter;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringConfig;
import org.springframework.util.CollectionUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Extended} 辅助工具类。
 *
 * @author wingsweaver
 */
public final class ExtendedUtil {
    private ExtendedUtil() {
        // 禁止实例化
    }

    /**
     * 输出扩展字段。
     *
     * @param extended    带扩展字段的异常
     * @param printStream 输出流
     */
    public static void printStackTrace(Extended<? extends Throwable> extended, PrintStream printStream) {
        if (extended != null && printStream != null) {
            printStackTrace(extended, PrintStreamOrWriter.of(printStream));
        }
    }

    /**
     * 输出扩展字段。
     *
     * @param extended    带扩展字段的异常
     * @param printWriter 输出流
     */
    public static void printStackTrace(Extended<? extends Throwable> extended, PrintWriter printWriter) {
        if (extended != null && printWriter != null) {
            printStackTrace(extended, PrintStreamOrWriter.of(printWriter));
        }
    }

    /**
     * 输出扩展字段。
     *
     * @param extended 带扩展字段的异常
     * @param printer  输出流
     */
    public static void printStackTrace(Extended<? extends Throwable> extended, PrintStreamOrWriter printer) {
        Map<String, Object> map = extended.extendedMap();
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        // 输出扩展字段
        ToStringConfig config = ToStringBuilder.getDefaultConfig().mutable()
                .setIncludeTypeName(false).setPublicOnly(true)
                .build();
        printer.println("extended:");
        List<String> segments = new ArrayList<>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            segments.add(entry.getKey() + " = " + ToStringBuilder.toString(entry.getValue(), config));
        }
        printer.println(String.join(", ", segments));
    }
}
