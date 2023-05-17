package org.jhaws.common.elasticsearch.common;

import java.util.Map;

/**
 * <b>alle datatypes ondersteunen arrays</b><br>
 * <br>
 * <b>long</b>: A signed 64-bit integer with a minimum value of -2<sup>63</sup> and a maximum value of 2<sup>63-1</sup>.<br>
 * <b>integer</b>: A signed 32-bit integer with a minimum value of -2<sup>31</sup> and a maximum value of 2<sup>31-1</sup>.<br>
 * <b>short</b>: A signed 16-bit integer with a minimum value of -32,768 and a maximum value of 32,767.<br>
 * <b>byte</b>: A signed 8-bit integer with a minimum value of -128 and a maximum value of 127.<br>
 * <b>double</b>: A double-precision 64-bit IEEE 754 floating point number, restricted to finite values.<br>
 * <b>float</b>: A single-precision 32-bit IEEE 754 floating point number, restricted to finite values.<br>
 * <b>half_float</b>: A half-precision 16-bit IEEE 754 floating point number, restricted to finite values.<br>
 * <b>scaled_float</b>: A floating point number that is backed by a long, scaled by a fixed double scaling factor.<br>
 * <br>
 * <table border=1>
 * <tr>
 * <td>Type</td>
 * <td>Minimum value</td>
 * <td>Maximum value</td>
 * <td>Significant bits / digits
 * </tr>
 * <tr>
 * <td>double</td>
 * <td>2<sup>-1074</sup></td>
 * <td>(2-2<sup>-52</sup>)·2<sup>1023</sup></td>
 * <td>53 / 15.95</td>
 * </tr>
 * <tr>
 * <td>float</td>
 * <td>2<sup>-149</sup></td>
 * <td>(2-2<sup>-23</sup>)·2<sup>127</sup></td>
 * <td>24 / 7.22</td>
 * </tr>
 * <tr>
 * <td>half_float</td>
 * <td>2<sup>-24</sup></td>
 * <td>65504</td>
 * <td>11 / 3.31</td>
 * </tr>
 * </table>
 * <br>
 * <b><font color=purple>numerische types zijn beter voor range queries, keyword is beter voor term queries ed. en zijn ook over het algemeen sneller</font>, gebruik een 'multi
 * field' als je niet zeker bent</b><br>
 * <br>
 * <b><u>extra properties:</u></b><br>
 * <b>meta</b><br>
 * key max 20, value max 50 lang<br>
 * bv: <i>"meta": { "unit": "ms" }</i><br>
 * <b>coerce</b><br>
 * default true, Try to convert strings to numbers and truncate fractions for integers, enkel voor numerische velden<br>
 * 
 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html
 */
