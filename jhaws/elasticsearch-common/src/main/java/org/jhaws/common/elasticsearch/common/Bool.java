package org.jhaws.common.elasticsearch.common;

public enum Bool {
    TRUE("true", Boolean.TRUE), //
    FALSE("false", Boolean.FALSE), //
    uninitialized(null, null);

    private String id;

    private Boolean value = null;

    private Bool(String id, Boolean value) {
        this.id = id;
        this.value = value;
    }

    public String id() {
        return id;
    }

    public Boolean value() {
        return value;
    }
}
