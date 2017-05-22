package org.jhaws.common.lang;

import java.util.Map;

public interface ValueMap<K, N extends Number> extends Map<K, N> {
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
}
