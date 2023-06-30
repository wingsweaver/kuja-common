package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.DefaultAppInfo;
import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
class AppInfoReturnValueCustomizerTest {
    @Test
    void test() throws Exception {
        ApplicationContext applicationContext = new StaticApplicationContext();
        IncludeSettings includeSettings = new IncludeSettings(IncludeAttribute.ALWAYS);
        AppInfo appInfo = new DefaultAppInfo();
        appInfo.setAttribute("host.name", "localhost");
        appInfo.setAttribute("pid", 1234);

        AppInfoReturnValueCustomizer customizer = new AppInfoReturnValueCustomizer();
        customizer.setApplicationContext(applicationContext);
        customizer.setSettings(includeSettings);
        customizer.setAppInfo(appInfo);

        customizer.afterPropertiesSet();

        ReturnValue returnValue = new ReturnValue();
        customizer.customize(returnValue);

        Map<String, Object> tags = returnValue.getTags();
        assertEquals(1, tags.size());

        Map<String, Object> appInfoMap = (Map<String, Object>) tags.get("appInfo");
        assertEquals(2, appInfoMap.size());
        assertEquals("localhost", appInfoMap.get("host.name"));
        assertEquals("1234", appInfoMap.get("pid"));
    }

    @Test
    void test2() throws Exception {
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        IncludeSettings includeSettings = new IncludeSettings(IncludeAttribute.ALWAYS);
        AppInfo appInfo = new DefaultAppInfo();
        appInfo.setAttribute("host.name", "localhost");
        appInfo.setAttribute("pid", 1234);
        AppInfoReturnValueCustomizer customizer = new AppInfoReturnValueCustomizer();
        customizer.setApplicationContext(applicationContext);
        customizer.setSettings(includeSettings);

        // 模拟没有 AppInfo 的错误
        assertThrows(IllegalStateException.class, customizer::afterPropertiesSet);

        // 模拟正常的含有 AppInfo 的情况
        DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();
        BeanDefinition beanDefinition = new RootBeanDefinition(AppInfo.class, () -> appInfo);
        beanFactory.registerBeanDefinition("appInfo", beanDefinition);

        customizer.afterPropertiesSet();

        ReturnValue returnValue = new ReturnValue();
        returnValue.getTags(true).put("appInfo",
                MapUtil.from("host.name", "unknown", "os", "windows"));
        customizer.customize(returnValue);

        Map<String, Object> tags = returnValue.getTags();
        assertEquals(1, tags.size());

        Map<String, Object> appInfoMap = (Map<String, Object>) tags.get("appInfo");
        assertEquals(3, appInfoMap.size());
        assertEquals("unknown", appInfoMap.get("host.name"));
        assertEquals("windows", appInfoMap.get("os"));
        assertEquals("1234", appInfoMap.get("pid"));
    }
}