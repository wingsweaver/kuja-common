package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.boot.include.IncludeCheckerFactory;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理者的 App 信息的 {@link ReturnValueCustomizer} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoReturnValueCustomizer extends AbstractOneTimeReturnValueCustomizer implements ApplicationContextAware {
    public static final String KEY_APP_INFO = "appInfo";

    public static final String INCLUDES_KEY_PREFIX = "return-value.app-info";

    /**
     * 应用程序上下文。
     */
    private ApplicationContext applicationContext;

    /**
     * App 信息。
     */
    private AppInfo appInfo;

    /**
     * 导出设置。
     */
    private IncludeSettings settings;

    /**
     * 是否导出的工厂。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private IncludeCheckerFactory includeCheckerFactory;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void customizeInternal(BusinessContext businessContext, ReturnValue returnValue) {
        if (this.appInfo != null) {
            Map<String, Object> map = this.extractAppInfo(businessContext);
            if (map.isEmpty()) {
                return;
            }
            Object current = returnValue.getTagValue(KEY_APP_INFO);
            if (current == null) {
                returnValue.setTagValue(KEY_APP_INFO, map);
            } else if (current instanceof Map) {
                MapUtil.copy(map, (Map) current, false);
            }
        }
    }

    private Map<String, Object> extractAppInfo(BusinessContext businessContext) {
        Map<String, Object> map = new HashMap<>(BufferSizes.SMALL);
        IncludeChecker includeChecker = this.includeCheckerFactory.build(businessContext, INCLUDES_KEY_PREFIX);
        this.appInfo.forEach((key, value) -> {
            if (value != null && includeChecker.includes(key)) {
                map.put(key, value.toString());
            }
        });
        return map;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        // 检查 settings 是否设置
        AssertState.Named.notNull("settings", this.getSettings());

        // 检查 AppInfo 是否设置
        if (this.getAppInfo() == null) {
            try {
                AssertState.Named.notNull("applicationContext", this.getApplicationContext());
                this.appInfo = this.applicationContext.getBean(AppInfo.class);
            } catch (Exception ex) {
                throw new IllegalStateException("Cannot find AppInfo bean", ex);
            }
        }

        // 更新内部数据
        this.includeCheckerFactory = new IncludeCheckerFactory(this.settings);
        this.setEnabled(this.settings.getInclude() != IncludeAttribute.NEVER);
    }
}
