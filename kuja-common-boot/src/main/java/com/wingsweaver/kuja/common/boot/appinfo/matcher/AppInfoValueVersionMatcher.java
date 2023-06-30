package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.vdurmont.semver4j.Semver;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;

/**
 * 检查指定的版本值是否符合要求的 {@link AbstractAppInfoValueMatcher} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoValueVersionMatcher extends AbstractAppInfoValueMatcher<AppInfoValueVersionMatcherSpec> {
    /**
     * 版本编号的类型。
     */
    private Semver.SemverType versionType = Semver.SemverType.NPM;

    /**
     * 版本要求。
     */
    private String requirement;

    @Override
    public void initialize(AppInfoValueVersionMatcherSpec spec) {
        super.initialize(spec);

        // 设置 versionType
        this.setVersionType(spec.getVersionType());

        // 设置 requirement
        this.setRequirement(spec.getRequirement());
    }

    @Override
    public void validate() {
        super.validate();

        // 校验 versionType
        AssertState.Named.notNull("versionType", this.versionType);

        // 检查 requirement
        AssertState.Named.notEmpty("requirement", this.requirement);
    }

    @Override
    protected boolean matchesValue(Object value) {
        String text = this.valueToString(value);
        if (text == null) {
            return false;
        }

        Semver version = new Semver(text, this.getVersionType());
        return version.satisfies(this.getRequirement());
    }
}
