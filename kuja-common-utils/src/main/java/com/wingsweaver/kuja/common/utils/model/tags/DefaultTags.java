package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 默认的 {@link Tags} 实现类。
 *
 * @author wingsweaver
 */
public class DefaultTags implements Tags {
    /**
     * 实际的字典。
     */
    private Map<TagKey<?>, Object> map;

    public DefaultTags() {
        // 什么也不做
    }

    public DefaultTags(Tags tags) {
        if (tags != null) {
            this.map = new HashMap<>(tags.asMap());
        } else {
            this.map = new HashMap<>(BufferSizes.SMALL);
        }
    }

    public DefaultTags(int initCapacity) {
        this.map = new HashMap<>(initCapacity);
    }

    public DefaultTags(Map<TagKey<?>, Object> map) {
        if (CollectionUtils.isEmpty(map)) {
            this.map = null;
        } else {
            int size = MapUtil.hashInitCapacity(map.size());
            this.map = new HashMap<>(size);
            for (Map.Entry<TagKey<?>, Object> entry : map.entrySet()) {
                this.map.put(entry.getKey(), entry.getKey().convertToValue(entry.getValue()));
            }
        }
    }

    /**
     * 校验 TagKey 是否为有效。
     *
     * @param key TagKey
     */
    private void validateTagKey(TagKey<?> key) {
        AssertArgs.Named.notNull("key", key);
    }

    /**
     * 校验 TagKey 的 Predicate 是否为有效。
     *
     * @param predicate TagKey 的 Predicate
     */
    private void validateTagKeyPredicate(Predicate<TagKey<?>> predicate) {
        AssertArgs.Named.notNull("predicate", predicate);
    }

    @Override
    public boolean isEmpty() {
        return this.mapOrEmpty().isEmpty();
    }

    @Override
    public int size() {
        return this.mapOrEmpty().size();
    }

    @Override
    public Collection<TagKey<?>> getTagKeys() {
        return new ArrayList<>(this.mapOrEmpty().keySet());
    }

    @Override
    public TagKey<?> findTagKey(Predicate<TagKey<?>> predicate) {
        this.validateTagKeyPredicate(predicate);
        return this.mapOrEmpty().keySet().stream()
                .filter(predicate)
                .findFirst().orElse(null);
    }

    @Override
    public Collection<TagKey<?>> findTagKeys(Predicate<TagKey<?>> predicate) {
        this.validateTagKeyPredicate(predicate);
        return this.mapOrEmpty().keySet().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasTag(TagKey<?> key) {
        this.validateTagKey(key);
        return this.mapOrEmpty().containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getTag(TagKey<T> key) {
        this.validateTagKey(key);
        return (T) this.mapOrEmpty().get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getTag(TagKey<T> key, T defaultValue) {
        this.validateTagKey(key);
        return (T) this.mapOrEmpty().getOrDefault(key, defaultValue);
    }

    @Override
    public <T> void setTag(TagKey<T> key, Object value) {
        this.validateTagKey(key);
        this.mapOrCreate().put(key, key.convertToValue(value));
    }

    @Override
    public void removeTag(TagKey<?> key) {
        this.validateTagKey(key);
        this.mapOrEmpty().remove(key);
    }

    @Override
    public void clearTags() {
        this.mapOrEmpty().clear();
    }

    @Override
    public Map<TagKey<?>, Object> asMap() {
        Map<TagKey<?>, Object> map = this.mapOrEmpty();
        if (!map.isEmpty()) {
            map = new HashMap<>(map);
        }
        return map;
    }

    @Override
    public void forEach(BiConsumer<TagKey<?>, Object> consumer) {
        this.mapOrEmpty().forEach(consumer);
    }

    private Map<TagKey<?>, Object> mapOrEmpty() {
        return this.map != null ? this.map : Collections.emptyMap();
    }

    private Map<TagKey<?>, Object> mapOrCreate() {
        if (this.map == null) {
            this.map = new HashMap<>(BufferSizes.SMALL);
        }
        return this.map;
    }
}
