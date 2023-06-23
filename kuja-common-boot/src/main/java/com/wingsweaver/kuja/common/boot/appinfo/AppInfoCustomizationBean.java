package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
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
public class AppInfoCustomizationBean implements SmartInitializingSingleton, InitializingBean, Ordered {
    private AppInfo appInfo;

    private List<AppInfoCustomizer> appInfoCustomizers;

    @Override
    public void afterSingletonsInstantiated() {
        this.appInfoCustomizers.forEach(customizer -> customizer.customize(this.appInfo));
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("appInfo", this.getAppInfo());
        AssertState.Named.notNull("appInfoCustomizers", this.getAppInfoCustomizers());
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.APP_INFO_CUSTOMIZATION_BEAN;
    }
}
