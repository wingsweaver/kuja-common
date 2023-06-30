package com.wingsweaver.kuja.common.utils.support.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 fastjson (v2) 的 {@link JsonCodec} 实现。
 *
 * @author wingsweaver
 */
public class FastJson2Codec implements JsonCodec {
    public static final JSONWriter.Feature[] DEFAULT_FEATURES = {
            JSONWriter.Feature.EscapeNoneAscii,
            JSONWriter.Feature.BrowserCompatible
    };

    /**
     * 特性设置。
     */
    @Getter
    private final JSONWriter.Feature[] features;

    public FastJson2Codec(JSONWriter.Feature[] features) {
        this.features = features;
    }

    public FastJson2Codec() {
        this(DEFAULT_FEATURES);
    }

    @Override
    public String toJsonString(Object object) throws JsonCodecException {
        try {
            return JSON.toJSONString(object, this.features);
        } catch (Exception ex) {
            throw new JsonCodecException(ex)
                    .withExtendedAttribute("object", object);
        }
    }

    @Override
    public Object parseObject(String jsonText, Type type) throws JsonCodecException {
        try {
            return JSON.parseObject(jsonText, type);
        } catch (Exception ex) {
            throw new JsonCodecException(ex)
                    .withExtendedAttribute("jsonText", jsonText)
                    .withExtendedAttribute("type", type);
        }
    }

    @Override
    public Map<String, Object> parseMap(String jsonText) throws JsonCodecException {
        return this.parseMap(jsonText, String.class, Object.class);
    }

    @Override
    public <K, V> Map<K, V> parseMap(String jsonText, Class<K> keyType, Class<V> valueType) throws JsonCodecException {
        try {
            return JSON.parseObject(jsonText, TypeReference.mapType(HashMap.class, keyType, valueType));
        } catch (Exception ex) {
            throw new JsonCodecException(ex)
                    .withExtendedAttribute("jsonText", jsonText)
                    .withExtendedAttribute("keyType", keyType)
                    .withExtendedAttribute("valueType", valueType);
        }
    }

    @Override
    public List<Object> parseList(String jsonText) throws JsonCodecException {
        return this.parseList(jsonText, Object.class);
    }

    @Override
    public <T> List<T> parseList(String jsonText, Class<T> itemType) throws JsonCodecException {
        try {
            return JSON.parseArray(jsonText, itemType);
        } catch (Exception ex) {
            throw new JsonCodecException(ex)
                    .withExtendedAttribute("jsonText", jsonText)
                    .withExtendedAttribute("itemType", itemType);
        }
    }
}
