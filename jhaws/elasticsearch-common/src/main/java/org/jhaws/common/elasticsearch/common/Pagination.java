package org.jhaws.common.elasticsearch.common;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings("serial")
@JsonInclude(Include.NON_NULL)
public class Pagination implements Serializable {
    public static final int DEFAULT_MAX = 100;

    /** start position */
    @Min(0)
    protected int start = 0;

    /** max results */
    @Min(0)
    @Max(50_000)
    protected int max = DEFAULT_MAX;

    /** [0-max] */
    protected Integer results;

    /** total results */
    protected Long total;

    public Pagination() {
        super();
    }

    // wanneer pagineren per positie
    public Pagination(int start, int max) {
        this.start = start;
        this.max = max;
    }

    @Override
    public String toString() {
        return "Pagination [start=" + this.start + ", max=" + this.max + ", " + (this.results != null ? "results=" + this.results + ", " : "") + (this.total != null ? "total=" + this.total : "") + "]";
    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Integer getResults() {
        return this.results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public boolean canContinue() {
        return results != null && start + max < total;
    }
}
