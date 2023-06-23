package com.wingsweaver.kuja.common.utils.model.tags;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 空的 {@link Tags} 的实现类。
 *
 * @author wingsweaver
 */
final class EmptyTags implements Tags {
    public static final EmptyTags INSTANCE = new EmptyTags();

    private EmptyTags() {
        // 禁止外部实例化
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<TagKey<?>> getTagKeys() {
        return Collections.emptyList();
    }

    @Override
    public TagKey<?> findTagKey(Predicate<TagKey<?>> predicate) {
        return null;
    }

    @Override
    public Collection<TagKey<?>> findTagKeys(Predicate<TagKey<?>> predicate) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasTag(TagKey<?> key) {
        return false;
    }

    @Override
    public <T> T getTag(TagKey<T> key) {
        return null;
    }

    @Override
    public <T> T getTag(TagKey<T> key, T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> void setTag(TagKey<T> key, Object value) {
        // 什么也不做
    }

    @Override
    public void removeTag(TagKey<?> key) {
        // 什么也不做
    }

    @Override
    public void clearTags() {
        // 什么也不做
    }

    @Override
    public Map<TagKey<?>, Object> asMap() {
        return Collections.emptyMap();
    }

    @Override
    public void forEach(BiConsumer<TagKey<?>, Object> consumer) {
        // 什么也不做
    }
}
