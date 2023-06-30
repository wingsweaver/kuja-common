package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 检查指定的属性值是否相等的 {@link AbstractAppInfoValueMatcher} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoValueEqualsMatcher extends AbstractAppInfoValueMatcher<AppInfoValueEqualsMatcherSpec> {
    /**
     * 是否忽略大小写。
     */
    private boolean ignoreCase = true;

    /**
     * 目标值。
     */
    private String target;

    @Override
    public void initialize(AppInfoValueEqualsMatcherSpec spec) {
        super.initialize(spec);

        // 设置 ignoreCase
        this.setIgnoreCase(spec.isIgnoreCase());

        // 设置 target
        this.setTarget(spec.getTarget());
    }

    @Override
    protected boolean matchesValue(Object value) {
        if (this.isIgnoreCase()) {
            return StringUtils.equalsIgnoreCase(this.valueToString(value), this.getTarget());
        } else {
            return StringUtils.equals(this.valueToString(value), this.getTarget());
        }
    }
}
