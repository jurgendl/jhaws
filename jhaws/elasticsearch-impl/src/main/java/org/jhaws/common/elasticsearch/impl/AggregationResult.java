package org.jhaws.common.elasticsearch.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregationResult {
	private String key;

	private long docCount;

	private Map<String, Object> results = new LinkedHashMap<>();

	public AggregationResult(String key, long docCount) {
		super();
		this.key = key;
		this.docCount = docCount;
	}

	public AggregationResult() {
		super();
	}

	@Override
	public String toString() {
		return key + " #" + docCount + (results.isEmpty() ? "" : "\n\t" + results.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("\n\t")));
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getDocCount() {
		return this.docCount;
	}

	public void setDocCount(long docCount) {
		this.docCount = docCount;
	}

	public Map<String, Object> getResults() {
		return this.results;
	}

	public void setResults(Map<String, Object> results) {
		this.results = results;
	}
}
