package com.wingsweaver.kuja.common.utils.support.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于 {@link Gson} 的 {@link JsonCodec} 实现。
 *
 * @author wingsweaver
 */
public class GsonCodec implements JsonCodec {
    /**
     * Gson 实例。
     */
    @Getter
    private final Gson gson;

    public GsonCodec(Gson gson) {
        this.gson = gson;
    }

    public GsonCodec() {
        this(createDefaultGson());
    }

    private static Gson createDefaultGson() {
        return new GsonBuilder()
                .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create();
    }

    @Override
    public String toJsonString(Object object) throws JsonCodecException {
        try {
            return this.gson.toJson(object);
        } catch (Exception ex) {
            throw new JsonCodecException(ex)
                    .withExtendedAttribute("object", object);
        }
    }

    @Override
    public Object parseObject(String jsonText, Type type) throws JsonCodecException {
        try {
            return this.gson.fromJson(jsonText, type);
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
            Type type = TypeToken.getParameterized(HashMap.class, keyType, valueType).getType();
            return this.gson.fromJson(jsonText, type);
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
            Type type = TypeToken.getParameterized(ArrayList.class, itemType).getType();
            return this.gson.fromJson(jsonText, type);
        } catch (Exception ex) {
            throw new JsonCodecException(ex)
                    .withExtendedAttribute("jsonText", jsonText)
                    .withExtendedAttribute("itemType", itemType);
        }
    }
}
