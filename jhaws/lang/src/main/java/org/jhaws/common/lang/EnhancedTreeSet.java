package org.jhaws.common.lang;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class EnhancedTreeSet<T> extends TreeSet<T> implements EnhancedSet<T> {
    private static final long serialVersionUID = 4557535423330527649L;

    public EnhancedTreeSet() {
        super();
    }

    public EnhancedTreeSet(Collection<? extends T> c) {
        super(c);
    }

    public EnhancedTreeSet(Comparator<? super T> comparator) {
        super(comparator);
    }

    public EnhancedTreeSet(SortedSet<T> s) {
        super(s);
    }
}
