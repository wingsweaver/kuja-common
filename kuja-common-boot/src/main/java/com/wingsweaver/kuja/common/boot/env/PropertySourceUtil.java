package com.wingsweaver.kuja.common.boot.env;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * {@link PropertySource} 的工具类。
 *
 * @author wingsweaver
 */
public final class PropertySourceUtil {
    private PropertySourceUtil() {
        // 禁止实例化
    }

    /**
     * 检查是否包含指定名称的 PropertySource。
     *
     * @param propertySources 目标 PropertySources
     * @param name            PropertySource 的名称
     * @return 是否包含
     */
    public static boolean containsProperty(PropertySources propertySources, String name) {
        return propertySources != null && StringUtil.isNotEmpty(name)
                && propertySources.stream().anyMatch(propertySource -> propertySource.containsProperty(name));
    }

    /**
     * 检查指定的 PropertySources 是否为空。
     *
     * @param propertySources 目标 PropertySources
     * @return 是否为空
     */
    public static boolean isEmpty(PropertySources propertySources) {
        return propertySources == null
                || propertySources.stream().allMatch(PropertySourceUtil::isEmpty);
    }

    /**
     * 检查指定的 PropertySource 是否为空。
     *
     * @param propertySource 目标 PropertySource
     * @return 是否为空
     */
    public static boolean isEmpty(PropertySource<?> propertySource) {
        boolean ret = true;
        if (propertySource != null) {
            Object source = propertySource.getSource();
            if (source instanceof Map) {
                ret = ((Map<?, ?>) source).isEmpty();
            } else if (propertySource instanceof EnumerablePropertySource) {
                ret = ArrayUtils.isEmpty(((EnumerablePropertySource<?>) propertySource).getPropertyNames());
            }
        }
        return ret;
    }

    /**
     * 获取指定 {@link PropertySources} 中的所有属性的名称。
     *
     * @param propertySources 目标 PropertySources
     * @param keys            保存属性名称的集合
     */
    public static void getPropertyNames(PropertySources propertySources, Collection<String> keys) {
        if (propertySources != null && keys != null) {
            Set<String> keySet = new HashSet<>(BufferSizes.SMALL);
            propertySources.stream().forEach(propertySource -> getPropertyNames(propertySource, keySet));
            keys.addAll(keySet);
        }
    }

    /**
     * 获取指定 {@link PropertySource} 中的所有属性的名称。
     *
     * @param propertySource 目标 PropertySource
     * @param keys           保存属性名称的集合
     */
    public static void getPropertyNames(PropertySource<?> propertySource, Collection<String> keys) {
        if (propertySource != null && keys != null) {
            Set<String> keySet = new HashSet<>(BufferSizes.SMALL);
            Object source = propertySource.getSource();
            if (source instanceof Map) {
                keySet.addAll(((Map<?, ?>) source).keySet().stream().map(Object::toString).collect(Collectors.toList()));
            } else if (propertySource instanceof EnumerablePropertySource) {
                keySet.addAll(Arrays.asList(((EnumerablePropertySource<?>) propertySource).getPropertyNames()));
            }
            keys.addAll(keySet);
        }
    }

    /**
     * 将指定的 PropertySources 中所有 PropertySource 的数据复制到指定的字典中。
     *
     * @param propertySources 要复制的 PropertySources 实例
     * @param map             保存数据的字典
     * @param overwrite       是否覆盖字典中的已有数据
     */
    public static void copyTo(PropertySources propertySources, Map<String, Object> map, boolean overwrite) {
        // 检查参数
        if (propertySources == null || map == null) {
            return;
        }

        PropertySourcesPropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        List<PropertySource<?>> propertySourceList = propertySources.stream().collect(Collectors.toList());
        if (overwrite) {
            // 如果覆盖 map 中的数据的话，那么直接从最后一个 PropertySource 向 map 中写入
            Collections.reverse(propertySourceList);
            for (PropertySource<?> propertySource : propertySourceList) {
                copyToInternal(propertyResolver, propertySource, map, true);
            }
        } else {
            // 如果不覆盖 map 中的数据的话，那么从第一个 PropertySource 向 map 中写入
            for (PropertySource<?> propertySource : propertySourceList) {
                copyToInternal(propertyResolver, propertySource, map, false);
            }
        }
    }

    /**
     * 将指定的 PropertySource 的数据复制到指定的字典中。
     *
     * @param propertySource 要复制的 PropertySource 实例
     * @param map            保存数据的字典
     * @param overwrite      是否覆盖字典中的已有数据
     */
    public static void copyTo(PropertySource<?> propertySource, Map<String, Object> map, boolean overwrite) {
        // 检查参数
        if (propertySource == null || map == null) {
            return;
        }

        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addLast(propertySource);
        PropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);
        copyToInternal(propertyResolver, propertySource, map, overwrite);
    }

    /**
     * 将指定的 PropertySource 的数据复制到指定的字典中。
     *
     * @param propertyResolver 用于解析占位符的解析器
     * @param propertySource   要复制的 PropertySource 实例
     * @param map              保存数据的字典
     * @param overwrite        是否覆盖字典中的已有数据
     */
    private static void copyToInternal(PropertyResolver propertyResolver, PropertySource<?> propertySource,
                                       Map<String, Object> map, boolean overwrite) {
        Object source = propertySource.getSource();
        if (source instanceof Map && source != map) {
            BiConsumer<String, Object> consumer = overwrite ? (map::put) : (map::putIfAbsent);
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) source).entrySet()) {
                String key = entry.getKey().toString();
                Object value = normalizeValue(entry.getValue());
                if (value instanceof CharSequence) {
                    value = propertyResolver.resolvePlaceholders(value.toString());
                }
                consumer.accept(key, value);
            }
            return;
        }

        if (propertySource instanceof EnumerablePropertySource) {
            BiConsumer<String, Object> consumer = overwrite ? (map::put) : (map::putIfAbsent);
            String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
            for (String propertyName : propertyNames) {
                Object value = normalizeValue(propertySource.getProperty(propertyName));
                if (value instanceof CharSequence) {
                    value = propertyResolver.resolvePlaceholders(value.toString());
                }
                consumer.accept(propertyName, value);
            }
        }
    }

    /**
     * 对指定的值进行规范化处理。
     *
     * @param value 要规范化的值
     * @return 规范化后的值
     */
    private static Object normalizeValue(Object value) {
        if (value instanceof OriginTrackedValue) {
            value = ((OriginTrackedValue) value).getValue();
        }
        return value;
    }

    /**
     * 从指定 {@link MutablePropertySources} 中删除指定名称的 {@link PropertySource}。
     *
     * @param propertySources    目标 MutablePropertySources
     * @param propertySourceName 要删除的 PropertySource 的名称
     * @return 被删除的 PropertySource，如果没有找到则返回 null
     */
    public static PropertySource<?> remove(MutablePropertySources propertySources, String propertySourceName) {
        // 检查参数
        if (propertySources == null || StringUtils.isEmpty(propertySourceName)) {
            return null;
        }

        // 先删除同名的 PropertySource
        PropertySource<?> propertySource = propertySources.remove(propertySourceName);
        if (propertySource == null) {
            return null;
        }

        // 再删除 PropertySource 中的数据，
        // 避免 MutablePropertySources 缓存型 PropertySource 中的同名的 PropertySource 还有残留
        Object source = propertySource.getSource();
        if (source instanceof Map) {
            ((Map<?, ?>) source).clear();
        }

        // 返回
        return propertySource;
    }
}
