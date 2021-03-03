package org.jhaws.common.elasticsearch.impl;

import java.util.List;

import org.jhaws.common.elasticsearch.common.Pagination;

public class QueryResults<T> {
	private Pagination pagination;

	private Float maxScore;

	private List<QueryResult<T>> results;

	private List<Long> partialTotalHits;

	public Pagination getPagination() {
		return this.pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public Float getMaxScore() {
		return this.maxScore;
	}

	public void setMaxScore(Float maxScore) {
		this.maxScore = maxScore;
	}

	public List<QueryResult<T>> getResults() {
		return this.results;
	}

	public void setResults(List<QueryResult<T>> results) {
		this.results = results;
	}

	public List<Long> getPartialTotalHits() {
		return this.partialTotalHits;
	}

	public void setPartialTotalHits(List<Long> partialTotalHits) {
		this.partialTotalHits = partialTotalHits;
	}
}
