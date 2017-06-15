package org.jhaws.common.lang;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class IntegerValueTreeMap<K extends Comparable<? super K>> extends TreeMap<K, Integer> implements IntegerValueMap<K> {
    private static final long serialVersionUID = -6712992103284295029L;

    public IntegerValueTreeMap() {
        super();
    }

    public IntegerValueTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public IntegerValueTreeMap(Map<? extends K, ? extends Integer> m) {
        super(m);
    }

    public IntegerValueTreeMap(SortedMap<K, ? extends Integer> m) {
        super(m);
    }
}
