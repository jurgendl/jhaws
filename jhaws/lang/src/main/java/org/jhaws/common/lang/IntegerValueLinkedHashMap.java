package org.jhaws.common.lang;

import java.util.LinkedHashMap;
import java.util.Map;

public class IntegerValueLinkedHashMap<K> extends LinkedHashMap<K, Integer> implements IntegerValueMap<K> {
    private static final long serialVersionUID = -699854120013083462L;

    public IntegerValueLinkedHashMap() {
        super();
    }

    public IntegerValueLinkedHashMap(Map<? extends K, ? extends Integer> m) {
        super(m);
    }
}
