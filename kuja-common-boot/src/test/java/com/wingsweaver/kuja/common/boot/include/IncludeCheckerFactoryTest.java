package com.wingsweaver.kuja.common.boot.include;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncludeCheckerFactoryTest {
    @Test
    void test() {
        IncludeSettings settings = new IncludeSettings();
        IncludeCheckerFactory checkerFactory = new IncludeCheckerFactory(settings);
        BusinessContext businessContext = BusinessContext.create();
        IncludeChecker checker = checkerFactory.build(businessContext, null);
        assertFalse(checker.includes(null));
        assertFalse(checker.includes(""));
        assertFalse(checker.includes("error"));
    }

    @Test
    void test2() {
        IncludeSettings settings = new IncludeSettings(IncludeAttribute.ALWAYS);
        IncludeCheckerFactory checkerFactory = new IncludeCheckerFactory(settings);
        BusinessContext businessContext = BusinessContext.create();
        IncludeChecker checker = checkerFactory.build(businessContext, null);
        assertFalse(checker.includes(null));
        assertFalse(checker.includes(""));
        assertTrue(checker.includes("error"));
    }

    @Test
    void test3() {
        IncludeSettings settings = new IncludeSettings(IncludeAttribute.ALWAYS,
                new IncludeSettings.IncludeItem(""),
                new IncludeSettings.IncludeItem("trace", IncludeAttribute.ALWAYS),
                new IncludeSettings.IncludeItem("error(.)*", IncludeAttribute.ON_ERROR),
                new IncludeSettings.IncludeItem("host"),
                new IncludeSettings.IncludeItem("service(.)*", IncludeAttribute.ON_ATTRIBUTE));
        IncludeCheckerFactory checkerFactory = new IncludeCheckerFactory(settings);

        BusinessContext businessContext = BusinessContext.create();
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
        accessor.setAttribute("checker.host", true);
        accessor.setAttribute("service", true);
        accessor.setAttribute("service.name", false);

        IncludeChecker checker = checkerFactory.build(businessContext, "checker");
        assertFalse(checker.includes(null));
        assertFalse(checker.includes(""));

        {
            assertTrue(checker.includes("trace"));
            assertTrue(checker.includes("trace.traceId"));
            assertTrue(checker.includes("trace.spanId.pid"));
        }

        {
            assertFalse(checker.includes("error"));
            assertFalse(checker.includes("error.message"));
            assertFalse(checker.includes("error.stackTrace.file"));

            Exception error = new Exception("some-error");
            accessor.setError(error);

            assertTrue(checker.includes("error"));
            assertTrue(checker.includes("error.message"));
            assertTrue(checker.includes("error.stackTrace.file"));
        }

        {
            assertFalse(checker.includes("host"));
            assertTrue(checker.includes("host.name"));
            assertTrue(checker.includes("host.os.windows"));
        }

        {
            assertFalse(checker.includes("service"));
            assertFalse(checker.includes("service.name"));
            assertFalse(checker.includes("service.git.commit-id"));

            accessor.setAttribute("checker.service", false);
            accessor.setAttribute("checker.service.name", true);

            assertFalse(checker.includes("service"));
            assertTrue(checker.includes("service.name"));
            assertFalse(checker.includes("service.git.commit-id"));
        }
    }
}