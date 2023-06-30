package com.wingsweaver.kuja.common.boot.appinfo.matcher;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

/**
 * 检查指定的属性值是否匹配指定的正则表达式的 {@link AbstractAppInfoValueMatcher} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoValueRegexMatcher extends AbstractAppInfoValueMatcher<AppInfoValueRegexMatcherSpec> {
    /**
     * 正则表达式的匹配设置。
     */
    private long regexFlags = 0;

    /**
     * 正则表达式。
     */
    private String pattern;

    /**
     * 编译模式。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private volatile Pattern regexPattern;

    @Override
    public void initialize(AppInfoValueRegexMatcherSpec spec) {
        super.initialize(spec);

        // 设置 regexFlags
        this.setRegexFlags(spec.getRegexFlags());

        // 设置 pattern
        this.setPattern(spec.getPattern());

        // 设置 regexPattern
        this.ensureRegexPattern();
    }

    @SuppressWarnings("MagicConstant")
    protected Pattern ensureRegexPattern() {
        if (this.regexPattern == null) {
            synchronized (this) {
                if (this.regexPattern == null) {
                    this.regexPattern = Pattern.compile(this.getPattern(), (int) this.getRegexFlags());
                }
            }
        }
        return this.regexPattern;
    }

    @Override
    public void validate() {
        super.validate();

        // 校验 pattern
        AssertState.Named.notEmpty("pattern", this.pattern);
    }

    @Override
    protected boolean matchesValue(Object value) {
        String text = this.valueToString(value);
        if (text == null) {
            return false;
        }

        return this.ensureRegexPattern().matcher(text).matches();
    }

    public void setRegexFlags(long regexFlags) {
        this.regexFlags = regexFlags;
        this.regexPattern = null;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        this.regexPattern = null;
    }
}
