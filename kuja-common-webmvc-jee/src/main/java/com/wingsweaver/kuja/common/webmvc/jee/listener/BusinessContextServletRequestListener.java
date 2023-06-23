package com.wingsweaver.kuja.common.webmvc.jee.listener;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextFactory;
import com.wingsweaver.kuja.common.boot.context.BusinessContextType;
import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.webmvc.common.constants.KujaCommonWebMvcOrders;
import com.wingsweaver.kuja.common.webmvc.jee.context.ServletContextAccessor;
import com.wingsweaver.kuja.common.webmvc.jee.util.ServletRequestUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * 用于处理业务上下文的 {@link ServletRequestListener} 的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@WebListener("kujaServletRequestListener")
public class BusinessContextServletRequestListener implements ServletRequestListener, InitializingBean, Ordered {
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
        ServletContextAccessor accessor = new ServletContextAccessor(businessContext);
        accessor.setContextType(BusinessContextType.Web.Blocked.J2EE.SERVLET);
        accessor.setOriginalRequest(sre.getServletRequest());
        accessor.setServletRequest(sre.getServletRequest());
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("businessContextFactory", this.getBusinessContextFactory());
    }
}
