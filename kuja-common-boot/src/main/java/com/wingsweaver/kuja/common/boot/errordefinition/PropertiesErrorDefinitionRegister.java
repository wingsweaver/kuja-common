package com.wingsweaver.kuja.common.boot.errordefinition;

import com.wingsweaver.kuja.common.utils.diag.AssertState;
import com.wingsweaver.kuja.common.utils.exception.ExtendedRuntimeException;
import com.wingsweaver.kuja.common.utils.logging.slf4j.LogUtil;
import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;
import com.wingsweaver.kuja.common.utils.support.DefaultOrdered;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import com.wingsweaver.kuja.common.utils.support.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 基于 Properties 定义的 {@link ErrorDefinitionRegister} 实现。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class PropertiesErrorDefinitionRegister implements ErrorDefinitionRegister, ApplicationContextAware,
        InitializingBean, DefaultOrdered {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesErrorDefinitionRegister.class);

    public static final String KEY_ORDER = "order";

    public static final String KEY_CODE = "code";

    public static final String KEY_MESSAGE = "message";

    public static final String KEY_USER_TIP = "userTip";

    public static final String KEY_TAGS = "tags";

    public static final String KEY_TEMPS = "temps";

    public static final char CHAR_WILDCARD = '*';

    private ApplicationContext applicationContext;

    private ErrorDefinitionProperties properties;

    @Override
    public void register(Collection<ErrorDefinition> errorDefinitions) {
        List<String> locations = properties.getLocations();
        if (CollectionUtils.isEmpty(locations)) {
            return;
        }

        for (String location : locations) {
            this.registerResource(errorDefinitions, location);
        }
    }

    private void registerResource(Collection<ErrorDefinition> errorDefinitions, String location) {
        LogUtil.trace(LOGGER, "registering error definitions from location [{}]", location);

        // 解析资源
        Resource[] resources = null;
        try {
            resources = this.applicationContext.getResources(location);
        } catch (IOException ex) {
            if (this.properties.isFailFast()) {
                throw new ExtendedRuntimeException("failed to resolve location pattern: " + location, ex)
                        .withExtendedAttribute("location", location);
            }
            LogUtil.info(LOGGER, ex, "failed to register error definitions from location [{}]", location);
            return;
        }

        // 逐个资源进行处理
        for (Resource resource : resources) {
            try {
                this.registerResourceInternal(errorDefinitions, resource);
            } catch (RuntimeException ex) {
                if (this.properties.isFailFast()) {
                    throw ex;
                }
                LogUtil.info(LOGGER, ex, "failed to register error definitions from resource [{}]", resource);
            }
        }
    }

    private void registerResourceInternal(Collection<ErrorDefinition> errorDefinitions, Resource resource) {
        // 检查资源是否存在
        if (!resource.exists()) {
            LogUtil.trace(LOGGER, "resource [{}] not exists, skip registering error definitions", resource);
            return;
        }

        // 读取资源中的 Properties
        Properties properties = new Properties();
        try (InputStream inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new ExtendedRuntimeException(e).withExtendedAttribute("resource", resource);
        }

        // 从 Properties 中解析错误定义
        Map<String, ? extends ErrorDefinition> errorDefinitionMap = this.parseErrorDefinitions(properties);

        // 加入到 errorDefinitions 集合中
        errorDefinitions.addAll(errorDefinitionMap.values());
    }

    private Map<String, ? extends ErrorDefinition> parseErrorDefinitions(Properties properties) {
        Map<String, DefaultErrorDefinition> map = new HashMap<>(MapUtil.hashInitCapacity(properties.size()));
        SortedMap<Object, Object> sortedProperties = new TreeMap<>(properties);
        for (Map.Entry<Object, Object> entry : sortedProperties.entrySet()) {
            Tuple2<String, String> tuple2 = this.parsePropertiesKey(entry.getKey().toString());
            String prefix = tuple2.getT1();
            if (StringUtil.isEmpty(prefix)) {
                // 忽略无效的设置（没有分隔符的字段）
                continue;
            }

            // 查找 map 中对应的 ErrorDefinition
            DefaultErrorDefinition errorDefinition = map.get(prefix);
            if (errorDefinition == null) {
                errorDefinition = new DefaultErrorDefinition();
                errorDefinition.setCode(prefix);
                map.put(prefix, errorDefinition);
            }

            // 更新错误定义中的数据
            String value = entry.getValue().toString();
            String key = tuple2.getT2();
            if (StringUtil.isEmpty(key)) {
                // 如果只设置了 prefix，而没有设置 key，则认为是设置了 message
                // 即 xxxx=something wrong 这种设置，解析为
                // xxxx.code=xxxx
                // xxxx.message=something wrong
                errorDefinition.setMessage(entry.getValue().toString());
            } else {
                switch (key) {
                    case KEY_ORDER:
                        errorDefinition.setOrder(Integer.parseInt(value));
                        break;
                    case KEY_CODE:
                        errorDefinition.setCode(value);
                        break;
                    case KEY_MESSAGE:
                        errorDefinition.setMessage(value);
                        break;
                    case KEY_USER_TIP:
                        errorDefinition.setUserTip(value);
                        break;
                    default:
                        if (key.startsWith(KEY_TAGS)) {
                            String tagKey = key.substring(KEY_TAGS.length() + 1).trim();
                            if (StringUtil.isNotEmpty(tagKey)) {
                                errorDefinition.getTags(true).put(tagKey, value);
                            }
                        } else if (key.startsWith(KEY_TEMPS)) {
                            String tempKey = key.substring(KEY_TEMPS.length() + 1).trim();
                            if (StringUtil.isNotEmpty(tempKey)) {
                                errorDefinition.getTemps(true).put(tempKey, value);
                            }
                        } else {
                            // 当作 temps 进行处理
                            errorDefinition.getTemps(true).put(key, value);
                        }
                        break;
                }
            }
        }

        // 返回
        return map;
    }

    private Tuple2<String, String> parsePropertiesKey(String key) {
        String errorCode = null;
        String subKey = null;
        int index = key.indexOf(this.properties.getDelimiter());
        if (index > 0 && index < key.length() - 1) {
            errorCode = key.substring(0, index).trim();
            subKey = key.substring(index + 1).trim();
        } else {
            errorCode = key.trim();
        }
        return Tuple2.of(errorCode, subKey);
    }

    @Override
    public void afterPropertiesSet() {
        AssertState.Named.notNull("applicationContext", this.getApplicationContext());
        AssertState.Named.notNull("properties", this.getProperties());
    }
}
