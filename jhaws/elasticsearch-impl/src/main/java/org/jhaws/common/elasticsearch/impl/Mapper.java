package org.jhaws.common.elasticsearch.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@FunctionalInterface
public interface Mapper<T> {
    static final Mapper<Map<String, Object>> TO_MAP = (index, id, version, json, map, calculatedFields) -> map;

    public static Mapper<Map<String, Object>> toMap() {
        return TO_MAP;
    }

    public static <T> Mapper<T> toObject(ObjectMapper om, Class<T> type) {
        return (index, id, version, json, map, calculatedFields) -> ElasticHelper.toObject(om, type, index, id, version, json, calculatedFields);
    }

    T map(String index, String id, Long version, String json, Map<String, Object> map, Map<String, Object> calculatedFields);
}
