package com.wingsweaver.kuja.common.utils.logging.slf4j;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.slf4j.event.Level;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LogSectionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogSectionTest.class);

    @Test
    void test() {
        LogSection.Builder builder = LogSection.builder(LOGGER);
        LogSection logSection = builder.build();
        logSection.enter();
        try {
            assertNotNull(logSection.getSectionNameSupplier());
            assertNotNull(logSection.getEnterMessageSupplier());
            assertNotNull(logSection.getLeaveMessageSupplier());
            assertNotNull(logSection.getFailMessageSupplier());

            logSection.log("test, do some operations");
            logSection.log(() -> "hello from lambda");
            logSection.log(Level.INFO, "test, do some operations");
            logSection.log(Level.ERROR, () -> "hello from lambda");

            Marker marker = MarkerFactory.getMarker("test-marker");
            logSection.log(marker, "test, do some operations");
            logSection.log(marker, () -> "hello from lambda");
            logSection.log(marker, Level.INFO, "test, do some operations");
            logSection.log(marker, Level.ERROR, () -> "hello from lambda");
        } finally {
            logSection.leave();
        }
    }

    @Test
    void test2() {
        LogSection.Builder builder = LogSection.builder(LOGGER)
                .marker(MarkerFactory.getMarker("test-marker"))
                .level(Level.INFO)
                .sectionName("test-section-2");
        {
            LogSection logSection = builder.build();
            logSection.enter();
            try {
                LogUtil.debug(LOGGER, "test2, do some operations & throw exception");
                throw new Exception("按需抛出异常, test2");
            } catch (Throwable e) {
                logSection.fail(e);
            } finally {
                logSection.leave();
            }
        }

        {
            LogSection logSection2 = builder.sectionName("test-section-2-2").build();
            logSection2.enter();
            try {
                try {
                    LogUtil.debug(LOGGER, "test2-2, do some operations & throw exception");
                    throw new Exception("按需抛出异常, test2-2");
                } catch (Throwable e) {
                    logSection2.rethrow(e);
                } finally {
                    logSection2.leave();
                }
            } catch (Throwable t) {
                // 忽略此错误
            }
        }

        {
            LogSection logSection3 = builder.sectionName("test-section-2-3").build();
            logSection3.enter();
            try {
                try {
                    LogUtil.debug(LOGGER, "test2-3, do some operations & throw exception");
                    throw new Exception("按需抛出异常, test2-3");
                } catch (Throwable e) {
                    logSection3.rethrow(e, IllegalStateException::new);
                } finally {
                    logSection3.leave();
                }
            } catch (Throwable t) {
                // 忽略此错误
            }
        }

        {
            LogSection logSection4 = builder.sectionName("test-section-2-4").build();
            logSection4.enter();
            try {
                try {
                    LogUtil.debug(LOGGER, "test2-4, do some operations & throw exception");
                    throw new Exception("按需抛出异常, test2-4");
                } catch (Throwable e) {
                    logSection4.rethrowSneaky(e);
                } finally {
                    logSection4.leave();
                }
            } catch (Throwable t) {
                // 忽略此错误
            }
        }

        {
            LogSection logSection4 = builder.sectionName("test-section-2-5").build();
            logSection4.enter();
            try {
                try {
                    LogUtil.debug(LOGGER, "test2-5, do some operations & throw exception");
                    throw new RuntimeException("按需抛出异常, test2-5");
                } catch (Throwable e) {
                    logSection4.rethrowSneaky(e);
                } finally {
                    logSection4.leave();
                }
            } catch (Throwable t) {
                // 忽略此错误
            }
        }
    }

    @Test
    void test3() {
        LogSection.Builder builder = LogSection.builder(LOGGER)
                .marker(MarkerFactory.getMarker("test-marker"))
                .level(Level.INFO)
                .sectionName("test-section-3");
        String ret = null;
        LogSection logSection = builder.build();
        logSection.enter();
        try {
            LogUtil.debug(LOGGER, "test3, return some value");
            ret = "test3-ret_" + UUID.randomUUID();
        } catch (Throwable e) {
            logSection.fail(e);
        } finally {
            logSection.leave(ret);
        }
    }
}