package org.jhaws.common.lang;

import java.util.SortedMap;

public interface EnhancedSortedMap<K, V> extends EnhancedMap<K, V>, SortedMap<K, V> {
	default V lastValue() {
		return get(lastKey());
	}

	default V firstValue() {
		return get(firstKey());
	}
}
