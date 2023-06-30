package com.wingsweaver.kuja.common.boot.env.resolver;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.env.postprocessor.TreeShakingEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;

/**
 * 用于在 {@link AbstractEnvironmentResolver} 删除控制设置的 {@link EnvironmentPostProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class PostEnvironmentResolverProcessor extends TreeShakingEnvironmentPostProcessor {
    /**
     * 构造函数。
     */
    public PostEnvironmentResolverProcessor() {
        this.getPropertyNames(true).add(KujaCommonBootKeys.PropertySourceNames.ENVIRONMENT_RESOLVER);
    }
}
