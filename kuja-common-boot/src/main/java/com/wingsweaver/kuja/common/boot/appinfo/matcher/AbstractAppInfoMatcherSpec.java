package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link AppInfoMatcherSpec} 实现类的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractAppInfoMatcherSpec extends AbstractPojo implements AppInfoMatcherSpec {
    /**
     * 是否反转匹配结果。
     */
    private boolean revert;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractAppInfoMatcherSpec that = (AbstractAppInfoMatcherSpec) o;
        return revert == that.revert;
    }

    @Override
    public int hashCode() {
        return Objects.hash(revert);
    }

    /**
     * 导出特定的配置项目。
     *
     * @param map    保存配置的字典
     * @param tagKey 配置项目的 TagKey
     * @param value  配置项目的值
     * @param <T>    配置项目的类型
     */
    protected <T> void exportConfigValue(Map<String, String> map, TagKey<T> tagKey, T value) {
        map.put(tagKey.getKey(), tagKey.saveAsText(value));
    }

    /**
     * 导入特定的配置项目。
     *
     * @param map    保存配置的字典
     * @param tagKey 配置项目的 TagKey
     * @param setter 配置项目的设置器
     * @param <T>    配置项目的类型
     */
    protected <T> void loadConfigValue(Map<String, String> map, TagKey<T> tagKey, Consumer<T> setter) {
        String text = map.get(tagKey.getKey());
        if (StringUtil.isNotEmpty(text)) {
            setter.accept(tagKey.convertToValue(text));
        }
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        this.exportRevert(map);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        this.loadRevert(map);
    }

    /**
     * {@link #revert} 字段的导入导出 TagKey。
     */
    public static final TagKey.BooleanKey TAG_KEY_REVERT = TagKeys.ofBoolean("revert");

    /**
     * 导出 revert 设置。
     *
     * @param map 保存配置的字典
     */
    protected void exportRevert(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_REVERT, this.revert);
    }

    /**
     * 导入 revert 设置。
     *
     * @param map 保存配置的字典
     */
    protected void loadRevert(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_REVERT, this::setRevert);
    }
}
