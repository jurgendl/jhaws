package org.jhaws.common.elasticsearch.impl;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.jhaws.common.elasticsearch.common.Pagination;

import java.util.Collection;
import java.util.List;

public class QueryContext<T> {
    public String index;

    public QueryBuilder query;

    public Pagination pagination;

    public List<SortBuilder<?>> sort;

    public String[] includes;

    public String[] excludes;

    public Mapper<T> mapper;

    public List<String> highlight;

    public Scrolling scrolling;

    public SearchSourceBuilder searchSourceBuilder;

    public SearchRequest searchRequest;

    public SearchScrollRequest scrollRequest;

    public SearchResponse searchResponse;

    public float maxScore;

    public Collection<ScriptedField> scriptedFields;
}
