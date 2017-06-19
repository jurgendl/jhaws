package org.jhaws.common.lang;

import java.util.HashMap;
import java.util.Map;

public class IntegerValueHashMap<K> extends HashMap<K, Integer> implements IntegerValueMap<K> {
    private static final long serialVersionUID = -699854120013083462L;

    public IntegerValueHashMap() {
        super();
    }

    public IntegerValueHashMap(Map<? extends K, ? extends Integer> m) {
        super(m);
    }
}
