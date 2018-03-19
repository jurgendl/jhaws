package org.jhaws.common.lang;

import java.util.Collection;
import java.util.HashSet;

public class EnhancedHashSet<T> extends HashSet<T> implements EnhancedSet<T> {
    private static final long serialVersionUID = -4801688775061149143L;

    public EnhancedHashSet() {
        super();
    }

    public EnhancedHashSet(Collection<? extends T> c) {
        super(c);
    }

    public EnhancedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public EnhancedHashSet(int initialCapacity) {
        super(initialCapacity);
    }
}
