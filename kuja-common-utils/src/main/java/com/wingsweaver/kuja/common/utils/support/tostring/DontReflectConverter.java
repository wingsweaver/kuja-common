package com.wingsweaver.kuja.common.utils.support.tostring;

import java.lang.reflect.AnnotatedElement;

/**
 * {@link DontReflect} 注解相应的 {@link ToStringWithConverter} 实现类。
 *
 * @author wingsweaver
 */
public class DontReflectConverter extends AbstractToStringWithConverter<DontReflect>
        implements ConfigurableToStringConverter<Void> {

    public DontReflectConverter() {
        super(DontReflect.class);
    }

    @Override
    protected void toStringInternal(Object value, AnnotatedElement annotatedElement, DontReflect annotation,
                                    StringBuilder builder, ToStringConfig config) {
        this.toString(value, builder, config, null);
    }

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config, Void customConfig) {
        builder.append(object);
        return true;
    }
}
