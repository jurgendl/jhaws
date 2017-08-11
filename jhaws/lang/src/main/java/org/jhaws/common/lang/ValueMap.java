package org.jhaws.common.lang;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;

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

    default N add(Entry<K, N> entry) {
        return add(entry.getKey(), entry.getValue());
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

    default List<K> sorted() {
        return sorted((n1, n2) -> new CompareToBuilder().append(n1, n2).toComparison());
    }

    default List<K> sortedInvers() {
        return sorted((n1, n2) -> -new CompareToBuilder().append(n1, n2).toComparison());
    }

    default List<K> sorted(Comparator<N> order) {
        return entrySet().stream().sorted((a, b) -> order.compare(a.getValue(), b.getValue())).map(Entry::getKey).collect(Collectors.toList());
    }

    default LinkedHashMap<K, N> sortedMap() {
        return sortedMap((n1, n2) -> new CompareToBuilder().append(n1, n2).toComparison());
    }

    default LinkedHashMap<K, N> sortedMapInvers() {
        return sortedMap((n1, n2) -> -new CompareToBuilder().append(n1, n2).toComparison());
    }

    default LinkedHashMap<K, N> sortedMap(Comparator<N> order) {
        return entrySet().stream().sorted((a, b) -> order.compare(a.getValue(), b.getValue())).collect(
                Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }
}
