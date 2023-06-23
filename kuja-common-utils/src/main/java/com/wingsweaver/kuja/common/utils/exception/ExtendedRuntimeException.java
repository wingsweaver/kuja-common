package com.wingsweaver.kuja.common.utils.exception;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 带附加数据的 {@link RuntimeException}。
 *
 * @author wingsweaver
 */
public class ExtendedRuntimeException extends RuntimeException implements Extended<ExtendedRuntimeException> {
    private Map<String, Object> extendedMap;

    public ExtendedRuntimeException() {
    }

    public ExtendedRuntimeException(String message) {
        super(message);
    }

    public ExtendedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtendedRuntimeException(Throwable cause) {
        super(cause);
    }

    public static final String KEY_MESSAGE = "message";

    @Override
    public String getMessage() {
        Object message = this.getExtendedAttribute(KEY_MESSAGE);
        return (message instanceof CharSequence) ? message.toString() : super.getMessage();
    }

    public void setMessage(String message) {
        this.setExtendedAttribute(KEY_MESSAGE, message);
    }

    private Map<String, Object> getExtendedMapOrEmpty() {
        return (this.extendedMap != null) ? this.extendedMap : Collections.emptyMap();
    }

    @SuppressWarnings("SameParameterValue")
    private Map<String, Object> getExtendedMap(boolean createIfAbsent) {
        if (this.extendedMap == null && createIfAbsent) {
            this.extendedMap = new HashMap<>(BufferSizes.TINY);
        }
        return this.extendedMap;
    }

    @Override
    public Collection<String> extendedKeys() {
        return this.getExtendedMapOrEmpty().keySet();
    }

    @Override
    public Map<String, Object> extendedMap() {
        return this.getExtendedMapOrEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void forEachExtended(BiConsumer<String, ?> consumer) {
        this.getExtendedMapOrEmpty().forEach((BiConsumer<String, Object>) consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtendedAttribute(String key) {
        return (T) this.getExtendedMapOrEmpty().get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getExtendedAttribute(String key, T defaultValue) {
        return (T) this.getExtendedMapOrEmpty().getOrDefault(key, defaultValue);
    }

    @Override
    public void setExtendedAttribute(String key, Object value) {
        if (value == null) {
            this.removeExtendedAttribute(key);
        } else {
            this.getExtendedMap(true).put(key, value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T setExtendedAttributeIfAbsent(String key, T value) {
        if (value == null) {
            return this.getExtendedAttribute(key);
        } else {
            return (T) this.getExtendedMap(true).putIfAbsent(key, value);
        }
    }

    @Override
    public void removeExtendedAttribute(String key) {
        if (this.extendedMap != null) {
            this.extendedMap.remove(key);
        }
    }

    @Override
    public ExtendedRuntimeException withExtendedAttribute(String key, Object value) {
        this.setExtendedAttribute(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExtendedRuntimeException withExtendedAttribute(Map<String, ?> map) {
        MapUtil.copy((Map<String, Object>) map, this.getExtendedMap(true), true);
        return this;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        ExtendedUtil.printStackTrace(this, s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        ExtendedUtil.printStackTrace(this, s);
    }
}