public enum FieldType {
    uninitialized(null), //
    /**
     * full text search, KAN NIET SORTEREN<br>
     * <b>store</b> (false)<br>
     * <b>term_vector</b> (no)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/text.html
     */
    TEXT("text"), //
    /**
     * full text search, KAN NIET SORTEREN<br>
     * <b>store</b> (false)<br>
     * <b>term_vector</b> (no) {@link TermVector}<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/text.html
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/term-vector.html
     */
    TEXT_VECTOR("text", java.util.Collections.singletonMap("term_vector", TermVector.yes.name())), //
    TEXT_VECTOR_POSITIONS("text", java.util.Collections.singletonMap("term_vector", TermVector.with_positions.name())), //
    TEXT_VECTOR_OFFSETS("text", java.util.Collections.singletonMap("term_vector", TermVector.with_offsets.name())), //
    TEXT_VECTOR_POSITIONS_OFFSETS("text", java.util.Collections.singletonMap("term_vector", TermVector.with_positions_offsets.name())), //
    TEXT_VECTOR_PAYLOADS("text", java.util.Collections.singletonMap("term_vector", TermVector.with_positions_payloads.name())), //
    TEXT_VECTOR_POSITIONS_OFFSETS_PAYLOADS("text", java.util.Collections.singletonMap("term_vector", TermVector.with_positions_offsets_payloads.name())), //
    /**
     * sorting , aggregations<br>
     * <b>store</b> (false)<br>
     * keyword fields are better for term and other term-level queries<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/keyword.html
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/keyword.html#keyword-field-type
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/keyword.html#constant-keyword-field-type
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/keyword.html#wildcard-field-type
     */
    KEYWORD("keyword"), //
    KEYWORD_CONSTANT("constant_keyword"), //
    KEYWORD_WILDCARD("wildcard"), //
    /**
     * <b>format</b>: bv. "format": "strict_date_optional_time||epoch_millis", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd"<br>
     * <b>store</b> (false)<br>
     * <b>locale</b><br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/date.html
     */
    DATE("date", java.util.Collections.singletonMap("format", "strict_date_optional_time||epoch_millis")), //
    /**
     * waarschijnlijk zelfde als 'DATE'<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/date_nanos.html
     */
    PRECISE_DATE("date_nanos", java.util.Collections.singletonMap("format", "strict_date_optional_time||epoch_millis")), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/boolean.html
     */
    BOOL("boolean"), //
    /**
     * 255.255.255.255; 1080:0:0:0:8:800:200C:417A (8 keer 2 bytes)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/ip.html
     */
    IP("ip"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    LONG("long"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    DOUBLE("double"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    INT("integer"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    SHORT("short"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    BYTE("byte"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    FLOAT("float"), //
    /**
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    HALF_FLOAT("half_float"), //
    /**
     * scaled_float heeft een scaling_factor dus speciefieke implemetaties zijn enkel mogelijk<br>
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    PERCENTAGE("scaled_float", java.util.Collections.singletonMap("scaling_factor", "100")),
    /**
     * scaled_float heeft een scaling_factor dus speciefieke implemetaties zijn enkel mogelijk<br>
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/number.html
     */
    MONETARY_AMOUNT("scaled_float", java.util.Collections.singletonMap("scaling_factor", "100")),
    /**
     * Base64 string zonder \n<br>
     * niet zoekbaar<br>
     * <b>store</b> (false)<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/binary.html
     */
    BYTES("binary"), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html
     */
    INT_RANGE("integer_range"), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html
     */
    FLOAT_RANGE("float_range"), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html
     */
    LONG_RANGE("long_range"), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html
     */
    DOUBLE_RANGE("double_range"), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html
     */
    DATE_RANGE("date_range"), //
    /**
     * <br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/range.html
     */
    IP_RANGE("ip_range"), //
    /**
     * {@link org.elasticsearch.common.geo.GeoPoint}<br>
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-point.html
     */
    GEO_POINT("geo_point"),
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/geo-shape.html
     */
    GEO_SHAPE("geo_shape"),
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/shape.html
     */
    SHAPE("shape"),
    /**
     * json string
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/flattened.html
     */
    JSON("flattened"),
    /**
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/nested.html
     */
    // NESTED("nested"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/object.html
     */
    OBJECT("object"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#completion-suggester
     */
    SUGGEST("completion"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/token-count.html
     */
    TOKEN_COUNT("token_count"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/parent-join.html
     */
    JOIN("join"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/search-as-you-type.html
     */
    SEARCH_AS_YOU_TYPE("search_as_you_type"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/alias.html
     */
    ALIAS("alias"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/flattened.html
     */
    FLATTENED("flattened"),
    /**
     * voor de volledigheid
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/constant-keyword.html
     */
    CONSTANT("constant_keyword"),
    /**
     * vector of float values of size = dims, default 3, max 1024
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/dense-vector.html
     * @see https://medium.com/gsi-technology/scalable-semantic-vector-search-with-elasticsearch-e79f9145ba8e
     * @see https://www.elastic.co/blog/text-similarity-search-with-vectors-in-elasticsearch
     */
    DENSE_VECTOR("dense_vector"), //
    /**
     * voor de volledigheid
     * 
     * @see http://man.hubwiz.com/docset/ElasticSearch.docset/Contents/Resources/Documents/www.elastic.co/guide/en/elasticsearch/reference/current/sparse-vector.html
     */
    SPARSE_VECTOR("sparse_vector"), //
    ;

    private String id;

    private Map<String, Object> options;

    private FieldType(String id) {
        this.id = id;
        this.options = java.util.Collections.emptyMap();
    }

    private FieldType(String id, Map<String, Object> options) {
        this.id = id;
        this.options = options;
    }

    public String id() {
        return id;
    }

    public Map<String, Object> options() {
        return options;
    }
}
