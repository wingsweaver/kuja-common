package com.wingsweaver.kuja.common.utils.exception;

import java.util.Map;
import java.util.UUID;

public class DummyErrorInfoExporterFactory implements ErrorInfoExporterFactory {
    public static final String PREFIX = "dummy-" + UUID.randomUUID() + "-";

    public DummyErrorInfoExporterFactory() {
        System.out.print("DummyErrorInfoExporterFactory instance created");
    }

    @Override
    public ErrorInfoExporter create() {
        return DummyErrorInfoExporterFactory::exportErrorInfo;
    }

    static void exportErrorInfo(Throwable error, Map<String, Object> map, ErrorExportPredicate predicate) {
        if (predicate.includes("dummy")) {
            System.out.print("add dummy attribute to map");
            map.put("dummy", PREFIX + System.currentTimeMillis());
        }
    }
}
