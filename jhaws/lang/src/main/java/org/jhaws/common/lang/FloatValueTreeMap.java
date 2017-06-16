package org.jhaws.common.lang;

import java.util.TreeMap;

public class FloatValueTreeMap<K extends Comparable<? super K>> extends TreeMap<K, Float> implements FloatValueMap<K> {
    private static final long serialVersionUID = 5840996068861570167L;
}
