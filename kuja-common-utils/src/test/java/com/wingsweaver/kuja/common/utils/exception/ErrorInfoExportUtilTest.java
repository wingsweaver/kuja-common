package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorInfoExportUtilTest {

    @Test
    void export() {
        assertSame(Collections.emptyMap(), ErrorInfoExportUtil.export(null, null));

        String message = "test exception 1 - " + UUID.randomUUID();
        Exception exception = new Exception(message);
        assertSame(Collections.emptyMap(), ErrorInfoExportUtil.export(exception, null));
        assertTrue(ErrorInfoExportUtil.export(exception, ErrorInfoExportUtilTest::allowNone).isEmpty());
        assertFalse(ErrorInfoExportUtil.export(exception, ErrorInfoExportUtilTest::allowAll).isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void export2() {
        String message = "test exception 2 - " + UUID.randomUUID();
        ExtendedRuntimeException exception = new ExtendedRuntimeException(message, new UnsupportedOperationException());

        {
            Map<String, Object> map = ErrorInfoExportUtil.export(exception, ErrorInfoExportUtilTest::allowAll);
            assertTrue(map.containsKey("cause"));
            assertTrue(map.containsKey(ErrorInfoExportUtil.KEY_CLASS));
            assertTrue(map.containsKey(ErrorInfoExportUtil.KEY_EXTEND));
            assertTrue(map.containsKey("dummy"));
            String dummyValue = map.get("dummy").toString();
            assertTrue(dummyValue.startsWith(DummyErrorInfoExporterFactory.PREFIX));
        }

        {
            exception.withExtendedAttribute("thread", Thread.currentThread());
            exception.withExtendedAttribute("timestamp", new Date());
            exception.withExtendedAttribute("uuid", UUID.randomUUID());
            Map<String, Object> map = ErrorInfoExportUtil.export(exception, ErrorInfoExportUtilTest::allowAll);
            assertTrue(map.containsKey(ErrorInfoExportUtil.KEY_EXTEND));
            Map<String, Object> extendMap = (Map<String, Object>) map.get(ErrorInfoExportUtil.KEY_EXTEND);
            assertEquals(4, extendMap.size());
            assertTrue(extendMap.containsKey("thread"));
            assertTrue(extendMap.containsKey("timestamp"));
            assertTrue(extendMap.containsKey("uuid"));
        }
    }

    @Test
    void test3() {
        CustomException exception = new CustomException();

        {
            Map<String, Object> map = ErrorInfoExportUtil.export(exception, ErrorInfoExportUtilTest::allowAll);
            assertTrue(map.containsKey("thread"));
            assertTrue(map.containsKey("timestamp"));
            assertTrue(map.containsKey("uuid"));
            assertTrue(map.containsKey("monday"));
            // void 类型的不返回
            assertFalse(map.containsKey("customId"));
            // protected 方法不返回
            assertFalse(map.containsKey("protectedId"));
            // 发生错误的不返回
            assertFalse(map.containsKey("failed"));
            // 首字母小写的不返回
            assertFalse(map.containsKey("lowercased"));
            assertFalse(map.containsKey("_thread"));
            assertFalse(map.containsKey("_timestamp"));
            assertFalse(map.containsKey("_uuid"));
            assertFalse(map.containsKey("_calendar"));
        }

        {
            exception.addSuppressed(new UnsupportedOperationException());
            Map<String, Object> map = ErrorInfoExportUtil.export(exception, ErrorInfoExportUtilTest::allowAll);
            assertTrue(map.containsKey("suppressed"));
        }

        {
            ErrorExportPredicate predicate = allBut(MapUtil.from("timestamp", false, "monday", false));
            Map<String, Object> map = ErrorInfoExportUtil.export(exception, predicate);
            assertTrue(map.containsKey("thread"));
            assertFalse(map.containsKey("timestamp"));
            assertTrue(map.containsKey("uuid"));
            assertFalse(map.containsKey("monday"));
        }
    }

    static boolean allowAll(String key) {
        return true;
    }

    static boolean allowNone(String key) {
        return false;
    }

    static ErrorExportPredicate allBut(Map<String, Boolean> map) {
        return key -> map.getOrDefault(key, true);
    }

    static class CustomException extends Exception {
        private final Thread _thread = Thread.currentThread();

        private final Date _timestamp = new Date();

        private final UUID _uuid = UUID.randomUUID();

        private final Calendar _calendar = Calendar.getInstance();

        public CustomException() {
            this._calendar.setTime(this._timestamp);
        }

        public Thread getThread() {
            return this._thread;
        }

        public Date getTimestamp() {
            return this._timestamp;
        }

        public UUID getUuid() {
            return this._uuid;
        }

        public boolean isMonday() {
            return this._calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
        }

        public void getCustomId() {
        }

        protected String getProtectedId() {
            return "protected";
        }

        public String getlowercased() {
            return "lowercased";
        }

        public boolean isFailed() {
            throw new RuntimeException("failed");
        }
    }
}