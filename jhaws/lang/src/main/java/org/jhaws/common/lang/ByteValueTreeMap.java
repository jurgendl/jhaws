package org.jhaws.common.lang;

import java.util.TreeMap;

public class ByteValueTreeMap<K extends Comparable<? super K>> extends TreeMap<K, Byte> implements ByteValueMap<K> {
    private static final long serialVersionUID = 5840996068861570167L;
}
