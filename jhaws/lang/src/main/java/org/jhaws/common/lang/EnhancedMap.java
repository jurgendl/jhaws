package org.jhaws.common.lang;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface EnhancedMap<K, V> extends Map<K, V> {
    default void removeAll(Collection<K> keys) {
        keys.stream().forEach(this::remove);
    }

    default Stream<K> streamKeys() {
        return keySet().stream();
    }

    default void forEachKey(Consumer<K> action) {
        streamKeys().forEach(action);
    }

    default Stream<V> streamValues() {
        return values().stream();
    }

    default void forEachValue(Consumer<V> action) {
        streamValues().forEach(action);
    }

    default Stream<Entry<K, V>> stream() {
        return entrySet().stream();
    }

    default void forEach(Consumer<? super Entry<K, V>> action) {
        entrySet().forEach(action);
    }
}
