package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * 检查指定的属性值是否相等的 {@link AbstractAppInfoValueMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoValueEqualsMatcherSpec extends AbstractAppInfoValueMatcherSpec {
    /**
     * 规范类型。
     */
    public static final String SPEC_TYPE = "equals";

    /**
     * 是否忽略大小写。
     */
    private boolean ignoreCase = true;

    /**
     * 目标值。
     */
    private String target;

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
        AppInfoValueEqualsMatcherSpec that = (AppInfoValueEqualsMatcherSpec) o;
        return ignoreCase == that.ignoreCase && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ignoreCase, target);
    }

    @Override
    public String getSpecType() {
        return SPEC_TYPE;
    }

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        AppInfoValueEqualsMatcher matcher = new AppInfoValueEqualsMatcher();
        matcher.initialize(this);
        matcher.validate();
        return matcher;
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        super.exportConfig(map);

        // 导出 ignoreCase
        this.exportIgnoreCase(map);

        // 导出 target
        this.exportTarget(map);

        // 导出 defaultValue
        this.exportDefaultValue(map);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        super.loadConfig(map);

        // 导入 ignoreCase
        this.loadIgnoreCase(map);

        // 导入 target
        this.loadTarget(map);

        // 导入 defaultValue
        this.loadDefaultValue(map);
    }

    public static final TagKey.BooleanKey TAG_KEY_IGNORECASE = TagKeys.ofBoolean("ignoreCase");

    protected void exportIgnoreCase(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_IGNORECASE, this.ignoreCase);
    }

    protected void loadIgnoreCase(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_IGNORECASE, this::setIgnoreCase);
    }

    public static final TagKey.StringKey TAG_KEY_TARGET = TagKeys.ofString("target");

    protected void exportTarget(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_TARGET, this.target);
    }

    protected void loadTarget(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_TARGET, this::setTarget);
    }
}
