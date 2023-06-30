package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.CommonBootProperties;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.info.BuildProperties;

/**
 * 默认的 {@link AppInfoCustomizer} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultAppInfoCustomizer extends AbstractComponent implements AppInfoCustomizer {
    /**
     * {@link CommonBootProperties} 实例。
     */
    private CommonBootProperties properties;

    @Override
    public void customize(AppInfo appInfo) {
        // 将 CommonBootProperties 中的 app 信息导入到 AppInfo 中
        appInfo.importMap(this.properties.getApp(), true);

        // 导入 BuildProperties 中的信息（如果存在的话）
        this.importBuildProperties(appInfo);
    }

    /**
     * 导入 BuildProperties 中的信息。
     *
     * @param appInfo AppInfo 实例
     */
    private void importBuildProperties(AppInfo appInfo) {
        try {
            BuildProperties buildProperties = this.getBean(BuildProperties.class, false);
            appInfo.setAttributeIfAbsent(ResourceAttributes.Service.KEY_GROUP, buildProperties.getGroup());
            appInfo.setAttributeIfAbsent(ResourceAttributes.Service.KEY_NAME, buildProperties.getArtifact());
            appInfo.setAttributeIfAbsent(ResourceAttributes.Service.KEY_VERSION, buildProperties.getVersion());
        } catch (Exception ignored) {
            // 忽略此错误
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 properties
        this.initProperties();
    }

    /**
     * 初始化 properties。
     */
    protected void initProperties() {
        if (this.properties == null) {
            this.properties = this.getBean(CommonBootProperties.class);
        }
    }
}
