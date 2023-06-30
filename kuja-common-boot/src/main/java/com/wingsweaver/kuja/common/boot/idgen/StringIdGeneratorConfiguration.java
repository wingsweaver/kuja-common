package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.model.AbstractConfiguration;
import com.wingsweaver.kuja.common.utils.diag.AssertArgs;
import com.wingsweaver.kuja.common.utils.model.AbstractComponent;
import com.wingsweaver.kuja.common.utils.support.idgen.LongIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.StringIdGenerator;
import com.wingsweaver.kuja.common.utils.support.idgen.UuidStringIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link StringIdGenerator} 的自动配置。
 *
 * @author wingsweaver
 */
@Configuration(proxyBeanMethods = false)
public class StringIdGeneratorConfiguration extends AbstractConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public StringIdGenerator kujaStringIdGenerator() {
        return new SmartStringIdGenerator(new UuidStringIdGenerator());
    }

    /**
     * {@link StringIdGenerator} 的智能实现类。<br>
     * 如果有 {@link LongIdGenerator} 类型的 Bean，则使用 {@link LongIdGenerator} 生成的 ID；
     * 否则使用指定的 {@link StringIdGenerator} 生成的 ID。
     *
     * @author wingsweaver
     */
    static class SmartStringIdGenerator extends AbstractComponent implements StringIdGenerator {
        private LongIdGenerator longIdGenerator;

        private final StringIdGenerator stringIdGenerator;

        public SmartStringIdGenerator(StringIdGenerator stringIdGenerator) {
            AssertArgs.Named.notNull("stringIdGenerator", stringIdGenerator);
            this.stringIdGenerator = stringIdGenerator;
        }

        @Override
        public String nextId() {
            if (this.longIdGenerator != null) {
                Long value = this.longIdGenerator.nextId();
                return (value != null) ? value.toString() : null;
            } else {
                return this.stringIdGenerator.toString();
            }
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            super.afterPropertiesSet();

            // 初始化 longIdGenerator
            this.initLongIdGenerator();
        }

        private void initLongIdGenerator() {
            if (this.longIdGenerator == null) {
                try {
                    this.longIdGenerator = this.getBean(LongIdGenerator.class);
                } catch (Exception ignored) {
                    // 忽略此错误
                }
            }
        }
    }
}
