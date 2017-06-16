package org.jhaws.common.lang;

public interface ShortValueMap<K> extends ValueMap<K, Short> {
    @Override
    default public Short add(Short n1, Short n2) {
        return (short) (n1 + n2);
    }

    @Override
    default public Short one() {
        return (short) 1;
    }

    @Override
    default public Short zero() {
        return (short) 0;
    }
}
