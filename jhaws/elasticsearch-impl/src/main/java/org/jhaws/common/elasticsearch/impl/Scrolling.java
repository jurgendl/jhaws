package org.jhaws.common.elasticsearch.impl;

import javax.validation.constraints.NotNull;

import org.jhaws.common.elasticsearch.common.Pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

// "sort": [
// "_doc"
// ]
@SuppressWarnings("serial")
@JsonInclude(Include.NON_NULL)
public class Scrolling extends Pagination {
	@NotNull
	protected String scrollId;

	// wanneer scrollen, eerste pagina
	public Scrolling() {
		this.max = 50_000;
	}

	// wanneer scrollen, eerste pagina
	public Scrolling(int max) {
		this.max = max;
	}

	// wanneer scrollen, volgende paginas
	public Scrolling(String scrollId) {
		this.scrollId = scrollId;
	}

	public String getScrollId() {
		return this.scrollId;
	}

	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}

	@Override
	public String toString() {
		return "Scrolling [scrollId=" + this.scrollId + ", max=" + this.max + ", " + (this.results != null ? "results=" + this.results + ", " : "") + (this.total != null ? "total=" + this.total : "") + "]";
	}

	@Override
	public boolean canContinue() {
		return (scrollId != null && results == max) || total == null;
	}
}
