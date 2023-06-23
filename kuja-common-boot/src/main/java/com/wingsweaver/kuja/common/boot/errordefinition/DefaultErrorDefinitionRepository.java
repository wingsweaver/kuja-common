package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 默认的 {@link ErrorDefinitionRepository} 实现。
 *
 * @author wingsweaver
 */
public class DefaultErrorDefinitionRepository implements ErrorDefinitionRepository, InitializingBean {
    /**
     * 错误定义注册器的集合。
     */
    @Getter
    @Setter
    private List<ErrorDefinitionRegister> errorDefinitionRegisters;

    /**
     * 错误定义的映射。
     */
    private Map<String, ErrorDefinition> errorDefinitionMap;

    @Override
    public ErrorDefinition getErrorDefinition(String code) {
        return this.errorDefinitionMap.get(code);
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("errorDefinitionRegisters", this.getErrorDefinitionRegisters());
        this.initialize();
    }

    protected void initialize() {
        // 获取原始的错误定义的集合
        List<ErrorDefinition> errorDefinitions = new LinkedList<>();
        for (ErrorDefinitionRegister errorDefinitionRegister : this.errorDefinitionRegisters) {
            errorDefinitionRegister.register(errorDefinitions);
        }

        // 如果没有错误定义，那么直接返回空的字典
        if (errorDefinitions.isEmpty()) {
            this.errorDefinitionMap = Collections.emptyMap();
            return;
        }

        // 将错误定义按照优先度排序
        // 数值越小的优先度越高，越靠后被合并（越可以覆盖优先度低的错误定义）
        errorDefinitions.sort((o1, o2) -> {
            int order1 = o1.getOrder();
            int order2 = o2.getOrder();
            return -Integer.compare(order1, order2);
        });

        // 合并成错误定义的集合
        Map<String, ErrorDefinition> tempMap = new HashMap<>(MapUtil.hashInitCapacity(errorDefinitions.size()));
        for (ErrorDefinition errorDefinition : errorDefinitions) {
            String code = errorDefinition.getCode();
            ErrorDefinition currentDefinition = tempMap.get(code);

            // 如果尚未出现在 tempMap 中，那么直接赋值
            if (currentDefinition == null) {
                tempMap.put(code, errorDefinition);
                continue;
            }

            // 如果已经出现在 tempMap 中，那么进行合并
            DefaultErrorDefinition mergedDefinition = null;
            if (currentDefinition instanceof DefaultErrorDefinition) {
                mergedDefinition = (DefaultErrorDefinition) currentDefinition;
            } else {
                mergedDefinition = new DefaultErrorDefinition();
                mergedDefinition.load(currentDefinition, true);
                tempMap.put(code, mergedDefinition);
            }
            mergedDefinition.load(errorDefinition, true);
        }

        // 更新字典
        this.errorDefinitionMap = tempMap;
    }
}
