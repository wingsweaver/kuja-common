package com.wingsweaver.kuja.common.boot.include;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.utils.support.RegexUtil;
import com.wingsweaver.kuja.common.utils.support.lang.ArrayUtil;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 检查指定的输出是否被包含的工具类。
 *
 * @author wingsweaver
 */
public class IncludeCheckerFactory {
    /**
     * 全局的设置。
     */
    @Getter
    private final IncludeSettings settings;

    /**
     * 具体的包含项目的列表。
     */
    private final List<IncludeItem> includeItems;

    /**
     * 构造函数。
     *
     * @param settings 全局的设置
     */
    public IncludeCheckerFactory(IncludeSettings settings) {
        // 检查参数
        Objects.requireNonNull(settings, "[settings] is null");

        // 初始化
        this.settings = settings;
        this.includeItems = this.buildIncludeItems();
    }

    private List<IncludeItem> buildIncludeItems() {
        IncludeSettings.IncludeItem[] items = this.settings.getItems();
        if (ArrayUtil.isEmpty(items)) {
            return Collections.emptyList();
        }

        List<IncludeItem> tempIncludeItems = new ArrayList<>(items.length);
        for (IncludeSettings.IncludeItem item : items) {
            String key = StringUtil.trimToEmpty(item.getKey());
            if (!StringUtils.hasText(key)) {
                continue;
            }

            IncludeItem includeItem = new IncludeItem.Builder()
                    .keyOrPattern(key).include(item.getInclude())
                    .build();
            tempIncludeItems.add(includeItem);
        }

        return tempIncludeItems;
    }

    /**
     * 创建 IncludeChecker 的实例。
     *
     * @param businessContext 业务上下文
     * @param keyPrefix       Key 的前缀
     * @return IncludeChecker 的实例
     */
    public IncludeChecker build(BusinessContext businessContext, String keyPrefix) {
        return new IncludeCheckerImpl(this, businessContext, keyPrefix);
    }

    /**
     * 带正则表达式的 {@link IncludeSettings.IncludeItem} 子类。
     */
    private static class IncludeItem extends IncludeSettings.IncludeItem {
        /**
         * 正则表达式。
         */
        private final Pattern pattern;

        private IncludeItem(String key, IncludeAttribute include) {
            super(key, include);
            this.pattern = null;
        }

        private IncludeItem(Pattern pattern, IncludeAttribute include) {
            super(null, include);
            this.pattern = pattern;
        }

        static final class Builder {
            private String keyOrPattern;

            private IncludeAttribute include = IncludeAttribute.NEVER;

            public Builder keyOrPattern(String keyOrPattern) {
                this.keyOrPattern = keyOrPattern;
                return this;
            }

            public Builder include(IncludeAttribute include) {
                this.include = include;
                return this;
            }

            public IncludeItem build() {
                if (RegexUtil.isRegexPattern(keyOrPattern)) {
                    return new IncludeItem(Pattern.compile(keyOrPattern), include);
                } else {
                    return new IncludeItem(keyOrPattern, include);
                }
            }
        }
    }

    /**
     * {@link IncludeChecker} 的实现类。
     */
    private static class IncludeCheckerImpl implements IncludeChecker {
        private final IncludeCheckerFactory factory;

        private final BusinessContext businessContext;

        private final String keyPrefix;

        private final BusinessContextAccessor accessor;

        /**
         * 默认是否包含。
         */
        private final boolean defaultIncludes;

        private IncludeCheckerImpl(IncludeCheckerFactory factory, BusinessContext businessContext, String keyPrefix) {
            this.factory = factory;
            this.businessContext = businessContext;
            this.keyPrefix = StringUtil.trimToEmpty(keyPrefix);
            this.accessor = new BusinessContextAccessor(businessContext);
            this.defaultIncludes = this.resolveDefaultIncludes(factory.getSettings().getInclude(), keyPrefix);
        }

        private boolean resolveDefaultIncludes(IncludeAttribute includeAttribute, String key) {
            boolean includes = false;
            switch (includeAttribute) {
                case ALWAYS:
                    includes = true;
                    break;
                case ON_ERROR:
                    includes = this.accessor.getError() != null;
                    break;
                case ON_ATTRIBUTE:
                    if (this.businessContext != null) {
                        Object value = this.businessContext.getAttribute(key);
                        if (value instanceof Boolean) {
                            includes = (Boolean) value;
                        } else {
                            includes = value != null;
                        }
                    }
                    break;
                default:
                    break;
            }
            return includes;
        }

        @Override
        public boolean includes(String key) {
            // 检查参数
            if (!StringUtils.hasText(key)) {
                return false;
            }

            // 检查全局设置
            if (!this.defaultIncludes) {
                return false;
            }

            // 检查本项目的设置
            IncludeAttribute includeAttribute = this.findIncludeAttribute(key);
            if (includeAttribute == null) {
                includeAttribute = this.factory.getSettings().getInclude();
            }

            String checkKey = this.keyPrefix.isEmpty() ? key : this.keyPrefix + "." + key;
            return this.resolveDefaultIncludes(includeAttribute, checkKey);
        }

        private IncludeAttribute findIncludeAttribute(String key) {
            for (IncludeItem item : this.factory.includeItems) {
                if (item.pattern != null) {
                    if (item.pattern.matcher(key).matches()) {
                        return item.getInclude();
                    }
                } else if (item.getKey().equals(key)) {
                    return item.getInclude();
                }
            }

            return null;
        }
    }
}
