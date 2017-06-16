package org.jhaws.common.lang;

public interface DoubleValueMap<K> extends ValueMap<K, Double> {
    @Override
    default public Double add(Double n1, Double n2) {
        return n1 + n2;
    }

    @Override
    default public Double one() {
        return 1d;
    }

    @Override
    default public Double zero() {
        return 0d;
    }
}
