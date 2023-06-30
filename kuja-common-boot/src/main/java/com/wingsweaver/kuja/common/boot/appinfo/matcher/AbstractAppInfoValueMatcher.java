package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;

/**
 * 校验指定的 AppInfo 的指定属性是否符合要求的 {@link AbstractAppInfoMatcher} 的实现类的基类。
 *
 * @param <S> 对应的 {@link AbstractAppInfoValueMatcherSpec} 实例的类型
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractAppInfoValueMatcher<S extends AbstractAppInfoValueMatcherSpec> extends AbstractAppInfoMatcher<S> {
    /**
     * 要匹配的数据的键。
     */
    private String key;

    /**
     * 默认值。
     */
    private String defaultValue;

    @Override
    public void initialize(S spec) {
        super.initialize(spec);

        // 初始化 key
        this.setKey(spec.getKey());

        // 初始化 defaultValue
        this.setDefaultValue(spec.getDefaultValue());
    }

    @Override
    public void validate() {
        super.validate();

        // 校验 key
        AssertState.Named.notEmpty("key", this.key);
    }

    @Override
    protected boolean matchesInternal(AppInfo target) {
        Object value = target != null ? target.getAttribute(this.getKey(), this.getDefaultValue()) : null;
        return this.matchesValue(value);
    }

    /**
     * 检查指定的值是否符合要求。
     *
     * @param value 指定的值
     * @return 是否符合要求
     */
    protected abstract boolean matchesValue(Object value);

    /**
     * 将指定的值转换为字符串。
     *
     * @param value 指定的值
     * @return 转换后的字符串
     */
    protected String valueToString(Object value) {
        return value == null ? null : value.toString();
    }
}
