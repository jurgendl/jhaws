package org.jhaws.common.lang;

import java.util.Map;

public interface ValueMap<K, N extends Number> extends Map<K, N> {
	default void add(K key) {
		N value = get(key);
		if (value == null) {
			put(key, one());
		} else {
			put(key, add(value, one()));
		}
	}

	default void add(K key, N number) {
		N value = get(key);
		if (value == null) {
			put(key, number);
		} else {
			put(key, add(value, number));
		}
	}

	abstract N add(N n1, N n2);

	abstract N one();
}
