package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * {@link AppInfo} 的初始化工具类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AppInfoCustomizationBean extends AbstractComponent implements SmartInitializingSingleton, Ordered {
    /**
     * App 信息。
     */
    private AppInfo appInfo;

    /**
     * App 信息定制器。
     */
    private List<AppInfoCustomizer> appInfoCustomizers;

    @Override
    public void afterSingletonsInstantiated() {
        this.appInfoCustomizers.forEach(customizer -> customizer.customize(this.appInfo));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 appInfo
        this.initAppInfo();

        // 初始化 appInfoCustomizers
        this.initAppInfoCustomizers();
    }

    /**
     * 初始化 appInfoCustomizers。
     */
    protected void initAppInfoCustomizers() {
        if (this.appInfoCustomizers == null) {
            this.appInfoCustomizers = this.getBeansOrdered(AppInfoCustomizer.class);
        }
    }

    /**
     * 初始化 appInfo。
     */
    protected void initAppInfo() {
        if (this.appInfo == null) {
            this.appInfo = this.getBean(AppInfo.class);
        }
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.APP_INFO_CUSTOMIZATION_BEAN;
    }
}
