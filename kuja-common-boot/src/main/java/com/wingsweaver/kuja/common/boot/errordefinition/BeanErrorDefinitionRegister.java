package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 基于 {@link ErrorDefinition} Bean 的 {@link ErrorDefinitionRegister} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class BeanErrorDefinitionRegister extends AbstractComponent implements ErrorDefinitionRegister, DefaultOrdered {
    @Override
    public void register(Collection<ErrorDefinition> errorDefinitions) {
        errorDefinitions.addAll(this.getBeansOrdered(ErrorDefinition.class));
    }
}
