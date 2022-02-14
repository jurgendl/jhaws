package org.jhaws.common.elasticsearch.common;

public enum Tokenizer {
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-chargroup-tokenizer.html
     */
    char_group, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-classic-tokenizer.html
     */
    classic, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-edgengram-tokenizer.html
     */
    edge_ngram, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keyword-tokenizer.html
     */
    keyword, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-letter-tokenizer.html
     */
    letter, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lowercase-tokenizer.html
     */
    lowercase, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-ngram-tokenizer.html
     */
    ngram, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pathhierarchy-tokenizer.html
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pathhierarchy-tokenizer-examples.html
     */
    path_hierarchy, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern-tokenizer.html
     */
    pattern, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-simplepattern-tokenizer.html
     */
    simple_pattern, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-simplepatternsplit-tokenizer.html
     */
    simple_pattern_split, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-standard-tokenizer.html
     */
    standard, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-thai-tokenizer.html
     */
    thai, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-uaxurlemail-tokenizer.html
     */
    uax_url_email, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-whitespace-tokenizer.html
     */
    whitespace,//
    ;

    private String id;

    private Tokenizer() {
        this.id = name();
    }

    public String id() {
        return id;
    }
}
