package com.wingsweaver.kuja.common.boot.include;

import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 是否包含的设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class IncludeSettings extends AbstractPojo {
    /**
     * 默认是否输出。
     */
    @NestedConfigurationProperty
    private IncludeAttribute include;

    /**
     * 包含的各个项目。
     */
    @NestedConfigurationProperty
    private IncludeItem[] items;

    /**
     * 构造函数。
     */
    public IncludeSettings() {
        this(IncludeAttribute.NEVER);
    }

    /**
     * 构造函数。
     *
     * @param include 默认是否输出
     * @param items   包含的各个项目
     */
    public IncludeSettings(IncludeAttribute include, IncludeItem... items) {
        this.include = include;
        this.items = items;
    }

    /**
     * 单个包含项目。
     */
    @Getter
    @Setter
    public static class IncludeItem extends AbstractPojo {
        /**
         * 项目名称，或者名称的正则表达式。
         */
        private String key;

        /**
         * 包含设置。
         */
        private IncludeAttribute include;

        /**
         * 构造函数。
         *
         * @param key     项目名称，或者名称的正则表达式
         * @param include 包含设置
         */
        public IncludeItem(String key, IncludeAttribute include) {
            this.key = StringUtil.trimToEmpty(key);
            this.include = include;
        }

        /**
         * 构造函数。
         *
         * @param key 项目名称，或者名称的正则表达式
         */
        public IncludeItem(String key) {
            this(key, IncludeAttribute.NEVER);
        }

        /**
         * 构造函数。
         */
        public IncludeItem() {
            this(null, IncludeAttribute.NEVER);
        }
    }
}
