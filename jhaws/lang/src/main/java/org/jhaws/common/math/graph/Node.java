package org.jhaws.common.math.graph;

public class Node<T> {
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
}
