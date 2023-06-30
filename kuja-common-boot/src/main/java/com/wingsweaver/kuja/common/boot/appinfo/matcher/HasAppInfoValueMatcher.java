package com.wingsweaver.kuja.common.boot.appinfo.matcher;

/**
 * 检查是否包含指定属性的 {@link AppInfoMatcherSpec} 实现类。
 *
 * @author wingsweaver
 */
public class HasAppInfoValueMatcher extends AbstractAppInfoValueMatcher<HasAppInfoValueMatcherSpec> {
    @Override
    protected boolean matchesValue(Object value) {
        return value != null;
    }
}
