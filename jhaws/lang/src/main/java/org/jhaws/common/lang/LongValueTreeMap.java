package org.jhaws.common.lang;

import java.util.TreeMap;

public class LongValueTreeMap<K extends Comparable<? super K>> extends TreeMap<K, Long> implements LongValueMap<K> {
    private static final long serialVersionUID = 5840996068861570167L;
}
