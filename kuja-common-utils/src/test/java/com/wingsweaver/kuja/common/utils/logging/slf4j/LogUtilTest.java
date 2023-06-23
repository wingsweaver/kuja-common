package com.wingsweaver.kuja.common.utils.logging.slf4j;

import com.wingsweaver.kuja.common.utils.support.lang.StringFormatter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

class LogUtilTest {
    @Test
    void log() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Level[] levels = Level.values();
        int levelIndex = ThreadLocalRandom.current().nextInt(0, 1000);

        // log(Logger logger, Marker marker, Level level, Throwable error, String format, Object... args) 的测试用例
        LogUtil.log(null, null, (Level) null, (Throwable) null,
                "logger = {}, marker = {}, level = {}, throwable = {}", null, null, null, null);
        Level level = levels[(levelIndex++) % levels.length];
        LogUtil.log(logger, null, level, (Throwable) null,
                "logger = {}, marker = {}, level = {}, throwable = {}", logger, null, level, null);
        level = levels[(levelIndex++) % levels.length];
        LogUtil.log(null, marker, level, (Throwable) null,
                "logger = {}, marker = {}, level = {}, throwable = {}", null, marker, level, null);
        level = levels[(levelIndex++) % levels.length];
        LogUtil.log(logger, marker, level, (Throwable) null,
                "logger = {}, marker = {}, level = {}, throwable = {}", logger, marker, level, null);

        level = levels[(levelIndex++) % levels.length];
        LogUtil.log(logger, marker, level, (Throwable) null,
                "logger = {}, marker = {}, level = {}, throwable = {}", logger, marker, level, null);

