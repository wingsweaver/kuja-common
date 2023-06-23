package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * 基于 {@link ErrorDefinitions} 注解的 {@link ErrorDefinitionRegister} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class AnnotationBeanErrorDefinitionRegister implements ErrorDefinitionRegister, ApplicationContextAware, DefaultOrdered {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationBeanErrorDefinitionRegister.class);

    private ApplicationContext applicationContext;

    @Override
    public void register(Collection<ErrorDefinition> errorDefinitions) {
        Map<String, Object> beansWithAnnotation = this.getApplicationContext().getBeansWithAnnotation(ErrorDefinitions.class);
        beansWithAnnotation.forEach((name, bean) -> this.registerBean(errorDefinitions, name, bean));
    }

    private void registerBean(Collection<ErrorDefinition> errorDefinitions, String name, Object bean) {
        LogUtil.trace(LOGGER, "Registering error definitions from bean {} with name {}", bean, name);
        Class<?> clazz = bean.getClass();

        // 注册所有 ErrorDefinition 类型的 field
        for (Field field : clazz.getFields()) {
            if (ErrorDefinition.class.isAssignableFrom(field.getType())) {
                try {
                    errorDefinitions.add((ErrorDefinition) field.get(bean));
                } catch (Exception ex) {
                    LogUtil.warn(LOGGER, ex, "Failed to get error definition from field {} in class {} of bean {}",
                            field, clazz, name);
                }
            }
        }

        // 注册所有的 get 方法
        for (Method method : clazz.getMethods()) {
            if (method.getParameterCount() == 0
                    && ErrorDefinition.class.isAssignableFrom(method.getReturnType())
                    && method.getName().startsWith("get")) {
                try {
                    errorDefinitions.add((ErrorDefinition) method.invoke(bean));
                } catch (Exception ex) {
                    LogUtil.warn(LOGGER, ex, "Failed to get error definition from method {} in class {} of bean {}",
                            method, clazz, name);
                }
            }
        }
    }
}
