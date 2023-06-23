package com.wingsweaver.kuja.common.utils.logging.slf4j;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogContextTest {

    @Test
    void logger() {
        String loggerName = "logger-" + System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger(loggerName);

        assertNull(LogContext.getLogger());

        LogContext.setLogger(logger);
        assertEquals(logger, LogContext.getLogger());
        LogContext.setLogger(null);
        assertNull(LogContext.getLogger());

        LogContext.setLogger(logger);
        assertEquals(logger, LogContext.getLogger());
        LogContext.removeLogger();
        assertNull(LogContext.getLogger());
    }

    @Test
    void marker() {
        // getMarker, setMarker, removeMarker 的测试用例
        String markerName = "marker-" + System.currentTimeMillis();
        Marker marker = MarkerFactory.getMarker(markerName);

        assertNull(LogContext.getMarker());

        LogContext.setMarker(marker);
        assertEquals(marker, LogContext.getMarker());
        LogContext.setMarker(null);
        assertNull(LogContext.getMarker());

        LogContext.setMarker(marker);
        assertEquals(marker, LogContext.getMarker());
        LogContext.removeMarker();
        assertNull(LogContext.getMarker());
    }

    @Test
    void level() {
        Level[] levels = Level.values();
        Level level = levels[ThreadLocalRandom.current().nextInt(1000) % levels.length];

        assertNull(LogContext.getLevel());

        LogContext.setLevel(level);
        assertEquals(level, LogContext.getLevel());
        LogContext.setLevel(null);
        assertNull(LogContext.getLevel());

        LogContext.setLevel(level);
        assertEquals(level, LogContext.getLevel());
        LogContext.removeLevel();
        assertNull(LogContext.getLevel());
    }

    @Test
    void with() {
        assertNull(LogContext.getLogger());
        assertNull(LogContext.getMarker());
        assertNull(LogContext.getLevel());

        Level[] levels = Level.values();
        Logger logger = LoggerFactory.getLogger("logger-" + System.currentTimeMillis());
        Marker marker = MarkerFactory.getMarker("marker-" + System.currentTimeMillis());
        int levelIndex = ThreadLocalRandom.current().nextInt(1000);
        Level level = levels[levelIndex % levels.length];
        LogContext.Config config = new LogContext.Config(logger, marker, level);
        try (LogContext.TempHolder tempHolder = LogContext.with(config)) {
            // 确认当前上下文跟 TempHolder 的配置一致
            assertNull(tempHolder.getOldConfig());
            assertEquals(config, tempHolder.getConfig());
            assertEquals(logger, LogContext.getLogger());
            assertEquals(marker, LogContext.getMarker());
            assertEquals(level, LogContext.getLevel());

            Logger logger2 = LoggerFactory.getLogger("logger2-" + System.currentTimeMillis());
            Marker marker2 = MarkerFactory.getMarker("marker2-" + System.currentTimeMillis());
            Level level2 = levels[(levelIndex + 1) % levels.length];
            LogContext.Config config2 = new LogContext.Config(logger2, marker2, level2);
            try (LogContext.TempHolder tempHolder2 = LogContext.with(config2)) {
                // 确认当前上下文跟 TempHolder2 的配置一致
                assertEquals(config, tempHolder2.getOldConfig());
                assertEquals(config2, tempHolder2.getConfig());
                assertEquals(logger2, LogContext.getLogger());
                assertEquals(marker2, LogContext.getMarker());
                assertEquals(level2, LogContext.getLevel());
            }

            // 确认当前上下文跟 TempHolder 的配置一致
            assertNull(tempHolder.getOldConfig());
            assertEquals(config, tempHolder.getConfig());
            assertEquals(logger, LogContext.getLogger());
            assertEquals(marker, LogContext.getMarker());
            assertEquals(level, LogContext.getLevel());
        }

        // 确认当前上下文跟默认值一致
        assertNull(LogContext.getLogger());
        assertNull(LogContext.getMarker());
        assertNull(LogContext.getLevel());
    }
}