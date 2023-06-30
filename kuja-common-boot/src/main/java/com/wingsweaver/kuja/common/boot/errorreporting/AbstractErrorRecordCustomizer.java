package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.boot.include.IncludeCheckerFactory;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link ErrorRecordCustomizer} 实现类的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractErrorRecordCustomizer extends AbstractComponent implements ErrorRecordCustomizer {
    public static final IncludeSettings DEFAULT_SETTINGS = new IncludeSettings(IncludeAttribute.ALWAYS);

    /**
     * 输出设置。
     */
    private IncludeSettings settings = DEFAULT_SETTINGS;

    /**
     * 是否导出的工厂。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private IncludeCheckerFactory includeCheckerFactory;

    /**
     * 创建 IncludeChecker 实例。
     *
     * @param businessContext 业务上下文
     * @param keyPrefix       键前缀
     * @return IncludeChecker 实例
     */
    @SuppressWarnings("SameParameterValue")
    protected IncludeChecker createIncludeChecker(BusinessContext businessContext, String keyPrefix) {
        return this.includeCheckerFactory.build(businessContext, keyPrefix);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化设置
        if (this.settings == null) {
            this.settings = DEFAULT_SETTINGS;
        }
        this.includeCheckerFactory = new IncludeCheckerFactory(this.settings);
    }
}
