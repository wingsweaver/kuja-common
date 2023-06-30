package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link AppInfoMatcher} 的实现类的基类。
 *
 * @param <S> 对应的 {@link AppInfoMatcherSpec} 实例的类型
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractAppInfoMatcher<S extends AbstractAppInfoMatcherSpec> implements AppInfoMatcher {
    /**
     * 对应的 {@link AppInfoMatcherSpec} 实例。
     */
    private S spec;

    /**
     * 是否反转匹配结果。
     */
    private boolean revert;

    @Override
    public boolean matches(AppInfo target) {
        boolean result = this.matchesInternal(target);
        return this.revert == (!result);
    }

    /**
     * 检查指定数据是否符合要求。
     *
     * @param target 目标数据
     * @return 是否符合要求
     */
    protected abstract boolean matchesInternal(AppInfo target);

    /**
     * 初始化处理。
     *
     * @param spec AppInfoMatcherSpec 实例
     */
    public void initialize(S spec) {
        AssertArgs.Named.notNull("spec", spec);

        // 设置 spec
        this.setSpec(spec);

        // 设置 revert
        this.setRevert(spec.isRevert());
    }

    /**
     * 校验是否有效。
     */
    public void validate() {
        // 什么也不做
    }
}
