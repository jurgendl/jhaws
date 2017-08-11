package org.jhaws.common.lang;

import java.util.stream.LongStream;

public interface LongValueMap<K> extends ValueMap<K, Long> {
    @Override
    default public Long add(Long n1, Long n2) {
        return n1 + n2;
    }

    @Override
    default public Long one() {
        return 1l;
    }

    @Override
    default public Long zero() {
        return 0l;
    }

    default public LongStream streamN() {
        return values().stream().mapToLong(i -> i);
    }

    default Long sum() {
        return streamN().sum();
    }
}
