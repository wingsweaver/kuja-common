package com.wingsweaver.kuja.common.messaging.common;

import com.wingsweaver.kuja.common.boot.appinfo.AppInfo;
import com.wingsweaver.kuja.common.messaging.autoconfigurer.broadcast.send.SenderInfoResolverProperties;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 默认的 {@link SenderInfoResolver} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultSenderInfoResolver extends AbstractComponent implements SenderInfoResolver {
    /**
     * 应用信息。
     */
    private AppInfo appInfo;

    /**
     * 需要导出的属性的列表。
     */
    private List<String> exportItems;

    @Override
    public void resolveSenderInfo(Map<String, String> senderInfo) {
        List<String> items = CollectionUtils.isEmpty(exportItems) ? SenderInfoResolverProperties.DEFAULT_EXPORT_ITEMS : exportItems;
        for (String item : items) {
            Object value = this.appInfo.getAttribute(item);
            if (value != null) {
                senderInfo.put(item, value.toString());
            }
        }
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
