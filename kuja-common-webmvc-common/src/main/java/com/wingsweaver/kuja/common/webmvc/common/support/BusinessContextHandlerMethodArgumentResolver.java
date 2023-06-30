package com.wingsweaver.kuja.common.webmvc.common.support;

import com.wingsweaver.kuja.common.boot.context.BusinessContext;
import com.wingsweaver.kuja.common.boot.context.BusinessContextHolder;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 用于解析 {@link BusinessContext} 的 {@link HandlerMethodArgumentResolver} 实现类。
 *
 * @author wingsweaver
 */
public class BusinessContextHandlerMethodArgumentResolver extends AbstractComponent implements HandlerMethodArgumentResolver {
    public static final String KEY_BUSINESS_CONTEXT = ClassUtil.resolveKey(BusinessContext.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return BusinessContext.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        BusinessContext businessContext = (BusinessContext) webRequest.getAttribute(KEY_BUSINESS_CONTEXT, NativeWebRequest.SCOPE_REQUEST);
        if (businessContext == null) {
            businessContext = BusinessContextHolder.getCurrent();
        }
        return businessContext;
    }
}
