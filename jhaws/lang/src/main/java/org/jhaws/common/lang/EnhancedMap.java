package org.jhaws.common.lang;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public interface EnhancedMap<K, V> extends Map<K, V> {
    default void removeAll(Collection<K> keys) {
        keys.stream().forEach(this::remove);
    }

    default Stream<K> streamKeys() {
        return keySet().stream();
    }

    default Stream<V> streamValues() {
        return values().stream();
    }

    default Stream<Entry<K, V>> stream() {
        return entrySet().stream();
    }
}
