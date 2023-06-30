package com.wingsweaver.kuja.common.boot.returnvalue;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextAccessor;
import com.wingsweaver.kuja.common.boot.include.IncludeAttribute;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.boot.include.IncludeCheckerFactory;
import com.wingsweaver.kuja.common.boot.include.IncludeSettings;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.exception.ErrorInfoExportUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 用于设置错误结果的 {@link ReturnValueCustomizer} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class ErrorReturnValueCustomizer extends AbstractOneTimeReturnValueCustomizer {
    /**
     * 错误信息的 Key。
     */
    public static final String KEY_ERROR = "error";

    /**
     * 错误信息的键的前缀。
     */
    public static final String INCLUDES_KEY_PREFIX = "return-value.error";

    /**
     * 是否启用。
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
        // 获取当前上下文中的错误信息
        BusinessContextAccessor accessor = new BusinessContextAccessor(businessContext);
        Throwable error = accessor.getError();
        if (error == null) {
            return;
        }

        // 设置错误信息
        IncludeChecker includeChecker = this.includeCheckerFactory.build(businessContext, INCLUDES_KEY_PREFIX);
        Map<String, Object> map = ErrorInfoExportUtil.export(error, includeChecker::includes);
        Object current = returnValue.getTagValue(KEY_ERROR);
        if (current == null) {
            returnValue.setTagValue(KEY_ERROR, map);
        } else if (current instanceof Map) {
            MapUtil.copy(map, (Map) current, false);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 检查 properties 是否设置
        AssertState.Named.notNull("settings", this.getSettings());

        // 更新内部数据
        this.includeCheckerFactory = new IncludeCheckerFactory(this.settings);
        this.setEnabled(this.settings.getInclude() != IncludeAttribute.NEVER);
    }
}
