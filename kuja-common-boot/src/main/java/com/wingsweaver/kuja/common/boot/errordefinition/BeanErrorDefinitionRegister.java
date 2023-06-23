package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 基于 {@link ErrorDefinition} Bean 的 {@link ErrorDefinitionRegister} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BeanErrorDefinitionRegister implements ErrorDefinitionRegister, ApplicationContextAware, DefaultOrdered {
    private ApplicationContext applicationContext;

    @Override
    public void register(Collection<ErrorDefinition> errorDefinitions) {
        errorDefinitions.addAll(
                this.getApplicationContext().getBeanProvider(ErrorDefinition.class)
                        .stream().collect(Collectors.toList()));

    }
}
