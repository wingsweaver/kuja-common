package com.wingsweaver.kuja.common.webmvc.jakarta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValueT;
import com.wingsweaver.kuja.common.web.controller.AbstractController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
@ContextConfiguration(classes = KujaCommonWebMvcJakartaConfigurationTest.class)
@EnableKujaCommonWebMvcJakarta
@Import(KujaCommonWebMvcJakartaConfigurationTest.TestConfiguration.class)
class KujaCommonWebMvcJakartaConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void test() {
        assertNotNull(this.applicationContext.getBean("kujaJakartaServletRequestListener"));
        assertNotNull(this.applicationContext.getBean("kujaJakartaBusinessContextFilter"));
        assertNotNull(this.applicationContext.getBean("kujaJakartaDynamicLogContextFilter"));
        assertNotNull(this.applicationContext.getBean("kujaJakartaWebMvcConfigurer"));
        assertNotNull(this.applicationContext.getBean("kujaJakartaGlobalErrorController"));
        assertNotNull(this.applicationContext.getBean("kujaJakartaServletResponseWriter"));
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testController() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // api/test/1
        this.mockMvc.perform(get("/api/test/1"))
                .andExpect(result -> {
                    assertEquals(200, result.getResponse().getStatus());
                    String content = result.getResponse().getContentAsString();
                    ReturnValue returnValue = objectMapper.readValue(content, ReturnValue.class);
                    assertEquals("0", returnValue.getCode());
                });

        // api/test/2
        TypeReference<ReturnValueT<String>> typeReference = new TypeReference<ReturnValueT<String>>() {
        };
        this.mockMvc.perform(get("/api/test/2"))
                .andExpect(result -> {
                    assertEquals(200, result.getResponse().getStatus());
                    String content = result.getResponse().getContentAsString();
                    ReturnValueT<String> returnValue = objectMapper.readValue(content, typeReference);
                    assertEquals("0", returnValue.getCode());
                    assertEquals("2", returnValue.getData());
                });
    }

    @Configuration
    static class TestConfiguration {
        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    @RestController
    @RequestMapping("/api/test")
    static class TestController extends AbstractController {
        @RequestMapping("/1")
        public ReturnValue test1() {
            return this.success();
        }

        @RequestMapping("/2")
        public ReturnValue test2() {
            return this.success("2");
        }
    }
}