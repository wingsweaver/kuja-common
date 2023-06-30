package com.wingsweaver.kuja.common.messaging.autoconfigurer.core.redis;

import com.wingsweaver.kuja.common.messaging.autoconfigurer.core.constants.KujaCommonMessagingKeys;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.support.lang.ClassUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于导入 Redis 发送服务的 {@link ImportSelector} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class RedisSendServiceImportSelector implements ImportSelector, EnvironmentAware {
    /**
     * 应用环境。
     */
    private Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> classNames = new ArrayList<>(BufferSizes.TINY);

        // 解析 RedisSendServiceProperties
        RedisSendServiceProperties properties =
                Binder.get(this.environment)
                        .bindOrCreate(KujaCommonMessagingKeys.PREFIX_REDIS_SEND_SERVICE, RedisSendServiceProperties.class);

        // 检查是否是 Reactive 模式
        boolean preferReactive = properties.isPreferReactive();
        boolean reactiveModeAllowed = ClassUtil.existsAll(
                "org.springframework.data.redis.core.ReactiveStringRedisTemplate",
                "reactor.core.publisher.Flux");
        boolean reactiveMode;
        if (preferReactive) {
            reactiveMode = (!Boolean.FALSE.equals(properties.getReactive())) && reactiveModeAllowed;
        } else {
            reactiveMode = Boolean.TRUE.equals(properties.getReactive()) && reactiveModeAllowed;
        }

        // 根据不同的模式，导入对应的配置类
        if (reactiveMode) {
            classNames.add("com.wingsweaver.kuja.common.messaging.boot.redis.ReactiveRedisSendServiceConfiguration");
        } else {
            classNames.add("com.wingsweaver.kuja.common.messaging.boot.redis.BlockingRedisSendServiceConfiguration");
        }

        // 返回
        return classNames.toArray(new String[0]);
    }
}
