package org.jhaws.common.elasticsearch.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryResult<T> {
    private Float score;

    private Float maxScore;

    private T result;

    private Map<String, List<String>> highlights = new LinkedHashMap<>();

    private String id;

    public QueryResult() {
        super();
    }

    public QueryResult(Float score, Float maxScore, String id, T result) {
        this.id = id;
        this.score = score;
        this.maxScore = maxScore;
        this.result = result;
    }

    public QueryResult(Float score, Float maxScore, String id, T result, Map<String, List<String>> highlights) {
        this.id = id;
        this.score = score;
        this.maxScore = maxScore;
        this.result = result;
        this.highlights = highlights;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Map<String, List<String>> getHighlights() {
        return this.highlights;
    }

    public void setHighlights(Map<String, List<String>> highlights) {
        this.highlights = highlights;
    }

    public Float getScore() {
        return this.score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Float getMaxScore() {
        return this.maxScore;
    }

    public void setMaxScore(Float maxScore) {
        this.maxScore = maxScore;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QueryResult [" + (this.score != null ? "score=" + this.score + ", " : "") + (this.maxScore != null ? "maxScore=" + this.maxScore + ", " : "") + (this.result != null ? "result=" + this.result + ", " : "")
                + (this.id != null ? "id=" + this.id : "") + "]";
    }
}