        Exception exception = new Exception("按需抛出异常-log-1");
        level = levels[(levelIndex++) % levels.length];
        LogUtil.log(logger, marker, level, exception,
                "logger = {}, marker = {}, level = {}, throwable = {}", logger, marker, level, Objects.hashCode(exception));
    }

    @Test
    void log2() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Level[] levels = Level.values();
        int levelIndex = 0;

        // log(Logger logger, Marker marker, Level level, Throwable error, String format, Object... args) 的测试用例
        LogUtil.log(null, null, null, null,
                () -> StringFormatter.format("logger = {}, marker = {}, level = {}, throwable = {}", null, null, null, null));
        {
            Level level = levels[(levelIndex++) % levels.length];
            LogUtil.log(logger, null, level, null,
                    () -> StringFormatter.format("logger = {}, marker = {}, level = {}, throwable = {}", logger, null, level, null));
        }
        {
            Level level = levels[(levelIndex++) % levels.length];
            LogUtil.log(null, marker, level, null,
                    () -> StringFormatter.format("logger = {}, marker = {}, level = {}, throwable = {}", null, marker, level, null));
        }
        {
            Level level = levels[(levelIndex++) % levels.length];
            LogUtil.log(logger, marker, level, (Throwable) null,
                    "logger = {}, marker = {}, level = {}, throwable = {}", logger, marker, level, null);
        }
        {
            Level level = levels[(levelIndex++) % levels.length];
            LogUtil.log(logger, marker, level, (Throwable) null,
                    "logger = {}, marker = {}, level = {}, throwable = {}", logger, marker, level, null);
        }
        {
            Exception exception = new Exception("按需抛出异常-log-2");
            Level level = levels[(levelIndex++) % levels.length];
            LogUtil.log(logger, marker, level, exception,
                    "logger = {}, marker = {}, level = {}, throwable = {}", logger, marker, level, Objects.hashCode(exception));
        }
    }

    Level randomLevel() {
        Level[] levels = Level.values();
        return levels[ThreadLocalRandom.current().nextInt(0, levels.length)];
    }

    @Test
    void log3() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Exception error = new Exception("按需抛出异常-log-3");

        // log(Logger logger, Marker marker, Level level, Throwable error)
        LogUtil.log(logger, marker, randomLevel(), error);

        // log(Logger logger, Marker marker, Level level, String format, Object... args)
        LogUtil.log(logger, marker, randomLevel(), "log(Logger logger, Marker marker, Level level, String format, Object... args)");

        // log(Logger logger, Marker marker, Level level, Supplier<String> msgSupplier)
        LogUtil.log(logger, marker, randomLevel(), () -> "log(Logger logger, Marker marker, Level level, Supplier<String> msgSupplier)");

        // log(Logger logger, Level level, Throwable error, String format, Object... args)
        LogUtil.log(logger, randomLevel(), error, "log(Logger logger, Level level, Throwable error, String format, Object... args)");

        // log(Logger logger, Level level, Throwable error, Supplier<String> msgSupplier)
        LogUtil.log(logger, randomLevel(), error, () -> "log(Logger logger, Level level, Throwable error, Supplier<String> msgSupplier)");

        // log(Logger logger, Level level, Throwable error)
        LogUtil.log(logger, randomLevel(), error);

        // log(Logger logger, Level level, String format, Object... args)
        LogUtil.log(logger, randomLevel(), "log(Logger logger, Level level, String format, Object... args)");

        // log(Logger logger, Level level, Supplier<String> msgSupplier)
        LogUtil.log(logger, randomLevel(), () -> "log(Logger logger, Level level, Supplier<String> msgSupplier)");
    }

    @Test
    void trace() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Exception error = new Exception("按需抛出异常-trace-1");

        LogUtil.trace(logger, marker, "logger = {}, marker = {}", logger, marker);
        LogUtil.trace(logger, marker, error, "logger = {}, marker = {}, error = {}", logger, marker, error);
        LogUtil.trace(logger, marker, () -> StringFormatter.format("logger = {}, marker = {}", logger, marker));
        LogUtil.trace(logger, marker, error, () -> StringFormatter.format("logger = {}, marker = {}, error = {}", logger, marker, error));
        LogUtil.trace(logger, marker, error);

        LogUtil.trace(logger, "logger = {} = {}", logger);
        LogUtil.trace(logger, error, "logger = {} = {}, error = {}", logger, error);
        LogUtil.trace(logger, () -> StringFormatter.format("logger = {} = {}", logger));
        LogUtil.trace(logger, error, () -> StringFormatter.format("logger = {} = {}, error = {}", logger, error));
        LogUtil.trace(logger, error);

        Logger contextLogger = LoggerFactory.getLogger("context-logger-" + System.currentTimeMillis());
        Marker contextMarker = MarkerFactory.getMarker("context-marker-" + System.currentTimeMillis());
        Level contextLevel = Level.DEBUG;
        LogContext.Config config = new LogContext.Config(contextLogger, contextMarker, contextLevel);
        try (LogContext.TempHolder tempHolder = LogContext.with(config)) {
            final String format = "logger = {}, marker = {}, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.trace(logger, marker, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.trace(logger, marker, error, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.trace(logger, marker, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.trace(logger, marker, error, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.trace(logger, marker, error);

            final String format2 = "logger = {}, marker = null, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.trace(logger, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.trace(logger, error, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.trace(logger, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.trace(logger, error, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.trace(logger, error);
        }
    }


    @Test
    void debug() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Exception error = new Exception("按需抛出异常-debug-1");

        LogUtil.debug(logger, marker, "logger = {}, marker = {}", logger, marker);
        LogUtil.debug(logger, marker, error, "logger = {}, marker = {}, error = {}", logger, marker, error);
        LogUtil.debug(logger, marker, () -> StringFormatter.format("logger = {}, marker = {}", logger, marker));
        LogUtil.debug(logger, marker, error, () -> StringFormatter.format("logger = {}, marker = {}, error = {}", logger, marker, error));
        LogUtil.debug(logger, marker, error);

        LogUtil.debug(logger, "logger = {} = {}", logger);
        LogUtil.debug(logger, error, "logger = {} = {}, error = {}", logger, error);
        LogUtil.debug(logger, () -> StringFormatter.format("logger = {} = {}", logger));
        LogUtil.debug(logger, error, () -> StringFormatter.format("logger = {} = {}, error = {}", logger, error));
        LogUtil.debug(logger, error);

        Logger contextLogger = LoggerFactory.getLogger("context-logger-" + System.currentTimeMillis());
        Marker contextMarker = MarkerFactory.getMarker("context-marker-" + System.currentTimeMillis());
        Level contextLevel = Level.INFO;
        LogContext.Config config = new LogContext.Config(contextLogger, contextMarker, contextLevel);
        try (LogContext.TempHolder tempHolder = LogContext.with(config)) {
            final String format = "logger = {}, marker = {}, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.debug(logger, marker, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.debug(logger, marker, error, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.debug(logger, marker, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.debug(logger, marker, error, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.debug(logger, marker, error);

            final String format2 = "logger = {}, marker = null, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.debug(logger, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.debug(logger, error, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.debug(logger, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.debug(logger, error, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.debug(logger, error);
        }
    }

    @Test
    void info() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Exception error = new Exception("按需抛出异常-info-1");

        LogUtil.info(logger, marker, "logger = {}, marker = {}", logger, marker);
        LogUtil.info(logger, marker, error, "logger = {}, marker = {}, error = {}", logger, marker, error);
        LogUtil.info(logger, marker, () -> StringFormatter.format("logger = {}, marker = {}", logger, marker));
        LogUtil.info(logger, marker, error, () -> StringFormatter.format("logger = {}, marker = {}, error = {}", logger, marker, error));
        LogUtil.info(logger, marker, error);

        LogUtil.info(logger, "logger = {} = {}", logger);
        LogUtil.info(logger, error, "logger = {} = {}, error = {}", logger, error);
        LogUtil.info(logger, () -> StringFormatter.format("logger = {} = {}", logger));
        LogUtil.info(logger, error, () -> StringFormatter.format("logger = {} = {}, error = {}", logger, error));
        LogUtil.info(logger, error);

        Logger contextLogger = LoggerFactory.getLogger("context-logger-" + System.currentTimeMillis());
        Marker contextMarker = MarkerFactory.getMarker("context-marker-" + System.currentTimeMillis());
        Level contextLevel = Level.WARN;
        LogContext.Config config = new LogContext.Config(contextLogger, contextMarker, contextLevel);
        try (LogContext.TempHolder tempHolder = LogContext.with(config)) {
            final String format = "logger = {}, marker = {}, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.info(logger, marker, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.info(logger, marker, error, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.info(logger, marker, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.info(logger, marker, error, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.info(logger, marker, error);

            final String format2 = "logger = {}, marker = null, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.info(logger, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.info(logger, error, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.info(logger, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.info(logger, error, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.info(logger, error);
        }
    }

    @Test
    void warn() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Exception error = new Exception("按需抛出异常-warn-1");

        LogUtil.warn(logger, marker, "logger = {}, marker = {}", logger, marker);
        LogUtil.warn(logger, marker, error, "logger = {}, marker = {}, error = {}", logger, marker, error);
        LogUtil.warn(logger, marker, () -> StringFormatter.format("logger = {}, marker = {}", logger, marker));
        LogUtil.warn(logger, marker, error, () -> StringFormatter.format("logger = {}, marker = {}, error = {}", logger, marker, error));
        LogUtil.warn(logger, marker, error);

        LogUtil.warn(logger, "logger = {} = {}", logger);
        LogUtil.warn(logger, error, "logger = {} = {}, error = {}", logger, error);
        LogUtil.warn(logger, () -> StringFormatter.format("logger = {} = {}", logger));
        LogUtil.warn(logger, error, () -> StringFormatter.format("logger = {} = {}, error = {}", logger, error));
        LogUtil.warn(logger, error);

        Logger contextLogger = LoggerFactory.getLogger("context-logger-" + System.currentTimeMillis());
        Marker contextMarker = MarkerFactory.getMarker("context-marker-" + System.currentTimeMillis());
        Level contextLevel = Level.ERROR;
        LogContext.Config config = new LogContext.Config(contextLogger, contextMarker, contextLevel);
        try (LogContext.TempHolder tempHolder = LogContext.with(config)) {
            final String format = "logger = {}, marker = {}, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.warn(logger, marker, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.warn(logger, marker, error, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.warn(logger, marker, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.warn(logger, marker, error, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.warn(logger, marker, error);

            final String format2 = "logger = {}, marker = null, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.warn(logger, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.warn(logger, error, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.warn(logger, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.warn(logger, error, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.warn(logger, error);
        }
    }

    @Test
    void error() {
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        Exception error = new Exception("按需抛出异常-error-1");

        LogUtil.error(logger, marker, "logger = {}, marker = {}", logger, marker);
        LogUtil.error(logger, marker, error, "logger = {}, marker = {}, error = {}", logger, marker, error);
        LogUtil.error(logger, marker, () -> StringFormatter.format("logger = {}, marker = {}", logger, marker));
        LogUtil.error(logger, marker, error, () -> StringFormatter.format("logger = {}, marker = {}, error = {}", logger, marker, error));
        LogUtil.error(logger, marker, error);

        LogUtil.error(logger, "logger = {} = {}", logger);
        LogUtil.error(logger, error, "logger = {} = {}, error = {}", logger, error);
        LogUtil.error(logger, () -> StringFormatter.format("logger = {} = {}", logger));
        LogUtil.error(logger, error, () -> StringFormatter.format("logger = {} = {}, error = {}", logger, error));
        LogUtil.error(logger, error);

        Logger contextLogger = LoggerFactory.getLogger("context-logger-" + System.currentTimeMillis());
        Marker contextMarker = MarkerFactory.getMarker("context-marker-" + System.currentTimeMillis());
        Level contextLevel = Level.TRACE;
        LogContext.Config config = new LogContext.Config(contextLogger, contextMarker, contextLevel);
        try (LogContext.TempHolder tempHolder = LogContext.with(config)) {
            final String format = "logger = {}, marker = {}, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.error(logger, marker, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.error(logger, marker, error, format, logger, marker, contextLogger, contextMarker, contextLevel);
            LogUtil.error(logger, marker, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.error(logger, marker, error, () -> StringFormatter.format(format, logger, marker, contextLogger, contextMarker, contextLevel));
            LogUtil.error(logger, marker, error);

            final String format2 = "logger = {}, marker = null, context-logger = {}, context-marker = {}, context-level = {}";
            LogUtil.error(logger, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.error(logger, error, format2, logger, contextLogger, contextMarker, contextLevel);
            LogUtil.error(logger, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.error(logger, error, () -> StringFormatter.format(format2, logger, contextLogger, contextMarker, contextLevel));
            LogUtil.error(logger, error);
        }
    }
}