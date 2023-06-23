package com.wingsweaver.kuja.common.webflux.util;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.springframework.web.server.ServerWebExchange;

/**
 * {@link ServerWebExchange} 工具类。
 *
 * @author wingsweaver
 */
public final class ServerWebExchangeUtil {
    private ServerWebExchangeUtil() {
        // 禁止实例化
    }

    public static final String KEY_BUSINESS_CONTEXT = ClassUtil.resolveKey(BusinessContext.class);

    public static BusinessContext getBusinessContext(ServerWebExchange exchange) {
        return exchange.getAttribute(KEY_BUSINESS_CONTEXT);
    }

    public static void setBusinessContext(ServerWebExchange exchange, BusinessContext businessContext) {
        exchange.getAttributes().put(KEY_BUSINESS_CONTEXT, businessContext);
    }
}
