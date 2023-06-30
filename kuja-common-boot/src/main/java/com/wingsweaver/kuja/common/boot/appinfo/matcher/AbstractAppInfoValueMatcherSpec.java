package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * 校验指定的 AppInfo 的指定属性是否符合要求的 {@link AbstractAppInfoMatcherSpec} 的实现类的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractAppInfoValueMatcherSpec extends AbstractAppInfoMatcherSpec {
    /**
     * 要匹配的数据的键。
     */
    private String key;

    /**
     * 默认值。
     */
    private String defaultValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AbstractAppInfoValueMatcherSpec that = (AbstractAppInfoValueMatcherSpec) o;
        return Objects.equals(key, that.key) && Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, defaultValue);
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        super.exportConfig(map);

        // 导出 key
        this.exportKey(map);

        // 导出 defaultValue
        this.exportDefaultValue(map);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        super.loadConfig(map);

        // 导入 key
        this.loadKey(map);

        // 导入 defaultValue
        this.loadDefaultValue(map);
    }

    /**
     * {@link #key} 的 TagKey。
     */
    public static final TagKey.StringKey TAG_KEY_KEY = TagKeys.ofString("key");

    /**
     * 导出 key 设置。
     *
     * @param map 保存设置的字典
     */
    protected void exportKey(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_KEY, this.key);
    }

    /**
     * 导入 key 设置。
     *
     * @param map 保存设置的字典
     */
    protected void loadKey(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_KEY, this::setKey);
    }

    /**
     * {@link #defaultValue} 的 TagKey。
     */
    public static final TagKey.StringKey TAG_KEY_DEFAULT_VALUE = TagKeys.ofString("defaultValue");

    /**
     * 导出 defaultValue 设置。
     *
     * @param map 保存设置的字典
     */
    protected void exportDefaultValue(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_DEFAULT_VALUE, this.defaultValue);
    }

    /**
     * 导入 defaultValue 设置。
     *
     * @param map 保存设置的字典
     */
    protected void loadDefaultValue(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_DEFAULT_VALUE, this::setDefaultValue);
    }
}
