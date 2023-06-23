package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.PropertySource;

/**
 * 用于删除 {@link InheritedEnvironmentPostProcessor} 中设置的继承 PropertySource 的 {@link EnvironmentPostProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class RemoveInheritedEnvironmentPostProcessor extends TreeShakingEnvironmentPostProcessor {
    public RemoveInheritedEnvironmentPostProcessor() {
        PropertySource<?> inheritEnvironmentPropertySource = InheritEnvironmentPropertySourceHolder.getInheritEnvironmentPropertySource();
        if (inheritEnvironmentPropertySource != null) {
            this.setOrder(KujaCommonBootOrders.REMOVE_INHERITED_ENVIRONMENT_POST_PROCESSOR);
            this.getPropertyNames(true).add(KujaCommonBootKeys.PropertySourceNames.INHERITED);
        }
    }
}
