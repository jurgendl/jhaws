package org.jhaws.common.lang;

public interface FloatValueMap<K> extends ValueMap<K, Float> {
    @Override
    default public Float add(Float n1, Float n2) {
        return n1 + n2;
    }

    @Override
    default public Float one() {
        return 1f;
    }

    @Override
    default public Float zero() {
        return 0f;
    }
}
