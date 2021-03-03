package org.jhaws.common.elasticsearch.impl;

import org.jhaws.common.elasticsearch.common.Field;

public interface MappingListener {
	static final MappingListener DUMMY = new MappingListener() {
		@Override
		public void map(String fullName, Field field, FieldMapping fieldMapping) {
			//
		}
	};

	void map(String fullName, Field field, FieldMapping fieldMapping);
}
