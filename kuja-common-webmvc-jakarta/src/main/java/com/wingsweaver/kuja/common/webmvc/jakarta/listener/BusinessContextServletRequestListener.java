package com.wingsweaver.kuja.common.webmvc.jakarta.listener;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.BusinessContextType;
import com.wingsweaver.kuja.common.boot.context.BusinessContextTypeSetter;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import com.wingsweaver.kuja.common.webmvc.jakarta.context.ServletContextAccessor;
import com.wingsweaver.kuja.common.webmvc.jakarta.util.ServletRequestUtil;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

/**
 * 用于处理业务上下文的 {@link ServletRequestListener} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@WebListener("kujaServletRequestListener")
public class BusinessContextServletRequestListener extends AbstractComponent implements ServletRequestListener, Ordered {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessContextServletRequestListener.class);

    private BusinessContextFactory businessContextFactory;

    @Override
    public int getOrder() {
        return KujaCommonWebMvcOrders.BUSINESS_CONTEXT_SERVLET_REQUEST_LISTENER_ORDER;
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ServletRequestUtil.removeBusinessContext(sre.getServletRequest());
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        BusinessContext businessContext = ServletRequestUtil.getBusinessContext(sre.getServletRequest());
        if (businessContext == null) {
            businessContext = this.businessContextFactory.create();
            ServletRequestUtil.setBusinessContext(sre.getServletRequest(), businessContext);
            LogUtil.trace(LOGGER, "Create new business context {} and bind to request", businessContext);
        }

        // 初始化 BusinessContext
        this.initializeBusinessContext(sre, businessContext);
    }

    private void initializeBusinessContext(ServletRequestEvent sre, BusinessContext businessContext) {
        // 设置 BusinessContextType
        if (businessContext instanceof BusinessContextTypeSetter setter) {
            setter.setContextType(BusinessContextType.Web.Blocked.JakartaEE.SERVLET);
        }

        // 按照 ServletContextAccessor 进行设置
        ServletContextAccessor accessor = new ServletContextAccessor(businessContext);
        accessor.setOriginalRequest(sre.getServletRequest());
        accessor.setServletRequest(sre.getServletRequest());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 businessContextFactory
        this.initializeBusinessContextFactory();
    }

    /**
     * 初始化 businessContextFactory。
     */
    protected void initializeBusinessContextFactory() {
        if (this.businessContextFactory == null) {
            this.businessContextFactory = this.getBean(BusinessContextFactory.class);
        }
    }
}
