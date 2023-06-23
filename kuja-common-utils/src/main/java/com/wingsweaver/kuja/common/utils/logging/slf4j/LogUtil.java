package com.wingsweaver.kuja.common.utils.logging.slf4j;

import com.wingsweaver.kuja.common.utils.support.lang.StringFormatter;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 日志工具类。
 *
 * @author wingsweaver
 */
@SuppressWarnings({"unused", "PMD.GuardLogStatement"})
public final class LogUtil {
    private LogUtil() {
        // 禁止实例化
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param level  日志级别
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void log(Logger logger, Marker marker, Level level, Throwable error, String format, Object... args) {
        logInternal(logger, marker, level, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param level       日志级别
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void log(Logger logger, Marker marker, Level level, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, marker, level, msgSupplier, error);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param level  日志级别
     * @param error  异常
     */
    public static void log(Logger logger, Marker marker, Level level, Throwable error) {
        logInternal(logger, marker, level, error::getMessage, error);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param level  日志级别
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void log(Logger logger, Marker marker, Level level, String format, Object... args) {
        logInternal(logger, marker, level, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param level       日志级别
     * @param msgSupplier 日志消息的提供者
     */
    public static void log(Logger logger, Marker marker, Level level, Supplier<String> msgSupplier) {
        logInternal(logger, marker, level, msgSupplier, null);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param level  日志级别
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void log(Logger logger, Level level, Throwable error, String format, Object... args) {
        logInternal(logger, null, level, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param level       日志级别
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void log(Logger logger, Level level, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, null, level, msgSupplier, error);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param level  日志级别
     * @param error  异常
     */
    public static void log(Logger logger, Level level, Throwable error) {
        logInternal(logger, null, level, error::getMessage, error);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param level  日志级别
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void log(Logger logger, Level level, String format, Object... args) {
        logInternal(logger, null, level, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param level       日志级别
     * @param msgSupplier 日志消息的提供者
     */
    public static void log(Logger logger, Level level, Supplier<String> msgSupplier) {
        logInternal(logger, null, level, msgSupplier, null);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void trace(Logger logger, Marker marker, Throwable error, String format, Object... args) {
        logInternal(logger, marker, Level.TRACE, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void trace(Logger logger, Marker marker, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.TRACE, msgSupplier, error);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     */
    public static void trace(Logger logger, Marker marker, Throwable error) {
        logInternal(logger, marker, Level.TRACE, error::getMessage, error);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void trace(Logger logger, Marker marker, String format, Object... args) {
        logInternal(logger, marker, Level.TRACE, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的提供者
     */
    public static void trace(Logger logger, Marker marker, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.TRACE, msgSupplier, null);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void trace(Logger logger, Throwable error, String format, Object... args) {
        logInternal(logger, null, Level.TRACE, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void trace(Logger logger, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.TRACE, msgSupplier, error);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     */
    public static void trace(Logger logger, Throwable error) {
        logInternal(logger, null, Level.TRACE, error::getMessage, error);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void trace(Logger logger, String format, Object... args) {
        logInternal(logger, null, Level.TRACE, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param msgSupplier 日志消息的提供者
     */
    public static void trace(Logger logger, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.TRACE, msgSupplier, null);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void debug(Logger logger, Marker marker, Throwable error, String format, Object... args) {
        logInternal(logger, marker, Level.DEBUG, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void debug(Logger logger, Marker marker, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.DEBUG, msgSupplier, error);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     */
    public static void debug(Logger logger, Marker marker, Throwable error) {
        logInternal(logger, marker, Level.DEBUG, error::getMessage, error);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void debug(Logger logger, Marker marker, String format, Object... args) {
        logInternal(logger, marker, Level.DEBUG, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的提供者
     */
    public static void debug(Logger logger, Marker marker, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.DEBUG, msgSupplier, null);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void debug(Logger logger, Throwable error, String format, Object... args) {
        logInternal(logger, null, Level.DEBUG, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void debug(Logger logger, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.DEBUG, msgSupplier, error);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     */
    public static void debug(Logger logger, Throwable error) {
        logInternal(logger, null, Level.DEBUG, error::getMessage, error);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void debug(Logger logger, String format, Object... args) {
        logInternal(logger, null, Level.DEBUG, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param msgSupplier 日志消息的提供者
     */
    public static void debug(Logger logger, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.DEBUG, msgSupplier, null);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void info(Logger logger, Marker marker, Throwable error, String format, Object... args) {
        logInternal(logger, marker, Level.INFO, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void info(Logger logger, Marker marker, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.INFO, msgSupplier, error);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     */
    public static void info(Logger logger, Marker marker, Throwable error) {
        logInternal(logger, marker, Level.INFO, error::getMessage, error);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void info(Logger logger, Marker marker, String format, Object... args) {
        logInternal(logger, marker, Level.INFO, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的提供者
     */
    public static void info(Logger logger, Marker marker, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.INFO, msgSupplier, null);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void info(Logger logger, Throwable error, String format, Object... args) {
        logInternal(logger, null, Level.INFO, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void info(Logger logger, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.INFO, msgSupplier, error);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     */
    public static void info(Logger logger, Throwable error) {
        logInternal(logger, null, Level.INFO, error::getMessage, error);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void info(Logger logger, String format, Object... args) {
        logInternal(logger, null, Level.INFO, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param msgSupplier 日志消息的提供者
     */
    public static void info(Logger logger, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.INFO, msgSupplier, null);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void warn(Logger logger, Marker marker, Throwable error, String format, Object... args) {
        logInternal(logger, marker, Level.WARN, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void warn(Logger logger, Marker marker, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.WARN, msgSupplier, error);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     */
    public static void warn(Logger logger, Marker marker, Throwable error) {
        logInternal(logger, marker, Level.WARN, error::getMessage, error);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void warn(Logger logger, Marker marker, String format, Object... args) {
        logInternal(logger, marker, Level.WARN, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的提供者
     */
    public static void warn(Logger logger, Marker marker, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.WARN, msgSupplier, null);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void warn(Logger logger, Throwable error, String format, Object... args) {
        logInternal(logger, null, Level.WARN, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void warn(Logger logger, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.WARN, msgSupplier, error);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     */
    public static void warn(Logger logger, Throwable error) {
        logInternal(logger, null, Level.WARN, error::getMessage, error);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void warn(Logger logger, String format, Object... args) {
        logInternal(logger, null, Level.WARN, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param msgSupplier 日志消息的提供者
     */
    public static void warn(Logger logger, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.WARN, msgSupplier, null);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void error(Logger logger, Marker marker, Throwable error, String format, Object... args) {
        logInternal(logger, marker, Level.ERROR, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void error(Logger logger, Marker marker, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.ERROR, msgSupplier, error);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param error  异常
     */
    public static void error(Logger logger, Marker marker, Throwable error) {
        logInternal(logger, marker, Level.ERROR, error::getMessage, error);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param marker 日志标记
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void error(Logger logger, Marker marker, String format, Object... args) {
        logInternal(logger, marker, Level.ERROR, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的提供者
     */
    public static void error(Logger logger, Marker marker, Supplier<String> msgSupplier) {
        logInternal(logger, marker, Level.ERROR, msgSupplier, null);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void error(Logger logger, Throwable error, String format, Object... args) {
        logInternal(logger, null, Level.ERROR, () -> StringFormatter.format(format, args), error);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param error       异常
     * @param msgSupplier 日志消息的提供者
     */
    public static void error(Logger logger, Throwable error, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.ERROR, msgSupplier, error);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param error  异常
     */
    public static void error(Logger logger, Throwable error) {
        logInternal(logger, null, Level.ERROR, error::getMessage, error);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger 日志记录器的实例
     * @param format 日志格式
     * @param args   日志参数
     */
    public static void error(Logger logger, String format, Object... args) {
        logInternal(logger, null, Level.ERROR, () -> StringFormatter.format(format, args), null);
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param msgSupplier 日志消息的提供者
     */
    public static void error(Logger logger, Supplier<String> msgSupplier) {
        logInternal(logger, null, Level.ERROR, msgSupplier, null);
    }

    /**
     * 以 TRACE 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的计算函数
     */
    private static void traceInternal(Logger logger, Marker marker, Supplier<String> msgSupplier, Throwable error) {
        if (marker != null) {
            if (logger.isTraceEnabled(marker)) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.trace(marker, StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.trace(marker, message);
                }
            }
        } else {
            if (logger.isTraceEnabled()) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.trace(StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.trace(message);
                }
            }
        }
    }

    /**
     * 以 DEBUG 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的计算函数
     */
    private static void debugInternal(Logger logger, Marker marker, Supplier<String> msgSupplier, Throwable error) {
        if (marker != null) {
            if (logger.isDebugEnabled(marker)) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.debug(marker, StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.debug(marker, message);
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.debug(StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.debug(message);
                }
            }
        }
    }

    /**
     * 以 INFO 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的计算函数
     */
    private static void infoInternal(Logger logger, Marker marker, Supplier<String> msgSupplier, Throwable error) {
        if (marker != null) {
            if (logger.isInfoEnabled(marker)) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.info(marker, StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.info(marker, message);
                }
            }
        } else {
            if (logger.isInfoEnabled()) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.info(StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.info(message);
                }
            }
        }
    }

    /**
     * 以 WARN 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的计算函数
     */
    private static void warnInternal(Logger logger, Marker marker, Supplier<String> msgSupplier, Throwable error) {
        if (marker != null) {
            if (logger.isWarnEnabled(marker)) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.warn(marker, StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.warn(marker, message);
                }
            }
        } else {
            if (logger.isWarnEnabled()) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.warn(StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.warn(message);
                }
            }
        }
    }

    /**
     * 以 ERROR 级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param marker      日志标记
     * @param msgSupplier 日志消息的计算函数
     */
    private static void errorInternal(Logger logger, Marker marker, Supplier<String> msgSupplier, Throwable error) {
        if (marker != null) {
            if (logger.isErrorEnabled(marker)) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.error(marker, StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.error(marker, message);
                }
            }
        } else {
            if (logger.isErrorEnabled()) {
                String message = StringUtil.fromSupplierOrEmpty(msgSupplier);
                if (error != null) {
                    logger.error(StringUtil.notEmptyOrElse(message, error::getMessage), error);
                } else {
                    logger.error(message);
                }
            }
        }
    }

    /**
     * 日志记录函数。
     */
    @FunctionalInterface
    interface LogFunction {
        /**
         * 以指定级别输出日志。
         *
         * @param logger      日志记录器的实例
         * @param marker      日志标记
         * @param msgSupplier 日志消息的计算函数
         * @param error       异常
         */
        void log(Logger logger, Marker marker, Supplier<String> msgSupplier, Throwable error);
    }

    /**
     * 日志级别与日志记录函数的映射。
     */
    static final Map<Level, LogFunction> LOG_FUNCTION_MAP;

    static {
        LOG_FUNCTION_MAP = new HashMap<>();
        LOG_FUNCTION_MAP.put(Level.TRACE, LogUtil::traceInternal);
        LOG_FUNCTION_MAP.put(Level.DEBUG, LogUtil::debugInternal);
        LOG_FUNCTION_MAP.put(Level.INFO, LogUtil::infoInternal);
        LOG_FUNCTION_MAP.put(Level.WARN, LogUtil::warnInternal);
        LOG_FUNCTION_MAP.put(Level.ERROR, LogUtil::errorInternal);
        LOG_FUNCTION_MAP.put(null, LogUtil::traceInternal);
    }

    /**
     * 以指定的日志级别记录日志。
     *
     * @param logger      日志记录器的实例
     * @param level       日志级别
     * @param msgSupplier 日志消息的提供者
     * @param error       异常
     */
    static void logInternal(Logger logger, Marker marker, Level level, Supplier<String> msgSupplier, Throwable error) {
        // 从上下文中更新 logger、marker、level
        Logger contextLogger = LogContext.getLogger();
        if (contextLogger != null) {
            logger = contextLogger;
        }
        // 如果 logger 为 null，则使用默认的 logger
        if (logger == null) {
            logger = LoggerFactory.getLogger("");
        }
        Marker contextMarker = LogContext.getMarker();
        if (contextMarker != null) {
            marker = contextMarker;
        }
        Level contextLevel = LogContext.getLevel();
        if (contextLevel != null) {
            level = contextLevel;
        }
        if (level == null) {
            level = Level.TRACE;
        }

        // 检查是否可以输出日志
        LogFunction logFunction = LOG_FUNCTION_MAP.getOrDefault(level, LogUtil::traceInternal);
        logFunction.log(logger, marker, msgSupplier, error);
    }
}
