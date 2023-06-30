package com.wingsweaver.kuja.common.utils.support.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * JSON 编码解码器的接口定义。
 *
 * @author wingsweaver
 */
public interface JsonCodec {
    /**
     * 将对象序列化成 JSON 字符串。
     *
     * @param object 对象
     * @return JSON 字符串
     * @throws JsonCodecException 编码异常
     */
    String toJsonString(Object object) throws JsonCodecException;

    /**
     * 将 JSON 字符串解析为指定类型的数据。
     *
     * @param jsonText JSON 字符串
     * @param type     指定类型
     * @return 解析后的对象
     * @throws JsonCodecException 解码异常
     */
    Object parseObject(String jsonText, Type type) throws JsonCodecException;

    /**
     * 将 JSON 字符串解析为字典。
     *
     * @param jsonText JSON 字符串
     * @return 解析后的对象
     * @throws JsonCodecException 解码异常
     */
    Map<String, Object> parseMap(String jsonText) throws JsonCodecException;

    /**
     * 将 JSON 字符串解析为指定类型的字典。
     *
     * @param jsonText  JSON 字符串
     * @param keyType   Key 类型
     * @param valueType Value 类型
     * @param <K>       Key 类型
     * @param <V>       Value 类型
     * @return 解析后的对象
     * @throws JsonCodecException 解码异常
     */
    <K, V> Map<K, V> parseMap(String jsonText, Class<K> keyType, Class<V> valueType) throws JsonCodecException;

    /**
     * 将 JSON 字符串解析为字典。
     *
     * @param jsonText JSON 字符串
     * @return 解析后的对象
     * @throws JsonCodecException 解码异常
     */
    List<Object> parseList(String jsonText) throws JsonCodecException;

    /**
     * 将 JSON 字符串解析为字典。
     *
     * @param jsonText JSON 字符串
     * @param itemType 元素的类型
     * @param <T>      元素的类型
     * @return 解析后的对象
     * @throws JsonCodecException 解码异常
     */
    <T> List<T> parseList(String jsonText, Class<T> itemType) throws JsonCodecException;
}
