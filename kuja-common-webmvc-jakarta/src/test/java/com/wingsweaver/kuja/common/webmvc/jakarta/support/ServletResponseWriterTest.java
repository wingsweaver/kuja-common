package com.wingsweaver.kuja.common.webmvc.jakarta.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wingsweaver.kuja.common.boot.returnvalue.ReturnValue;
import com.wingsweaver.kuja.common.webmvc.jakarta.EnableKujaCommonWebMvcJakarta;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
@ContextConfiguration(classes = ServletResponseWriterTest.class)
@EnableKujaCommonWebMvcJakarta
@Import(ServletResponseWriterTest.TestConfiguration.class)
class ServletResponseWriterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServletResponseWriter servletResponseWriter;

    @Autowired
    private TestFilter testFilter;

    @Test
    void test() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/");
        this.testFilter.setUseReturnValue(true);
        this.testFilter.setReturnValue(null);

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(StringUtils.isEmpty(content));
    }

    @Test
    void test2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/");

        ReturnValue returnValue = new ReturnValue();
        returnValue.setCode("error.code.1");
        returnValue.setMessage("error.message.1");
        returnValue.setUserTip("error.userTip.1");
        this.testFilter.setUseReturnValue(true);
        this.testFilter.setReturnValue(ResponseEntity.ok().body(returnValue));
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        ReturnValue returnValue2 = objectMapper.readValue(content, ReturnValue.class);
        assertEquals(returnValue.getCode(), returnValue2.getCode());
        assertEquals(returnValue.getMessage(), returnValue2.getMessage());
        assertEquals(returnValue.getUserTip(), returnValue2.getUserTip());
    }

    @Test
    void test3() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/");

        this.testFilter.setUseReturnValue(true);
        this.testFilter.setReturnValue(Locale.getDefault());

        Exception error = null;
        try {
            this.mockMvc.perform(requestBuilder).andReturn();
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof IllegalArgumentException);
    }

    static final class TestConfiguration {
        @Bean
        public TestFilter testFilter(ServletResponseWriter webResponseWriter) {
            TestFilter testFilter = new TestFilter();
            testFilter.setWebResponseWriter(webResponseWriter);
            return testFilter;
        }
    }

    @Getter
    @Setter
    static class TestFilter extends GenericFilterBean {
        private ServletResponseWriter webResponseWriter;

        private boolean useReturnValue;

        private Object returnValue;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            if (!this.useReturnValue) {
                chain.doFilter(request, response);
                return;
            }

            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            try {
                this.webResponseWriter.write(servletRequest, servletResponse, this.returnValue);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}