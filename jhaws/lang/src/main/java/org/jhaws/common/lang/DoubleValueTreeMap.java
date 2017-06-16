package org.jhaws.common.lang;

import java.util.TreeMap;

public class DoubleValueTreeMap<K extends Comparable<? super K>> extends TreeMap<K, Double> implements DoubleValueMap<K> {
    private static final long serialVersionUID = 5840996068861570167L;
}
