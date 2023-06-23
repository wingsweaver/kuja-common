package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("unchecked")
class ErrorReturnValueCustomizerTest {
    @Test
    void test() {
        ApplicationContext applicationContext = new StaticApplicationContext();
        IncludeSettings includeSettings = new IncludeSettings(IncludeAttribute.ALWAYS);

        ErrorReturnValueCustomizer customizer = new ErrorReturnValueCustomizer();
        customizer.setSettings(includeSettings);
        customizer.afterPropertiesSet();

        {
            ReturnValue returnValue = new ReturnValue();
            customizer.customize(returnValue);
            assertNull(returnValue.getTags());
        }

        {
            BusinessContext businessContext = BusinessContext.create();
            BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
            Exception error = new Exception("some-error");
            accessor.setError(error);

            ReturnValue returnValue = new ReturnValue();
            customizer.customize(businessContext, returnValue);

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(1, tags.size());

            Map<String, Object> errorMap = (Map<String, Object>) tags.get("error");
            assertEquals("some-error", errorMap.get("message"));
            assertEquals(error.getClass().getName(), errorMap.get("class"));
        }

        {
            BusinessContext businessContext = BusinessContext.create();
            BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
            Exception error = new Exception("some-error");
            accessor.setError(error);

            ReturnValue returnValue = new ReturnValue();
            returnValue.getTags(true).put("error",
                    MapUtil.from("message", "some-other-error"));

            customizer.customize(businessContext, returnValue);

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(1, tags.size());

            Map<String, Object> errorMap = (Map<String, Object>) tags.get("error");
            assertEquals("some-other-error", errorMap.get("message"));
            assertEquals(error.getClass().getName(), errorMap.get("class"));
        }
    }
}