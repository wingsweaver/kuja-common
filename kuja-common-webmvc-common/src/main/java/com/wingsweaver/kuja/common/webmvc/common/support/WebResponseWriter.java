package com.wingsweaver.kuja.common.webmvc.common.support;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.spring.MethodParameterWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 将数据写入 HTTP 响应的工具类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class WebResponseWriter extends AbstractComponent {
    /**
     * 所有的 {@link HandlerMethodReturnValueHandler} 实例的列表。
     */
    private List<HandlerMethodReturnValueHandler> returnValueHandlers;

    /**
     * Mock 用的 {@link MethodParameter}。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MethodParameter mockMethodParameter;

    /**
     * 将指定的数据写入到 HTTP 响应中。
     *
     * @param webRequest WEB 请求
     * @param value      要写入的数据
     * @throws Exception 发生错误
     */
    public void write(NativeWebRequest webRequest, Object value) throws Exception {
        // 检查参数
        if (value == null) {
            return;
        }

        // 调用 HandlerMethodReturnValueHandlerComposite 将数据写入到 HTTP 请求中
        MethodParameter methodParameter = this.resolveMethodParameter(value);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        for (HandlerMethodReturnValueHandler returnValueHandler : this.returnValueHandlers) {
            if (returnValueHandler.supportsReturnType(methodParameter)) {
                returnValueHandler.handleReturnValue(value, methodParameter, mavContainer, webRequest);
                return;
            }
        }

        // 如果找不到对应的 HandlerMethodReturnValueHandler，则抛出异常
        throw new IllegalArgumentException("Unknown return value type: " + methodParameter.getParameterType().getName());
    }

    /**
     * 用于生成临时 MethodParameter 的辅助函数。
     *
     * @return 临时 MethodParameter
     */
    private static Object dummyMethod() {
        return null;
    }

    /**
     * 生成临时 MethodParameter。
     *
     * @param value 任意对象
     * @return 临时 MethodParameter
     */
    protected MethodParameter resolveMethodParameter(Object value) {
        MethodParameterWrapper methodParameterWrapper = new MethodParameterWrapper(this.mockMethodParameter);
        methodParameterWrapper.setParameterType(value.getClass());
        methodParameterWrapper.setGenericParameterType(value.getClass().getGenericSuperclass());
        return methodParameterWrapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 returnValueHandlers
        this.initReturnValueHandlers();

        // 初始化 methodParameter
        this.initMethodParameter();
    }

    /**
     * 初始化 methodParameter。
     */
    protected void initMethodParameter() throws NoSuchMethodException {
        Method method = WebResponseWriter.class.getDeclaredMethod("dummyMethod");
        this.mockMethodParameter = new MethodParameter(method, -1);
    }

    /**
     * 初始化 returnValueHandlers。
     */
    protected void initReturnValueHandlers() {
        if (this.returnValueHandlers == null) {
            this.returnValueHandlers = this.getBeansOrdered(HandlerMethodReturnValueHandler.class);
        }
    }
}
