package org.jhaws.common.lang;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnhancedLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements EnhancedMap<K, V> {
    private static final long serialVersionUID = 6198137611588663728L;

    public EnhancedLinkedHashMap() {
        super();

    }

    public EnhancedLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);

    }

    public EnhancedLinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);

    }

    public EnhancedLinkedHashMap(int initialCapacity) {
        super(initialCapacity);

    }

    public EnhancedLinkedHashMap(Map<? extends K, ? extends V> m) {
        super(m);

    }
}
