package com.wingsweaver.kuja.common.utils.support.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 基于 {@link ObjectMapper} 的 {@link JsonCodec} 实现类。
 *
 * @author wingsweaver
 */
public class JacksonCodec implements JsonCodec {
    /**
     * ObjectMapper 实例。
     */
    @Getter
    private final ObjectMapper objectMapper;

    public JacksonCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonCodec() {
        this(createDefaultObjectMapper());
    }

    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature());
        mapper.enable(JsonParser.Feature.IGNORE_UNDEFINED);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper;
    }

    @Override
    public String toJsonString(Object object) throws JsonCodecException {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonCodecException(e)
                    .withExtendedAttribute("object", object);
        }
    }

    @Override
    public Object parseObject(String jsonText, Type type) throws JsonCodecException {
        try {
            return this.objectMapper.readValue(jsonText, this.objectMapper.constructType(type));
        } catch (JsonProcessingException e) {
            throw new JsonCodecException(e)
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
            return this.objectMapper.readValue(jsonText, this.objectMapper.getTypeFactory().constructMapType(HashMap.class, keyType, valueType));
        } catch (JsonProcessingException e) {
            throw new JsonCodecException(e)
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
            return this.objectMapper.readValue(jsonText, this.objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, itemType));
        } catch (JsonProcessingException e) {
            throw new JsonCodecException(e)
                    .withExtendedAttribute("jsonText", jsonText)
                    .withExtendedAttribute("itemType", itemType);
        }
    }
}
