package com.wingsweaver.kuja.common.boot.appinfo;

import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootKeys;
import com.wingsweaver.kuja.common.boot.constants.KujaCommonBootOrders;
import com.wingsweaver.kuja.common.boot.env.postprocessor.FallbackEnvironmentProcessor;
import com.wingsweaver.kuja.common.utils.constants.BufferSizes;
import com.wingsweaver.kuja.common.utils.model.semconv.ResourceAttributes;
import com.wingsweaver.kuja.common.utils.model.semconv.SemanticAttributes;
import com.wingsweaver.kuja.common.utils.support.lang.StringUtil;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用于更新应用信息的 {@link FallbackEnvironmentProcessor} 实现类。
 *
 * @author wingsweaver
 */
public class AppInfoFallbackEnvironmentProcessor implements FallbackEnvironmentProcessor {
    @Override
    public void process(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> map = new HashMap<>(BufferSizes.SMALL);
        this.updateAppInfo(environment, map);
        MapPropertySource propertySource = new MapPropertySource(KujaCommonBootKeys.PropertySourceNames.APP_INFO_FALLBACK, map);
        environment.getPropertySources().addLast(propertySource);
    }

    /**
     * 更新 AppInfo 信息。<br>
     * 参照 {@link ResourceAttributes.Service}。
     *
     * @param environment 环境对象
     * @param map         属性映射
     */
    protected void updateAppInfo(ConfigurableEnvironment environment, Map<String, Object> map) {
        this.updateHost(environment, map);
        this.updateOs(environment, map);
        this.updateProcess(environment, map);
        this.updateService(environment, map);
        this.updateNet(environment, map);

        // 加入 AppInfo 的前缀
        Map<String, Object> tempMap = new HashMap<>(map);
        String prefix = KujaCommonBootKeys.PREFIX_APP + ".";
        tempMap.forEach((key, value) -> {
            map.putIfAbsent(prefix + key, value);
        });
    }

    /**
     * 更新 Net 信息。<br>
     * 参照 {@link SemanticAttributes.Net}。
     *
     * @param environment 环境对象
     * @param map         属性映射
     */
    protected void updateNet(ConfigurableEnvironment environment, Map<String, Object> map) {
        map.put(SemanticAttributes.Net.KEY_HOST_NAME, map.get(ResourceAttributes.Host.KEY_NAME));
        String serverPort = environment.getProperty("server.port");
        if (StringUtil.isNotEmpty(serverPort)) {
            map.put(SemanticAttributes.Net.KEY_HOST_PORT, Integer.parseInt(serverPort));
        }
    }

    /**
     * 更新 OS 信息。<br>
     * 参照 {@link ResourceAttributes.OS}。
     *
     * @param environment 环境对象
     * @param map         属性映射
     */
    protected void updateOs(ConfigurableEnvironment environment, Map<String, Object> map) {
        // os.name
        map.put(ResourceAttributes.OS.KEY_NAME, SystemUtils.OS_NAME);

        // os.version
        map.put(ResourceAttributes.OS.KEY_VERSION, SystemUtils.OS_VERSION);

        // os.type
        if (SystemUtils.IS_OS_WINDOWS) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.WINDOWS);
        } else if (SystemUtils.IS_OS_MAC) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.DARWIN);
        } else if (SystemUtils.IS_OS_LINUX) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.LINUX);
        } else if (SystemUtils.IS_OS_FREE_BSD) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.FREEBSD);
        } else if (SystemUtils.IS_OS_NET_BSD) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.NETBSD);
        } else if (SystemUtils.IS_OS_OPEN_BSD) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.OPENBSD);
        } else if (SystemUtils.IS_OS_AIX) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.AIX);
        } else if (SystemUtils.IS_OS_HP_UX) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.HPUX);
        } else if (SystemUtils.IS_OS_SOLARIS) {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.SOLARIS);
        } else {
            map.put(ResourceAttributes.OS.KEY_TYPE, ResourceAttributes.OS.Types.OTHERS);
        }
    }

    /**
     * 更新服务信息。<br>
     * 参照 {@link ResourceAttributes.Service}。
     *
     * @param environment 环境对象
     * @param map         属性映射
     */
    protected void updateService(ConfigurableEnvironment environment, Map<String, Object> map) {
        // service.name
        String serviceName = environment.getProperty("spring.application.name");
        if (serviceName != null) {
            map.put(ResourceAttributes.Service.KEY_NAME, serviceName);
        }

        // service.instance-id
        map.put(ResourceAttributes.Service.KEY_INSTANCE_ID, UUID.randomUUID().toString());
    }

    /**
     * 更新进程信息。<br>
     * 参照 {@link ResourceAttributes.Process}。
     *
     * @param environment 环境对象
     * @param map         属性映射
     */
    protected void updateProcess(ConfigurableEnvironment environment, Map<String, Object> map) {
        // 进程 ID
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String pidText = runtime.getName().split("@")[0];
        map.put(ResourceAttributes.Process.KEY_PID, Long.valueOf(pidText));

        // Runtime 信息
        map.put(ResourceAttributes.Process.KEY_RUNTIME_NAME, runtime.getVmName());
        map.put(ResourceAttributes.Process.KEY_RUNTIME_VERSION, runtime.getVmVersion());
        map.put(ResourceAttributes.Process.KEY_RUNTIME_DESCRIPTION, SystemUtils.JAVA_VM_INFO);
    }

    /**
     * 更新主机信息。<br>
     * 参照 {@link ResourceAttributes.Host}。
     *
     * @param environment 环境对象
     * @param map         属性映射
     */
    protected void updateHost(ConfigurableEnvironment environment, Map<String, Object> map) {
        // 主机名称
        String hostName = environment.getProperty("spring.cloud.client.hostname", SystemUtils.getHostName());
        map.put(ResourceAttributes.Host.KEY_NAME, hostName);

        // 主机地址
        String hostAddress = environment.getProperty("spring.cloud.client.ip-address");
        if (StringUtil.isEmpty(hostAddress)) {
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                // 忽略此错误
            }
        }
        map.put(ResourceAttributes.Host.KEY_ADDRESS, hostAddress);

        // 主机架构信息
        map.put(ResourceAttributes.Host.KEY_ARCH, SystemUtils.OS_ARCH);
    }

    @Override
    public int getOrder() {
        return KujaCommonBootOrders.APP_INFO_FALLBACK_ENVIRONMENT_PROCESSOR;
    }
}
