package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractOneTimeReturnValueCustomizerTest {
    @Test
    void test() {
        ReturnValue returnValue = new ReturnValue();
        CustomOneTimeReturnValueCustomizer returnValueCustomizer = new CustomOneTimeReturnValueCustomizer(1234);
        int count = 100;
        for (int i = 0; i < count; i++) {
            returnValueCustomizer.customize(returnValue);
        }
        assertEquals(1234, returnValue.getTemps(false).get("custom-value"));
    }

    static class CustomOneTimeReturnValueCustomizer extends AbstractOneTimeReturnValueCustomizer {
        private final int startValue;

        CustomOneTimeReturnValueCustomizer(int startValue) {
            this.startValue = startValue;
        }

        @Override
        protected void customizeInternal(BusinessContext businessContext, ReturnValue returnValue) {
            returnValue.getTemps(true).compute("custom-value", (k, v) -> {
                return v != null ? ((int) v + 1) : startValue;
            });
        }
    }
}