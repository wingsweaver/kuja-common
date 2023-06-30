package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import lombok.Getter;
import lombok.Setter;

/**
 * 检查是否包含指定属性的 {@link AppInfoMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class HasAppInfoValueMatcherSpec extends AbstractAppInfoValueMatcherSpec {
    public static final String SPEC_TYPE = "has";

    @Override
    public String getSpecType() {
        return SPEC_TYPE;
    }

    @Override
    public AppInfoMatcher createAppInfoMatcher() {
        HasAppInfoValueMatcher matcher = new HasAppInfoValueMatcher();
        matcher.initialize(this);
        matcher.validate();
        return matcher;
    }
}
