package com.wingsweaver.kuja.common.boot.idgen;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.utils.model.AbstractPojo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.sql.Statement;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * WorkerId 的生成设置。
 *
 * @author wingsweaver
 */
@Getter
@Setter
@ConfigurationProperties(prefix = KujaCommonBootKeys.PREFIX_ID_GENERATOR_WORKER_ID)
public class WorkerIdResolverProperties extends AbstractPojo {
    /**
     * 是否启用。
     */
    private boolean enabled = true;

    /**
     * BIT 数。
     */
    private int bits = 10;

    /**
     * 是否裁剪到掩码值。
     */
    private boolean trimToMaskValue = true;

    /**
     * 模式值。
     */
    private Type type;

    /**
     * 固定值模式的设置。
     */
    @NestedConfigurationProperty
    private FixedProperties fixed = new FixedProperties();

    /**
     * JDBC 模式的设置。
     */
    private JdbcProperties jdbc = new JdbcProperties();

    /**
     * Redis 模式的设置。
     */
    private RedisProperties redis = new RedisProperties();

    /**
     * Redisson 模式的设置。
     */
    private RedissonProperties redisson = new RedissonProperties();

    /**
     * 模式定义。
     */
    public enum Type {
        /**
         * 固定值。
         */
        FIXED,

        /**
         * Local PID。
         */
        LOCAL_PID,

        /**
         * JDBC。
         */
        JDBC,

        /**
         * Redis。
         */
        REDIS,

        /**
         * Redisson。
         */
        REDISSON
    }

    /**
     * 固定值模式的设置。
     */
    @Getter
    @Setter
    public static class FixedProperties extends AbstractPojo {
        /**
         * 工作节点 ID。
         */
        private int workerId;
    }

    /**
     * JDBC 模式的设置。
     */
    @Getter
    @Setter
    public static class JdbcProperties extends AbstractPojo {
        /**
         * JdbcTemplate Bean 的名称。
         */
        private String jdbcTemplate;

        /**
         * 获取 WorkerId 的 SQL 脚本。
         */
        private String sql;

        /**
         * 是否自动生成 Key。
         */
        private int autoGeneratedKeys = Statement.RETURN_GENERATED_KEYS;

        /**
         * PrepareStatement 的设置器的 Bean 定义。
         */
        private String preparedStatementSetter;
    }

    /**
     * 可续租的设置。
     */
    @Getter
    @Setter
    public abstract static class AbstractRenewableProperties extends AbstractPojo {
        /**
         * 是否启用自动续约。
         */
        private boolean autoRenew = true;

        /**
         * 用于定期续约 WorkerId 的 ScheduledExecutorService 实例的名称。
         */
        private String scheduledExecutorService;

        /**
         * 续约任务的时间单位。
         */
        private TimeUnit renewTimeUnit = TimeUnit.MINUTES;

        /**
         * 续约任务的时间间隔的下限值。
         */
        private long renewIntervalMin = 10;

        /**
         * 续约任务的时间间隔的上限值。
         */
        private long renewIntervalMax = 20;
    }

    /**
     * Redis 模式的设置。
     */
    @Getter
    @Setter
    public static class RedisProperties extends AbstractRenewableProperties {
        /**
         * RedisTemplate Bean 的名称。
         */
        private String redisTemplate;

        /**
         * 获取 WorkerId 的 Redis Key。
         */
        private String key;

        /**
         * WorkerId 节点的值。
         */
        private String workerIdValue;

        /**
         * WorkerId 节点的有效期间。
         */
        private Duration workerIdExpiration = Duration.of(20, ChronoUnit.MINUTES);
    }

    /**
     * Redisson 模式的设置。
     */
    @Getter
    @Setter
    public static class RedissonProperties extends AbstractRenewableProperties {
        /**
         * RedissonClient Bean 的名称。
         */
        private String redissonClient;

        /**
         * 获取 WorkerId 的 Redis Key。
         */
        private String key;

        /**
         * WorkerId 节点的值。
         */
        private String workerIdValue;

        /**
         * WorkerId 节点的有效期间。
         */
        private Duration workerIdExpiration = Duration.of(20, ChronoUnit.MINUTES);
    }
}
