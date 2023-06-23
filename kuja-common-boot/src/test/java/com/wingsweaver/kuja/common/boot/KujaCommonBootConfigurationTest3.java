package com.wingsweaver.kuja.common.boot;

import com.wingsweaver.kuja.common.boot.errordefinition.EnableKujaErrorDefinition;
import com.wingsweaver.kuja.common.boot.i18n.MessageHelper;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueFactory;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueT;
import com.wingsweaver.kuja.common.boot.warmup.WarmUpTriggerBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@SpringBootApplication
@EnableKujaErrorDefinition
@Import(KujaCommonBootConfigurationTest3.TestConfiguration.class)
@PropertySource("classpath:application-test3.properties")
@EnableKujaCommonBoot
class KujaCommonBootConfigurationTest3 {
    @Autowired
    private WarmUpTriggerBean warmUpTriggerBean;

    @Test
    void testWarmUpTriggerBean() {
        assertEquals(-1234, warmUpTriggerBean.getOrder());
        warmUpTriggerBean.stop();
    }

    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private ReturnValueFactory returnValueFactory;

    @Test
    void testReturnValueFactory() {
        String userTip401 = messageHelper.getMessage("http-401").get();

        {
            ReturnValue returnValue = returnValueFactory.success();
            assertEquals("13579", returnValue.getCode());
            assertEquals("done:)", returnValue.getMessage());
            assertEquals(userTip401, returnValue.getUserTip());

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(2, tags.size());
            assertEquals("trace-12345678", tags.get("traceId"));
            assertEquals("span-12345678", tags.get("spanId"));

            Map<String, Object> temps = returnValue.getTemps(false);
            assertEquals(1, temps.size());
            assertEquals("9876", temps.get("status"));
        }

        {
            Object data = new Object();
            ReturnValueT<?> returnValue = returnValueFactory.success(data);
            assertSame(data, returnValue.getData());
            assertEquals("13579", returnValue.getCode());
            assertEquals("done:)", returnValue.getMessage());
            assertEquals(userTip401, returnValue.getUserTip());

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(2, tags.size());
            assertEquals("trace-12345678", tags.get("traceId"));
            assertEquals("span-12345678", tags.get("spanId"));

            Map<String, Object> temps = returnValue.getTemps(false);
            assertEquals(1, temps.size());
            assertEquals("9876", temps.get("status"));
        }

        String userTip403 = messageHelper.getMessage("http-403").get();

        {
            ReturnValue returnValue = returnValueFactory.fail();
            assertEquals("24680", returnValue.getCode());
            assertEquals("failed!!!", returnValue.getMessage());
            assertEquals(userTip403, returnValue.getUserTip());

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(2, tags.size());
            assertEquals("trace-abcdefgh", tags.get("traceId"));
            assertEquals("span-abcdefgh", tags.get("spanId"));

            Map<String, Object> temps = returnValue.getTemps(false);
            assertEquals(1, temps.size());
            assertEquals("1234", temps.get("status"));
        }

        {
            Exception error = new Exception("some-error");
            ReturnValue returnValue = returnValueFactory.fail(error);
            assertEquals("24680", returnValue.getCode());
            assertEquals("failed!!!", returnValue.getMessage());
            assertEquals(userTip403, returnValue.getUserTip());

            Map<String, Object> tags = returnValue.getTags();
            assertEquals(2, tags.size());
            assertEquals("trace-abcdefgh", tags.get("traceId"));
            assertEquals("span-abcdefgh", tags.get("spanId"));

            Map<String, Object> temps = returnValue.getTemps(false);
            assertEquals(1, temps.size());
            assertEquals("1234", temps.get("status"));
        }
    }

    @Configuration
    public static class TestConfiguration {
        @Bean
        public Executor kujaBootWarmupAsyncExecutor() {
            return Executors.newFixedThreadPool(3);
        }
    }
}