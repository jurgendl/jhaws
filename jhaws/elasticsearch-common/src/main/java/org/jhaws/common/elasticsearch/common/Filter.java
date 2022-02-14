package org.jhaws.common.elasticsearch.common;

public enum Filter {
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-apostrophe-tokenfilter.html
     */
    apostrophe, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-asciifolding-tokenfilter.html
     */
    asciifolding, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-cjk-bigram-tokenfilter.html
     */
    cjk_bigram, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-cjk-width-tokenfilter.html
     */
    cjk_width, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-classic-tokenfilter.html
     */
    classic, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-common-grams-tokenfilter.html
     */
    common_grams, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-condition-tokenfilter.html
     */
    condition, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-decimal-digit-tokenfilter.html
     */
    decimal_digit, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-delimited-payload-tokenfilter.html
     */
    delimited_payload_filter, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-dict-decomp-tokenfilter.html
     */
    dictionary_decompounder, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-edgengram-tokenfilter.html
     */
    edge_ngram, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-elision-tokenfilter.html
     */
    elision, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-fingerprint-tokenfilter.html
     */
    fingerprint, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-flatten-graph-tokenfilter.html
     */
    flatten_graph, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-hunspell-tokenfilter.html
     */
    hunspell, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-hyp-decomp-tokenfilter.html
     */
    hyphenation_decompounder, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keep-types-tokenfilter.html
     */
    keep_types, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keep-words-tokenfilter.html
     */
    keep, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keyword-marker-tokenfilter.html
     */
    keyword_marker, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keyword-repeat-tokenfilter.html
     */
    keyword_repeat, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-kstem-tokenfilter.html
     */
    kstem, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-length-tokenfilter.html
     */
    length, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-limit-token-count-tokenfilter.html
     */
    limit, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lowercase-tokenfilter.html
     */
    lowercase, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-minhash-tokenfilter.html
     */
    min_hash, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-multiplexer-tokenfilter.html
     */
    multiplexer, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-ngram-tokenfilter.html
     */
    ngram, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    arabic_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    german_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    hindi_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    indic_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    sorani_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    persian_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    scandinavian_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    scandinavian_folding, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-normalization-tokenfilter.html
     */
    serbian_normalization, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern-capture-tokenfilter.html
     */
    pattern_capture, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern_replace-tokenfilter.html
     */
    pattern_replace, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-phonetic-tokenfilter.html
     */
    phonetic, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-porterstem-tokenfilter.html
     */
    porter_stem, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-predicatefilter-tokenfilter.html
     */
    predicate_token_filter, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-remove-duplicates-tokenfilter.html
     */
    remove_duplicates, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-reverse-tokenfilter.html
     */
    reverse, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-shingle-tokenfilter.html
     */
    shingle, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-snowball-tokenfilter.html
     */
    snowball, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-stemmer-tokenfilter.html
     */
    stemmer, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-stemmer-override-tokenfilter.html
     */
    stemmer_override, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-stop-tokenfilter.html
     */
    stop, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-synonym-tokenfilter.html
     */
    synonym, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-synonym-graph-tokenfilter.html
     */
    graph_synonyms, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-trim-tokenfilter.html
     */
    trim, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-truncate-tokenfilter.html
     */
    truncate, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-unique-tokenfilter.html
     */
    unique, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-uppercase-tokenfilter.html
     */
    uppercase, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-word-delimiter-tokenfilter.html
     */
    word_delimiter, //
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-word-delimiter-graph-tokenfilter.html
     */
    word_delimiter_graph, //
    //
    //
    // https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-phonetic.html
    // bin/elasticsearch-plugin install analysis-phonetic
    //
    // encoder: metaphone (default), double_metaphone, soundex, refined_soundex, caverphone1, caverphone2, cologne, nysiis, koelnerphonetik,
    // haasephonetik, beider_morse, daitch_mokotoff
    //
    // replace: Whether or not the original token should be replaced by the phonetic token. Accepts true (default) and false. Not supported by
    // beider_morse encoding.
    //
    //
    // Double metaphone settings
    // edit
    //
    // If the double_metaphone encoder is used, then this additional setting is supported:
    //
    // max_code_len
    // The maximum length of the emitted metaphone token. Defaults to 4.
    //
    // Beider Morse settings
    // edit
    //
    // If the beider_morse encoder is used, then these additional settings are supported:
    //
    // rule_type
    // Whether matching should be exact or approx (default).
    // name_type
    // Whether names are ashkenazi, sephardic, or generic (default).
    // languageset
    // An array of languages to check. If not specified, then the language will be guessed. Accepts: any, common, cyrillic, english, french, german,
    // hebrew, hungarian, polish, romanian, russian, spanish.
    //
    //
    // "filter": {
    // "my_metaphone": {
    // "type": "phonetic",
    // "encoder": "metaphone",
    // "replace": false
    // }
    // }
    ;

    private String id;

    private Filter() {
        this.id = name();
    }

    public String id() {
        return id;
    }
}
