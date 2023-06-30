package com.wingsweaver.kuja.common.messaging.common;

import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 带有 SenderInfo ({@code 发送者信息}) 的类的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractSenderInfoOwner extends AbstractComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSenderInfoOwner.class);

    /**
     * 发送者信息的解析器。
     */
    private SenderInfoResolver senderInfoResolver;

    /**
     * 发送者信息。
     */
    private Map<String, String> senderInfo;

    /**
     * 确保发送者信息被正确初始化。
     *
     * @param forceUpdate 是否强制刷新
     * @return 发送者信息
     */
    protected Map<String, String> ensureSenderInfo(boolean forceUpdate) {
        if (this.senderInfo == null || forceUpdate) {
            LogUtil.trace(LOGGER, "Initialize sender info ...");
            Map<String, String> map = new HashMap<>(BufferSizes.TINY);
            this.senderInfoResolver.resolveSenderInfo(map);
            synchronized (this) {
                this.senderInfo = map;
            }
        }
        return this.senderInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 SenderInfoResolver
        this.initSenderInfoResolver();
    }

    /**
     * 初始化 SenderInfoResolver。
     */
    protected void initSenderInfoResolver() {
        if (this.senderInfoResolver == null) {
            this.senderInfoResolver = this.getBean(SenderInfoResolver.class);
        }
    }
}
