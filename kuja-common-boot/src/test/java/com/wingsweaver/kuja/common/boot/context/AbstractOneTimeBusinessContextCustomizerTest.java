package com.wingsweaver.kuja.common.boot.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AbstractOneTimeBusinessContextCustomizerTest {
    @Test
    void test() {
        BusinessContext context = new MapBusinessContext();
        assertFalse(context.hasAttribute("sum"));

        int count = 10;
        CustomOneTimeBusinessContextCustomizer customizer = new CustomOneTimeBusinessContextCustomizer();
        for (int i = 0; i < count; i++) {
            customizer.customize(context);
        }

        assertEquals(1, (int) context.getAttribute("sum"));
    }

    static class CustomOneTimeBusinessContextCustomizer extends AbstractOneTimeBusinessContextCustomizer {
        @Override
        protected void customizeInternal(BusinessContext businessContext) {
            businessContext.<Integer>updateAttribute("sum", (k, v) -> {
                if (v == null) {
                    v = 1;
                } else {
                    v = v + 1;
                }
                return v;
            });
        }
    }
}