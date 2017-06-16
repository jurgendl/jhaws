package org.jhaws.common.lang;

import java.util.TreeMap;

public class ShortValueTreeMap<K extends Comparable<? super K>> extends TreeMap<K, Short> implements ShortValueMap<K> {
    private static final long serialVersionUID = 5840996068861570167L;
}
