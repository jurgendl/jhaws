package org.jhaws.common.elasticsearch8.impl;

public enum HighlighterType {
    unified//
    , plain//
    , fvh//
    ;

    private String id;

    private HighlighterType() {
        this.id = name();
    }

    private HighlighterType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
