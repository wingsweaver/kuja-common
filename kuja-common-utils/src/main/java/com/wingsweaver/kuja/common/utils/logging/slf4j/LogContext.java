package com.wingsweaver.kuja.common.utils.logging.slf4j;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.wingsweaver.kuja.common.utils.support.lang.NonThrowableCloseable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * 日志上下文工具类。
 *
 * @author wingsweaver
 */
public final class LogContext {
    private LogContext() {
        // 禁止实例化
    }

    private static final ThreadLocal<Config> THREAD_LOCAL = new TransmittableThreadLocal<Config>() {
    };

    public static Config getConfig() {
        return getConfig(false);
    }

    static Config getConfig(boolean createIfAbsent) {
        Config config = THREAD_LOCAL.get();
        if (config == null && createIfAbsent) {
            config = new Config();
            setConfig(config);
        }
        return config;
    }

    public static void setConfig(Config config) {
        if (config != null) {
            THREAD_LOCAL.set(config);
        } else {
            THREAD_LOCAL.remove();
        }
    }

    /**
     * 清除日志上下文配置。
     */
    public static void removeConfig() {
        THREAD_LOCAL.remove();
    }

    public static Logger getLogger() {
        Config config = getConfig(false);
        if (config == null) {
            return null;
        } else {
            return config.getLogger();
        }
    }

    public static void setLogger(Logger logger) {
        if (logger != null) {
            getConfig(true).setLogger(logger);
        } else {
            Config config = getConfig(false);
            if (config != null) {
                config.setLogger(null);
                if (config.isEmpty()) {
                    removeConfig();
                }
            }
        }
    }

    public static void removeLogger() {
        setLogger(null);
    }

    public static Marker getMarker() {
        Config config = getConfig(false);
        if (config == null) {
            return null;
        } else {
            return config.getMarker();
        }
    }

    public static void setMarker(Marker marker) {
        if (marker != null) {
            getConfig(true).setMarker(marker);
        } else {
            Config config = getConfig(false);
            if (config != null) {
                config.setMarker(null);
                if (config.isEmpty()) {
                    removeConfig();
                }
            }
        }
    }

    public static void removeMarker() {
        setMarker(null);
    }

    public static Level getLevel() {
        Config config = getConfig(false);
        if (config == null) {
            return null;
        } else {
            return config.getLevel();
        }
    }

    public static void setLevel(Level level) {
        if (level != null) {
            getConfig(true).setLevel(level);
        } else {
            Config config = getConfig(false);
            if (config != null) {
                config.setLevel(null);
                if (config.isEmpty()) {
                    removeConfig();
                }
            }
        }
    }

    public static void removeLevel() {
        setLevel(null);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Config {
        /**
         * Logger 实例。
         */
        private Logger logger;

        /**
         * Marker 实例。
         */
        private Marker marker;

        /**
         * 日志级别。
         */
        private Level level;

        /**
         * 检查是否为空。
         *
         * @return 如果为空则返回 true，否则返回 false
         */
        public boolean isEmpty() {
            return logger == null && marker == null && level == null;
        }

        /**
         * 空的实例。
         */
        public static final Config EMPTY = new Config();
    }

    /**
     * 临时设置日志上下文配置。
     *
     * @param config 日志上下文配置
     * @return 临时设置日志上下文配置的工具类
     */
    public static TempHolder with(Config config) {
        return new TempHolder(config);
    }

    /**
     * 临时设置日志上下文配置的工具类。
     *
     * @author wingsweaver
     */
    public static class TempHolder implements NonThrowableCloseable {
        /**
         * 旧的日志上下文配置。
         */
        private final Config oldConfig;

        /**
         * 新的日志上下文配置。
         */
        private final Config config;

        public TempHolder(Config config) {
            this.config = config;
            this.oldConfig = LogContext.getConfig(false);
            setConfig(config);
        }

        @Override
        public void close() {
            setConfig(this.oldConfig);
        }

        public Config getOldConfig() {
            return oldConfig;
        }

        public Config getConfig() {
            return config;
        }
    }
}
