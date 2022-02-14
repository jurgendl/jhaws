package org.jhaws.common.elasticsearch.common;

public enum CharacterFilter {
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-htmlstrip-charfilter.html
     */
    html_strip, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-mapping-charfilter.html
     */
    mapping, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern-replace-charfilter.html
     */
    pattern_replace,//
    ;

    private String id;

    private CharacterFilter() {
        this.id = name();
    }

    public String id() {
        return id;
    }
}
