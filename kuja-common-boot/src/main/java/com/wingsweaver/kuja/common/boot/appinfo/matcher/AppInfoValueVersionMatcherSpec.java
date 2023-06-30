package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.vdurmont.semver4j.Semver;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.model.tags.TagKey;
import com.wingsweaver.kuja.common.utils.model.tags.TagKeys;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * 检查指定的版本值是否符合要求的 {@link AbstractAppInfoValueMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoValueVersionMatcherSpec extends AbstractAppInfoValueMatcherSpec {
    /**
     * 规格类型。
     */
    public static final String SPEC_TYPE = "version";

    /**
     * 版本编号的类型。
     */
    private Semver.SemverType versionType = Semver.SemverType.NPM;

    /**
     * 版本要求。
     */
    private String requirement;

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
        AppInfoValueVersionMatcherSpec spec = (AppInfoValueVersionMatcherSpec) o;
        return versionType == spec.versionType && Objects.equals(requirement, spec.requirement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), versionType, requirement);
    }

    @Override
    public String getSpecType() {
        return SPEC_TYPE;
    }

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        AppInfoValueVersionMatcher matcher = new AppInfoValueVersionMatcher();
        matcher.initialize(this);
        matcher.validate();
        return matcher;
    }

    @Override
    public void exportConfig(Map<String, String> map) {
        super.exportConfig(map);

        // 导出 versionType
        this.exportVersionType(map);

        // 导出 pattern
        this.exportPattern(map);

        // 导出 requirement
        this.exportRequirement(map);
    }

    @Override
    public void loadConfig(Map<String, String> map) {
        super.loadConfig(map);

        // 导入 versionType
        this.loadVersionType(map);

        // 导入 pattern
        this.loadPattern(map);

        // 导入 requirement
        this.loadRequirement(map);
    }

    public static final TagKey.EnumKey<Semver.SemverType> TAG_KEY_VER_TYPE = TagKeys.ofEnum("versionType", Semver.SemverType.class);

    protected void exportVersionType(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_VER_TYPE, this.versionType);
    }

    protected void loadVersionType(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_VER_TYPE, this::setVersionType);
    }

    public static final TagKey.StringKey TAG_KEY_PATTERN = TagKeys.ofString("pattern");

    protected void exportPattern(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_PATTERN, this.requirement);
    }

    protected void loadPattern(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_PATTERN, this::setRequirement);
    }

    public static final TagKey.StringKey TAG_KEY_REQUIREMENT = TagKeys.ofString("requirement");

    protected void exportRequirement(Map<String, String> map) {
        this.exportConfigValue(map, TAG_KEY_REQUIREMENT, this.requirement);
    }

    protected void loadRequirement(Map<String, String> map) {
        this.loadConfigValue(map, TAG_KEY_REQUIREMENT, this::setRequirement);
    }
}
