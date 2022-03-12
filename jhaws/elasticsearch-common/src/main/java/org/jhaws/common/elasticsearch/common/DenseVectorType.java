package org.jhaws.common.elasticsearch.common;

public enum DenseVectorType {
	uninitialized, hnsw;

	public String id() {
		return name();
	}
}
