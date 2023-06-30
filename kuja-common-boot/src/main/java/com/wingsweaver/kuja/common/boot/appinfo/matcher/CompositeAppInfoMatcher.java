package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 有多个 {@link AppInfoMatcher} 组合而成的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class CompositeAppInfoMatcher extends AbstractAppInfoMatcher<CompositeAppInfoMatcherSpec> {
    /**
     * 匹配模式。
     */
    private CompositeMatchMode mode = CompositeMatchMode.ALL;

    /**
     * 具体的匹配设置的列表。
     */
    private List<AppInfoMatcher> matchers;

    @Override
    protected boolean matchesInternal(AppInfo target) {
        if (CollectionUtils.isEmpty(this.matchers)) {
            return false;
        }

        switch (this.mode) {
            case ALL:
                return this.matchers.stream().allMatch(matcher -> matcher.matches(target));
            case ANY:
                return this.matchers.stream().anyMatch(matcher -> matcher.matches(target));
            default:
                throw new IllegalStateException("Unknown CompositeMatchMode: " + this.mode);
        }
    }

    @Override
    public void initialize(CompositeAppInfoMatcherSpec spec) {
        super.initialize(spec);

        // 设置 mode
        this.setMode(spec.getMode());

        // 设置 matchers
        if (CollectionUtils.isEmpty(spec.getMatchers())) {
            this.matchers = null;
        } else {
            this.matchers = spec.getMatchers().stream()
                    .map(AppInfoMatcherSpec::createAppInfoMatcher)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void validate() {
        super.validate();

        // 校验 mode
        AssertState.Named.notNull("mode", this.mode);
    }
}
