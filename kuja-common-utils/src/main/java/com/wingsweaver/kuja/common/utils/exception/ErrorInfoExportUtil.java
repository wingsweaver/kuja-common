package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.lang.reflect.ReflectUtil;
import com.wingsweaver.kuja.common.utils.support.tostring.ToStringBuilder;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 异常导出的工具类。
 *
 * @author wingsweaver
 */
public final class ErrorInfoExportUtil {
    private ErrorInfoExportUtil() {
        // 禁止实例化
    }

    public static final String KEY_CLASS = "class";

    public static final String KEY_MESSAGE = "message";

    public static final String KEY_LOCALIZED_MESSAGE = "localizedMessage";

    public static final String KEY_EXTEND = "extended";

    public static final String FIELD_STACK_TRACE = "stackTrace";

    public static final String FIELD_CAUSE = "cause";

    public static final String FIELD_SUPPRESSED = "suppressed";

    public static final String FIELD_GETTERS = "getters";

    private static final ErrorInfoExporter[] ERROR_INFO_EXPORTERS;

    static {
        ERROR_INFO_EXPORTERS = SpringFactoriesLoader.loadFactories(ErrorInfoExporterFactory.class, null).stream()
                .map(ErrorInfoExporterFactory::create)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(DefaultOrdered::getOrder)).toArray(ErrorInfoExporter[]::new);
    }

    /**
     * 将异常数据导出为 Map。
     *
     * @param error     异常
     * @param predicate 检查是否导出的处理
     * @return Map 实例
     */
    public static Map<String, Object> export(Throwable error, ErrorExportPredicate predicate) {
        // 检查参数
        if (error == null || predicate == null) {
            return Collections.emptyMap();
        }

        // 逐个调用 ErrorInfoExporter，进行导出
        Map<String, Object> map = new HashMap<>(BufferSizes.TINY);
        for (ErrorInfoExporter exporter : ERROR_INFO_EXPORTERS) {
            exporter.exportErrorInfo(error, map, predicate);
        }

        // 默认导出
        exportErrorInfo(error, map, predicate);

        // 返回
        return map;
    }

    /**
     * 默认的导出处理。
     *
     * @param error     异常
     * @param map       存储导出数据的字典
     * @param predicate 导出设置
     */
    private static void exportErrorInfo(Throwable error, Map<String, Object> map, ErrorExportPredicate predicate) {
        // 基本信息
        if (predicate.includes(KEY_CLASS)) {
            map.putIfAbsent(KEY_CLASS, error.getClass().getName());
        }
        if (predicate.includes(KEY_MESSAGE)) {
            map.putIfAbsent(KEY_MESSAGE, error.getMessage());
        }
        if (predicate.includes(KEY_LOCALIZED_MESSAGE)) {
            map.putIfAbsent(KEY_LOCALIZED_MESSAGE, error.getLocalizedMessage());
        }

        // 导出扩展信息
        if (predicate.includes(KEY_EXTEND)) {
            exportExtendedMap(error, map);
        }

        // 导出堆栈信息
        if (predicate.includes(FIELD_STACK_TRACE)) {
            exportStackTrace(error, map);
        }

        // 导出 cause 信息
        if (predicate.includes(FIELD_CAUSE)) {
            exportCause(error, map, predicate);
        }

        // 导出 suppressed 信息
        if (predicate.includes(FIELD_SUPPRESSED)) {
            exportSuppressed(error, map, predicate);
        }

        // 导出 Getter 信息
        if (predicate.includes(FIELD_GETTERS)) {
            exportGetters(error, map, predicate);
        }
    }

    /**
     * 导出扩展数据。
     *
     * @param error 错误
     * @param map   存储导出数据的字典
     */
    private static void exportExtendedMap(Throwable error, Map<String, Object> map) {
        // 检查是否实现了 Extended 接口
        if (!(error instanceof Extended)) {
            return;
        }

        // 获取扩展数据
        Map<String, Object> extendedMap = ((Extended<?>) error).extendedMap();
        if (CollectionUtils.isEmpty(extendedMap)) {
            return;
        }

        // 导出扩展数据
        Map<String, Object> valueMap = new HashMap<>(MapUtil.hashInitCapacity(extendedMap.size()));
        for (Map.Entry<String, Object> entry : extendedMap.entrySet()) {
            valueMap.put(entry.getKey(), ToStringBuilder.toString(entry.getValue()));
        }
        map.put(KEY_EXTEND, valueMap);
    }

    /**
     * 导出 Getter 方法对应的数据。
     *
     * @param error     错误
     * @param map       存储导出数据的字典
     * @param predicate 导出设置
     */
    public static void exportGetters(Throwable error, Map<String, Object> map, ErrorExportPredicate predicate) {
        Method[] methods = error.getClass().getMethods();
        for (Method method : methods) {
            // 提取属性名称
            String fieldName = ReflectUtil.resolveFieldNameStrict(method, true);
            if (StringUtil.isEmpty(fieldName)) {
                continue;
            }

            // 检查同名属性是否可以导出
            if (map.containsKey(fieldName) || !predicate.includes(fieldName)) {
                continue;
            }

            // 获取属性值
            try {
                Object value = method.invoke(error);
                if (value != null && value != error) {
                    map.put(fieldName, ToStringBuilder.toString(value));
                }
            } catch (Exception ignored) {
                // 什么也不做
            }
        }
    }

    /**
     * 导出 {@link Throwable#getSuppressed()} 信息。
     *
     * @param error     异常
     * @param map       存储导出数据的字典
     * @param predicate 导出设置
     */
    public static void exportSuppressed(Throwable error, Map<String, Object> map, ErrorExportPredicate predicate) {
        Throwable[] suppressed = error.getSuppressed();
        if (suppressed != null && suppressed.length > 0) {
            map.put(FIELD_SUPPRESSED, Arrays.stream(suppressed)
                    .map(e -> export(e, predicate))
                    .collect(Collectors.toList()));
        }
    }

    /**
     * 导出 {@link Throwable#getCause()} 信息。
     *
     * @param error     异常
     * @param map       存储导出数据的字典
     * @param predicate 导出设置
     */
    public static void exportCause(Throwable error, Map<String, Object> map, ErrorExportPredicate predicate) {
        Throwable cause = error.getCause();
        if (cause != null) {
            map.put(FIELD_CAUSE, export(cause, predicate));
        }
    }

    /**
     * 导出 {@link Throwable#getStackTrace()} 信息。
     *
     * @param error 异常
     * @param map   存储导出数据的字典
     */
    public static void exportStackTrace(Throwable error, Map<String, Object> map) {
        List<StackTrace> stackTraces = Arrays.stream(error.getStackTrace())
                .map(StackTrace::new)
                .collect(Collectors.toList());
        map.put(FIELD_STACK_TRACE, stackTraces);
    }
}
