package com.wingsweaver.kuja.common.utils.support.tostring;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 有多个 {@link ToStringConverter} 组合而成的实现类。
 *
 * @author wingsweaver
 */
@Getter
@Setter
public class CompositeToStringConverter implements ToStringConverter {
    private int order;

    private List<ToStringConverter> converters;

    @Override
    public boolean toString(Object object, StringBuilder builder, ToStringConfig config) {
        if (this.converters != null) {
            for (ToStringConverter converter : this.converters) {
                if (converter.toString(object, builder, config)) {
                    return true;
                }
            }
        }
        return false;
    }
}
