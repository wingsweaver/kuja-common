package com.wingsweaver.kuja.common.boot.include;

import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 是否包含的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class IncludeSettings {
    /**
     * 默认是否输出。
     */
    private IncludeAttribute include;

    /**
     * 各个属性是否输出。
     */
    private IncludeItem[] items;

    public IncludeSettings() {
        this(IncludeAttribute.NEVER);
    }

    public IncludeSettings(IncludeAttribute include, IncludeItem... items) {
        this.include = include;
        this.items = items;
    }

    /**
     * 单个包含项目。
     */
    @Getter
    @Setter
    public static class IncludeItem {
        /**
         * 项目名称，或者名称的正则表达式。
         */
        private String key;

        /**
         * 包含设置。
         */
        private IncludeAttribute include;

        public IncludeItem(String key, IncludeAttribute include) {
            this.key = StringUtil.trimToEmpty(key);
            this.include = include;
        }

        public IncludeItem(String key) {
            this(key, IncludeAttribute.NEVER);
        }

        public IncludeItem() {
            this(null, IncludeAttribute.NEVER);
        }
    }
}
