package org.jhaws.common.lang;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class EnhancedTreeMap<K, V> extends TreeMap<K, V> implements EnhancedSortedMap<K, V> {
    private static final long serialVersionUID = 6198137611588663728L;

    public EnhancedTreeMap() {
        super();
    }

    public EnhancedTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public EnhancedTreeMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public EnhancedTreeMap(SortedMap<K, ? extends V> m) {
        super(m);
    }
}
