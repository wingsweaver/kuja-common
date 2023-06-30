package com.wingsweaver.kuja.common.boot.errorreporting;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.errorhandling.ErrorHandlerContext;
import com.wingsweaver.kuja.common.boot.include.IncludeChecker;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于应用信息的 {@link ErrorRecordCustomizer} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoErrorRecordCustomizer extends AbstractErrorRecordCustomizer {
    public static final String KEY = "appInfo";

    /**
     * 应用信息。
     */
    private AppInfo appInfo;

    @Override
    public ErrorRecord customize(ErrorHandlerContext context, ErrorRecord record) {
        Map<String, Object> errorInfo = this.resolveAppInfo(context.getBusinessContext());
        record.setTagValue(KEY, errorInfo);
        return record;
    }

    /**
     * 解析应用信息。
     *
     * @param businessContext 业务上下文
     * @return 应用信息
     */
    protected Map<String, Object> resolveAppInfo(BusinessContext businessContext) {
        IncludeChecker includeChecker = this.createIncludeChecker(businessContext, null);
        Map<String, ?> map = this.appInfo.asMap();
        Map<String, Object> appInfoMap = new HashMap<>(map.size() + 1, MapUtil.FULL_LOAD_FACTOR);
        map.forEach((key, value) -> {
            if (includeChecker.includes(key)) {
                appInfoMap.put(key, value);
            }
        });
        return appInfoMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 appInfo
        this.initAppInfo();
    }

    /**
     * 初始化 appInfo。
     */
    protected void initAppInfo() {
        if (this.appInfo == null) {
            this.appInfo = this.getBean(AppInfo.class);
        }
    }
}
