package com.wingsweaver.kuja.common.boot.env;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.util.CollectionUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertySourceUtilTest {

    MutablePropertySources createPropertySources() {
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(new MapPropertySource("#1", MapUtil.from("host.name", "localhost")));
        propertySources.addLast(new MapPropertySource("#2", MapUtil.from("host.address", "127,0.0.1",
                "pid", 1234)));
        propertySources.addLast(new MapPropertySource("#3", MapUtil.from("host.name", "unknown",
                "os", "windows")));

        CompositePropertySource compositePropertySource = new CompositePropertySource("composite");
        propertySources.forEach(compositePropertySource::addPropertySource);
        propertySources.addFirst(compositePropertySource);

        return propertySources;
    }

    @Test
    void containsProperty() {
        PropertySources propertySources = createPropertySources();
        assertFalse(PropertySourceUtil.containsProperty(null, "name"));
        assertFalse(PropertySourceUtil.containsProperty(propertySources, null));
        assertTrue(PropertySourceUtil.containsProperty(propertySources, "host.name"));
        assertTrue(PropertySourceUtil.containsProperty(propertySources, "host.address"));
        assertFalse(PropertySourceUtil.containsProperty(propertySources, "host.port"));
    }

    @Test
    void isEmptyPropertySources() {
        assertTrue(PropertySourceUtil.isEmpty((PropertySources) null));
        assertTrue(PropertySourceUtil.isEmpty(new MutablePropertySources()));
        assertFalse(PropertySourceUtil.isEmpty(createPropertySources()));
    }

    @Test
    void isEmptyPropertySource() {
        assertTrue(PropertySourceUtil.isEmpty((PropertySource<?>) null));
        assertTrue(PropertySourceUtil.isEmpty(new MapPropertySource("empty", Collections.emptyMap())));
        assertFalse(PropertySourceUtil.isEmpty(new MapPropertySource("empty", MapUtil.from("id", null))));
        assertTrue(PropertySourceUtil.isEmpty(new CompositePropertySource("empty")));
    }

    @Test
    void getPropertyNames4PropertySources() {
        PropertySources propertySources = createPropertySources();
        List<String> keys = new LinkedList<>();

        PropertySourceUtil.getPropertyNames((PropertySources) null, null);
        PropertySourceUtil.getPropertyNames(propertySources, null);
        PropertySourceUtil.getPropertyNames((PropertySources) null, keys);

        PropertySourceUtil.getPropertyNames(propertySources, keys);
        keys.sort(String::compareTo);
        assertIterableEquals(CollectionUtil.listOf("host.address", "host.name", "os", "pid"), keys);
    }

    @Test
    void getPropertyNames4PropertySource() {
        PropertySourceUtil.getPropertyNames((PropertySource<?>) null, null);

        {
            PropertySource<?> mapPropertySource = new MapPropertySource("empty", Collections.emptyMap());
            PropertySourceUtil.getPropertyNames(mapPropertySource, null);
            List<String> keys = new LinkedList<>();
            PropertySourceUtil.getPropertyNames(mapPropertySource, keys);
            assertTrue(keys.isEmpty());
        }

        {
            PropertySource<?> mapPropertySource = new MapPropertySource("empty",
                    MapUtil.from("host.name", "localhost", "host.address", ""));
            List<String> keys = new LinkedList<>();
            PropertySourceUtil.getPropertyNames(mapPropertySource, keys);
            keys.sort(String::compareTo);
            assertIterableEquals(CollectionUtil.listOf("host.address", "host.name"), keys);
        }

        {
            CompositePropertySource compositePropertySource = new CompositePropertySource("empty");
            PropertySources propertySources = createPropertySources();
            propertySources.forEach(compositePropertySource::addPropertySource);
            List<String> keys = new LinkedList<>();
            PropertySourceUtil.getPropertyNames(compositePropertySource, keys);
            keys.sort(String::compareTo);
            assertIterableEquals(CollectionUtil.listOf("host.address", "host.name", "os", "pid"), keys);
        }
    }

    @Test
    void copyToPropertySources() {
        MutablePropertySources propertySources = createPropertySources();

        {
            Map<String, Object> map = new HashMap<>(BufferSizes.SMALL);
            PropertySourceUtil.copyTo(propertySources, map, true);
            assertEquals(4, map.size());
            assertEquals("localhost", map.get("host.name"));
            assertEquals("127,0.0.1", map.get("host.address"));
            assertEquals(1234, map.get("pid"));
            assertEquals("windows", map.get("os"));
        }

        {
            Map<String, Object> map = MapUtil.from("host.name", "some-host", "pid", 9876, "color", "red");
            PropertySourceUtil.copyTo(propertySources, map, false);
            assertEquals(5, map.size());
            assertEquals("some-host", map.get("host.name"));
            assertEquals("127,0.0.1", map.get("host.address"));
            assertEquals(9876, map.get("pid"));
            assertEquals("windows", map.get("os"));
            assertEquals("red", map.get("color"));
        }
    }


    @Test
    void copyToPropertySource() {
        {
            PropertySource<?> propertySource = new MapPropertySource("empty", Collections.emptyMap());
            Map<String, Object> map = new HashMap<>();
            PropertySourceUtil.copyTo((PropertySource<?>) null, map, true);
            PropertySourceUtil.copyTo(propertySource, null, false);
            PropertySourceUtil.copyTo(propertySource, map, true);
            assertTrue(map.isEmpty());
        }

        {
            PropertySource<?> propertySource = new MapPropertySource("empty",
                    MapUtil.from("host.name", "localhost",
                            "host.address", OriginTrackedValue.of("127,0.0.1", Origin.from("some-origin"))));
            Map<String, Object> map = MapUtil.from("host.name", "some-host", "host.port", 1234);
            PropertySourceUtil.copyTo(propertySource, map, false);
            assertEquals(3, map.size());
            assertEquals("some-host", map.get("host.name"));
            assertEquals("127,0.0.1", map.get("host.address"));
            assertEquals(1234, map.get("host.port"));
        }
    }

    @Test
    void remove() {
        MutablePropertySources propertySources = createPropertySources();

        assertNull(PropertySourceUtil.remove(propertySources, null));
        assertNull(PropertySourceUtil.remove(null, "null"));
        assertNull(PropertySourceUtil.remove(propertySources, "some-property-source"));

        PropertySourcesPropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        assertEquals("localhost", propertyResolver.getProperty("host.name"));

        PropertySource<?> propertySource = PropertySourceUtil.remove(propertySources, "#1");
        assertNotNull(propertySource);
        assertEquals("unknown", propertyResolver.getProperty("host.name"));
    }
}