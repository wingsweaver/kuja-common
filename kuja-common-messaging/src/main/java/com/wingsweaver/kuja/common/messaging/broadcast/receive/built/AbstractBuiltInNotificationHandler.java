package com.wingsweaver.kuja.common.messaging.broadcast.receive.built;

import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotification;
import com.wingsweaver.kuja.common.messaging.broadcast.receive.BroadcastNotificationHandler;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 内置广播消息的 {@link BroadcastNotificationHandler} 实现类的基类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public abstract class AbstractBuiltInNotificationHandler extends AbstractComponent implements BroadcastNotificationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBuiltInNotificationHandler.class);

    /**
     * 忽略的消息类型。
     */
    private Set<String> disabledMessageTypes;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 disabledMessageTypes
        this.initDisabledMessageTypes();
    }

    /**
     * 设置忽略的消息类型。
     *
     * @param disabledMessageTypes 忽略的消息类型
     */
    public void setDisabledMessageTypes(Collection<String> disabledMessageTypes) {
        if (disabledMessageTypes == null) {
            this.disabledMessageTypes = null;
        } else if (disabledMessageTypes instanceof Set) {
            this.disabledMessageTypes = (Set<String>) disabledMessageTypes;
        } else {
            this.disabledMessageTypes = new HashSet<>(disabledMessageTypes);
        }
    }

    /**
     * 检查指定的消息是否被禁用。
     *
     * @param notification 消息通知
     * @return 是否被禁用
     */
    @SuppressWarnings("PMD.GuardLogStatement")
    protected boolean isDisabled(BroadcastNotification notification) {
        String messageType = notification.getMessageType();
        boolean disabled = this.disabledMessageTypes != null && this.disabledMessageTypes.contains(messageType);
        if (disabled) {
            LogUtil.trace(LOGGER, "Message type {} is disabled in handler {}, notification id = {}",
                    messageType, this, notification.getId());
        }
        return disabled;
    }

    /**
     * 初始化 disabledMessageTypes。
     */
    protected void initDisabledMessageTypes() {
        if (this.disabledMessageTypes == null) {
            this.disabledMessageTypes = Collections.emptySet();
        }
    }
}
