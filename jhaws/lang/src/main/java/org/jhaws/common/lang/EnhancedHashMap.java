package org.jhaws.common.lang;

import java.util.HashMap;
import java.util.Map;

public class EnhancedHashMap<K, V> extends HashMap<K, V> implements EnhancedMap<K, V> {
	private static final long serialVersionUID = 2612641800194703157L;

	public EnhancedHashMap() {
		super();
	}

	public EnhancedHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public EnhancedHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	public EnhancedHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}
}
