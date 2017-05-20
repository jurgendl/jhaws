package org.jhaws.common.lang;

public interface IntegerValueMap<K> extends ValueMap<K, Integer> {
	@Override
	default public Integer add(Integer n1, Integer n2) {
		return n1 + n2;
	}

	@Override
	default public Integer one() {
		return 1;
	}
}
