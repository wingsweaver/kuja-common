package com.wingsweaver.kuja.common.boot.env;

import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.model.tags.convert.TagConversionService;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * PropertySource 加载工具类。
 *
 * @author wingsweaver
 */
public final class PropertySourceLoadUtil {
    private PropertySourceLoadUtil() {
        // 禁止实例化
    }

    /**
     * 用于指定 PropertySource 的顺序的属性名。
     */
    public static final String KEY_PROPERTY_ORDER = "!kuja.properties.order";

    /**
     * 比较两个 PropertySource 的顺序。
     *
     * @param p1 PropertySource 1
     * @param p2 PropertySource 2
     * @return 比较结果
     */
    public static int comparePropertySource(PropertySource<?> p1, PropertySource<?> p2) {
        return Integer.compare(resolvePropertySourceOrder(p1), resolvePropertySourceOrder(p2));
    }

    /**
     * 获取指定 PropertySource 的顺序。
     *
     * @param propertySource PropertySource
     * @return 顺序
     */
    public static int resolvePropertySourceOrder(PropertySource<?> propertySource) {
        if (propertySource == null) {
            return 0;
        }

        Object value = propertySource.getProperty(KEY_PROPERTY_ORDER);
        if (value == null) {
            return 0;
        }
        return TagConversionService.toValue(value, Long.class);
    }

    /**
     * 从指定的资源加载 PropertySource。
     *
     * @param resourceLoader 资源加载器
     * @param locations      资源路径的列表
     * @return PropertySource 列表
     */
    public static List<PropertySource<?>> load(ResourceLoader resourceLoader, String[] locations) {
        // 检查参数
        if (locations == null || locations.length < 1) {
            return Collections.emptyList();
        }

        List<PropertySource<?>> propertySources = new LinkedList<>();

        // 生成 ResourcePatternResolver 实例
        ResourcePatternResolver resourcePatternResolver;
        if (resourceLoader == null) {
            resourcePatternResolver = new PathMatchingResourcePatternResolver();
        } else {
            resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
        }

        // 逐个加载资源
        for (String location : locations) {
            loadFromLocation(propertySources, resourcePatternResolver, location);
        }

        // 返回结果
        if (propertySources.isEmpty()) {
            return Collections.emptyList();
        } else {
            propertySources.sort(PropertySourceLoadUtil::comparePropertySource);
            return new ArrayList<>(propertySources);
        }
    }

    private static void loadFromLocation(List<PropertySource<?>> propertySources, ResourcePatternResolver resourcePatternResolver,
                                         String location) {
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(location);
        } catch (IOException ex) {
            throw new ExtendedRuntimeException("Failed to get resources from location " + location, ex)
                    .withExtendedAttribute("location", location);
        }

        // 处理各个资源
        for (Resource resource : resources) {
            try {
                // 检查资源是否存在
                if (!resource.exists()) {
                    continue;
                }

                // 从资源中读取 PropertySource
                PropertySourceLoader propertySourceLoader = deduecePropertySourceLoader(resource);
                if (propertySourceLoader != null) {
                    List<PropertySource<?>> tempPropertySources = propertySourceLoader.load(resource.toString(), resource);
                    if (tempPropertySources != null) {
                        propertySources.addAll(tempPropertySources);
                    }
                }
            } catch (IOException ex) {
                throw new ExtendedRuntimeException("Failed to load properties from resource " + resource, ex)
                        .withExtendedAttribute("resource", resource);
            }
        }
    }

    private static final PropertySourceLoader[] PROPERTY_SOURCE_LOADERS;

    private static final Tuple2<PropertySourceLoader, String>[] EXTENSION_PROPERTY_SOURCE_LOADERS;

    static {
        List<PropertySourceLoader> propertySourceLoaders = new LinkedList<>(SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, null));
        propertySourceLoaders.add(new PropertiesPropertySourceLoader());
        propertySourceLoaders.add(new YamlPropertySourceLoader());
        PROPERTY_SOURCE_LOADERS = propertySourceLoaders.toArray(new PropertySourceLoader[0]);

        List<Tuple2<PropertySourceLoader, String>> extensionPropertySourceLoaders = new LinkedList<>();
        for (PropertySourceLoader propertySourceLoader : PROPERTY_SOURCE_LOADERS) {
            for (String fileExtension : propertySourceLoader.getFileExtensions()) {
                extensionPropertySourceLoaders.add(new Tuple2<>(propertySourceLoader, fileExtension));
            }
        }
        extensionPropertySourceLoaders.sort(Comparator.comparing(Tuple2::getT2));
        //noinspection unchecked
        EXTENSION_PROPERTY_SOURCE_LOADERS = extensionPropertySourceLoaders.toArray(new Tuple2[0]);
    }

    private static PropertySourceLoader deduecePropertySourceLoader(Resource resource) throws IOException {
        String url = resource.getURL().toString();
        for (Tuple2<PropertySourceLoader, String> extensionPropertySourceLoader : EXTENSION_PROPERTY_SOURCE_LOADERS) {
            if (url.endsWith(extensionPropertySourceLoader.getT2())) {
                return extensionPropertySourceLoader.getT1();
            }
        }
        return null;
    }
}
