package com.wingsweaver.kuja.common.utils.support.util;

import com.wingsweaver.kuja.common.utils.diag.AssertArgs;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * {@link Map} 工具类。
 *
 * @author wingsweaver
 */
public final class MapUtil {
    private MapUtil() {
        // 禁止实例化
    }

    /**
     * 默认的负载因子。
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75F;

    /**
     * 满负载因子。
     */
    public static final float FULL_LOAD_FACTOR = 1.0F;

    /**
     * 计算 HashMap 的初始容量。
     *
     * @param expectedSize 预期大小
     * @param loadFactor   负载因子
     * @return 初始容量
     */
    public static int hashInitCapacity(int expectedSize, float loadFactor) {
        return (int) Math.floor(expectedSize / loadFactor);
    }

    /**
     * 计算 HashMap 的初始容量。
     *
     * @param expectedSize 预期大小
     * @return 初始容量
     */
    public static int hashInitCapacity(int expectedSize) {
        return hashInitCapacity(expectedSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 根据 Value 的值，来设置或者删除数据。
     *
     * @param map   字典
     * @param key   Key
     * @param value Value
     * @param <K>   Key 的类型
     * @param <V>   Value 的类型
     * @return 旧的值
     */
    public static <K, V> V put(Map<K, V> map, K key, V value) {
        // 检查参数
        if (map == null || key == null) {
            return null;
        }

        // 根据 Value 的值，来设置或者删除数据
        if (value != null) {
            return map.put(key, value);
        } else {
            return map.remove(key);
        }
    }

    /**
     * 根据 Value 的值，来设置或者删除数据。
     *
     * @param map   字典
     * @param key   Key
     * @param value Value
     * @param <K>   Key 的类型
     * @param <V>   Value 的类型
     * @return 旧的值
     */
    public static <K, V> V putIfAbsent(Map<K, V> map, K key, V value) {
        // 检查参数
        if (map == null || key == null) {
            return null;
        }

        // 根据 Value 的值，来设置或者删除数据
        if (value != null) {
            return map.putIfAbsent(key, value);
        } else {
            return map.get(key);
        }
    }

    /**
     * 将字典中的数据，复制到另一个字典中。
     *
     * @param source    源字典
     * @param target    目标字典
     * @param overwrite 是否覆盖目标字典中已有的数据
     * @param <K>       Key 的类型
     * @param <V>       Value 的类型
     */
    public static <K, V> void copy(Map<K, V> source, Map<K, V> target, boolean overwrite) {
        // 检查参数
        if (source == target || source == null || source.isEmpty() || target == null) {
            return;
        }

        // 复制数据
        BiConsumer<K, V> consumer = overwrite ? target::put : target::putIfAbsent;
        source.forEach(consumer);
    }

    private static final int TWO = 2;

    /**
     * 通过 key, value 来生成 HashMap 的实例。
     *
     * @param key       第一个 Key
     * @param value     第一个 Value
     * @param keyValues 后续的 key-value
     * @param <K>       Key 的类型
     * @param <V>       Value 的类型
     * @return HashMap 的实例
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> from(K key, V value, Object... keyValues) {
        // 检查参数
        int keyValuesLength = 0;
        if (keyValues != null) {
            keyValuesLength = keyValues.length;
            AssertArgs.isTrue(keyValuesLength % TWO == 0, "keys and values in [keyValues] do not match");
        }

        // 生成字典
        int size = hashInitCapacity(keyValuesLength / TWO + 1, FULL_LOAD_FACTOR);
        Map<K, V> map = new HashMap<>(size);
        map.put(key, value);
        for (int i = 0; i < keyValuesLength; i += TWO) {
            map.put((K) keyValues[i], (V) keyValues[i + 1]);
        }

        // 返回
        return map;
    }

    /**
     * 通过 key, value 的数组生成 HashMap 实例。
     *
     * @param keys   Key 的集合
     * @param values Value 的集合
     * @param <K>    Key 的类型
     * @param <V>    Value 的类型
     * @return HashMap 的实例
     */
    public static <K, V> Map<K, V> fromArray(K[] keys, V[] values) {
        // 检查参数
        AssertArgs.Named.notNull("keys", keys);
        AssertArgs.Named.notNull("values", values);
        AssertArgs.isTrue(keys.length == values.length, "length of [keys] and [values] must be the same");

        // 生成字典
        int size = hashInitCapacity(keys.length + 1, FULL_LOAD_FACTOR);
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }

        // 返回
        return map;
    }
}
