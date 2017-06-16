package org.jhaws.common.lang;

import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface ValueMap<K, N extends Number> extends EnhancedMap<K, N> {
    default N add(K key) {
        N value = get(key);
        if (value == null) {
            value = one();
        } else {
            value = add(value, one());
        }
        put(key, value);
        return value;
    }

    default void operate(UnaryOperator<N> operation) {
        keySet().forEach(key -> operate(key, operation));
    }

    default N operate(K key, UnaryOperator<N> operation) {
        N value = get(key);
        value = operation.apply(value);
        if (value == null) value = zero();
        put(key, value);
        return value;
    }

    default N add(K key, N number) {
        N value = get(key);
        if (value == null) {
            value = number;
        } else {
            value = add(value, number);
        }
        put(key, value);
        return value;
    }

    abstract N add(N n1, N n2);

    abstract N one();

    abstract N zero();

    default List<K> sorted(Comparator<N> order) {
        return entrySet().stream().sorted((a, b) -> order.compare(a.getValue(), b.getValue())).map(Entry::getKey).collect(Collectors.toList());
    }
}
