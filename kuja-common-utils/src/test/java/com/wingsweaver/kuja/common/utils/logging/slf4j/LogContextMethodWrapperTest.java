package com.wingsweaver.kuja.common.utils.logging.slf4j;

import com.wingsweaver.kuja.common.utils.model.ValueWrapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LogContextMethodWrapperTest {
    @Test
    void runnable() {
        ValueWrapper<UUID> valueWrapper = new ValueWrapper<>();

        {
            UUID uuid = UUID.randomUUID();
            LogContextMethodWrapper.runnable(() -> valueWrapper.setValue(uuid)).run();
            assertSame(uuid, valueWrapper.getValue());
        }

        {
            UUID uuid = UUID.randomUUID();
            LogContextMethodWrapper.runnable(() -> valueWrapper.setValue(uuid), LogContext.getConfig()).run();
            assertSame(uuid, valueWrapper.getValue());
        }
    }

    @Test
    void throwableRunnable() {
        ValueWrapper<UUID> valueWrapper = new ValueWrapper<>();

        {
            UUID uuid = UUID.randomUUID();
            LogContextMethodWrapper.throwableRunnable(() -> valueWrapper.setValue(uuid)).run();
            assertSame(uuid, valueWrapper.getValue());
        }

        {
            UUID uuid = UUID.randomUUID();
            LogContextMethodWrapper.throwableRunnable(() -> valueWrapper.setValue(uuid), LogContext.getConfig()).run();
            assertSame(uuid, valueWrapper.getValue());
        }
    }

    @Test
    void callable() throws Exception {

        {
            UUID uuid = UUID.randomUUID();
            assertEquals(uuid, LogContextMethodWrapper.callable(() -> uuid).call());
        }

        {
            UUID uuid = UUID.randomUUID();
            assertEquals(uuid, LogContextMethodWrapper.callable(() -> uuid, LogContext.getConfig()).call());
        }
    }

    @Test
    void supplier() {
        {
            UUID uuid = UUID.randomUUID();
            assertEquals(uuid, LogContextMethodWrapper.supplier(() -> uuid).get());
        }

        {
            UUID uuid = UUID.randomUUID();
            assertEquals(uuid, LogContextMethodWrapper.supplier(() -> uuid, LogContext.getConfig()).get());
        }
    }
}