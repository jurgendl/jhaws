package org.jhaws.common.elasticsearch8.impl;

// TODO https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-metrics-scripted-metric-aggregation.html
// TODO https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-metrics-geobounds-aggregation.html
// TODO https://www.elastic.co/guide/en/elasticsearch/reference/7.6/search-aggregations-metrics-geocentroid-aggregation.html
public enum MetricAggregation {
    count, avg, max, min, sum, median_absolute_deviation, cardinality, tdigest_percentile_ranks,
    /**/
    extended_stats,
    /**/
    stats;
}
