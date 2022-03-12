package org.jhaws.common.elasticsearch.common;

public enum DenseVectorSimilarity {
	uninitialized, //
	l2_norm, //
	dot_product, //
	cosine;

	public String id() {
		return name();
	}
}
