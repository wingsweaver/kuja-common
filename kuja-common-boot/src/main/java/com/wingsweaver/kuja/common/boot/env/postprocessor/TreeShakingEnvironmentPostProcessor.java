package com.wingsweaver.kuja.common.boot.env.postprocessor;

import com.wingsweaver.kuja.common.boot.env.PropertySourceUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.util.LinkedList;
import java.util.List;

/**
 * 从 {@link ConfigurableEnvironment} 中删除一组指定 PropertySource 的 {@link EnvironmentPostProcessor} 实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class TreeShakingEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private int order;

    private List<String> propertyNames;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (this.getPropertyNames() != null) {
            MutablePropertySources propertySources = environment.getPropertySources();
            this.getPropertyNames().forEach(propertyName -> PropertySourceUtil.remove(propertySources, propertyName));
        }
    }

    public List<String> getPropertyNames(boolean createIfAbsent) {
        if (this.propertyNames == null && createIfAbsent) {
            this.propertyNames = new LinkedList<>();
        }
        return propertyNames;
    }
}
