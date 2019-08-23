package org.jhaws.common.lang;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class SortedList<E> extends AbstractList<E> {
    private ArrayList<E> internalList = new ArrayList<>();

    private Comparator<? super E> comparator;

    public SortedList(Collection<? extends E> c, Comparator<? super E> comparator) {
        this.comparator = comparator;
        addAll(c);
    }

    public SortedList(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void add(int position, E e) {
        internalList.add(e);
        Collections.sort(internalList, comparator);
    }

    @Override
    public boolean remove(Object o) {
        return internalList.remove(o);
    }

    @Override
    public E remove(int index) {
        return internalList.remove(index);
    }

    @Override
    public E get(int i) {
        return internalList.get(i);
    }

    @Override
    public int size() {
        return internalList.size();
    }
}