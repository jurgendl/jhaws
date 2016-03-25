package org.jhaws.common.lang;

import java.util.LinkedHashMap;
import java.util.Map;

public class IMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = -7644530180220179181L;

    public IMap() {
        super();
    }

    public IMap(Map<K, V> m) {
        super(m);
    }

    public IMap<K, V> add(K key, V value) {
        put(key, value);
        return this;
    }

    public IMap<K, V> copy() {
        return new IMap<K, V>(this);
    }
}
