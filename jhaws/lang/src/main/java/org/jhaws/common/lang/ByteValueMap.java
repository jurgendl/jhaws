package org.jhaws.common.lang;

public interface ByteValueMap<K> extends ValueMap<K, Byte> {
    @Override
    default public Byte add(Byte n1, Byte n2) {
        return (byte) (n1.byteValue() + n2.byteValue());
    }

    @Override
    default public Byte one() {
        return 1;
    }

    @Override
    default public Byte zero() {
        return 0;
    }
}
