package com.wingsweaver.kuja.common.utils.model.tags.convert;

import com.wingsweaver.kuja.common.utils.model.tuple.Tuple2;

import java.lang.reflect.Type;
import java.util.Collection;

public class DummyTagConversionServiceConfigurer implements TagConversionServiceConfigurer {
    public DummyTagConversionServiceConfigurer() {
        System.out.println("DummyTagConversionServiceConfigurer instance created");
    }

    @Override
    public void registerConverters(Collection<TagValueConverter> converters) {
        System.out.println("register DummyTagValueConverter ...");
        converters.add(new DummyTagValueConverter());
    }

    static class DummyTagValueConverter implements TagValueConverter {
        @Override
        public Tuple2<Boolean, Object> convertTo(Object source, Type type) {
            if (type == Dummy.class) {
                if (source == null) {
                    throw new RuntimeException("source is null");
                }
                return Tuple2.of(true, new Dummy(source));
            }
            return Tuple2.of(false, null);
        }
    }

    @Override
    public void registerWriters(Collection<TagValueWriter> writers) {
        System.out.println("register DummyTagValueWriter ...");
        writers.add(new DummyTagValueWriter());
    }

    static class DummyTagValueWriter implements TagValueWriter {

        @Override
        public Tuple2<Boolean, String> saveAsText(Object value) {
            if (value instanceof Dummy) {
                Object data = ((Dummy) value).getData();
                if (data == null) {
                    throw new RuntimeException("data is null");
                }
                return Tuple2.of(true, "dummy-" + data);
            }
            return Tuple2.of(false, null);
        }
    }
}
