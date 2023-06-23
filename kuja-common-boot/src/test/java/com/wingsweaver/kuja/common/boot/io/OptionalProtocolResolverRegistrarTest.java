package com.wingsweaver.kuja.common.boot.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class OptionalProtocolResolverRegistrarTest {
    @Test
    void test() throws IOException {
        try {
            List<String> applicationListeners = SpringFactoriesLoader.loadFactoryNames(ApplicationListener.class, null);
            System.out.println("count of ApplicationListener: " + applicationListeners.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SpringApplication application = new SpringApplication(OptionalProtocolResolverRegistrarTest.class);
        application.setBannerMode(Banner.Mode.OFF);
//        application.addListeners(new OptionalProtocolResolverRegistrar());
        ApplicationContext applicationContext = application.run();
        assertNotNull(applicationContext);

        Resource resource = applicationContext.getResource("optional:classpath:application-test4.properties");
        assertTrue(resource instanceof OptionalResource);

        Resource baseResource = applicationContext.getResource("classpath:application-test4.properties");
        OptionalResource optionalResource = (OptionalResource) resource;
        assertTrue(optionalResource.isOptional());
        assertTrue(optionalResource.toString().startsWith(OptionalResource.PREFIX_OPTIONAL));
        assertNotNull(optionalResource.getResource());

        assertEquals(baseResource.exists(), optionalResource.exists());
        assertEquals(baseResource.getDescription(), optionalResource.getDescription());
        assertEquals(baseResource.getFilename(), optionalResource.getFilename());
        assertEquals(baseResource.getFile(), optionalResource.getFile());
        assertEquals(baseResource.getURI(), optionalResource.getURI());
        assertEquals(baseResource.getURL(), optionalResource.getURL());
        assertEquals(baseResource.isOpen(), optionalResource.isOpen());
        assertEquals(baseResource.isReadable(), optionalResource.isReadable());
        assertEquals(baseResource.isFile(), optionalResource.isFile());
        assertEquals(baseResource.contentLength(), optionalResource.contentLength());
        assertEquals(baseResource.lastModified(), optionalResource.lastModified());
        assertNotNull(optionalResource.readableChannel());
        assertEquals(baseResource.toString(), optionalResource.getResource().toString());
        assertNotNull(optionalResource.getInputStream());

        assertNotNull(optionalResource.createRelative("./application-test2.properties"));
    }
}