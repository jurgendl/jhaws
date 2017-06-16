package org.jhaws.common.lang;

import java.util.Collection;
import java.util.Map;

public interface EnhancedMap<K, V> extends Map<K, V> {
    default void removeAll(Collection<K> keys) {
        keys.stream().forEach(this::remove);
    }
}
