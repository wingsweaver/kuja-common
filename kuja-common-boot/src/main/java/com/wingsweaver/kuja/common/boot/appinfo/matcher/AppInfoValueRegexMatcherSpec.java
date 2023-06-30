package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * 检查指定的属性值是否匹配指定的正则表达式的 {@link AbstractAppInfoValueMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoValueRegexMatcherSpec extends AbstractAppInfoValueMatcherSpec {
    /**
     * 规格类型。
     */
    public static final String SPEC_TYPE = "regex";

    /**
     * 正则表达式的匹配设置。
     */
    private long regexFlags = 0;

    /**
     * 正则表达式。
     */
    private String pattern;

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
        AppInfoValueRegexMatcherSpec spec = (AppInfoValueRegexMatcherSpec) o;
        return regexFlags == spec.regexFlags && Objects.equals(pattern, spec.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), regexFlags, pattern);
    }

    @Override
    public String getSpecType() {
        return SPEC_TYPE;
    }

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        AppInfoValueRegexMatcher matcher = new AppInfoValueRegexMatcher();
        matcher.initialize(this);
        matcher.validate();
        return matcher;
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        super.exportConfig(map);

        // 导出 regexFlags
        this.exportRegexFlags(map);

        // 导出 pattern
        this.exportPattern(map);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        super.loadConfig(map);

        // 导入 regexFlags
        this.loadRegexFlags(map);

        // 导入 pattern
        this.loadPattern(map);
    }

    public static final TagKey.LongKey TAG_KEY_REGEX_FLAGS = TagKeys.ofLong("regexFlags");

    protected void exportRegexFlags(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_REGEX_FLAGS, this.regexFlags);
    }

    protected void loadRegexFlags(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_REGEX_FLAGS, this::setRegexFlags);
    }

    public static final TagKey.StringKey TAG_KEY_PATTERN = TagKeys.ofString("pattern");

    protected void exportPattern(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_PATTERN, this.pattern);
    }

    protected void loadPattern(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_PATTERN, this::setPattern);
    }
}
