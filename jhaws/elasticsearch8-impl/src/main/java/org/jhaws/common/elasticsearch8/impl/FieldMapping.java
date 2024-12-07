package org.jhaws.common.elasticsearch8.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jhaws.common.elasticsearch.common.FieldType;
import org.jhaws.common.elasticsearch.common.Language;

public class FieldMapping {
    public Map<String, Object> fieldMapping = new LinkedHashMap<>();

    public Language language = Language.dutch;

    public FieldType fieldType;

    public void put(String key, Object value) {
        this.fieldMapping.put(key, value);
    }

    @Override
    public String toString() {
        return "FieldMapping [" + (this.fieldType != null ? "fieldType=" + this.fieldType + ", " : "") + (this.language != null ? "language=" + this.language + ", " : "") + (this.fieldMapping != null ? "fieldMapping=" + this.fieldMapping : "") + "]";
    }
}
