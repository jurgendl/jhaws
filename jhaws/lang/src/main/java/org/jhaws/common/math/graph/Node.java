package org.jhaws.common.math.graph;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class Node<T> implements Serializable, Comparable<Node<T>> {
    private static final long serialVersionUID = 1L;

    protected T item;

    protected Double[] coordinates;

    public Node() {
        super();
    }

    public Node(T item) {
        super();
        this.item = item;
    }

    public T getItem() {
        return this.item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return item.toString();
    }

    public Double[] getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int compareTo(Node<T> o) {
        return new CompareToBuilder().append(item, o.item).toComparison();
    }
}
