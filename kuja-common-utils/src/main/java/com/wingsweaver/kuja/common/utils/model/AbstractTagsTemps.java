package com.wingsweaver.kuja.common.utils.model;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 含有 Tags 和 Temps 两种数据的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractTagsTemps extends AbstractPojo {
    /**
     * 附加数据。
     */
    private Map<String, Object> tags;

    /**
     * 临时数据。
     */
    @Getter(AccessLevel.NONE)
    private transient Map<String, Object> temps;

    public Map<String, Object> getTags(boolean createIfAbsent) {
        if (this.tags == null && createIfAbsent) {
            this.tags = new HashMap<>(BufferSizes.SMALL);
        }
        return this.tags;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTagValue(String key) {
        return (this.tags != null) ? (T) this.tags.get(key) : null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTagValue(String key, T defaultValue) {
        return (this.tags != null) ? (T) this.tags.getOrDefault(key, defaultValue) : defaultValue;
    }

    public void setTagValue(String key, Object value) {
        if (value != null) {
            this.getTags(true).put(key, value);
        } else {
            this.removeTagValue(key);
        }
    }

    /**
     * 移除指定的附加数据。
     *
     * @param key 附加数据的 Key
     */
    public void removeTagValue(String key) {
        if (this.tags != null) {
            this.tags.remove(key);
        }
    }

    public Map<String, Object> getTemps(boolean createIfAbsent) {
        if (this.temps == null && createIfAbsent) {
            this.temps = new HashMap<>(BufferSizes.SMALL);
        }
        return this.temps;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTempValue(String key) {
        return (this.temps != null) ? (T) this.temps.get(key) : null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTempValue(String key, T defaultValue) {
        return (this.temps != null) ? (T) this.temps.getOrDefault(key, defaultValue) : defaultValue;
    }

    public void setTempValue(String key, Object value) {
        if (value != null) {
            this.getTemps(true).put(key, value);
        } else {
            this.removeTempValue(key);
        }
    }

    /**
     * 移除指定的临时数据。
     *
     * @param key 临时数据的 Key
     */
    public void removeTempValue(String key) {
        if (this.temps != null) {
            this.temps.remove(key);
        }
    }
}
