package org.jhaws.common.lang;

import java.util.stream.IntStream;

public interface IntegerValueMap<K> extends ValueMap<K, Integer> {
    @Override
    default public Integer add(Integer n1, Integer n2) {
        return n1 + n2;
    }

    @Override
    default public Integer one() {
        return 1;
    }

    @Override
    default public Integer zero() {
        return 0;
    }

    default public IntStream streamN() {
        return values().stream().mapToInt(i -> i);
    }

    default Integer sum() {
        return streamN().sum();
    }
}
