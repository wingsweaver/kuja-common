package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.errordefinition.DefaultErrorDefinition;
import com.wingsweaver.kuja.common.boot.errordefinition.ErrorDefinition;
import com.wingsweaver.kuja.common.boot.exception.BusinessException;
import com.wingsweaver.kuja.common.boot.exception.BusinessExceptionFactory;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueT;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@Import(KujaCommonBootConfigurationTest4.TestConfiguration.class)
@PropertySource("classpath:application-test4.properties")
@EnableKujaCommonBoot
class KujaCommonBootConfigurationTest4 {
    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private ReturnValueFactory returnValueFactory;

    @Autowired
    private BusinessExceptionFactory businessExceptionFactory;

    @Autowired
    private Environment environment;

    @Test
    void testReturnValueFactory() {

        {
            ReturnValue returnValue = returnValueFactory.success();
            assertNull(returnValue.getCode());
            assertNull(returnValue.getMessage());
            assertNull(returnValue.getUserTip());
            assertNull(returnValue.getTags());
            assertNull(returnValue.getTemps(false));
        }

        {
            Object data = new Object();
            ReturnValueT<?> returnValue = returnValueFactory.success(data);
            assertSame(data, returnValue.getData());
            assertNull(returnValue.getCode());
            assertNull(returnValue.getMessage());
            assertNull(returnValue.getUserTip());
            assertNull(returnValue.getTags());
            assertNull(returnValue.getTemps(false));
        }

        {
            ReturnValue returnValue = returnValueFactory.fail();
            assertNull(returnValue.getCode());
            assertNull(returnValue.getMessage());
            assertNull(returnValue.getUserTip());
            assertNull(returnValue.getTags());
            assertNull(returnValue.getTemps(false));
        }

        {
            Exception error = new Exception("some-error");
            ReturnValue returnValue = returnValueFactory.fail(error);
            assertNull(returnValue.getCode());
            assertEquals("some-error", returnValue.getMessage());
            assertEquals("some-error", returnValue.getUserTip());
            assertNull(returnValue.getTags());
            assertNull(returnValue.getTemps(false));
        }

        {
            String textHttp401 = messageHelper.getMessage("http-401").get();
            String textHttp403 = messageHelper.getMessage("http-403").get();

            ErrorDefinition errorDefinition = DefaultErrorDefinition.builder()
                    .code("error.code.1")
                    .message("{http-401}")
                    .userTip("{http-403}")
                    .tags(MapUtil.from("traceId", "trace-12345678"))
                    .temps(MapUtil.from("status", 1357))
                    .build();
            BusinessException error = this.businessExceptionFactory.create(errorDefinition);
            ReturnValue returnValue = returnValueFactory.fail(error);

            assertEquals("error.code.1", returnValue.getCode());
            assertEquals(textHttp401, returnValue.getMessage());
            assertEquals(textHttp403, returnValue.getUserTip());

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(1, tags.size());
            assertEquals("trace-12345678", tags.get("traceId"));

            Map<String, Object> temps = returnValue.getTemps(false);
            assertEquals(1, temps.size());
            assertEquals(1357, temps.get("status"));
        }
    }

    @Configuration
    public static class TestConfiguration {
    }
}