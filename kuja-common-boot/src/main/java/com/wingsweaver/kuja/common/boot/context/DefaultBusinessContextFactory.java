package com.wingsweaver.kuja.common.boot.context;

import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.model.id.IdSetter;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link BusinessContext} 工厂类的默认实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class DefaultBusinessContextFactory extends AbstractComponent implements BusinessContextFactory {
    /**
     * Id 生成器。
     */
    private StringIdGenerator idGenerator;

    @Override
    public BusinessContext create() {
        return this.create(null);
    }

    @SuppressWarnings({"unchecked", "ConstantValue"})
    @Override
    public BusinessContext create(BusinessContext parent) {
        BusinessContext businessContext = (parent != null) ? new LayeredBusinessContext(parent) : new MapBusinessContext();
        if (businessContext instanceof IdSetter) {
            ((IdSetter<String>) businessContext).setId(this.idGenerator.nextId());
        }
        return businessContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        // 初始化 idGenerator
        this.initIdGenerator();
    }

    /**
     * 初始化 ID 生成器。
     */
    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            this.idGenerator = this.getBean(StringIdGenerator.class, () -> StringIdGenerator.FALLBACK);
        }
    }
}
