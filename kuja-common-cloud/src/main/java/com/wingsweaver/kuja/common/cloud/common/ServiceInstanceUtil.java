package com.wingsweaver.kuja.common.cloud.common;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * {@link ServiceInstance} 辅助工具类。
 *
 * @author wingsweaver
 */
public final class ServiceInstanceUtil {
    private ServiceInstanceUtil() {
        // 禁止实例化
    }

    /**
     * 更新 {@link ServiceInstance} 中的元数据。
     *
     * @param serviceInstance ServiceInstance 实例
     * @param map             元数据字典
     * @param forceOverwrite  是否强制覆盖 ServiceInstance 中的元数据
     */
    public static void importMetadata(ServiceInstance serviceInstance, Map<String, ?> map, boolean forceOverwrite) {
        if (serviceInstance != null) {
            importMetadata(serviceInstance.getMetadata(), map, forceOverwrite);
        }
    }

    /**
     * 更新指定的元数据字典。
     *
     * @param metadata       ServiceInstance 实例中的元数据字典
     * @param map            要导入的元数据字典
     * @param forceOverwrite 是否强制覆盖 ServiceInstance 中的元数据
     */
    @SuppressWarnings("rawtypes")
    public static void importMetadata(Map<String, String> metadata, Map<String, ?> map, boolean forceOverwrite) {
        // 检查参数
        if (metadata == null || metadata == map || CollectionUtils.isEmpty(map) || metadata == (Map) Collections.emptyMap()) {
            return;
        }

        // 复制数据
        BiConsumer<String, String> consumer = forceOverwrite ? metadata::put : metadata::putIfAbsent;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = StringUtil.trimToEmpty(entry.getKey());
            if (key.isEmpty()) {
                continue;
            }

            Object value = entry.getValue();
            consumer.accept(key, value != null ? value.toString() : null);
        }
    }

    /**
     * 更新 {@link ServiceInstance} 中的元数据。<br>
     * 如果元数据键以 {@link #SUFFIX_FORCE_OVERWRITE} 结尾，则强制覆盖 ServiceInstance 中的元数据。<br>
     * 否则，不覆盖 ServiceInstance 中的元数据。
     *
     * @param serviceInstance ServiceInstance 实例
     * @param map             元数据字典
     */
    public static void importMetadata(ServiceInstance serviceInstance, Map<String, ?> map) {
        if (serviceInstance != null) {
            importMetadata(serviceInstance.getMetadata(), map);
        }
    }


    /**
     * 更新指定的元数据字典。<br>
     * 如果元数据键以 {@link #SUFFIX_FORCE_OVERWRITE} 结尾，则强制覆盖 ServiceInstance 中的元数据。<br>
     * 否则，不覆盖 ServiceInstance 中的元数据。
     *
     * @param metadata ServiceInstance 实例中的元数据字典
     * @param map      要导入的元数据字典
     */
    @SuppressWarnings("rawtypes")
    public static void importMetadata(Map<String, String> metadata, Map<String, ?> map) {
        // 检查参数
        if (metadata == null || metadata == map || CollectionUtils.isEmpty(map) || metadata == (Map) Collections.emptyMap()) {
            return;
        }

        // 复制数据
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            Tuple2<String, Boolean> tuple2 = parseMetadataKey(entry.getKey());
            String key = tuple2.getT1();
            if (key.isEmpty()) {
                continue;
            }

            String value = entry.getValue() != null ? entry.getValue().toString() : null;
            if (tuple2.getT2()) {
                // 覆盖现有数据
                metadata.put(key, value);
            } else {
                // 不覆盖现有数据
                metadata.putIfAbsent(key, value);
            }
        }
    }

    /**
     * 元数据键的后缀，用于标记是否强制覆盖。
     */
    public static final char SUFFIX_FORCE_OVERWRITE = '!';

    /**
     * 解析元数据键。
     *
     * @param key 元数据键
     * @return 元数据键(t1)和是否强制覆盖的标记(t2)
     */
    public static Tuple2<String, Boolean> parseMetadataKey(String key) {
        key = StringUtil.trimToEmpty(key);

        boolean forceOverwrite = false;
        // 检查后缀是否是强制覆盖的标记
        if ((!key.isEmpty() && key.charAt(key.length() - 1) == SUFFIX_FORCE_OVERWRITE)) {
            key = StringUtils.trimTrailingCharacter(key, SUFFIX_FORCE_OVERWRITE);
            forceOverwrite = true;
        }

        // 返回
        return Tuple2.of(key, forceOverwrite);
    }
}
