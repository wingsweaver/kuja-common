package com.wingsweaver.kuja.common.messaging.broadcast.receive;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.boot.appinfo.AppInfoMatcher;
import com.wingsweaver.kuja.common.boot.appinfo.matcher.AppInfoMatcherSpec;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * 默认的 {@link BroadcastNotificationPredicate} 实现类。<br>
 * 只校验本服务实例 {@link AppInfo} 是否符合 {@link BroadcastNotification} 中指定的目标要求。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultBroadcastNotificationPredicate extends AbstractComponent implements BroadcastNotificationPredicate {
    /**
     * 应用程序的信息。
     */
    private AppInfo appInfo;

    @Override
    public boolean shouldDispatch(BroadcastNotification notification) {
        AppInfoMatcherSpec matcherSpec = notification.getTarget();
        if (matcherSpec == null) {
            // 如果没有指定 target，则意味着所有的接收方都要处理
            return true;
        }

        // 否则返回匹配结果
        AppInfoMatcher matcher = matcherSpec.createAppInfoMatcher();
        return matcher.matches(this.getAppInfo());
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
