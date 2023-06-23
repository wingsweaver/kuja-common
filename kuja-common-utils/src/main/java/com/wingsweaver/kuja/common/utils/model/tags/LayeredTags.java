package com.wingsweaver.kuja.common.utils.model.tags;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 带层级结构的 {@link Tags} 实现类。<br>
 * 类似于 OverlayFS 的机制，可以基于某个指定的 <b>Base Tags</b>，添加一层新的 Tags。
 *
 * @author wingsweaver
 */
public class LayeredTags implements Tags {
    /**
     * 不存在的 Tag 数据。
     */
    private static final Object NON_TAG_VALUE = new Object();

    /**
     * 用作基础的 Base Tags。
     */
    private final Tags base;

    /**
     * 已经删除的 Tag Key 的集合。
     */
    private final Set<TagKey<?>> keysRemoved;

    /**
     * 新增的自定义 Tags。
     */
    private final Map<TagKey<?>, Object> customTags;

    public LayeredTags(Tags base, int initCapacity) {
        this.base = base;
        this.keysRemoved = new HashSet<>(initCapacity);
        this.customTags = new HashMap<>(initCapacity);
    }

    public LayeredTags(Tags base) {
        this(base, BufferSizes.SMALL);
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
        // 先检查自定义的 Tags
        if (!this.customTags.isEmpty()) {
            return false;
        }

        // 再检查 base 和 keysRemoved
        Tags tempBase = this.baseOrEmpty();
        if (tempBase.isEmpty()) {
            // 如果 base 为空，那么返回 true
            return true;
        }

        // 否则判断是否所有的 base 的 key 都在 keysRemoved 中（也就是都被删除了）
        return this.keysRemoved.containsAll(tempBase.getTagKeys());
    }

    @Override
    public int size() {
        return this.getTagKeys().size();
    }

    @Override
    public Collection<TagKey<?>> getTagKeys() {
        Tags tempBase = this.baseOrEmpty();
        int size = tempBase.size() + this.customTags.size();
        Set<TagKey<?>> keys = new HashSet<>(size);

        // 加入 customTags 中所有的 Keys
        keys.addAll(this.customTags.keySet());

        // 加入 base 中所有的 Keys
        keys.addAll(tempBase.getTagKeys());

        // 删除 keysRemoved 中所有的 Keys
        keys.removeAll(this.keysRemoved);

        // 返回
        return keys;
    }

    /**
     * 检查指定的 Key 是否已经被删除。
     *
     * @param key 要检查的 Key
     * @return 如果已经被删除，返回 true；否则返回 false。
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isRemoved(TagKey<?> key) {
        return this.keysRemoved.contains(key);
    }

    @Override
    public TagKey<?> findTagKey(Predicate<TagKey<?>> predicate) {
        this.validateTagKeyPredicate(predicate);

        // 先从 customTags 中查找匹配的 key
        for (TagKey<?> tagKey : this.customTags.keySet()) {
            if (predicate.test(tagKey) && !this.isRemoved(tagKey)) {
                return tagKey;
            }
        }

        // 再从 base 中查找匹配的 key
        Tags tempBase = this.baseOrEmpty();
        for (TagKey<?> tagKey : tempBase.getTagKeys()) {
            if (predicate.test(tagKey) && !this.isRemoved(tagKey)) {
                return tagKey;
            }
        }

        // 仍然没找到的话，返回 null
        return null;
    }

    @Override
    public Collection<TagKey<?>> findTagKeys(Predicate<TagKey<?>> predicate) {
        this.validateTagKeyPredicate(predicate);

        Tags tempBase = this.baseOrEmpty();
        int size = tempBase.size() + this.customTags.size();
        Set<TagKey<?>> keys = new HashSet<>(size);

        // 先从 customTags 中查找匹配的 key
        for (TagKey<?> tagKey : this.customTags.keySet()) {
            if (predicate.test(tagKey) && !this.isRemoved(tagKey)) {
                keys.add(tagKey);
            }
        }

        // 再从 base 中查找匹配的 key
        for (TagKey<?> tagKey : tempBase.getTagKeys()) {
            if (predicate.test(tagKey) && !this.isRemoved(tagKey)) {
                keys.add(tagKey);
            }
        }

        // 返回
        return keys;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean hasTag(TagKey<?> key) {
        this.validateTagKey(key);
        return this.getTag((TagKey<Object>) key, NON_TAG_VALUE) != NON_TAG_VALUE;
    }

    @Override
    public <T> T getTag(TagKey<T> key) {
        this.validateTagKey(key);
        return this.getTag(key, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getTag(TagKey<T> key, T defaultValue) {
        this.validateTagKey(key);

        // 先检查 key 是否已经被删除
        if (this.isRemoved(key)) {
            return defaultValue;
        }

        // 再尝试从 customTags 中获取
        Object value = this.customTags.getOrDefault(key, NON_TAG_VALUE);
        if (value == NON_TAG_VALUE) {
            // 不存在的话，再尝试从 base 中获取
            Tags tempBase = this.baseOrEmpty();
            value = tempBase.getTag((TagKey<Object>) key, NON_TAG_VALUE);
        }

        // 返回结果
        return (value != NON_TAG_VALUE) ? (T) value : defaultValue;
    }

    @Override
    public <T> void setTag(TagKey<T> key, Object value) {
        this.validateTagKey(key);

        Object realValue = key.convertToValue(value);
        this.keysRemoved.remove(key);
        this.customTags.put(key, realValue);
    }

    @Override
    public void removeTag(TagKey<?> key) {
        this.validateTagKey(key);

        this.keysRemoved.add(key);
        this.customTags.remove(key);
    }

    @Override
    public void clearTags() {
        this.customTags.clear();
        this.keysRemoved.clear();
        if (this.getBase() != null) {
            this.keysRemoved.addAll(this.baseOrEmpty().getTagKeys());
        }
    }

    @Override
    public Map<TagKey<?>, Object> asMap() {
        Tags tempBase = this.baseOrEmpty();
        int size = tempBase.size() + this.customTags.size();
        Map<TagKey<?>, Object> map = new HashMap<>(MapUtil.hashInitCapacity(size));

        // 先加入 base 中所有的、未被删除的 Tags 到 map 中
        tempBase.forEach((key, value) -> {
            if (!this.isRemoved(key)) {
                map.put(key, value);
            }
        });

        // 再加入 customTags 中所有的、未被删除的 Tags 到 map 中
        this.customTags.forEach((key, value) -> {
            if (!this.isRemoved(key)) {
                map.put(key, value);
            }
        });

        // 返回
        return map;
    }

    @Override
    public void forEach(BiConsumer<TagKey<?>, Object> consumer) {
        asMap().forEach(consumer);
    }

    private Tags baseOrEmpty() {
        Tags tempBase = this.getBase();
        return tempBase != null ? tempBase : Tags.EMPTY;
    }

    public Tags getBase() {
        return base;
    }
}
